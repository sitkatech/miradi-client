/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.martus.util.MultiCalendar;
import org.miradi.commands.TestCommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.*;
import org.miradi.project.ProjectForTesting;
import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.DateRange;

public class TestTask extends AbstractObjectWithBudgetDataToDeleteTestCase
{
	public TestTask(String name)
	{
		super(name);
	}
	
	@Override
	protected int getType()
	{
		return TaskSchema.getObjectType();
	}
	
	public void testIsAssignmentDataSuperseded() throws Exception
	{
		Task activity = getProject().createActivity();
		verifyIsAssignmentDataSuperseded(getProject(), activity, Task.TAG_SUBTASK_IDS);
	}

	public static void verifyIsAssignmentDataSuperseded(ProjectForTesting projectToUse, BaseObject parentOfTask, String tagSubtaskIdsTag) throws Exception
	{
		projectToUse.setProjectStartDate(2005);
		projectToUse.setProjectEndDate(2006);
		DateUnit projectTotal = new DateUnit();
		
		assertFalse("parent without subtasks is superseded?", isAssignmentDataSuperseded(parentOfTask, dateUnit2006));
		
		ResourceAssignment parentOfTaskResourceAssignment2005 = projectToUse.addResourceAssignment(parentOfTask, 1.0, 2005, 2005);
		ResourceAssignment parentOfTaskResourceAssignment2006 = projectToUse.addResourceAssignment(parentOfTask, 1.0, 2006, 2006);
		
		ExpenseAssignment parentOfTaskExpenseAssignment2005 = projectToUse.addExpenseAssignment(parentOfTask, dateUnit2005, 2.0);
		ExpenseAssignment parentOfTaskExpenseAssignment2006 = projectToUse.addExpenseAssignment(parentOfTask, dateUnit2006, 2.0);
		ExpenseAssignment parentOfTaskExpenseAssignmentTotal = projectToUse.addExpenseAssignment(parentOfTask, projectTotal, 5);
		
		assertFalse("resource assignment is superseded?", isAssignmentDataSuperseded(parentOfTaskResourceAssignment2006, dateUnit2006));
		assertFalse("expense assignment is superseded?", isAssignmentDataSuperseded(parentOfTaskExpenseAssignment2006, dateUnit2006));
		assertFalse("expense assignment is superseded?", isAssignmentDataSuperseded(parentOfTaskExpenseAssignmentTotal, projectTotal));
		
		Task subTask = addSubtask(projectToUse, parentOfTask, tagSubtaskIdsTag);
		ResourceAssignment subTaskResourceAssignment = projectToUse.addResourceAssignment(subTask, 10.0, 2005, 2005);
		ExpenseAssignment subTaskExpenseAssignment = projectToUse.addExpenseAssignment(subTask, dateUnit2005, 1.0);
		assertFalse("subtask's resource assignment is superseded?", isAssignmentDataSuperseded(subTaskResourceAssignment, dateUnit2005));
		assertFalse("subtask's expense assignment is superseded?", isAssignmentDataSuperseded(subTaskExpenseAssignment, dateUnit2005));
		assertTrue("parent of task resource assignment is not superseded?", isAssignmentDataSuperseded(parentOfTaskResourceAssignment2005, dateUnit2005));
		assertTrue("parent of task expense assignment is not superseded?", isAssignmentDataSuperseded(parentOfTaskExpenseAssignment2005, dateUnit2005));
		assertTrue("parent of task expense assignment is not superseded?", isAssignmentDataSuperseded(parentOfTaskExpenseAssignmentTotal, projectTotal));
		
		Task supersedingSubTask = addSubtask(projectToUse, parentOfTask, tagSubtaskIdsTag);
		projectToUse.addResourceAssignment(supersedingSubTask, 10.0, 2006, 2006);
		projectToUse.addExpenseAssignment(supersedingSubTask, dateUnit2006, 1.0);
		assertTrue("resource assignment is not superseded?", isAssignmentDataSuperseded(parentOfTaskResourceAssignment2006, dateUnit2006));
		assertTrue("expense assignment is not superseded?", isAssignmentDataSuperseded(parentOfTaskExpenseAssignment2006, dateUnit2006));
	}

	public void testBasics() throws Exception
	{
		verifyFields(getType());
		BaseId id = new BaseId(5);
		
		Task task = new Task(getObjectManager(), new FactorId(id.asInt()));
		assertEquals("bad id?", id, task.getId());
		
		String label = "Name of task";
		task.setData(Task.TAG_LABEL, label);
		assertEquals("bad label?", label, task.getData(Task.TAG_LABEL));
		
		Task sameTask = new Task(getObjectManager(), new FactorId(id.asInt()));
		assertEquals("same ids not equal?", task, sameTask);
		Task otherTask = new Task(getObjectManager(), new FactorId(id.asInt()+1));
		otherTask.setData(Task.TAG_LABEL, label);
		assertNotEquals("different ids are equal?", task, otherTask);
	}
	
	public void testData() throws Exception
	{
		IdList sampleIds = new IdList(TaskSchema.getObjectType());
		sampleIds.add(1);
		sampleIds.add(1527);
		String sampleIdData = sampleIds.toString(); 
		Task task = new Task(getObjectManager(), new FactorId(0));
		task.setData(Task.TAG_SUBTASK_IDS, sampleIdData);
		assertEquals("bad data?", sampleIdData, task.getData(Task.TAG_SUBTASK_IDS));
	}
	
	public void testNesting() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();
		Task top = new Task(getObjectManager(), new FactorId(idAssigner.takeNextId().asInt()));
		Task child1 = new Task(getObjectManager(), new FactorId(idAssigner.takeNextId().asInt()));
		Task child2 = new Task(getObjectManager(), new FactorId(idAssigner.takeNextId().asInt()));
		Task grandchild21 = new Task(getObjectManager(), new FactorId(idAssigner.takeNextId().asInt()));
		
		TestCommandSetObjectData.addSubtaskId(top, child1.getId());
		TestCommandSetObjectData.addSubtaskId(top, child2.getId());
		TestCommandSetObjectData.addSubtaskId(child2, grandchild21.getId());
		
		assertEquals("wrong subtask count?", 2, top.getSubtaskCount());
		assertEquals("not zero subtasks?", 0, child1.getSubtaskCount());
		assertEquals("wrong child1?", child1.getId(), top.getSubtaskId(0));
		assertEquals("wrong child2?", child2.getId(), top.getSubtaskId(1));
	}
	
	public void testSubtaskIdList() throws Exception
	{
		Task parent = createBasicTree();
		
		IdList ids = parent.getSubtaskIdList();
		assertEquals("wrong count?", 2, ids.size());
		assertEquals("wrong 1?", parent.getSubtaskId(0), ids.get(0));
		assertEquals("wrong 2?", parent.getSubtaskId(1), ids.get(1));
		
		IdList shouldBeCopy = parent.getSubtaskIdList();
		shouldBeCopy.add(2727);
		assertEquals("modified the actual list?", 2, parent.getSubtaskIdList().size());
	}
	
	private Task createBasicTree() throws Exception
	{
		Task parent = new Task(getObjectManager(), new FactorId(1));
		Task child1 = new Task(getObjectManager(), new FactorId(2));
		Task child2 = new Task(getObjectManager(), new FactorId(3));
		TestCommandSetObjectData.addSubtaskId(parent, child1.getId());
		TestCommandSetObjectData.addSubtaskId(parent, child2.getId());
		return parent;
	}
	
	public void testGetCombinedEffortDates() throws Exception
	{
		getProject().setProjectStartDate(2002);
		getProject().setProjectEndDate(2007);
		
		Task activityWithNoSubtasksNoAssignment = getProject().createActivity(); 
		DateRange combinedDateRange = activityWithNoSubtasksNoAssignment.getAssignedWhenRollup();
		assertEquals("combined date range is not null?", null, combinedDateRange);
		
		Task activityWithNoSubTasksWithAssignment = getProject().createActivity();
		addAssignment(activityWithNoSubTasksWithAssignment, 1.0, 2006, 2006);
		assertEquals("assignment was not added?", 1, activityWithNoSubTasksWithAssignment.getSafeIdListData(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS).size());
		DateRange whenRollup = activityWithNoSubTasksWithAssignment.getAssignedWhenRollup();
		assertEquals("wrong combined date range?", "2006", whenRollup.toString());
		
		Task activityWithoutUnits = getProject().createActivity();
		addAssignment(activityWithoutUnits, 0, 2003, 2003);
		assertEquals("assignment was not added?", 1, activityWithoutUnits.getSafeIdListData(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS).size());
		assertEquals("wrong combined date range?", "2003", activityWithoutUnits.getAssignedWhenRollup().toString());
		
		Task taskWithSubtasks = getProject().createActivity();
		Task subTask = addSubTask(taskWithSubtasks);
		assertEquals("sub task combined date range was not null?", null, taskWithSubtasks.getAssignedWhenRollup());
		
		
		MultiCalendar projectStartDate = ProjectForTesting.createStartYear(1995);
		MultiCalendar projectEndDate = ProjectForTesting.createEndYear(2011);
		getProject().setProjectDate(projectStartDate, ProjectMetadata.TAG_START_DATE);
		getProject().setProjectDate(projectEndDate, ProjectMetadata.TAG_EXPECTED_END_DATE);
		addAssignment(subTask, 1.0, 2000, 2010);
		addAssignment(subTask, 1.0, 1995, 1998);
		addAssignment(subTask, 0, 2011, 2013);
		assertEquals("wrong sub task combined date range?", getProject().createDateRange(projectStartDate, projectEndDate), taskWithSubtasks.getAssignedWhenRollup());
	}

	private Task addSubTask(BaseObject parentOfTask) throws Exception
	{
		return addSubtask(getProject(), parentOfTask, Task.TAG_SUBTASK_IDS);
	}

	private static Task addSubtask(ProjectForTesting projectToUse, BaseObject parentOfTask, String tagSubtaskIds) throws Exception
	{
		Task subtask = projectToUse.createTask(parentOfTask);
		
		return subtask;
	}
	
	private void addAssignment(BaseObject parentOfAssignment, double units, int startYear, int endYear) throws Exception
	{
		getProject().addResourceAssignment(parentOfAssignment, units, startYear, endYear);
	}

	public void testGetWorkUnitsForTaskWithoutSubTasks() throws Exception
	{
		getProject().setProjectStartDate(2009);
		getProject().setProjectEndDate(2011);
		
		Task task = getProject().createActivity();
		addAssignment(task, 5, 2009, 2009);
		addAssignment(task, 15, 2010, 2010);
		
		DateUnit dateUnit = getProject().createDateUnit(2010, 2010);
		assertEquals("wrong task work units for date range?", 15.0, ProjectForTesting.calculateTimePeriodCosts(task, dateUnit));
	}
	
	public void testGetWorkUnitsForTaskWithSubTasks() throws Exception
	{		
		getProject().setProjectDates(1999, 2012);

		Task task = getProject().createActivity();
		DateUnit projectDateUnit = new DateUnit();
		assertFalse("Empty task has work unit values?", task.calculateTimePeriodCostsForAssignments(projectDateUnit).getTotalWorkUnits().hasValue());
		addAssignment(task, 99, 2000, 2010);
		
		Task subTask = addSubTask(task);
		addAssignment(subTask, 5, 2010, 2010);
		addAssignment(subTask, 15, 2005, 2005);

		DateUnit dateUnit = getProject().createDateUnit(2005);
		assertEquals("wrong subtask work units for date range?", 15.0, ProjectForTesting.calculateTimePeriodCosts(task, dateUnit));
		
		DateUnit dateUnit1 = getProject().createDateUnit(2010);
		assertEquals("wrong subtask work units for date range?", 5.0, ProjectForTesting.calculateTimePeriodCosts(task, dateUnit1));
	}
	
	public void testGetRelevantIndicatorRefList() throws Exception
	{
		ORef strategyRef = getProject().createObject(ObjectType.STRATEGY);
		Strategy strategy = Strategy.find(getProject(), strategyRef);
		BaseId indicatorId = getProject().addItemToFactorList(strategyRef, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		ORef indicatorRef = getProject().findObject(ObjectType.INDICATOR, indicatorId).getRef();
		Task activity = getProject().createActivity(strategy);

		assertEquals("wrong indicator count?", 0, activity.getIndicatorsOnSameFactor().size());

		verifyRelevancy(indicatorRef, activity, true, 1);
		verifyRelevancy(indicatorRef, activity, false, 0);
	}

	private void verifyRelevancy(ORef indicatorRef, Task activity, boolean overrideBoolean, int expectedValue) throws Exception
	{
		RelevancyOverrideSet relevancyOverrides = new RelevancyOverrideSet();
		relevancyOverrides.add(new RelevancyOverride(activity.getRef(), overrideBoolean));
		Indicator indicator = Indicator.find(getProject(), indicatorRef);
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyOverrides.toString());
		assertEquals("wrong indicator count?", expectedValue, activity.getRelevantIndicatorRefList().size());
	}

	private static boolean isAssignmentDataSuperseded(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		return baseObject.hasAnyChildTaskResourceData(dateUnit);
	}

	private static boolean isAssignmentDataSuperseded(ResourceAssignment assignment, DateUnit dateUnit) throws Exception
	{
		return assignment.getOwner().hasAnyChildTaskResourceData(dateUnit);
	}

	private static boolean isAssignmentDataSuperseded(ExpenseAssignment assignment, DateUnit dateUnit) throws Exception
	{
		return assignment.getOwner().hasAnyChildTaskExpenseData(dateUnit);
	}

	private final static DateUnit dateUnit2006 = new DateUnit("YEARFROM:2006-01");
	private final static DateUnit dateUnit2005 = new DateUnit("YEARFROM:2005-01");
}

