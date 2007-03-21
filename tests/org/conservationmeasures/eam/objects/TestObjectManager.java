package org.conservationmeasures.eam.objects;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;
import org.martus.util.DirectoryUtils;


public class TestObjectManager extends EAMTestCase
{
	public TestObjectManager(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		manager = project.getObjectManager();		
		db = project.getDatabase();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
	}

	public void testObjectLifecycles() throws Exception
	{
		int[] types = new int[] {
			ObjectType.RATING_CRITERION, 
			ObjectType.VALUE_OPTION,  
			ObjectType.VIEW_DATA, 
			ObjectType.PROJECT_RESOURCE,
			ObjectType.INDICATOR,
			ObjectType.OBJECTIVE,
			ObjectType.GOAL,
			ObjectType.PROJECT_METADATA,
		};
		
		for(int i = 0; i < types.length; ++i)
		{
			verifyObjectLifecycle(types[i], null);
		}
		
		CreateFactorParameter factor = new CreateFactorParameter(new FactorTypeCause());
		verifyObjectLifecycle(ObjectType.FACTOR, factor);
		
		CreateFactorParameter target = new CreateFactorParameter(new FactorTypeTarget());
		FactorId factorId = (FactorId)manager.createObject(ObjectType.FACTOR, BaseId.INVALID, factor);
		FactorId targetId = (FactorId)manager.createObject(ObjectType.FACTOR, BaseId.INVALID, target);
		CreateFactorLinkParameter link = new CreateFactorLinkParameter(factorId, targetId);
		verifyBasicObjectLifecycle(ObjectType.FACTOR_LINK, link);
	}

	public void testIsPseudoTag() throws Exception
	{
		boolean isPseudoTag = manager.isPseudoTag("PseudoTagFactor");
		assertEquals("pseudo tag?", true, isPseudoTag);

		isPseudoTag = manager.isPseudoTag("notAPseudoField");
		assertEquals("not pseudo tag?", false, isPseudoTag);
	}
	
	public void testPseudoTagTargetViability() throws Exception
	{
		String sampleStatusCode = "2";
		String NOT_SPECIFIED = "";
		FactorId targetId = project.createNode(Factor.TYPE_TARGET);
		Target target = (Target)project.findNode(targetId);
		target.setData(Target.TAG_TARGET_STATUS, sampleStatusCode);
		
		String simple = project.getObjectData(target.getRef(), Target.PSEUDO_TAG_TARGET_VIABILITY);
		assertEquals("Didn't return simple viability?", sampleStatusCode, simple);
		
		target.setData(Target.TAG_VIABILITY_MODE, ViabilityModeQuestion.TNC_STYLE_CODE);
		String notRated = project.getObjectData(target.getRef(), Target.PSEUDO_TAG_TARGET_VIABILITY);
		assertEquals("Didn't return simple viability?", NOT_SPECIFIED, notRated);
	}

	private void verifyObjectLifecycle(int type, CreateObjectParameter parameter) throws Exception
	{
		verifyBasicObjectLifecycle(type, parameter);
		verifyObjectWriteAndRead(type, parameter);
		verifyGetPool(type);
	}

	private void verifyBasicObjectLifecycle(int type, CreateObjectParameter parameter) throws Exception, IOException, ParseException
	{
		BaseId createdId = manager.createObject(type, BaseId.INVALID, parameter);
		assertNotEquals(type + " Created with invalid id", BaseId.INVALID, createdId);
		db.readObject(type, createdId);
		
		String tag = RatingCriterion.TAG_LABEL;
		manager.setObjectData(type, createdId, tag, "data");
		EAMObject withData = db.readObject(type, createdId);
		assertEquals(type + " didn't write/read data for " + tag + "?", "data", withData.getData(tag));
		assertEquals(type + " can't get data from project?", "data", manager.getObjectData(type, createdId, tag));
		
		manager.deleteObject(type, createdId);
		try
		{
			manager.getObjectData(type, createdId, tag);
			fail(type + " Should have thrown getting data from deleted object");
		}
		catch(Exception ignoreExpected)
		{
		}
		
		try
		{
			db.readObject(type, createdId);
			fail(type + " Should have thrown reading deleted object");
		}
		catch(Exception ignoreExpected)
		{
		}
		
		BaseId desiredId = new BaseId(2323);
		assertEquals(type + " didn't use requested id?", desiredId, manager.createObject(type, desiredId, parameter));
	}

	private void verifyObjectWriteAndRead(int type, CreateObjectParameter parameter) throws IOException, Exception
	{
		File tempDirectory = createTempDirectory();
		try
		{
			Project projectToWrite = new Project();
			projectToWrite.createOrOpen(tempDirectory);
			BaseId idToReload = projectToWrite.createObject(type, BaseId.INVALID, parameter);
			projectToWrite.close();
			
			Project projectToRead = new Project();
			projectToRead.createOrOpen(tempDirectory);
			try
			{
				projectToRead.getObjectData(type, idToReload, EAMBaseObject.TAG_LABEL);
			}
			catch (NullPointerException e)
			{
				fail("Didn't reload object from disk, type: " + type + " (did the pool get loaded?)");
			}
			projectToRead.close();
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}
	
	private void verifyGetPool(int type) throws Exception
	{
		CreateObjectParameter cop = null;
		if(type == ObjectType.FACTOR)
			cop = new CreateFactorParameter(new FactorTypeTarget());
		else if(type == ObjectType.FACTOR_LINK)
			cop = new CreateFactorLinkParameter(new FactorId(1), new FactorId(2));
		BaseId createdId = manager.createObject(type, BaseId.INVALID, cop);
		EAMObjectPool pool = manager.getPool(type);
		assertNotNull("Missing pool type " + type, pool);
		EAMObject created = (EAMObject)pool.getRawObject(createdId);
		assertNotNull("Pool doesn't have object type " + created);
	}
	
	ProjectForTesting project;
	ObjectManager manager;
	ProjectServer db;
}
