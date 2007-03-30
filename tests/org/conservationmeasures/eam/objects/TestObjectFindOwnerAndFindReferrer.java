/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
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
		
		verifyOwner(factorId, ObjectType.FACTOR, indicator.getRef());
		verifyOwner(factorId, ObjectType.FACTOR, goal.getRef());
		verifyOwner(factorId, ObjectType.FACTOR, objective.getRef());
		verifyOwner(factorId, ObjectType.FACTOR, kea.getRef());
		verifyOwner(factorId, ObjectType.FACTOR, task.getRef());
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
		
		verifyOwner(taskId, ObjectType.TASK, new ORef(ObjectType.TASK, subTaskId));
		verifyOwner(taskId, ObjectType.TASK, new ORef(ObjectType.ASSIGNMENT, assignmentId));
		
	}
	
	public void testTaskRefer() throws Exception
	{
		BaseId factorId = project.createFactor(Factor.TYPE_STRATEGY);
		BaseId taskId = project.createTask(new ORef(ObjectType.FACTOR,factorId));
		BaseId subTaskId = project.createTask(new ORef(ObjectType.TASK,taskId));
		
		//----------- start test -----------
		
		vertifyRefer(subTaskId, ObjectType.TASK, new ORef(ObjectType.TASK, taskId));
	}
	
	
	public void testAssignmentRefer() throws Exception
	{
		BaseId factorId = project.createFactor(Factor.TYPE_STRATEGY);
		BaseId taskId = project.createTask(new ORef(ObjectType.FACTOR,factorId));
		BaseId assignmentId = project.createAssignment(new ORef(ObjectType.TASK,taskId));
		
		BaseId projectResourceId = project.createObject(ObjectType.PROJECT_RESOURCE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, projectResourceId.toString());
		
		BaseId accountingCodeId = project.createObject(ObjectType.ACCOUNTING_CODE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ACCOUNTING_CODE, accountingCodeId.toString());
		
		BaseId fundingSourceId = project.createObject(ObjectType.FUNDING_SOURCE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE, fundingSourceId.toString());
		
		BaseId subTaskId = project.createObject(ObjectType.PROJECT_RESOURCE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_TASK_ID, subTaskId.toString());
		
		//----------- start test -----------
		
		vertifyRefer(assignmentId, ObjectType.ASSIGNMENT, new ORef(ObjectType.PROJECT_RESOURCE, projectResourceId));
		vertifyRefer(assignmentId, ObjectType.ASSIGNMENT, new ORef(ObjectType.ACCOUNTING_CODE, accountingCodeId));
		vertifyRefer(assignmentId, ObjectType.ASSIGNMENT, new ORef(ObjectType.FUNDING_SOURCE, fundingSourceId));
		vertifyRefer(assignmentId, ObjectType.ASSIGNMENT, new ORef(ObjectType.TASK, subTaskId));
	}


	public void testDiagramFactorRefer() throws Exception
	{
		DiagramFactorId diagramFactorId = project.createAndAddFactorToDiagram(Factor.TYPE_STRATEGY);
		DiagramFactor diagramFactor = (DiagramFactor)project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		ORef orefFactor = diagramFactor.getReferencedObjects(ObjectType.FACTOR).get(0);
		
		//----------- start test -----------
		
		vertifyRefer(diagramFactorId, ObjectType.DIAGRAM_FACTOR, orefFactor);
	}
	
	
	private void vertifyRefer(BaseId assignmentId, final int type, final ORef ref)
	{
		ORefList orefsIds = BaseObject.findObjectThatReferToUs(project, type, ref);
		assertEquals(1,orefsIds.size());
		assertEquals(assignmentId, orefsIds.get(0).getObjectId());
	}
	
	private void verifyOwner(BaseId factorId, final int type, final ORef ref)
	{
		ORef oref = BaseObject.findObjectWhoOwnesUs(project, type, ref);
		assertEquals(new ORef(type,factorId), oref);
	}
	
	
	ProjectForTesting project;
}
