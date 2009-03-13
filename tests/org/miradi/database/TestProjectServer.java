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
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Target;
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
		tempDataDirectory = createTempDirectory();
		storage = new ProjectServer();
		storage.setLocalDataLocation(tempDataDirectory);
		storage.createProject(getName());
		idAssigner = new IdAssigner();
	}
	
	public void tearDown() throws Exception
	{
		storage.close();
		DirectoryUtils.deleteEntireDirectoryTree(tempDataDirectory);
		super.tearDown();
	}
	
	public void testObjectManifest() throws Exception
	{
		int type = Target.getObjectType();
		ObjectManifest beforeWrite = storage.readObjectManifest(type);
		assertEquals(0, beforeWrite.size());
		
		BaseId[] idsToWrite = {new BaseId(19), new BaseId(25), new BaseId(727), };
		for(int i = 0; i < idsToWrite.length; ++i)
		{
			beforeWrite.put(idsToWrite[i]);
		}
		storage.writeObjectManifest(storage.getCurrentProjectName(), type, beforeWrite);

		ObjectManifest afterRead = storage.readObjectManifest(type);
		assertEquals("wrong number of objects?", idsToWrite.length, afterRead.size());
		for(int i = 0; i < idsToWrite.length; ++i)
			assertTrue("missing id " + idsToWrite[i], afterRead.has(idsToWrite[i]));
		
		Manifest afterDelete = storage.readObjectManifest(ObjectType.TASK);
		assertFalse("didn't delete id?", afterDelete.has(idsToWrite[0]));
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
		ProjectServer db = getProject().getDatabase();
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
			storage.setLocalDataLocation(tempDirectory);
			storage.createProject("blah");
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
			
			ProjectServer anotherStorage = new ProjectServer();
			assertFalse("project exists?", anotherStorage.isExistingLocalProject(tempDirectory));
			try
			{
				anotherStorage.setLocalDataLocation(tempDirectory.getParentFile());
				anotherStorage.openProject(tempDirectory.getName());
				fail("Should have thrown trying to open non-empty, non-project directory");
			}
			catch(IOException ignoreExpected)
			{
			}
			
			try
			{
				anotherStorage.openProject("DoesNotExist");
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
	private ProjectServer storage;
	private File tempDataDirectory;
}
