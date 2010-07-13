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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
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
			TestProjectRepairer.repairProblemsWherePossible(getProject());
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
		ProjectRepairer repairer = new ProjectRepairer(getProject());
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
			TestProjectRepairer.repairProblemsWherePossible(getProject());
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
		TestProjectRepairer.repairProblemsWherePossible(getProject());
		//assertContains("Deleting orphan", EAM.getLoggedString());
		//assertNull("Didn't delete orphan?", project.findObject(annotationType, orphan));
		assertEquals("Deleted non-orphan?", nonOrphan, getProject().findObject(annotationType, nonOrphan).getId());
	}
	
	public void testScanForCorruptedObjects() throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(getProject());

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
		
		ProjectRepairer repairer = new ProjectRepairer(getProject());
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

	public static void repairProblemsWherePossible(Project project) throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		repairer.repairProblemsWherePossible();
	}	
}
