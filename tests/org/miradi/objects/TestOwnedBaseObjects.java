/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.ThreatReductionResultSchema;

public class TestOwnedBaseObjects extends TestCaseWithProject
{
	public TestOwnedBaseObjects(String name)
	{
		super(name);
	}
	
	public void testDiagramObjectOwnedObjects() throws Exception
	{
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());
		ORef diagramLinkRef = getProject().createDiagramLinkAndAddToDiagram(strategy, cause);
		DiagramObject diagramObject = getProject().getMainDiagramObject();

		ORefList expectedOwnedObjects = new ORefList(new ORef[]{cause.getRef(), strategy.getRef(), diagramLinkRef});
		verifyOwnedObjects(diagramObject, expectedOwnedObjects);
	}
	
	public void testTargetOwnedObjects() throws Exception
	{
		Target target = getProject().createTarget();
		Goal goal = getProject().createGoal(target);
		KeyEcologicalAttribute kea = getProject().createKea(target);
		Indicator indicator = getProject().createIndicator(target);
		Stress stress = getProject().createStress();
		getProject().fillObjectUsingCommand(target, Target.TAG_STRESS_REFS, new ORefList(stress));
		
		SubTarget subtarget = getProject().createSubTarget();
		getProject().fillObjectUsingCommand(target, Target.TAG_SUB_TARGET_REFS, new ORefList(subtarget));
		
		ORefList expectedOwnedObjects = new ORefList(new ORef[]{goal.getRef(), kea.getRef(), indicator.getRef(), stress.getRef(), subtarget.getRef()});
		verifyOwnedObjects(target, expectedOwnedObjects);
	}
	
	public void testStrategyOwnedObjects() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ORef indicatorRef = getProject().createIndicator(strategy).getRef();
		ORef objectiveRef = getProject().createObjective(strategy).getRef();
		ORef progressReportRef = getProject().addProgressReport(strategy).getRef();
		ORef resourceAssignmentRef = getProject().addResourceAssignment(strategy).getRef();
		ORef expenseAssignmentRef = getProject().addExpenseWithValue(strategy).getRef();
		
		verifyOwnedObjects(strategy, new ORefList(new ORef[]{indicatorRef, objectiveRef, progressReportRef, resourceAssignmentRef, expenseAssignmentRef}));
	}
	
	public void testTaskOwnedObjects() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task task = getProject().createTask(strategy);
		ORef subTaskRef = getProject().createTask(task).getRef();
		ORef progressReportRef = getProject().addProgressReport(task).getRef();
		ORef resourceAssignmentRef = getProject().addResourceAssignment(task).getRef();
		ORef expenseAssignmentRef = getProject().addExpenseWithValue(task).getRef();
		 
		verifyOwnedObjects(task, new ORefList(new ORef[]{subTaskRef, progressReportRef, resourceAssignmentRef, expenseAssignmentRef}));
	}
	
	public void testIndicatorOwnedObjects() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		ORef progressReportRef = getProject().addProgressReport(indicator).getRef();
		ORef resourceAssignmentRef = getProject().addResourceAssignment(indicator).getRef();
		ORef expenseAssignmentRef = getProject().addExpenseWithValue(indicator).getRef();
		
		verifyOwnedObjects(indicator, new ORefList(new ORef[]{progressReportRef, resourceAssignmentRef, expenseAssignmentRef}));
	}
	
	public void testKeaOwnedObjects() throws Exception
	{
		KeyEcologicalAttribute kea = getProject().createKea();
		ORef indicatorRef = getProject().createIndicator(kea).getRef();
		
		verifyOwnedObjects(kea, new ORefList(new ORef[]{indicatorRef}));
	}
	
	public void testCauseOwnedObjects() throws Exception
	{
		verifyFactorWithIndictorAndObjective(CauseSchema.getObjectType());
	}
	
	public void testIntermediateResultsOwnedObjects() throws Exception
	{
		verifyFactorWithIndictorAndObjective(IntermediateResultSchema.getObjectType());
	}
	
	public void testThreatReductionResultOwnedObjects() throws Exception
	{
		verifyFactorWithIndictorAndObjective(ThreatReductionResultSchema.getObjectType());
	}

	private ORef verifyFactorWithIndictorAndObjective(final int factorType) throws Exception
	{
		ORef ownerRef = getProject().createBaseObject(factorType).getRef();
		Factor factor = Factor.findFactor(getProject(), ownerRef);
		verifyFactorWithIndictorAndObjective(factor);
		
		return ownerRef;
	}

	private void verifyFactorWithIndictorAndObjective(Factor factor)	throws Exception
	{
		Indicator indicator = getProject().createIndicator(factor);
		Objective objective = getProject().createObjective(factor);
		
		verifyOwnedObjects(factor, new ORefList(new ORef[]{indicator.getRef(), objective.getRef()}));
	}

	private void verifyOwnedObjects(BaseObject baseObject, ORefList expectedOwnedObjects)
	{
		ORefList actualOwnedObjects = baseObject.getOwnedObjectRefs();
		assertEquals("incrrect onwed objects count?", expectedOwnedObjects.size(), actualOwnedObjects.size());
		expectedOwnedObjects.removeAll(actualOwnedObjects);
		assertEquals("unexpected owned object found?", 0, expectedOwnedObjects.size());
	}
}
