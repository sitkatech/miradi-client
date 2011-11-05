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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;

public class TestBaseObject extends TestCaseWithProject
{
	public TestBaseObject(String name)
	{
		super(name);
	}
	
	public void testGetBaseObjectLabelsOnASingleLine() throws Exception
	{
		final String FIRST_STRING = "first";
		final String MIDDLE_STRING = "middle";
		final String LAST_STRING = "ZLast";

		ORef causeMiddleRef = getProject().createObject(ObjectType.CAUSE);
		getProject().setObjectData(causeMiddleRef, BaseObject.TAG_LABEL, MIDDLE_STRING);
		ORef causeLastRef = getProject().createObject(ObjectType.CAUSE);
		getProject().setObjectData(causeLastRef, BaseObject.TAG_LABEL, LAST_STRING);
		ORef causeFirstRef = getProject().createObject(ObjectType.CAUSE);
		getProject().setObjectData(causeFirstRef, BaseObject.TAG_LABEL, FIRST_STRING);

		BaseObject doesNotMatterWhichObject = Cause.find(getProject(), causeFirstRef);
		ORefList refs = new ORefList();
		refs.add(causeMiddleRef);
		refs.add(causeLastRef);
		refs.add(causeFirstRef);
		String result = doesNotMatterWhichObject.getBaseObjectLabelsOnASingleLine(refs);
		int firstAt = result.indexOf(FIRST_STRING);
		int middleAt = result.indexOf(MIDDLE_STRING);
		int lastAt = result.indexOf(LAST_STRING);
		assertTrue("First not first in " + result + "?", firstAt < middleAt);
		assertTrue("Last not last " + result + "?", middleAt < lastAt);
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
		Cause cause = getProject().createCause();
		cause.setNonUserField(Cause.TAG_TAXONOMY_CODE);
		assertTrue("is user tag?" , cause.isNonUserField(Cause.TAG_TAXONOMY_CODE));
		assertFalse("is non user tag?", cause.isNonUserField(Cause.TAG_LABEL));
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
