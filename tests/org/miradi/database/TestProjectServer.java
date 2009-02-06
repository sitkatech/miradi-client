/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.database;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeWriter;
import org.miradi.database.FileBasedProjectServer;
import org.miradi.database.Manifest;
import org.miradi.database.ObjectManifest;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.project.ProjectServerForTesting;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class TestProjectServer extends TestCaseWithProject
{
	public TestProjectServer(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		storage = new ProjectServerForTesting();
		storage.openMemoryDatabase(getName());
		idAssigner = new IdAssigner();
	}
	
	public void tearDown() throws Exception
	{
		storage.close();
		super.tearDown();
	}
	
	public void testObjectManifest() throws Exception
	{
		BaseId[] idsToWrite = {new BaseId(19), new BaseId(25), new BaseId(727), };
		for(int i = 0; i < idsToWrite.length; ++i)
		{
			Task task = new Task(getObjectManager(), new FactorId(idsToWrite[i].asInt()));
			storage.writeObject(task);
		}
		RatingCriterion criterion = new RatingCriterion(getObjectManager(), new BaseId(99));
		storage.writeObject(criterion);

		ObjectManifest manifest = storage.readObjectManifest(ObjectType.TASK);
		assertEquals("wrong number of objects?", idsToWrite.length, manifest.size());
		for(int i = 0; i < idsToWrite.length; ++i)
			assertTrue("missing id " + idsToWrite[i], manifest.has(idsToWrite[i]));
		
		storage.deleteObject(ObjectType.TASK, idsToWrite[0]);
		Manifest afterDelete = storage.readObjectManifest(ObjectType.TASK);
		assertFalse("didn't delete id?", afterDelete.has(idsToWrite[0]));
	}
	
	public void testWriteAndReadNode() throws Exception
	{

		Strategy intervention = new Strategy(getObjectManager(), takeNextModelNodeId());
		storage.writeObject(intervention);
		Strategy gotIntervention = (Strategy)readNode(intervention.getRef());
		assertEquals("not a strategy?", intervention.getNodeType(), gotIntervention.getNodeType());
		assertEquals("wrong id?", intervention.getId(), gotIntervention.getId());

		Cause factor = new Cause(getObjectManager(), takeNextModelNodeId());
		
		storage.writeObject(factor);
		Cause gotContributingFactor = (Cause)readNode(factor.getRef());
		assertEquals("not indirect factor?", factor.getNodeType(), gotContributingFactor.getNodeType());
		
		Target target = new Target(getObjectManager(), takeNextModelNodeId());
		storage.writeObject(target);
		Target gotTarget = (Target)readNode(target.getRef());
		assertEquals("not a target?", target.getNodeType(), gotTarget.getNodeType());
		
		
		ObjectManifest strategyNodeIds = storage.readObjectManifest(ObjectType.STRATEGY);
		assertEquals("not one node?", 1, strategyNodeIds.size());
		assertTrue("missing the strategy node?", strategyNodeIds.has(intervention.getId()));
		
		ObjectManifest causeNodeIds = storage.readObjectManifest(ObjectType.CAUSE);
		assertEquals("not one node?", 1, causeNodeIds.size());
		assertTrue("missing the cause node?", causeNodeIds.has(gotContributingFactor.getId()));
		
		ObjectManifest targetNodeIds = storage.readObjectManifest(ObjectType.TARGET);
		assertEquals("not one node?", 1, targetNodeIds.size());
		assertTrue("missing the target node?", targetNodeIds.has(target.getId()));
	}
	
	private FactorId takeNextModelNodeId()
	{
		return new FactorId(idAssigner.takeNextId().asInt());
	}
	
	private Factor readNode(ORef factorRef) throws Exception
	{
		return (Factor)storage.readObject(getObjectManager(), factorRef.getObjectType(), factorRef.getObjectId());
	}
	
	public void testWriteAndReadLinkage() throws Exception
	{
		ORef fromRef = new ORef(ObjectType.CAUSE, new FactorId(2));
		ORef toRef = new ORef(ObjectType.CAUSE, new FactorId(3));
		FactorLink original = new FactorLink(getObjectManager(), new FactorLinkId(1), fromRef, toRef);
		storage.writeObject(original);
		FactorLink got = (FactorLink)storage.readObject(getObjectManager(), original.getType(), original.getId());
		assertEquals("wrong id?", original.getId(), got.getId());
		assertEquals("wrong from?", original.getFromFactorRef(), got.getFromFactorRef());
		assertEquals("wrong to?", original.getToFactorRef(), got.getToFactorRef());
		
		ObjectManifest linkageIds = storage.readObjectManifest(original.getType());
		assertEquals("not one link?", 1, linkageIds.size());
		assertTrue("wrong link id in manifest?", linkageIds.has(original.getId()));
		
		storage.writeObject(original);
		assertEquals("dupe in manifest?", 1, storage.readObjectManifest(original.getType()).size());
		
	}
	
	public void testDeleteLinkage() throws Exception
	{
		ORef fromRef = new ORef(ObjectType.CAUSE, new FactorId(2));
		ORef toRef = new ORef(ObjectType.CAUSE, new FactorId(3));
		FactorLink original = new FactorLink(getObjectManager(), new FactorLinkId(1), fromRef, toRef);
		storage.writeObject(original);
		storage.deleteObject(original.getType(), original.getId());
		assertEquals("didn't delete?", 0, storage.readObjectManifest(original.getType()).size());
		try
		{
			storage.readObject(getObjectManager(), original.getType(), original.getId());
		}
		catch(IOException ignoreExpected)
		{
		}
	}
	
	public void testWriteThreatRatingBundle() throws Exception
	{
		FactorId threatId = new FactorId(68);
		FactorId targetId = new FactorId(99);
		BaseId defaultId = new BaseId(929);
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, defaultId);
		storage.writeThreatRatingBundle(bundle);
		
		ThreatRatingBundle got = storage.readThreatRatingBundle(threatId, targetId);
		assertEquals("didn't read?", defaultId, got.getValueId(new BaseId(38838)));
		
		assertNull("didn't return null for non-existent bundle?", storage.readThreatRatingBundle(new BaseId(282), new BaseId(2995)));
	}
	
	public void testReadAndWriteThreatRatingFramework() throws Exception
	{
		ProjectServerForTesting db = getProject().getTestDatabase();
		db.writeThreatRatingFramework(getProject().getSimpleThreatRatingFramework());
		JSONObject got = db.readRawThreatRatingFramework();
		assertEquals(got.toString(), getProject().getSimpleThreatRatingFramework().toJson().toString());
	}
	
	public void testCreateInNonEmptyDirectory() throws Exception
	{
		File tempDirectory = createTempDirectory();
		File anyFile = new File(tempDirectory, "blah");
		anyFile.mkdirs();
		try
		{
			storage.create(tempDirectory);
			fail("Should have thrown");
		}
		catch (Exception ignoreExpected)
		{
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}
	
	public void testOpenNonProjectDirectory() throws Exception
	{
		File tempDirectory = createTempDirectory();
		try
		{
			File nonProjectFile = new File(tempDirectory, "foo");
			UnicodeWriter writer = new UnicodeWriter(nonProjectFile);
			writer.writeln("nothing");
			writer.close();
			
			assertFalse("project exists?", ProjectServer.isExistingProject(tempDirectory));
			ProjectServer anotherStorage = new FileBasedProjectServer();
			try
			{
				anotherStorage.open(tempDirectory);
				fail("Should have thrown trying to open non-empty, non-project directory");
			}
			catch(IOException ignoreExpected)
			{
			}
			
			File nonExistantProjectDirectory = new File(tempDirectory, "DoesNotExist");
			try
			{
				anotherStorage.open(nonExistantProjectDirectory);
				fail("Should throw when opening a non-existant directory");
			}
			catch (Exception ignoreExpected)
			{
			}
			anotherStorage.close();
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}

	IdAssigner idAssigner;
	private ProjectServerForTesting storage;
}
