/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramFactorLinkId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdList;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ViewData;
import org.miradi.project.ProjectForTesting;

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
		project = null;
	}

	public void testIntermediateOwn() throws Exception
	{
		BaseId factorId = project.createFactorAndReturnId(ObjectType.INTERMEDIATE_RESULT);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.INTERMEDIATE_RESULT, factorId);
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
	}

	public void testThreatReductionResultOwn() throws Exception
	{
		BaseId factorId = project.createFactorAndReturnId(ObjectType.THREAT_REDUCTION_RESULT);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.THREAT_REDUCTION_RESULT, factorId);
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
	}

	public void testCauseOwn() throws Exception
	{
		BaseId factorId = project.createFactorAndReturnId(ObjectType.CAUSE);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.CAUSE, factorId);
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
	}
	
	public void testStrategyOwn() throws Exception
	{
		BaseId factorId = project.createFactorAndReturnId(ObjectType.STRATEGY);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId objectiveId = project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
		BaseId taskId = project.createTask();
		IdList taskList = new IdList(Task.getObjectType(), new BaseId[] {taskId});
		project.setObjectData(ObjectType.STRATEGY, factorId, Strategy.TAG_ACTIVITY_IDS, taskList.toString());
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.STRATEGY, factorId);
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.OBJECTIVE, objectiveId));
		verifyRefer(owner, new ORef(ObjectType.TASK, taskId));
	}

	public void testTargetOwn() throws Exception
	{
		BaseId factorId = project.createFactorAndReturnId(ObjectType.TARGET);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId goalId = project.addItemToFactorList(factorId, ObjectType.GOAL, Factor.TAG_GOAL_IDS);
		BaseId keaId = project.addItemToFactorList(factorId, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.TARGET, factorId);
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.GOAL, goalId));
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keaId));
	}
	
	public void testTaskOwn() throws Exception
	{
		BaseId factorId = project.createFactorAndReturnId(ObjectType.STRATEGY);
		BaseId taskId = project.createTask();
		BaseId subTaskId = project.createTask();
		BaseId assignmentId = project.createAssignment(new ORef(ObjectType.TASK,taskId));
		
		IdList taskList = new IdList(Task.getObjectType(), new BaseId[] {taskId});
		project.setObjectData(ObjectType.STRATEGY, factorId, Strategy.TAG_ACTIVITY_IDS, taskList.toString());
		
		IdList subTaskList = new IdList(Task.getObjectType(), new BaseId[] {subTaskId});
		project.setObjectData(ObjectType.TASK, taskId, Task.TAG_SUBTASK_IDS, subTaskList.toString());

		IdList assignmentList = new IdList(Assignment.getObjectType(), new BaseId[] {assignmentId});
		project.setObjectData(ObjectType.TASK, taskId, Task.TAG_ASSIGNMENT_IDS, assignmentList.toString());

		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.TASK, taskId);
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.TASK, subTaskId));
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.ASSIGNMENT, assignmentId));
	}
	
	
	public void testAssignmentRefer() throws Exception
	{
		BaseId taskId = project.createTask();
		BaseId assignmentId = project.createAssignment(new ORef(ObjectType.TASK,taskId));
		
		BaseId projectResourceId = project.createObjectAndReturnId(ObjectType.PROJECT_RESOURCE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, projectResourceId.toString());
		
		BaseId accountingCodeId = project.createObjectAndReturnId(ObjectType.ACCOUNTING_CODE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ACCOUNTING_CODE, accountingCodeId.toString());
		
		BaseId fundingSourceId = project.createObjectAndReturnId(ObjectType.FUNDING_SOURCE);
		project.setObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE, fundingSourceId.toString());
		
		//----------- start test -----------
		
	   	ORef owner = new ORef(ObjectType.ASSIGNMENT, assignmentId);
		verifyReferenceFunctions(1,owner, new ORef(ObjectType.PROJECT_RESOURCE, projectResourceId));
		verifyReferenceFunctions(1,owner, new ORef(ObjectType.ACCOUNTING_CODE, accountingCodeId));
		verifyReferenceFunctions(1,owner, new ORef(ObjectType.FUNDING_SOURCE, fundingSourceId));
	}


	public void testDiagramFactorRefer() throws Exception
	{
		verifyDiagramReference(ObjectType.CAUSE);
		verifyDiagramReference(ObjectType.STRATEGY);
		verifyDiagramReference(ObjectType.TARGET);
		verifyDiagramReference(ObjectType.INTERMEDIATE_RESULT);
		verifyDiagramReference(ObjectType.THREAT_REDUCTION_RESULT);
		verifyDiagramReference(ObjectType.TEXT_BOX);
		verifyDiagramReference(ObjectType.GROUP_BOX);
	}

	private void verifyDiagramReference(int type) throws Exception
	{
		DiagramFactor diagramFactor = project.createDiagramFactorAndAddToDiagram(type);
		ORef orefFactor = diagramFactor.getReferencedObjects(type).get(0);
		
		//----------- start test -----------
		
		verifyReferenceFunctions(1, diagramFactor.getRef(), orefFactor);
	}
	
	public void testDiagramFactorLinkAndLinkFactorRefer() throws Exception
	{
		verifyDiagramFactorLinkAndLinkFactorRefer(ObjectType.STRATEGY, ObjectType.CAUSE);
		verifyDiagramFactorLinkAndLinkFactorRefer(ObjectType.CAUSE, ObjectType.TARGET);
		verifyDiagramFactorLinkAndLinkFactorRefer(ObjectType.INTERMEDIATE_RESULT, ObjectType.THREAT_REDUCTION_RESULT);
	}
	
	public void verifyDiagramFactorLinkAndLinkFactorRefer(int fromType, int toType) throws Exception
	{
		DiagramFactor from = project.createDiagramFactorAndAddToDiagram(fromType);
		DiagramFactor to = project.createDiagramFactorAndAddToDiagram(toType);

		ORef diagramLinkRef = project.createDiagramLink(from, to);
		DiagramLink diagramLink = (DiagramLink) project.findObject(diagramLinkRef);
		
		//----------- start test -----------

		verifyReferenceFunctions(1, diagramLink.getWrappedRef(), from.getWrappedORef());
		verifyReferenceFunctions(1, diagramLink.getWrappedRef(), to.getWrappedORef());
    	
		verifyReferenceFunctions(2, diagramLinkRef, to.getRef());
		verifyReferenceFunctions(2, diagramLinkRef, from.getRef());
		verifyReferenceFunctions(1, diagramLinkRef, diagramLink.getWrappedRef());
	}
	
	public void testIndicatorOwn() throws Exception
	{
		ORef indicatorRef = project.createObject(Indicator.getObjectType());
	
		ORef taskRef = project.createObject(Task.getObjectType());
		IdList taskList = new IdList(Task.getObjectType(), new BaseId[] {taskRef.getObjectId()});
		project.setObjectData(indicatorRef, Indicator.TAG_TASK_IDS, taskList.toString());
		
		ORef measurementRef = project.createObject(Measurement.getObjectType());
		ORefList measurementList = new ORefList(new ORef[] {measurementRef});
		project.setObjectData(indicatorRef, Indicator.TAG_MEASUREMENT_REFS, measurementList.toString());
		
		//----------- start test -----------
		
		verifyRefer(indicatorRef, taskRef);
		verifyRefer(indicatorRef, measurementRef);
	}
	
	
	public void testKeyEcologicalAttributeOwn() throws Exception
	{
		BaseId keaId = project.createObjectAndReturnId(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
		BaseId indicatorId = project.addItemToKeyEcologicalAttributeList(keaId, ObjectType.INDICATOR, KeyEcologicalAttribute.TAG_INDICATOR_IDS);
		
		//----------- start test -----------
		
		ORef owner = new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keaId);
		verifyOwnershipFunctions(1,owner, new ORef(ObjectType.INDICATOR, indicatorId));
	}
	
	
	public void testVeiwDataRefer() throws Exception
	{
		BaseId viewDataId = project.createObjectAndReturnId(ObjectType.VIEW_DATA);
		BaseId factorId = project.createFactorAndReturnId(ObjectType.TARGET);
		ORefList oRefList = new ORefList(new ORef[] {new ORef(ObjectType.TARGET, factorId)});
		project.setObjectData(ObjectType.VIEW_DATA, viewDataId, ViewData.TAG_CHAIN_MODE_FACTOR_REFS, oRefList.toString());
		
		//----------- start test -----------
		
		ORef owner = new ORef(ObjectType.VIEW_DATA, viewDataId);
		verifyReferenceFunctions(1,owner, new ORef(ObjectType.TARGET, factorId));
	}
	
	public void testResultsChainRefer() throws Exception
	{
		BaseId resultsChainId = project.createObjectAndReturnId(ObjectType.RESULTS_CHAIN_DIAGRAM);
		ORef diagramRef = new ORef(ObjectType.RESULTS_CHAIN_DIAGRAM, resultsChainId);

		DiagramFactor strategy = createFactorAndDiagramFactor(Strategy.getObjectType());
		DiagramFactor cause = createFactorAndDiagramFactor(Cause.getObjectType());
		DiagramFactor target = createFactorAndDiagramFactor(Target.getObjectType());
		IdList factorList = new IdList(DiagramFactor.getObjectType(), new BaseId[] {strategy.getId(), cause.getId(), target.getId()});
		
		DiagramLink link = createDiagramFactorLink(strategy, cause);
		IdList linkList = new IdList(DiagramLink.getObjectType(), new BaseId[] {link.getId()});
		project.setObjectData(diagramRef, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS, factorList.toString());
		project.setObjectData(diagramRef, ResultsChainDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS, linkList.toString());
		
		//----------- start test -----------
		
		verifyOwnershipFunctions(3,diagramRef, strategy.getRef());
		verifyOwnershipFunctions(3,diagramRef, cause.getRef());
		verifyOwnershipFunctions(3,diagramRef, target.getRef());
		verifyOwnershipFunctions(1,diagramRef, link.getRef());
	}
	
	private DiagramFactor createFactorAndDiagramFactor(int type) throws Exception
	{
		ORef factorRef = project.createObject(type);
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(factorRef);
		DiagramFactorId diagramFactorId = (DiagramFactorId)project.createObject(DiagramFactor.getObjectType(), extraInfo);
		return (DiagramFactor)project.findObject(DiagramFactor.getObjectType(), diagramFactorId);
	}
	
	private DiagramLink createDiagramFactorLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		CreateFactorLinkParameter linkExtraInfo = new CreateFactorLinkParameter(from.getWrappedORef(), to.getWrappedORef());
		FactorLinkId linkId = (FactorLinkId)project.createObject(FactorLink.getObjectType(), linkExtraInfo);
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(linkId, from.getDiagramFactorId(), to.getDiagramFactorId());
		DiagramFactorLinkId diagramLinkId = (DiagramFactorLinkId)project.createObject(DiagramLink.getObjectType(), diagramLinkExtraInfo);
		return (DiagramLink)project.findObject(DiagramLink.getObjectType(), diagramLinkId);
	}
	

	private void verifyRefer(ORef referrer, ORef referred)
	{
		ORefList foundReferrers1 = BaseObject.findObjectsThatReferToUs(project.getObjectManager(), referrer.getObjectType(), referred);
		assertEquals(1,foundReferrers1.size());
		assertEquals(referrer.getObjectId(), foundReferrers1.get(0).getObjectId());

		BaseObject referredObject =  project.getObjectManager().findObject(referred);
		ORefList foundReferrers2 = referredObject.findObjectsThatReferToUs();
		assertContains(referrer, foundReferrers2.toArray());
	}	
	
	private void verifyReferenced(int size, ORef referrer, ORef referred)
	{
		BaseObject refererObject = project.findObject(referrer);
		ORefList ownedObjects = refererObject.getReferencedObjects(referred.getObjectType());
		assertEquals(size, ownedObjects.size());
		assertEquals(true, ownedObjects.contains(referred));
	}
	
	
	private void verifyReferenceFunctions(int size, ORef owner, ORef ref)
	{
		verifyReferenced(size, owner, ref);
		verifyRefer(owner, ref);
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
	
	private void verifyOwnershipFunctions(int size, ORef owner, ORef ref)
	{
		verifyOwned(size, owner, ref);
		verifyOwner(owner, ref);
	}

	
	
	ProjectForTesting project;
}
