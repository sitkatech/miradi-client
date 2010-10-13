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
package org.miradi.project;

import java.awt.Dimension;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.Factor;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.utils.EnhancedJsonObject;

public class TestProjectRepairer extends TestCaseWithProject
{
	public TestProjectRepairer(String name)
	{
		super(name);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		repairer = new ProjectRepairer(getProject());
	}
	
	public void testIndicatorsWithNoResourceAssignments() throws Exception
	{
		Indicator indicator = getProject().createIndicator(getProject().createStrategy());
		
		verifyRepairingIndicator(indicator, new ORefList(), 0);
	}
	
	public void testIndicatorWithExistingResourceAssignment() throws Exception
	{
		Indicator indicator = getProject().createIndicator(getProject().createStrategy());
		ResourceAssignment resourceAssignment = getProject().createResourceAssignment();
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_RESOURCE_ASSIGNMENT_IDS, new IdList(resourceAssignment));
		
		verifyRepairingIndicator(indicator, new ORefList(resourceAssignment), 0);
	}
	
	public void testIndicatorReferringToMissingResourceAssignment() throws Exception
	{
		Indicator indicator = getProject().createIndicator(getProject().createStrategy());
		ORef referringToNonExistingAssginmentRef = new ORef(ResourceAssignment.getObjectType(), new BaseId(99999));
		IdList resourceAssignmentIds = new IdList(ResourceAssignment.getObjectType());
		resourceAssignmentIds.addRef(referringToNonExistingAssginmentRef);
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignmentIds);
		
		verifyRepairingIndicator(indicator, new ORefList(), 1);
	}
	
	public void testIndicatorReferringToExistingAndNonExistingResourceAssignment() throws Exception
	{
		Indicator indicator = getProject().createIndicator(getProject().createStrategy());
		ResourceAssignment resourceAssignment = getProject().createResourceAssignment();
		ORef referringToNonExistingAssginmentRef = new ORef(ResourceAssignment.getObjectType(), new BaseId(99999));
		IdList resourceAssignmentIds = new IdList(ResourceAssignment.getObjectType());
		resourceAssignmentIds.addRef(referringToNonExistingAssginmentRef);
		resourceAssignmentIds.add(resourceAssignment.getId());
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignmentIds);
		
		verifyRepairingIndicator(indicator, new ORefList(resourceAssignment), 1);
	}
	
	private void verifyRepairingIndicator(Indicator indicator, ORefList expectedAssignmentRefs, int expectedRepairedIndicatorCount) throws Exception
	{
		ORefSet repairedIndicatorRefs = new ProjectRepairer(getProject()).fixIndicatorsReferringToMissingAssignments();
		assertEquals("incorrect number if indicators were repaired?", expectedRepairedIndicatorCount, repairedIndicatorRefs.size());
		assertEquals("indicator's assignment refs were not repaired", expectedAssignmentRefs, indicator.getResourceAssignmentRefs());
	}

	public void testRepaireAssignmentsReferringToMissingObjects() throws Exception
	{
		ResourceAssignment resourceAssignment = getProject().createResourceAssignment();
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_ACCOUNTING_CODE_ID, new BaseId(100).toString(), "");
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_FUNDING_SOURCE_ID, new BaseId(100).toString(), "");
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_CATEGORY_ONE_REF, new ORef(BudgetCategoryOne.getObjectType(), new BaseId(100)).toString(), "");
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_CATEGORY_TWO_REF, new ORef(BudgetCategoryTwo.getObjectType(), new BaseId(100)).toString(), "");
		
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_ACCOUNTING_CODE_ID, BaseId.INVALID.toString(), "");
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_FUNDING_SOURCE_ID, BaseId.INVALID.toString(), "");
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_CATEGORY_ONE_REF, ORef.INVALID.toString(), "");
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_CATEGORY_TWO_REF, ORef.INVALID.toString(), "");
		
		ORef accountingCodeRef = getProject().createObject(AccountingCode.getObjectType());
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_ACCOUNTING_CODE_ID, accountingCodeRef.getObjectId().toString(), accountingCodeRef.getObjectId().toString());
		ORef fundingSourceRef = getProject().createObject(FundingSource.getObjectType());
		verifyRepair(resourceAssignment, ResourceAssignment.TAG_FUNDING_SOURCE_ID, fundingSourceRef.getObjectId().toString(), fundingSourceRef.getObjectId().toString());
		verifyAssginmentReferringToExistingObject(resourceAssignment, ResourceAssignment.TAG_CATEGORY_ONE_REF, BudgetCategoryOne.getObjectType());
		verifyAssginmentReferringToExistingObject(resourceAssignment, ResourceAssignment.TAG_CATEGORY_TWO_REF, BudgetCategoryTwo.getObjectType());
		
		
		ExpenseAssignment expenseAssignment = getProject().createExpenseAssignment();
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, new ORef(AccountingCode.getObjectType(), new BaseId(100)).toString(), "");
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_FUNDING_SOURCE_REF, new ORef(FundingSource.getObjectType(), new BaseId(200)).toString(), "");
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_CATEGORY_ONE_REF, new ORef(BudgetCategoryOne.getObjectType(), new BaseId(300)).toString(), "");
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_CATEGORY_TWO_REF, new ORef(BudgetCategoryTwo.getObjectType(), new BaseId(00)).toString(), "");
		
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, ORef.INVALID.toString(), "");
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_FUNDING_SOURCE_REF, ORef.INVALID.toString(), "");
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_CATEGORY_ONE_REF, ORef.INVALID.toString(), "");
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_CATEGORY_TWO_REF, ORef.INVALID.toString(), "");
		
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, accountingCodeRef.toString(), accountingCodeRef.toString());
		verifyRepair(expenseAssignment, ExpenseAssignment.TAG_FUNDING_SOURCE_REF, fundingSourceRef.toString(), fundingSourceRef.toString());
		verifyAssginmentReferringToExistingObject(expenseAssignment, ExpenseAssignment.TAG_CATEGORY_ONE_REF, BudgetCategoryOne.getObjectType());
		verifyAssginmentReferringToExistingObject(expenseAssignment, ExpenseAssignment.TAG_CATEGORY_TWO_REF, BudgetCategoryTwo.getObjectType());
	}
	
	private void verifyAssginmentReferringToExistingObject(Assignment assignment, String tag, int referrerType) throws Exception
	{
		ORef ref = getProject().createObject(referrerType);
		verifyRepair(assignment, tag, ref.toString(), ref.toString());
	}
	
	private void verifyRepair(Assignment assignment, String tag, String fieldValue, String expectedValueAfterRepair) throws Exception
	{
		assignment.setData(tag, fieldValue);
		new ProjectRepairer(getProject()).repairProblemsWherePossible();
		assertEquals("Assignment is still pointing to missing object", expectedValueAfterRepair, assignment.getData(tag));
	}

	public void testDeleteOrphans() throws Exception
	{
		Factor empty = getProject().createStrategy();
		ORef emptyRef = empty.getRef();

		Target target = getProject().createTarget();
		ORef targetRef = target.getRef();
		getProject().setObjectData(targetRef, Target.TAG_LABEL, "Sample data");
		Goal goal = getProject().createGoal(target);
		ORef goalRef = goal.getRef();
		getProject().setObjectData(goalRef, Goal.TAG_SHORT_LABEL, "Id");

		FundingSource fundingSource = getProject().createFundingSource();
		ORef fundingSourceRef = fundingSource.getRef();

		repairer.quarantineOrphans();
		assertNull("Didn't delete empty strategy?", BaseObject.find(getProject(), emptyRef));
		assertNull("Didn't delete target?", BaseObject.find(getProject(), targetRef));
		assertNull("Didn't delete goal?", BaseObject.find(getProject(), goalRef));
		assertNotNull("Deleted Funding Source?", BaseObject.find(getProject(), fundingSourceRef));
		
		String quarantined = getProject().getQuarantineFileContents();
		assertNotContains(emptyRef.toString(), quarantined);
		assertContains(targetRef.toString(), quarantined);
		assertContains(goalRef.toString(), quarantined);
		assertNotContains(fundingSourceRef.toString(), quarantined);
	}
	
	public void testRemoveInvalidDiagramLinkRefs() throws Exception
	{
		DiagramFactor targetDF = getProject().createAndAddFactorToDiagram(Target.getObjectType());
		DiagramFactor causeDF = getProject().createAndAddFactorToDiagram(Cause.getObjectType());
		getProject().createDiagramLinkAndAddToDiagram(causeDF, targetDF);
		DiagramFactor strategyDF = getProject().createAndAddFactorToDiagram(Strategy.getObjectType());
		getProject().createDiagramLinkAndAddToDiagram(strategyDF, causeDF);

		DiagramObject mainDiagram = getProject().getMainDiagramObject();
		IdList originalDiagramLinkIds = mainDiagram.getAllDiagramFactorLinkIds();
		assertNotEquals("Missing sample data?", 0, originalDiagramLinkIds.size());
		
		IdList badIds = new IdList(originalDiagramLinkIds);
		badIds.insertAt(BaseId.INVALID, 0);
		badIds.add(BaseId.INVALID);
		getProject().setObjectData(mainDiagram, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, badIds.toString());

		EAM.setLogToString();
		try
		{
			repairer.repairProblemsWherePossible();
			assertContains("-1", EAM.getLoggedString());
		}
		finally
		{
			EAM.setLogToConsole();
		}
		IdList repairedIds = mainDiagram.getAllDiagramFactorLinkIds();
		assertEquals("Didn't remove -1's?", originalDiagramLinkIds, repairedIds);
	}
	
	public void testGetFactorsWithoutDiagramFactors() throws Exception
	{
		verifyFactorBeingReferredToByDiagramFactor(ScopeBox.getObjectType());
		verifyFactorBeingReferredToByDiagramFactor(Target.getObjectType());
		verifyFactorBeingReferredToByDiagramFactor(HumanWelfareTarget.getObjectType());
		verifyFactorBeingReferredToByDiagramFactor(Cause.getObjectType());
		verifyFactorBeingReferredToByDiagramFactor(Strategy.getObjectType());
		verifyFactorBeingReferredToByDiagramFactor(ThreatReductionResult.getObjectType());
		verifyFactorBeingReferredToByDiagramFactor(IntermediateResult.getObjectType());
		verifyFactorBeingReferredToByDiagramFactor(GroupBox.getObjectType());
		verifyFactorBeingReferredToByDiagramFactor(TextBox.getObjectType());
	}

	private void verifyFactorBeingReferredToByDiagramFactor(int factorType) throws Exception
	{
		DiagramFactor diagramFactor = getProject().createAndAddFactorToDiagram(factorType);
		assertEquals("factor should be covered by a diagramFactor?", 0, repairer.getFactorsWithoutDiagramFactors(factorType).size());
		
		getProject().deleteObject(diagramFactor);
		assertEquals("factor should be covered by a diagramFactor?", 1, repairer.getFactorsWithoutDiagramFactors(factorType).size());
	}
	
	public void testDeleteDefectiveThreatStressRatings() throws Exception
	{
		getProject().disableThreatStressRatingEnsurer();		
		createThreatThreatTargetLinkedFactors();
		
		assertEquals("Incorrect threat stress rating count?", 0, getProject().getThreatStressRatingPool().size());
		getProject().beginCommandSideEffectMode();
		try
		{
			repairer.repairProblemsWherePossible();
		}
		finally
		{
			getProject().endCommandSideEffectMode();
		}

		assertEquals("Incorrect threat stress rating count?", 2, getProject().getThreatStressRatingPool().size());
	}

	private void createThreatThreatTargetLinkedFactors() throws Exception
	{
		DiagramFactor threat1 = getProject().createAndAddFactorToDiagram(Cause.getObjectType());
		getProject().enableAsThreat((Cause) threat1.getWrappedFactor());
		
		DiagramFactor threat2 = getProject().createAndAddFactorToDiagram(Cause.getObjectType());
		getProject().enableAsThreat((Cause) threat2.getWrappedFactor());
		
		DiagramFactor target = getProject().createAndAddFactorToDiagram(Target.getObjectType());
		ORefList stressRefs = new ORefList(getProject().createAndPopulateStress().getRef());
		getProject().fillObjectUsingCommand(target.getWrappedORef(), Target.TAG_STRESS_REFS, stressRefs.toString());
		
		getProject().createDiagramLinkAndAddToDiagram(threat2, threat1);
		getProject().createDiagramLinkAndAddToDiagram(threat1, target);
	}
	
	public void testDeleteOrphanObjectives() throws Exception
	{
		int annotationType = ObjectType.OBJECTIVE;
		String nodeTagForAnnotationList = Factor.TAG_OBJECTIVE_IDS;

		verifyDeleteOrphanAnnotations(annotationType, ObjectType.CAUSE, nodeTagForAnnotationList);
		
	}

	public void testDeleteOrphanGoals() throws Exception
	{
		int annotationType = ObjectType.GOAL;
		String nodeTagForAnnotationList = AbstractTarget.TAG_GOAL_IDS;

		verifyDeleteOrphanAnnotations(annotationType, ObjectType.TARGET, nodeTagForAnnotationList);
		
	}

	public void testDeleteOrphanIndicators() throws Exception
	{
		int annotationType = ObjectType.INDICATOR;
		String nodeTagForAnnotationList = Factor.TAG_INDICATOR_IDS;

		verifyDeleteOrphanAnnotations(annotationType, ObjectType.CAUSE, nodeTagForAnnotationList);
		
	}

	private void verifyDeleteOrphanAnnotations(int annotationType, int nodeType, String nodeTagForAnnotationList) throws Exception
	{
		//BaseId orphan = project.createObject(annotationType);
		BaseId nonOrphan = getProject().createObjectAndReturnId(annotationType);
		ORef nodeRef = getProject().createObject(nodeType);
		IdList annotationIds = new IdList(annotationType);
		annotationIds.add(nonOrphan);
		getProject().setObjectData(nodeRef, nodeTagForAnnotationList, annotationIds.toString());

		EAM.setLogToString();

//		TODO: removed orphan deletion code until a solution can be found to general extentions of having annoations having annoations
		repairer.repairProblemsWherePossible();
		//assertContains("Deleting orphan", EAM.getLoggedString());
		//assertNull("Didn't delete orphan?", project.findObject(annotationType, orphan));
		assertEquals("Deleted non-orphan?", nonOrphan, getProject().findObject(annotationType, nonOrphan).getId());
	}
	
	public void testScanForCorruptedObjects() throws Exception
	{
		ORef indicatorRef = getProject().createObject(Indicator.getObjectType());
		Indicator indicator = (Indicator) getProject().findObject(indicatorRef);
		BaseId nonExistantId = new BaseId(500);
		CommandSetObjectData appendTask = CommandSetObjectData.createAppendIdCommand(indicator, Indicator.TAG_METHOD_IDS, nonExistantId);
		getProject().executeCommand(appendTask);
		assertEquals("task not appended?", 1, indicator.getMethodRefs().size());		
		assertEquals("Didn't detect non-existant method?", 1, repairer.findAllMissingObjects().size());
		
		ORef taskRef = getProject().createObject(Task.getObjectType());
		Task task = (Task) getProject().findObject(taskRef);
		CommandSetObjectData appendSubTask = CommandSetObjectData.createAppendIdCommand(task, Task.TAG_SUBTASK_IDS, nonExistantId);
		getProject().executeCommand(appendSubTask);
		assertEquals("subtask not appended?", 1, task.getSubtaskCount());
		assertEquals("Didn't detect second instance?", 2, repairer.findAllMissingObjects().size());
	}
	
	public void testRepairUnsnappedFactorSizes() throws Exception
	{
		DiagramFactor cause1 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause1.setData(DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(15, 15)));
		
		DiagramFactor cause2 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause2.setData(DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(30, 30)));
		
		DiagramFactor cause3 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause3.setData(DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(31, 31)));
		
		DiagramFactor cause4 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		cause4.setData(DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(45, 45)));
		
		repairer.repairProblemsWherePossible();
		
		DiagramFactor repairedCause1 = DiagramFactor.find(getProject(), cause1.getRef());
		Dimension expectedSnappedSize = new Dimension(30, 30);
		assertEquals("wrong cause1 size?", expectedSnappedSize, repairedCause1.getSize());
		
		DiagramFactor repairedCause2 = DiagramFactor.find(getProject(), cause2.getRef());
		assertEquals("wrong cause2 size?", expectedSnappedSize, repairedCause2.getSize());
		
		DiagramFactor repairedCause3 = DiagramFactor.find(getProject(), cause3.getRef());
		assertEquals("wrong cause3 size?", expectedSnappedSize, repairedCause3.getSize());
		
		DiagramFactor repairedCause4 = DiagramFactor.find(getProject(), cause4.getRef());
		assertEquals("wrong cause4 size?", new Dimension(60, 60), repairedCause4.getSize());
	}

	private ProjectRepairer repairer;
}
