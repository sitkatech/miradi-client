/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.HtmlUtilities;

public class TestBaseObject extends TestCaseWithProject
{
	public TestBaseObject(String name)
	{
		super(name);
	}
	
	public void testGetLabelsAsMultiline() throws Exception
	{
		ORef causeRef1 = getProject().createObject(ObjectType.CAUSE);
		Factor cause1 = Factor.findFactor(getProject(), causeRef1);
		ORef causeRef2 = getProject().createObject(ObjectType.CAUSE);
		Factor cause2 = Factor.findFactor(getProject(), causeRef2);

		getProject().setObjectData(cause1, BaseObject.TAG_LABEL, "Label 1");
		getProject().setObjectData(cause2, BaseObject.TAG_LABEL, "Label 2");
		
		FactorSet factors = new FactorSet();
		factors.attemptToAdd(cause1);
		factors.attemptToAdd(cause2);
		String labels = cause1.getLabelsAsMultiline(factors);
		assertNotContains("Newline in psuedo result?", "\n", labels);
		assertStartsWith("Didn't start with <ul>?", "<ul>", labels);
		assertContains("Didn't have cause1?", "<li>" + cause1.getLabel() + "</li>", labels);
		assertContains("Didn't have cause2?", "<li>" + cause2.getLabel() + "</li>", labels);
	}
	
	public void testSetHtmlDataFromNonHtml() throws Exception
	{
		ORef causeRef = getProject().createObject(ObjectType.CAUSE);
		BaseObject cause = Cause.find(getProject(), causeRef);
		String[] allPossibleValuesToConvertToHtml = new String[]{"a\nb", "&", "<", ">", "\\", "\""};
		for (int index = 0; index < allPossibleValuesToConvertToHtml.length; ++index)
		{
			String value = cause.getHtmlDataFromNonHtml(Cause.TAG_COMMENTS, allPossibleValuesToConvertToHtml[index]);
			cause.setData(Cause.TAG_COMMENTS, value);
			String data = HtmlUtilities.convertHtmlToPlainText(cause.getData(Cause.TAG_COMMENTS));
			assertEquals("data was not converted?", allPossibleValuesToConvertToHtml[index], data);
		}
	}
	
	public void testLoadFromJson() throws Exception
	{
		final String sampleCommentJson = "{\"" + Cause.TAG_COMMENTS + "\":\"A sample comment with \\n a new line.\"}";
		ORef causeRef = getProject().createObject(ObjectType.CAUSE);
		BaseObject cause = Cause.find(getProject(), causeRef);
		cause.loadFromJson(new EnhancedJsonObject(sampleCommentJson));
		String comments = cause.getData(Cause.TAG_COMMENTS);
		assertContains("does not contain <br>?", HtmlUtilities.BR_TAG, comments);
		assertNotContains("should not contain non html new line?", "\n", comments);
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
		String result = doesNotMatterWhichObject.getLabelsAsMultiline(refs);
		int firstAt = result.indexOf(FIRST_STRING);
		int middleAt = result.indexOf(MIDDLE_STRING);
		int lastAt = result.indexOf(LAST_STRING);
		assertTrue("First not first in " + result + "?", firstAt < middleAt);
		assertTrue("Last not last " + result + "?", middleAt < lastAt);
		assertNotContains("\n", result);
		assertContains(HtmlUtilities.UL_START_TAG, result);
		assertContains(HtmlUtilities.LI_START_TAG + FIRST_STRING, result);
		assertContains(HtmlUtilities.LI_START_TAG + MIDDLE_STRING, result);
		assertContains(HtmlUtilities.LI_START_TAG + LAST_STRING, result);
	}
	
	public void testGetOwnerRef() throws Exception
	{
		ORef taskRef = getProject().createObject(TaskSchema.getObjectType());
		Task task = (Task)getProject().findObject(taskRef);
		
		ORef parentRef = getProject().createObject(TaskSchema.getObjectType());
		Task parent = (Task)getProject().findObject(parentRef);
		IdList children = new IdList(TaskSchema.getObjectType(), new BaseId[] {task.getId()});
		parent.setData(Task.TAG_SUBTASK_IDS, children.toString());
		assertEquals("Owner not detected?", parentRef, task.getOwnerRef());
	}

	public void testGetAllOwnedObjects() throws Exception
	{
		ORef causeRef = getProject().createObject(ObjectType.CAUSE);
		getProject().addItemToFactorList(causeRef, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		getProject().addItemToFactorList(causeRef, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
	   	BaseObject ownerObject = getProject().findObject(causeRef);	
	   	ORefList allOwnedObjects = ownerObject.getOwnedObjectRefs();
	   	assertEquals("incorrect owned object count?", 2, allOwnedObjects.size());
	}
	
	public void testGetReferredObjects() throws Exception
	{
		ORef taskRef = getProject().createObject(TaskSchema.getObjectType());
		Task task = Task.find(getProject(), taskRef);
		assertEquals("Had referenced objects?", 0, task.getAllReferencedObjects().size());
		
		ResourceAssignment resourceAssignment = getProject().createResourceAssignment();
		final ORefSet allReferencedObjects = resourceAssignment.getAllReferencedObjects();
		assertEquals("Had referenced objects?", 0, allReferencedObjects.size());
	}
	
	public void testIsNavigationField() throws Exception
	{
		Cause cause = getProject().createCause();
		cause.setIsNavigationField(Cause.TAG_TAXONOMY_CODE);
		assertTrue("is user tag?" , cause.isNavigationField(Cause.TAG_TAXONOMY_CODE));
		assertFalse("is non user tag?", cause.isNavigationField(Cause.TAG_LABEL));
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
	}
	
	public void testGetRefList() throws Exception
	{
		Strategy strategy = getProject().createAndPopulateStrategy();
		final ORefList objectiveRefs = strategy.getSafeRefListData(Strategy.TAG_OBJECTIVE_IDS);
		assertTrue("strategy should have objectives?", objectiveRefs.hasRefs());
		
		final ORefList subtargetRefs = strategy.getSafeRefListData(Target.TAG_SUB_TARGET_REFS);
		assertNotNull("even though its not a strategy field, a null should not be returned?", subtargetRefs);
		assertFalse("strategy should have subtargets?", subtargetRefs.hasRefs());
	}
}
