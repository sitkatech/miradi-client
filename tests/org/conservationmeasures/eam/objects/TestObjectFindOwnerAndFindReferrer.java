/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestObjectFindOwnerAndFindReferrer extends EAMTestCase
{
	public TestObjectFindOwnerAndFindReferrer(String name)
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
	}


	//TODO: not hooked in to main test as it is still in process of being wrttien
	public void testFactorOwn() throws Exception
	{
		BaseId factorId = project.createFactor(Factor.TYPE_STRATEGY);
		
		BaseId indicatorId = project.createObject(ObjectType.INDICATOR);
		IdList indicatorList = new IdList(new BaseId[] {indicatorId});
		project.setObjectData(ObjectType.FACTOR, factorId, Factor.TAG_INDICATOR_IDS, indicatorList.toString());
		BaseObject indicator = project.findObject(ObjectType.INDICATOR, indicatorId);

		BaseId goalId = project.createObject(ObjectType.GOAL);
		IdList goalList = new IdList(new BaseId[] {goalId});
		project.setObjectData(ObjectType.FACTOR, factorId, Factor.TAG_GOAL_IDS, goalList.toString());
		BaseObject goal = project.findObject(ObjectType.GOAL, goalId);
		
		
		BaseId objectiveId = project.createObject(ObjectType.OBJECTIVE);
		IdList objectiveList = new IdList(new BaseId[] {objectiveId});
		project.setObjectData(ObjectType.FACTOR, factorId, Factor.TAG_OBJECTIVE_IDS, objectiveList.toString());
		BaseObject objective = project.findObject(ObjectType.OBJECTIVE, objectiveId);
		
		
		BaseId keaId = project.createObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
		IdList keaList = new IdList(new BaseId[] {keaId});
		project.setObjectData(ObjectType.FACTOR, factorId, Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaList.toString());
		BaseObject kea = project.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keaId);
		
		
		BaseId taskId = project.createTask(new ORef(ObjectType.FACTOR, factorId));
		IdList taskList = new IdList(new BaseId[] {taskId});
		project.setObjectData(ObjectType.FACTOR, factorId, Strategy.TAG_ACTIVITY_IDS, taskList.toString());
		BaseObject task = project.findObject(ObjectType.TASK, taskId);
		
		//----------- start test -----------
		
		ORef orefIndicator = BaseObject.findObjectWhoOwnesUs(project, ObjectType.FACTOR, indicator.getRef());
		assertNotNull(orefIndicator);
		BaseObject ownerOfIndicator = project.findObject(orefIndicator);
		assertEquals(factorId, ownerOfIndicator.getId());
		
		ORef orefGoal = BaseObject.findObjectWhoOwnesUs(project, ObjectType.FACTOR, goal.getRef());
		assertNotNull(orefGoal);
		BaseObject ownerOfGoal = project.findObject(orefGoal);
		assertEquals(factorId, ownerOfGoal.getId());
		
		ORef orefObjective = BaseObject.findObjectWhoOwnesUs(project, ObjectType.FACTOR, objective.getRef());
		assertNotNull(orefObjective);
		BaseObject ownerOfObjective = project.findObject(orefObjective);
		assertEquals(factorId, ownerOfObjective.getId());
		
		ORef orefKea = BaseObject.findObjectWhoOwnesUs(project, ObjectType.FACTOR, kea.getRef());
		assertNotNull(orefKea);
		BaseObject ownerOfKea = project.findObject(orefKea);
		assertEquals(factorId, ownerOfKea.getId());
		
		ORef orefTask = BaseObject.findObjectWhoOwnesUs(project, ObjectType.FACTOR, task.getRef());
		assertNotNull(orefTask);
		BaseObject ownerOfTask = project.findObject(orefTask);
		assertEquals(factorId, ownerOfTask.getId());
	}
	
	
	public void testTaskOwn() throws Exception
	{
		BaseId factorId = project.createFactor(Factor.TYPE_STRATEGY);
		BaseId taskId = project.createTask(new ORef(ObjectType.FACTOR,factorId));
		BaseId subTaskId = project.createTask(new ORef(ObjectType.TASK,taskId));
		BaseId assignmentId = project.createAssignment(new ORef(ObjectType.TASK,taskId));
		
		IdList taskList = new IdList(new BaseId[] {taskId});
		project.setObjectData(ObjectType.FACTOR, factorId, Strategy.TAG_ACTIVITY_IDS, taskList.toString());
		
		IdList subTaskList = new IdList(new BaseId[] {subTaskId});
		project.setObjectData(ObjectType.TASK, taskId, Task.TAG_SUBTASK_IDS, subTaskList.toString());

		IdList assignmentList = new IdList(new BaseId[] {assignmentId});
		project.setObjectData(ObjectType.TASK, taskId, Task.TAG_ASSIGNMENT_IDS, assignmentList.toString());

		//----------- start test -----------
		
		ORef orefFactor = BaseObject.findObjectWhoOwnesUs(project, ObjectType.FACTOR, new ORef(ObjectType.TASK, taskId));
		assertNotNull(orefFactor);
		BaseObject ownerOfTask = project.findObject(orefFactor);
		assertEquals(ownerOfTask.getRef(), orefFactor);
		
		ORef orefTask = BaseObject.findObjectWhoOwnesUs(project, ObjectType.TASK, new ORef(ObjectType.TASK, subTaskId));
		assertNotNull(orefTask);
		BaseObject ownerOfSubTask = project.findObject(orefTask);
		assertEquals(ownerOfSubTask.getRef(), orefTask);
		
		ORef orefAssignment = BaseObject.findObjectWhoOwnesUs(project, ObjectType.TASK, new ORef(ObjectType.ASSIGNMENT, assignmentId));
		assertNotNull(orefAssignment);
		BaseObject ownerOfAssignment = project.findObject(orefAssignment);
		assertEquals(ownerOfAssignment.getRef(), orefTask);
		
	}
	
	public void testTaskRefer() throws Exception
	{
		BaseId factorId = project.createFactor(Factor.TYPE_STRATEGY);
		BaseId taskId = project.createTask(new ORef(ObjectType.FACTOR,factorId));
		BaseId subTaskId = project.createTask(new ORef(ObjectType.TASK,taskId));
		Task subTask = (Task)project.findObject(ObjectType.TASK, subTaskId);
		
		ORefList orefs = BaseObject.findObjectThatReferToUs(project, ObjectType.TASK, new ORef(ObjectType.TASK, taskId));
		assertEquals(1,orefs.size());
		assertEquals(subTask.getRef(), orefs.get(0));
	}
	
	ProjectForTesting project;
}
