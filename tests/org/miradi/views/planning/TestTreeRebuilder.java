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
import org.miradi.dialogs.planning.upperPanel.rebuilder.NodeSorter;
import org.miradi.dialogs.planning.upperPanel.rebuilder.NormalTreeRebuilder;
import org.miradi.dialogs.planning.upperPanel.rebuilder.TreeRebuilderNodeSorter;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.*;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.PlanningTreeTargetPositionQuestion;
import org.miradi.questions.StrategyObjectiveTreeOrderQuestion;
import org.miradi.schemas.*;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;

import java.util.Vector;

public class TestTreeRebuilder extends TestCaseWithProject
{
	public TestTreeRebuilder(String name)
	{
		super(name);
	}
	
	public void testTargetsAtTopLevel() throws Exception
	{
		Target target = getProject().createTarget();
		Goal goal = getProject().createGoal(target);
		DiagramFactor targetDiagramFactor = getProject().createAndAddFactorToDiagram(getProject().getMainDiagramObject(), target.getRef());
		
		Strategy strategy = getProject().createStrategy();
		getProject().executeCommands(goal.createCommandsToEnsureStrategyOrActivityIsRelevant(strategy.getRef()));
		DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(getProject().getMainDiagramObject(), strategy.getRef());
		getProject().createDiagramLink(strategyDiagramFactor, targetDiagramFactor);
		
		CodeList rowCodes = new CodeList();
		rowCodes.add(TargetSchema.OBJECT_NAME);
		
		createAndBuildTree(rowCodes, StrategyObjectiveTreeOrderQuestion.OBJECTIVE_CONTAINS_STRATEGY_CODE, PlanningTreeTargetPositionQuestion.TARGET_NODES_TOP_OF_PLANNING_TREE_CODE);
	}
	
	public void testObjectiveContainsStrategyNodes() throws Exception
	{
		verifyRelevantNodes(StrategyObjectiveTreeOrderQuestion.OBJECTIVE_CONTAINS_STRATEGY_CODE);
	}
	
	public void testStrategyContainsObjectiveNodes() throws Exception
	{
		verifyRelevantNodes(StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
	}

	public void testRelevantIndicatorNodes() throws Exception
	{
		ResultsChainDiagram resultChainA = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());
		Strategy strategyA = getProject().createStrategy();
		getProject().createAndAddFactorToDiagram(resultChainA, strategyA.getRef());
		getProject().addObjective(strategyA);
		Indicator indicator = getProject().createIndicator(strategyA);
        int resultChainAExpectedChildCount = 1;
        int resultChainAExpectedGrandchildCount = 1;

		ResultsChainDiagram resultChainB = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());
		Strategy strategyB = getProject().createStrategy();
		getProject().createAndAddFactorToDiagram(resultChainB, strategyB.getRef());
		Objective objectiveB = getProject().addObjective(strategyB);
		getProject().addSingleItemRelevantBaseObject(objectiveB, indicator, Objective.TAG_RELEVANT_INDICATOR_SET);
        int resultChainBExpectedChildCount = 1;
        int resultChainBExpectedGrandchildCount = 0;

		CodeList rowCodes = new CodeList();
		rowCodes.add(ResultsChainDiagramSchema.OBJECT_NAME);
		rowCodes.add(ObjectiveSchema.OBJECT_NAME);
		rowCodes.add(IndicatorSchema.OBJECT_NAME);
		
        verifyNodeHierarchy(resultChainA, resultChainAExpectedChildCount, resultChainAExpectedGrandchildCount,
                            resultChainB, resultChainBExpectedChildCount, resultChainBExpectedGrandchildCount,
                            rowCodes, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
	}

	private void verifyRelevantNodes(final String strategyContainsObjectiveCode) throws Exception
	{
		ResultsChainDiagram resultChainA = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());
		Strategy strategyA = getProject().createStrategy();
		getProject().createAndAddFactorToDiagram(resultChainA, strategyA.getRef());
		Objective objectiveA = getProject().addObjective(strategyA);
        int resultChainAExpectedChildCount = 1;
        int resultChainAExpectedGrandchildCount = 1;

		ResultsChainDiagram resultChainB = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());
		Strategy strategyB = getProject().createStrategy();
		getProject().createAndAddFactorToDiagram(resultChainB, strategyB.getRef());
		getProject().addSingleItemRelevantBaseObject(objectiveA, strategyB, Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
        int resultChainBExpectedChildCount = 1;
        int resultChainBExpectedGrandchildCount = 0;

		CodeList rowCodes = new CodeList();
		rowCodes.add(ResultsChainDiagramSchema.OBJECT_NAME);
		rowCodes.add(StrategySchema.OBJECT_NAME);
		rowCodes.add(ObjectiveSchema.OBJECT_NAME);

        verifyNodeHierarchy(resultChainA, resultChainAExpectedChildCount, resultChainAExpectedGrandchildCount,
                            resultChainB, resultChainBExpectedChildCount, resultChainBExpectedGrandchildCount,
                            rowCodes, strategyContainsObjectiveCode);
	}

	private void verifyNodeHierarchy(ResultsChainDiagram resultChainA, final int resultChainAExpectedChildCount, final int resultChainAExpectedGrandchildCount,
                                     ResultsChainDiagram resultChainB, final int resultChainBExpectedChildCount, final int resultChainBExpectedGrandchildCount,
                                     CodeList rowCodes, final String strategyContainsObjectiveCode) throws Exception
	{
		AbstractPlanningTreeNode rootNode = createAndBuildTree(rowCodes, strategyContainsObjectiveCode);
		Vector<AbstractPlanningTreeNode> resultsChainNodes = rootNode.getRawChildrenByReference();
		assertEquals("incorrect children count?", 2, resultsChainNodes.size());
		ORefList resultsChainNodeRefs = new ORefList();
		resultsChainNodeRefs.add(resultsChainNodes.get(0).getObjectReference());
		resultsChainNodeRefs.add(resultsChainNodes.get(1).getObjectReference());
		assertTrue("Should contain resultsChain?", resultsChainNodeRefs.contains(resultChainA.getRef()));
		assertTrue("Should contain resultsChain?", resultsChainNodeRefs.contains(resultChainB.getRef()));
		
		AbstractPlanningTreeNode resultChainANode = findMatchingNode(resultChainA.getRef(), resultsChainNodes);
		assertEquals("incorrect child count?", resultChainAExpectedChildCount, resultChainANode.getChildCount());
		assertEquals("incorrect child count?", resultChainAExpectedGrandchildCount, resultChainANode.getChild(0).getChildCount());

		AbstractPlanningTreeNode resultChainBNode = findMatchingNode(resultChainB.getRef(), resultsChainNodes);
		assertEquals("incorrect child count?", resultChainBExpectedChildCount, resultChainBNode.getChildCount());
		assertEquals("incorrect child count?", resultChainBExpectedGrandchildCount, resultChainBNode.getChild(0).getChildCount());
	}

    public void testStrategyContainsActivityNodes() throws Exception
    {
        verifyActivityNodes(StrategyObjectiveTreeOrderQuestion.OBJECTIVE_CONTAINS_STRATEGY_CODE);
    }

    private void verifyActivityNodes(final String strategyContainsObjectiveCode) throws Exception
    {
        ResultsChainDiagram resultChainA = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());
        Strategy strategyA = getProject().createStrategy();
        getProject().createAndAddFactorToDiagram(resultChainA, strategyA.getRef());
        Objective objectiveA = getProject().addObjective(strategyA);
        Task activity = getProject().createActivity();
        getProject().addSingleItemRelevantBaseObject(objectiveA, activity, Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
        int resultChainAExpectedChildCount = 1;
        int resultChainAExpectedGrandchildCount = 2; // objective + task

        ResultsChainDiagram resultChainB = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());
        Strategy strategyB = getProject().createStrategy();
        getProject().createAndAddFactorToDiagram(resultChainB, strategyB.getRef());
        int resultChainBExpectedChildCount = 1;
        int resultChainBExpectedGrandchildCount = 0;

        CodeList rowCodes = new CodeList();
        rowCodes.add(ResultsChainDiagramSchema.OBJECT_NAME);
        rowCodes.add(StrategySchema.OBJECT_NAME);
        rowCodes.add(ObjectiveSchema.OBJECT_NAME);
        rowCodes.add(TaskSchema.ACTIVITY_NAME);

        verifyNodeHierarchy(resultChainA, resultChainAExpectedChildCount, resultChainAExpectedGrandchildCount,
                            resultChainB, resultChainBExpectedChildCount, resultChainBExpectedGrandchildCount,
                            rowCodes, strategyContainsObjectiveCode);
    }

    private AbstractPlanningTreeNode findMatchingNode(ORef ref, Vector<AbstractPlanningTreeNode> resultsChainNodes) throws Exception
	{
		for(AbstractPlanningTreeNode node : resultsChainNodes)
		{
			if (ref.equals(node.getObjectReference()))
				return node;
		}
		
		throw new Exception("Didnt find node for ref: " + ref);
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
		NodeSorter rebuilder = new TreeRebuilderNodeSorter(parentRef);
		assertTrue(string, rebuilder.shouldSortChildren(childRef));
	}
	
	private void verifyShouldNotSort(String string, ORef parentRef, ORef childRef)
	{
		NodeSorter rebuilder = new TreeRebuilderNodeSorter(parentRef);
		assertFalse(string, rebuilder.shouldSortChildren(childRef));
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
		
		AbstractPlanningTreeNode rootNode = createAndBuildTree(rowCodes);

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
		return createAndBuildTree(rowCodes, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
	}

	private AbstractPlanningTreeNode createAndBuildTree(CodeList rowCodes, final String strategyObjeciveOrder) throws Exception
	{
		return createAndBuildTree(rowCodes, strategyObjeciveOrder, PlanningTreeTargetPositionQuestion.TARGET_NODES_CHILDREN_OF_DIAGRAM_OBJECTS_CODE);
	}

	public AbstractPlanningTreeNode createAndBuildTree(CodeList rowCodes, final String strategyObjeciveOrder, final String targetPositionCode) throws Exception
	{
		AbstractPlanningTreeNode rootNode = new PlanningTreeRootNodeAlwaysExpanded(getProject());
		ObjectTreeTableConfiguration configuration = new ObjectTreeTableConfiguration(getObjectManager(), BaseId.INVALID);
		configuration.setData(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, rowCodes.toString());
		configuration.setData(ObjectTreeTableConfiguration.TAG_STRATEGY_OBJECTIVE_ORDER, strategyObjeciveOrder);
		configuration.setData(ObjectTreeTableConfiguration.TAG_TARGET_NODE_POSITION, targetPositionCode);
		CustomTablePlanningTreeRowColumnProvider rowColumnProvider = new CustomTablePlanningTreeRowColumnProvider(getProject(), configuration);
		NormalTreeRebuilder rebuilder = new NormalTreeRebuilder(getProject(), rowColumnProvider);
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
