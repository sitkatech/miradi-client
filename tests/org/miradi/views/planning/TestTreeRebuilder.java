/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.planning;

import org.miradi.dialogs.planning.CustomTablePlanningTreeRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNodeAlwaysExpanded;
import org.miradi.dialogs.planning.upperPanel.rebuilder.NormalTreeRebuilder;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Indicator;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.StrategyObjectiveTreeOrderQuestion;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;

public class TestTreeRebuilder extends TestCaseWithProject
{
	public TestTreeRebuilder(String name)
	{
		super(name);
	}
	
	public void testShouldSortChildren()
	{
		ORef taskRef = ORef.createInvalidWithType(TaskSchema.getObjectType());
		verifyShouldNotSort("Sorted subtasks within task?", taskRef, taskRef);

		ORef strategyRef = ORef.createInvalidWithType(StrategySchema.getObjectType());
		verifyShouldNotSort("Sorted activities within strategy?", strategyRef, taskRef);

		ORef indicatorRef = ORef.createInvalidWithType(IndicatorSchema.getObjectType());
		verifyShouldNotSort("Sorted methods within indicator?", indicatorRef, taskRef);
		
		ORef objectiveRef = ORef.createInvalidWithType(ObjectiveSchema.getObjectType());
		verifyShouldSort("Didn't sort tasks within objective?", objectiveRef, taskRef);
		
		verifyShouldSort("Didn't sort top-level tasks?", ORef.INVALID, taskRef);
	}

	private void verifyShouldSort(String string, ORef parentRef, ORef childRef)
	{
		NormalTreeRebuilder rebuilder = new NormalTreeRebuilder(getProject(), null);
		assertTrue(string, rebuilder.shouldSortChildren(parentRef, childRef));
	}
	
	private void verifyShouldNotSort(String string, ORef parentRef, ORef childRef)
	{
		NormalTreeRebuilder rebuilder = new NormalTreeRebuilder(getProject(), null);
		assertFalse(string, rebuilder.shouldSortChildren(parentRef, childRef));
	}
	
// TODO: Should have tests for all the basic "children of" cases
	
	public void testSimple() throws Exception
	{
		setupFactors();

		CodeList rowCodes = new CodeList();
		rowCodes.add(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR);
		AbstractPlanningTreeNode rootNode = createAndBuildTree(rowCodes);

		assertEquals(1, rootNode.getChildCount());

		final TreeTableNode firstChild = rootNode.getChild(0);
		assertEquals(diagramCause.getWrappedORef(), firstChild.getObjectReference());
		assertEquals(1, firstChild.getProportionShares());
		assertFalse("Full proportion task is allocated?", firstChild.areBudgetValuesAllocated());

	}

	public void testMergePromotedChildren() throws Exception
	{
		setupFactors();

		CodeList rowCodes = new CodeList();
		rowCodes.add(TaskSchema.ACTIVITY_NAME);
		AbstractPlanningTreeNode rootNode = createAndBuildTree(rowCodes);
		
		assertEquals("More than one activity?", 1, rootNode.getChildCount());
		AbstractPlanningTreeNode childNode = (AbstractPlanningTreeNode) rootNode.getChild(0);
		assertEquals("Not the activity?", activityId, childNode.getObjectReference().getObjectId());
		assertEquals(2, childNode.getProportionShares());
		assertFalse("Full proportion task is allocated?", childNode.areBudgetValuesAllocated());
	}
	
	public void testDeleteUncles() throws Exception
	{
		CodeList rowCodes = new CodeList();
		rowCodes.add(StrategySchema.OBJECT_NAME);
		rowCodes.add(ObjectiveSchema.OBJECT_NAME);

		ProjectForTesting project = getProject();
		
		diagramStrategy1 = project.createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());		
		diagramStrategy2 = project.createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());		
		diagramCause = project.createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		project.createDiagramLinkAndAddToDiagram(diagramStrategy1, diagramCause).getObjectId();		
		objectiveId = project.addItemToObjectiveList(diagramCause.getWrappedORef(), Cause.TAG_OBJECTIVE_IDS);
		
		ObjectTreeTableConfiguration configuration = new ObjectTreeTableConfiguration(getObjectManager(), BaseId.INVALID);
		configuration.setData(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, rowCodes.toString());
		configuration.setData(ObjectTreeTableConfiguration.TAG_STRATEGY_OBJECTIVE_ORDER, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
		CustomTablePlanningTreeRowColumnProvider rowColumnProvider = new CustomTablePlanningTreeRowColumnProvider(getProject(), configuration);
		NormalTreeRebuilder rebuilder = new NormalTreeRebuilder(getProject(), rowColumnProvider);
		AbstractPlanningTreeNode rootNode = new PlanningTreeRootNodeAlwaysExpanded(getProject());
		rebuilder.rebuildTree(rootNode);

		assertEquals("Didn't put Strats at top level?", 2, rootNode.getChildCount());
		TreeTableNode parentOfObjectiveNode = null;
		TreeTableNode strategyNode1 = rootNode.getChild(0);
		assertEquals(StrategySchema.getObjectType(), strategyNode1.getType());
		if(strategyNode1.getObjectReference().equals(diagramStrategy1.getWrappedORef()))
			parentOfObjectiveNode = strategyNode1;
		TreeTableNode strategyNode2 = rootNode.getChild(1);
		assertEquals(StrategySchema.getObjectType(), strategyNode2.getType());
		if(strategyNode2.getObjectReference().equals(diagramStrategy1.getWrappedORef()))
			parentOfObjectiveNode = strategyNode2;
		
		assertEquals("Missing objective child?", 1, parentOfObjectiveNode.getChildCount());
		ORef objectiveRef = new ORef(ObjectiveSchema.getObjectType(), objectiveId);
		assertEquals(objectiveRef, parentOfObjectiveNode.getChild(0).getObjectReference());
	}
	
	private void setupFactors() throws Exception
	{
		ProjectForTesting project = getProject();
		
		diagramStrategy1 = project.createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());		
		diagramStrategy2 = project.createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());		
		diagramCause = project.createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		
		project.createDiagramLinkAndAddToDiagram(diagramStrategy1, diagramCause).getObjectId();		
		
		objectiveId = project.addItemToObjectiveList(diagramCause.getWrappedORef(), Cause.TAG_OBJECTIVE_IDS);
		indicatorId = project.addItemToIndicatorList(diagramCause.getWrappedORef(), Cause.TAG_INDICATOR_IDS);
		taskId = project.addItemToIndicatorList(indicatorId, TaskSchema.getObjectType(), Indicator.TAG_METHOD_IDS);
		activityId = project.addActivityToStrateyList(diagramStrategy1.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS);
		subtaskId = project.addSubtaskToActivity(getTask().getRef(), Task.TAG_SUBTASK_IDS);
		
		IdList activityIds = new IdList(TaskSchema.getObjectType(), new BaseId[] {activityId});
		project.setObjectData(diagramStrategy2.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS, activityIds.toString());
		
		ORef objectiveRef = new ORef(ObjectiveSchema.getObjectType(), objectiveId);
		Objective objective = Objective.find(getProject(), objectiveRef);
		CommandVector commands = objective.createCommandsToEnsureStrategyOrActivityIsRelevant(diagramStrategy1.getWrappedORef());
		project.executeCommands(commands);
	}

	private AbstractPlanningTreeNode createAndBuildTree(CodeList rowCodes) throws Exception
	{
		ObjectTreeTableConfiguration configuration = new ObjectTreeTableConfiguration(getObjectManager(), BaseId.INVALID);
		configuration.setData(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, rowCodes.toString());
		configuration.setData(ObjectTreeTableConfiguration.TAG_STRATEGY_OBJECTIVE_ORDER, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
		CustomTablePlanningTreeRowColumnProvider rowColumnProvider = new CustomTablePlanningTreeRowColumnProvider(getProject(), configuration);
		NormalTreeRebuilder rebuilder = new NormalTreeRebuilder(getProject(), rowColumnProvider);
		AbstractPlanningTreeNode rootNode = new PlanningTreeRootNodeAlwaysExpanded(getProject());
		rebuilder.rebuildTree(rootNode);
		return rootNode;
	}
		

	public Strategy getStrategy()
	{
		return (Strategy) getProject().findObject(diagramStrategy1.getWrappedORef());
	}

	public Indicator getIndicator()
	{
		return (Indicator) getProject().findObject(new ORef(IndicatorSchema.getObjectType(), indicatorId));
	}
	
	public Task getTask()
	{
		return (Task) getProject().findObject(new ORef(TaskSchema.getObjectType(), taskId));
	}
	
	public Task getSubtask()
	{
		return (Task) getProject().findObject(new ORef(TaskSchema.getObjectType(), subtaskId));
	}

	private DiagramFactor diagramStrategy1;
	private DiagramFactor diagramStrategy2;
	private DiagramFactor diagramCause;
	private BaseId objectiveId;
	private BaseId activityId;
	private BaseId indicatorId;
	private BaseId subtaskId;
	private BaseId taskId;
}
