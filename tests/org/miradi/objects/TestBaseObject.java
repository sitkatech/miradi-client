/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Task;
import org.miradi.project.ProjectForTesting;

public class TestBaseObject extends EAMTestCase
{
	public TestBaseObject(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	public void testGetOwnerRef() throws Exception
	{
		ORef taskRef = project.createFactorAndReturnRef(Task.getObjectType());
		Task task = (Task)project.findObject(taskRef);
		
		ORef parentRef = project.createFactorAndReturnRef(Task.getObjectType());
		Task parent = (Task)project.findObject(parentRef);
		IdList children = new IdList(Task.getObjectType(), new BaseId[] {task.getId()});
		parent.setData(Task.TAG_SUBTASK_IDS, children.toString());
		assertEquals("Owner not detected?", parentRef, task.getOwnerRef());
	}

	public void testGetAllOwnedObjects() throws Exception
	{
		BaseId factorId = project.createFactorAndReturnId(ObjectType.CAUSE);
		project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
	   	ORef ownerRef = new ORef(ObjectType.THREAT_REDUCTION_RESULT, factorId);
	   	BaseObject ownerObject = project.findObject(ownerRef);	
	   	ORefList allOwnedObjects = ownerObject.getAllOwnedObjects();
	   	assertEquals("incorrect owned object count?", 2, allOwnedObjects.size());
	}

	ProjectForTesting project;
}
