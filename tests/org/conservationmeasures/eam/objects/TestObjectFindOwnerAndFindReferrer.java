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
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
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

	public void testIntermediateOwn() throws Exception
	{
		BaseId factorId = project.createFactor(ObjectType.INTERMEDIATE_RESULT);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.INTERMEDIATE_RESULT, factorId);
		verifyOwner(owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwner(owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
		verifyOwned(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwned(1,owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
	}

	public void testThreatReductionResultOwn() throws Exception
	{
		BaseId factorId = project.createFactor(ObjectType.THREAT_REDUCTION_RESULT);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.THREAT_REDUCTION_RESULT, factorId);
		verifyOwner(owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwner(owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
		verifyOwned(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwned(1,owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
	}

	public void testCauseOwn() throws Exception
	{
		BaseId factorId = project.createFactor(ObjectType.CAUSE);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.CAUSE, factorId);
		verifyOwner(owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwner(owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
		verifyOwned(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwned(1,owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
	}
	
	public void testStrategyOwn() throws Exception
	{
		BaseId factorId = project.createFactor(ObjectType.STRATEGY);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
		BaseId taskId = project.createTask();
		IdList taskList = new IdList(new BaseId[] {taskId});
		project.setObjectData(ObjectType.STRATEGY, factorId, Strategy.TAG_ACTIVITY_IDS, taskList.toString());
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.STRATEGY, factorId);
		verifyOwner(owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwner(owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
		verifyOwner(owner, new ORef(ObjectType.TASK, taskId));
		verifyOwned(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwned(1,owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
		verifyOwned(1,owner, new ORef(ObjectType.TASK, taskId));
	}

	public void testTargetOwn() throws Exception
	{
		BaseId factorId = project.createFactor(ObjectType.TARGET);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId goalId = project.addItemToFactorList(factorId, ObjectType.GOAL, Factor.TAG_GOAL_IDS);
		BaseId keaId = project.addItemToFactorList(factorId, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.TARGET, factorId);
		verifyOwner(owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwner(owner, new ORef(ObjectType.GOAL, goalId));
		verifyOwner(owner, new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keaId));
		verifyOwned(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwned(1,owner, new ORef(ObjectType.GOAL, goalId));
		verifyOwned(1,owner, new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keaId));
	}
	
	public void testTaskOwn() throws Exception
	{
		BaseId factorId = project.createFactor(ObjectType.STRATEGY);
		BaseId taskId = project.createTask();
		BaseId subTaskId = project.createTask();
		BaseId assignmentId = project.createAssignment(new ORef(ObjectType.TASK,taskId));
		
		IdList taskList = new IdList(new BaseId[] {taskId});
		project.setObjectData(ObjectType.STRATEGY, factorId, Strategy.TAG_ACTIVITY_IDS, taskList.toString());
		
		IdList subTaskList = new IdList(new BaseId[] {subTaskId});
		project.setObjectData(ObjectType.TASK, taskId, Task.TAG_SUBTASK_IDS, subTaskList.toString());

		IdList assignmentList = new IdList(new BaseId[] {assignmentId});
		project.setObjectData(ObjectType.TASK, taskId, Task.TAG_ASSIGNMENT_IDS, assignmentList.toString());

		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.TASK, taskId);
		verifyOwner(owner, new ORef(ObjectType.TASK, subTaskId));
		verifyOwner(owner, new ORef(ObjectType.ASSIGNMENT, assignmentId));
		verifyOwned(1,owner, new ORef(ObjectType.TASK, subTaskId));
		verifyOwned(1,owner, new ORef(ObjectType.ASSIGNMENT, assignmentId));
		
	}
	
	
	public void testAssignmentRefer() throws Exception
	{
		BaseId taskId = project.createTask();
		BaseId assignmentId = project.createAssignment(new ORef(ObjectType.TASK,taskId));
		
		BaseId projectResourceId = project.createObject(ObjectType.PROJECT_RESOURCE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, projectResourceId.toString());
		
		BaseId accountingCodeId = project.createObject(ObjectType.ACCOUNTING_CODE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ACCOUNTING_CODE, accountingCodeId.toString());
		
		BaseId fundingSourceId = project.createObject(ObjectType.FUNDING_SOURCE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE, fundingSourceId.toString());
		
		BaseId subTaskId = project.createTask();
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_TASK_ID, subTaskId.toString());
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.ASSIGNMENT, assignmentId);
		verifyRefer(owner, new ORef(ObjectType.PROJECT_RESOURCE, projectResourceId));
		verifyRefer(owner, new ORef(ObjectType.ACCOUNTING_CODE, accountingCodeId));
		verifyRefer(owner, new ORef(ObjectType.FUNDING_SOURCE, fundingSourceId));
		verifyRefer(owner, new ORef(ObjectType.TASK, subTaskId));
		verifyReferenced(1,owner, new ORef(ObjectType.PROJECT_RESOURCE, projectResourceId));
		verifyReferenced(1,owner, new ORef(ObjectType.ACCOUNTING_CODE, accountingCodeId));
		verifyReferenced(1,owner, new ORef(ObjectType.FUNDING_SOURCE, fundingSourceId));
		verifyReferenced(1,owner, new ORef(ObjectType.TASK, subTaskId));
	}


	public void testDiagramFactorRefer() throws Exception
	{
		verifyDiagramReference(ObjectType.CAUSE);
		verifyDiagramReference(ObjectType.STRATEGY);
		verifyDiagramReference(ObjectType.TARGET);
		verifyDiagramReference(ObjectType.INTERMEDIATE_RESULT);
		verifyDiagramReference(ObjectType.THREAT_REDUCTION_RESULT);
	}

	private void verifyDiagramReference(int type) throws Exception
	{
		DiagramFactorId diagramFactorId = project.createAndAddFactorToDiagram(type);
		DiagramFactor diagramFactor = (DiagramFactor)project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		ORef orefFactor = diagramFactor.getReferencedObjects(type).get(0);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		verifyRefer(owner, orefFactor);
		verifyReferenced(1,owner, orefFactor);
	}
	
	
	public void testDiagramFactorLinkAndLinkFactorRefer() throws Exception
	{
		//TODO: look at this method to refactor
		FactorId interventionId = project.createNodeAndAddToDiagram(ObjectType.STRATEGY);
		FactorId factorId = project.createNodeAndAddToDiagram(ObjectType.CAUSE);
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(interventionId, factorId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
    	project.executeCommand(createModelLinkage);
    	FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		
    	DiagramFactorId fromDiagramFactorId = project.createAndAddFactorToDiagram(ObjectType.CAUSE);
		DiagramFactorId toDiagramFactorId =  project.createAndAddFactorToDiagram(ObjectType.TARGET);
		
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
    	project.executeCommand(createDiagramLinkCommand);
    	DiagramFactorLinkId diagramFactorLinkId = (DiagramFactorLinkId)createDiagramLinkCommand.getCreatedId();

		//----------- start test -----------

    	
    	ORef linkRef = new ORef(ObjectType.FACTOR_LINK, modelLinkageId);
		verifyRefer(linkRef, new ORef(ObjectType.STRATEGY, interventionId));
		verifyRefer(linkRef, new ORef(ObjectType.CAUSE, factorId));
		verifyReferenced(1,linkRef, new ORef(ObjectType.STRATEGY, interventionId));
		verifyReferenced(1,linkRef, new ORef(ObjectType.CAUSE, factorId));
    	
		ORef diagramLinkRef = new ORef(ObjectType.DIAGRAM_LINK, diagramFactorLinkId);
		verifyRefer(diagramLinkRef, new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		verifyRefer(diagramLinkRef, new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		verifyRefer(diagramLinkRef, new ORef(ObjectType.FACTOR_LINK, modelLinkageId));
		verifyReferenced(2,diagramLinkRef, new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		verifyReferenced(2,diagramLinkRef, new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		verifyReferenced(1,diagramLinkRef, new ORef(ObjectType.FACTOR_LINK, modelLinkageId));
	}
	
	public void testIndicatorOwn() throws Exception
	{
		BaseId indicatorId = project.createObject(ObjectType.INDICATOR);
	
		BaseId taskId = project.createTask();
		IdList taskList = new IdList(new BaseId[] {taskId});
		project.setObjectData(ObjectType.INDICATOR, indicatorId, Indicator.TAG_TASK_IDS, taskList.toString());
		
		//----------- start test -----------
		
		ORef owner = new ORef(ObjectType.INDICATOR, indicatorId);
		verifyOwner(owner, new ORef(ObjectType.TASK, taskId));
		verifyOwned(1,owner, new ORef(ObjectType.TASK, taskId));
	}
	
	
	public void testKeyEcologicalAttributeOwn() throws Exception
	{
		BaseId keaId = project.createObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
		BaseId indicatorId = project.addItemToKeyEcologicalAttributeList(keaId, ObjectType.INDICATOR, KeyEcologicalAttribute.TAG_INDICATOR_IDS);
		
		//----------- start test -----------
		
		ORef owner = new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keaId);
		verifyOwner(owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwned(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
	}
	
	
	public void testVeiwDataRefer() throws Exception
	{
		BaseId viewDataId = project.createObject(ObjectType.VIEW_DATA);
		BaseId factorId = project.createFactor(ObjectType.TARGET);
		ORefList oRefList = new ORefList(new ORef[] {new ORef(ObjectType.TARGET, factorId)});
		project.setObjectData(ObjectType.VIEW_DATA, viewDataId, ViewData.TAG_CHAIN_MODE_FACTOR_REFS, oRefList.toString());
		
		//----------- start test -----------
		
		ORef owner = new ORef(ObjectType.VIEW_DATA, viewDataId);
		verifyRefer(owner, new ORef(ObjectType.TARGET, factorId));
		verifyReferenced(1,owner, new ORef(ObjectType.TARGET, factorId));
	}
	
	public void testResultsChainRefer() throws Exception
	{
		BaseId resultsChainId = project.createObject(ObjectType.RESULTS_CHAIN_DIAGRAM);
		ORef diagramRef = new ORef(ObjectType.RESULTS_CHAIN_DIAGRAM, resultsChainId);

		DiagramFactor strategy = createFactorAndDiagramFactor(Strategy.getObjectType());
		DiagramFactor cause = createFactorAndDiagramFactor(Cause.getObjectType());
		DiagramFactor target = createFactorAndDiagramFactor(Target.getObjectType());
		IdList factorList = new IdList(new BaseId[] {strategy.getId(), cause.getId(), target.getId()});
		
		DiagramFactorLink link = createDiagramFactorLink(strategy, cause);
		IdList linkList = new IdList(new BaseId[] {link.getId()});
		project.setObjectData(diagramRef, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS, factorList.toString());
		project.setObjectData(diagramRef, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS, linkList.toString());
		
		//----------- start test -----------
		
		verifyOwner(diagramRef, strategy.getRef());
		verifyOwner(diagramRef, cause.getRef());
		verifyOwner(diagramRef, target.getRef());
		verifyOwner(diagramRef, link.getRef());
		verifyOwned(3,diagramRef, strategy.getRef());
		verifyOwned(3,diagramRef, cause.getRef());
		verifyOwned(3,diagramRef, target.getRef());
		verifyOwned(1,diagramRef, link.getRef());
	}
	
	private DiagramFactor createFactorAndDiagramFactor(int type) throws Exception
	{
		FactorId factorId = (FactorId)project.createObject(type);
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(factorId);
		DiagramFactorId diagramFactorId = (DiagramFactorId)project.createObject(DiagramFactor.getObjectType(), extraInfo);
		return (DiagramFactor)project.findObject(DiagramFactor.getObjectType(), diagramFactorId);
	}
	
	private DiagramFactorLink createDiagramFactorLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		CreateFactorLinkParameter linkExtraInfo = new CreateFactorLinkParameter(from.getWrappedId(), to.getWrappedId());
		FactorLinkId linkId = (FactorLinkId)project.createObject(FactorLink.getObjectType(), linkExtraInfo);
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(linkId, from.getDiagramFactorId(), to.getDiagramFactorId());
		DiagramFactorLinkId diagramLinkId = (DiagramFactorLinkId)project.createObject(DiagramFactorLink.getObjectType(), diagramLinkExtraInfo);
		return (DiagramFactorLink)project.findObject(DiagramFactorLink.getObjectType(), diagramLinkId);
	}
	

	private void verifyRefer(ORef referrer, ORef referred)
	{
		ORefList foundReferrers1 = BaseObject.findObjectsThatReferToUs(project.getObjectManager(), referrer.getObjectType(), referred);
		assertEquals(1,foundReferrers1.size());
		assertEquals(referrer.getObjectId(), foundReferrers1.get(0).getObjectId());

		BaseObject referredObject =  project.getObjectManager().findObject(referred);
		ORefList foundReferrers2 = referredObject.findObjectThatReferToUs();
		assertContains(referrer, foundReferrers2.toArray());
	}
	
	
	private void verifyReferenced(int size, ORef referrer, ORef referred)
	{
		BaseObject refererObject = project.findObject(referrer);
		ORefList ownedObjects = refererObject.getReferencedObjects(referred.getObjectType());
		assertEquals(size, ownedObjects.size());
		assertEquals(true, ownedObjects.contains(referred));
	}
	

	private void verifyOwner(ORef owner, ORef ref)
	{
		ORef oref = BaseObject.findObjectWhoOwnesUs(project.getObjectManager(), owner.getObjectType(), ref);
		assertEquals(owner, oref);
		
		BaseObject baseObject =  project.getObjectManager().findObject(ref);
		ORef orefOwner = baseObject.getOwnerRef();
		
		assertEquals(oref, orefOwner);
		assertNotEquals("Parentage wrong:", oref, ref);
		assertNotEquals("Parentage wrong:", oref, baseObject.getRef());
	}
	
	private void verifyOwned(int size, ORef owner, ORef ref)
	{
		BaseObject ownerObject = project.findObject(owner);
		ORefList ownedObjects = ownerObject.getOwnedObjects(ref.getObjectType());
		assertEquals(size, ownedObjects.size());
		assertEquals(true, ownedObjects.contains(ref));
	}
	

	
	
	ProjectForTesting project;
}
