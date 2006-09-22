package org.conservationmeasures.eam.objects;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;
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
			ObjectType.THREAT_RATING_CRITERION, 
			ObjectType.THREAT_RATING_VALUE_OPTION, 
			ObjectType.TASK, 
			ObjectType.VIEW_DATA, 
			ObjectType.PROJECT_RESOURCE,
			ObjectType.INDICATOR,
			ObjectType.OBJECTIVE,
			ObjectType.GOAL,
		};
		
		for(int i = 0; i < types.length; ++i)
		{
			verifyObjectLifecycle(types[i], null);
		}
		
		CreateModelNodeParameter factor = new CreateModelNodeParameter(new NodeTypeFactor());
		verifyObjectLifecycle(ObjectType.MODEL_NODE, factor);
		
		CreateModelNodeParameter target = new CreateModelNodeParameter(new NodeTypeTarget());
		ModelNodeId factorId = (ModelNodeId)manager.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, factor);
		ModelNodeId targetId = (ModelNodeId)manager.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, target);
		CreateModelLinkageParameter link = new CreateModelLinkageParameter(factorId, targetId);
		verifyBasicObjectLifecycle(ObjectType.MODEL_LINKAGE, link);
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
		assertNotEquals("Created with invalid id", BaseId.INVALID, createdId);
		db.readObject(type, createdId);
		
		String tag = ThreatRatingCriterion.TAG_LABEL;
		manager.setObjectData(type, createdId, tag, "data");
		EAMObject withData = db.readObject(type, createdId);
		assertEquals("didn't write/read data?", "data", withData.getData(tag));
		assertEquals("can't get data from project?", "data", manager.getObjectData(type, createdId, tag));
		
		manager.deleteObject(type, createdId);
		try
		{
			manager.getObjectData(type, createdId, tag);
			fail("Should have thrown getting data from deleted object");
		}
		catch(Exception ignoreExpected)
		{
		}
		
		try
		{
			db.readObject(type, createdId);
			fail("Should have thrown reading deleted object");
		}
		catch(Exception ignoreExpected)
		{
		}
		
		BaseId desiredId = new BaseId(2323);
		assertEquals("didn't use requested id?", desiredId, manager.createObject(type, desiredId, parameter));
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
		if(type == ObjectType.MODEL_NODE)
			cop = new CreateModelNodeParameter(new NodeTypeTarget());
		else if(type == ObjectType.MODEL_LINKAGE)
			cop = new CreateModelLinkageParameter(new ModelNodeId(1), new ModelNodeId(2));
		BaseId createdId = manager.createObject(type, BaseId.INVALID, cop);
		EAMObjectPool pool = manager.getPool(type);
		assertNotNull("Missing pool type " + type, pool);
		EAMObject created = (EAMObject)pool.getRawObject(createdId);
		assertNotNull("Pool doesn't have object type " + created);
	}
	
	Project project;
	ObjectManager manager;
	ProjectServer db;
}
