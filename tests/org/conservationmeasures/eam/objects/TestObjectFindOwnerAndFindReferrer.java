/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
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
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId goalId = project.addItemToFactorList(factorId, ObjectType.GOAL, Factor.TAG_GOAL_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		BaseId keaId = project.addItemToFactorList(factorId, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		
		BaseId taskId = project.createTask(new ORef(ObjectType.FACTOR, factorId));
		IdList taskList = new IdList(new BaseId[] {taskId});
		project.setObjectData(ObjectType.FACTOR, factorId, Strategy.TAG_ACTIVITY_IDS, taskList.toString());
		
		//----------- start test -----------
		
		verifyOwner(factorId, ObjectType.FACTOR, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwner(factorId, ObjectType.FACTOR, new ORef(ObjectType.GOAL, goalId));
		verifyOwner(factorId, ObjectType.FACTOR, new ORef(ObjectType.OBJECTIVE, objectiveId));
		verifyOwner(factorId, ObjectType.FACTOR, new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keaId));
		verifyOwner(factorId, ObjectType.FACTOR, new ORef(ObjectType.TASK, taskId));
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
		
		BaseId subTaskId = project.createTask(new ORef(ObjectType.ASSIGNMENT,assignmentId));
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
	
	
	public void testDiagramFactorLinkAndLinkFactorRefer() throws Exception
	{
		//TODO: look at this method to refactor
		FactorId interventionId = project.createNodeAndAddToDiagram(Factor.TYPE_STRATEGY);
		FactorId factorId = project.createNodeAndAddToDiagram(Factor.TYPE_CAUSE);
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(interventionId, factorId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
    	project.executeCommand(createModelLinkage);
    	FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		
    	DiagramFactorId fromDiagramFactorId = project.createAndAddFactorToDiagram(Factor.TYPE_CAUSE);
		DiagramFactorId toDiagramFactorId =  project.createAndAddFactorToDiagram(Factor.TYPE_CAUSE);
		
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
    	project.executeCommand(createDiagramLinkCommand);
    	DiagramFactorLinkId diagramFactorLinkId = (DiagramFactorLinkId)createDiagramLinkCommand.getCreatedId();

		//----------- start test -----------

		vertifyRefer(diagramFactorLinkId, ObjectType.DIAGRAM_LINK, new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		vertifyRefer(diagramFactorLinkId, ObjectType.DIAGRAM_LINK, new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		vertifyRefer(diagramFactorLinkId, ObjectType.DIAGRAM_LINK, new ORef(ObjectType.FACTOR_LINK, modelLinkageId));
		
		vertifyRefer(modelLinkageId, ObjectType.FACTOR_LINK, new ORef(ObjectType.FACTOR, interventionId));
		vertifyRefer(modelLinkageId, ObjectType.FACTOR_LINK, new ORef(ObjectType.FACTOR, factorId));
	}
	
	public void testIndicatorOwn() throws Exception
	{
		BaseId indicatorId = project.createObject(ObjectType.INDICATOR);
		BaseId goalId = project.addItemToIndicatorList(indicatorId, ObjectType.GOAL, Indicator.TAG_GOAL_IDS);
	
		BaseId taskId = project.createTask(new ORef(ObjectType.INDICATOR, indicatorId));
		IdList taskList = new IdList(new BaseId[] {taskId});
		project.setObjectData(ObjectType.INDICATOR, indicatorId, Indicator.TAG_TASK_IDS, taskList.toString());
		
		//----------- start test -----------
		
		verifyOwner(indicatorId, ObjectType.INDICATOR, new ORef(ObjectType.GOAL, goalId));
		verifyOwner(indicatorId, ObjectType.INDICATOR, new ORef(ObjectType.TASK, taskId));
	}
	
	
	public void testKeyEcologicalAttributeOwn() throws Exception
	{
		BaseId keaId = project.createObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
		BaseId indicatorId = project.addItemToKeyEcologicalAttributeList(keaId, ObjectType.INDICATOR, KeyEcologicalAttribute.TAG_INDICATOR_IDS);
		
		//----------- start test -----------
		
		verifyOwner(keaId, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, new ORef(ObjectType.INDICATOR, indicatorId));
	}
	
	
	public void testVeiwDataRefer() throws Exception
	{
		BaseId viewDataId = project.createObject(ObjectType.VIEW_DATA);
		BaseId factorId = project.createFactor(Factor.TYPE_TARGET);
		IdList idList = new IdList(new BaseId[] {factorId});
		project.setObjectData(ObjectType.VIEW_DATA, viewDataId, ViewData.TAG_BRAINSTORM_NODE_IDS, idList.toString());
		
		//----------- start test -----------
		
		vertifyRefer(viewDataId, ObjectType.VIEW_DATA, new ORef(ObjectType.FACTOR, factorId));
	}
	
	private void vertifyRefer(BaseId id, int type, ORef ref)
	{
		ORefList orefsIds = BaseObject.findObjectsThatReferToUs(project.getObjectManager(), type, ref);
		assertEquals(1,orefsIds.size());
		assertEquals(id, orefsIds.get(0).getObjectId());


		BaseObject baseObject =  project.getObjectManager().findObject(ref);
		ORefList orefOwner = baseObject.findObjectThatReferToUs();
		
		final ORef referrerOref = new ORef(type, id);
		assertEquals(referrerOref, orefOwner.get(0));
		assertNotEquals("Parentage wrong:", ref, orefOwner.get(0));
	}
	
	private void verifyOwner(BaseId id, int type, ORef ref)
	{
		ORef oref = BaseObject.findObjectWhoOwnesUs(project.getObjectManager(), type, ref);
		assertEquals(new ORef(type,id), oref);
		
		BaseObject baseObject =  project.getObjectManager().findObject(ref);
		ORef orefOwner = baseObject.getOwner();
		
		assertEquals(oref, orefOwner);
		assertNotEquals("Parentage wrong:", oref, ref);
		assertNotEquals("Parentage wrong:", oref, baseObject.getRef());
	}
	
	
	ProjectForTesting project;
}
