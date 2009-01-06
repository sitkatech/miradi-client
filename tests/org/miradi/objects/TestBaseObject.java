/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAMTestCase;
import org.miradi.objectdata.StringData;
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
		ORef causeRef = project.createObject(ObjectType.CAUSE);
		project.addItemToFactorList(causeRef, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		project.addItemToFactorList(causeRef, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
	   	BaseObject ownerObject = project.findObject(causeRef);	
	   	ORefList allOwnedObjects = ownerObject.getAllOwnedObjects();
	   	assertEquals("incorrect owned object count?", 2, allOwnedObjects.size());
	}
	
	public void testIsRefList() throws Exception
	{
		ORef taskRef = project.createObject(Task.getObjectType());
		Task task = Task.find(project, taskRef);
		assertTrue("is tag refList?", task.isRefList(BaseObject.TAG_WHO_OVERRIDE_REFS));	
	}

	public void testGetAnnotationType() throws Exception
	{
		ORef taskRef = project.createObject(Task.getObjectType());
		Task task = Task.find(project, taskRef);
		assertEquals("wrong annotation type?", ProjectResource.getObjectType(), task.getAnnotationType(BaseObject.TAG_WHO_OVERRIDE_REFS));	
	}
	
	public void testGetReferredObjects() throws Exception
	{
		ORef taskRef = project.createObject(Task.getObjectType());
		Task task = Task.find(project, taskRef);
		assertEquals("Had referenced objects?", 0, task.getAllReferencedObjects().size());
	}
	
	public void testIsUserDataCommand() throws Exception
	{
		String someNonUserDefinedTag = "SomeTag";
		Cause cause = project.createCause();
		cause.addNonUserDataField(someNonUserDefinedTag, new StringData(someNonUserDefinedTag));
		assertTrue("is user tag?" , cause.isUserDataCommand(someNonUserDefinedTag));
		assertFalse("is non user tag?", cause.isUserDataCommand(Cause.TAG_LABEL));
	}
	
	ProjectForTesting project;
}
