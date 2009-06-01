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

import org.martus.util.MultiCalendar;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;

public class TestTask extends ObjectTestCase
{
	public TestTask(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		verifyFields(ObjectType.TASK);
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
		IdList sampleIds = new IdList(Task.getObjectType());
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
		
		top.addSubtaskId(child1.getId());
		top.addSubtaskId(child2.getId());
		child2.addSubtaskId(grandchild21.getId());
		
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
	
	public void testJson() throws Exception
	{
		Task parent = createBasicTree();
		
		Task got = (Task)BaseObject.createFromJson(getObjectManager(), parent.getType(), parent.toJson());
		assertEquals("wrong count?", parent.getSubtaskCount(), got.getSubtaskCount());
	}
	
	public void testGetChildTaskTypeCode()
	{
		assertEquals(Task.ACTIVITY_NAME, Task.getChildTaskTypeCode(Strategy.getObjectType()));
		assertEquals(Task.METHOD_NAME, Task.getChildTaskTypeCode(Indicator.getObjectType()));
		assertEquals(Task.OBJECT_NAME, Task.getChildTaskTypeCode(Task.getObjectType()));
		assertEquals(Task.OBJECT_NAME, Task.getChildTaskTypeCode(AccountingCode.getObjectType()));
	}

	private Task createBasicTree() throws Exception
	{
		Task parent = new Task(getObjectManager(), new FactorId(1));
		Task child1 = new Task(getObjectManager(), new FactorId(2));
		Task child2 = new Task(getObjectManager(), new FactorId(3));
		parent.addSubtaskId(child1.getId());
		parent.addSubtaskId(child2.getId());
		return parent;
	}
	
	public void testGetCombinedEffortDates() throws Exception
	{
		Task taskWithNoSubtasksNoAssignment = createTask(); 
		DateRange combinedDateRange = taskWithNoSubtasksNoAssignment.getWhenRollup();
		assertEquals("combined date range is not null?", null, combinedDateRange);
		
		Task taskWithNoSubTasksWithAssignment = createTask();
		addAssignment(taskWithNoSubTasksWithAssignment, 1.0, 2006, 2006);
		assertEquals("assignment was not added?", 1, taskWithNoSubTasksWithAssignment.getAssignmentIdList().size());
		DateRange whenRollup = taskWithNoSubTasksWithAssignment.getWhenRollup();
		assertEquals("wrong combined date range?", "2006", whenRollup.toString());
		
		Task taskWithoutUnits = createTask();
		addAssignment(taskWithoutUnits, 0, 2003, 2003);
		assertEquals("assignment was not added?", 1, taskWithoutUnits.getAssignmentIdList().size());
		assertEquals("wrong combined date range?", "2003", taskWithoutUnits.getWhenRollup().toString());
		
		Task taskWithSubtasks = createTask();
		Task subTask = createTask();
		IdList subTaskIds = new IdList(Task.getObjectType());
		subTaskIds.add(subTask.getId());
		taskWithSubtasks.setData(Task.TAG_SUBTASK_IDS, subTaskIds.toString());
		assertEquals("sub task combined date range was not null?", null, taskWithSubtasks.getWhenRollup());
		
		
		MultiCalendar projectStartDate = ProjectForTesting.createStartYear(1995);
		MultiCalendar projectEndDate = ProjectForTesting.createEndYear(2011);
		getProject().setProjectDate(projectStartDate, ProjectMetadata.TAG_START_DATE);
		getProject().setProjectDate(projectEndDate, ProjectMetadata.TAG_EXPECTED_END_DATE);
		addAssignment(subTask, 1.0, 2000, 2010);
		addAssignment(subTask, 1.0, 1995, 1998);
		addAssignment(subTask, 0, 2011, 2013);
		assertEquals("wrong sub task combined date range?", getProject().createDateRange(projectStartDate, projectEndDate), taskWithSubtasks.getWhenRollup());
	}
	
	private void addAssignment(Task task, double units, int startYear, int endYear) throws Exception
	{
		getProject().addResourceAssignment(task, units, startYear, endYear);
	}
	
	private Task createTask() throws Exception
	{
		return getProject().createTask();
	}

	public DateUnitEffort createDateUnitEffort(int startYear, int endYear) throws Exception
	{
		return getProject().createDateUnitEffort(startYear, endYear);
	}
	
	public void testGetWorkUnitsForTaskWithoutSubTasks() throws Exception
	{
		Task task = createTask();
		addAssignment(task, 5, 2009, 2009);
		addAssignment(task, 15, 2010, 2010);
		
		DateUnit dateUnit = getProject().createDateUnit(2010, 2010);
		assertEquals("wrong task work units for date range?", 15.0, task.getWorkUnits(dateUnit).getValue());
	}
	
	public void testGetWorkUnitsForTaskWithSubTasks() throws Exception
	{		
		getProject().setProjectDates(1999, 2012);

		Task task = createTask();
		DateUnit projectDateUnit = new DateUnit();
		assertFalse("Empty task has work unit values?", task.getWorkUnits(projectDateUnit).hasValue());
		addAssignment(task, 99, 2000, 2010);
		
		Task subTask = createTask();
		IdList subTaskIds = new IdList(Task.getObjectType());
		subTaskIds.add(subTask.getId());
		task.setData(Task.TAG_SUBTASK_IDS, subTaskIds.toString());
		addAssignment(subTask, 5, 2010, 2010);
		addAssignment(subTask, 15, 2005, 2005);

		DateUnit dateUnit = getProject().createDateUnit(2005);
		assertEquals("wrong subtask work units for date range?", 15.0, task.getWorkUnits(dateUnit).getValue());
		
		DateUnit dateUnit1 = getProject().createDateUnit(2010);
		assertEquals("wrong subtask work units for date range?", 5.0, task.getWorkUnits(dateUnit1).getValue());
	}
	
	public MultiCalendar createMultiCalendar(int year)
	{
		return getProject().createMultiCalendar(year);
	}
}

