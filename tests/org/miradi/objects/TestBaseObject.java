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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;

public class TestBaseObject extends TestCaseWithProject
{
	public TestBaseObject(String name)
	{
		super(name);
	}
	
	public void testGetOwnerRef() throws Exception
	{
		ORef taskRef = getProject().createFactorAndReturnRef(Task.getObjectType());
		Task task = (Task)getProject().findObject(taskRef);
		
		ORef parentRef = getProject().createFactorAndReturnRef(Task.getObjectType());
		Task parent = (Task)getProject().findObject(parentRef);
		IdList children = new IdList(Task.getObjectType(), new BaseId[] {task.getId()});
		parent.setData(Task.TAG_SUBTASK_IDS, children.toString());
		assertEquals("Owner not detected?", parentRef, task.getOwnerRef());
	}

	public void testGetAllOwnedObjects() throws Exception
	{
		ORef causeRef = getProject().createObject(ObjectType.CAUSE);
		getProject().addItemToFactorList(causeRef, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		getProject().addItemToFactorList(causeRef, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		ProgressReport progressReport = getProject().createProgressReport();
		getProject().fillObjectUsingCommand(causeRef, BaseObject.TAG_PROGRESS_REPORT_REFS, new ORefList(progressReport).toString());
		
	   	BaseObject ownerObject = getProject().findObject(causeRef);	
	   	ORefList allOwnedObjects = ownerObject.getAllOwnedObjects();
	   	assertEquals("incorrect owned object count?", 3, allOwnedObjects.size());
	}
	
	public void testGetReferredObjects() throws Exception
	{
		ORef taskRef = getProject().createObject(Task.getObjectType());
		Task task = Task.find(getProject(), taskRef);
		assertEquals("Had referenced objects?", 0, task.getAllReferencedObjects().size());
	}
	
	public void testIsPresentationDataField() throws Exception
	{
		String someNonUserDefinedTag = "SomeTag";
		Cause cause = getProject().createCause();
		cause.addPresentationDataField(someNonUserDefinedTag, new StringData(someNonUserDefinedTag));
		assertTrue("is user tag?" , cause.isPresentationDataField(someNonUserDefinedTag));
		assertFalse("is non user tag?", cause.isPresentationDataField(Cause.TAG_LABEL));
	}
	
	public void testEquals() throws Exception
	{
		Cause cause = getProject().createCause();
		Target target = new Target(getProject().getObjectManager(), cause.getFactorId());
		assertNotEquals("Target/Cause with same Id were equal?", cause, target);
	}
	
	public void testGetBudgetTotal() throws Exception
	{
		getProject().setProjectStartDate(2006);
		getProject().setProjectEndDate(2007);
		
		Indicator indicator = getProject().createIndicatorWithCauseParent();
		assertFalse("indicator should not have a total cost?", indicator.getTotalBudgetCost().hasValue());
		
		getProject().addResourceAssignment(indicator, 20.0, 2006, 2006);
		assertEquals("wrong total budget cost?", 200.0, indicator.getTotalBudgetCost().getValue());
	}
}
