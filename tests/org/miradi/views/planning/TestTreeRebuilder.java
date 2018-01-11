/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
		assertEquals("incorrect grandchild count?", resultChainAExpectedGrandchildCount, resultChainANode.getChild(0).getChildCount());

		AbstractPlanningTreeNode resultChainBNode = findMatchingNode(resultChainB.getRef(), resultsChainNodes);
		assertEquals("incorrect child count?", resultChainBExpectedChildCount, resultChainBNode.getChildCount());
		assertEquals("incorrect grandchild count?", resultChainBExpectedGrandchildCount, resultChainBNode.getChild(0).getChildCount());
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

    //region tests that illustrate defect analysis outlined on https://bugs.benetech.org/browse/MRD-5842
    public void testRelevancyStrategyObjectiveDefaultAssociations() throws Exception
    {
        createAndVerifyTreeForStrategyObjectiveRelevancyTests(true, false, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
    }

    public void testRelevancyObjectiveStrategyDefaultAssociations() throws Exception
    {
        createAndVerifyTreeForStrategyObjectiveRelevancyTests(true, false, StrategyObjectiveTreeOrderQuestion.OBJECTIVE_CONTAINS_STRATEGY_CODE);
    }

    public void testRelevancyStrategyObjectiveDefaultAssociationsHideStrategy() throws Exception
    {
        createAndVerifyTreeForStrategyObjectiveRelevancyTests(true, true, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
    }

    public void testRelevancyObjectiveStrategyDefaultAssociationsHideStrategy() throws Exception
    {
        createAndVerifyTreeForStrategyObjectiveRelevancyTests(true, true, StrategyObjectiveTreeOrderQuestion.OBJECTIVE_CONTAINS_STRATEGY_CODE);
    }

    public void testRelevancyStrategyObjectiveUserAssertedAssociations() throws Exception
    {
        createAndVerifyTreeForStrategyObjectiveRelevancyTests(false, false, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
    }

    public void testRelevancyObjectiveStrategyUserAssertedAssociations() throws Exception
    {
        createAndVerifyTreeForStrategyObjectiveRelevancyTests(false, false, StrategyObjectiveTreeOrderQuestion.OBJECTIVE_CONTAINS_STRATEGY_CODE);
    }

    public void testRelevancyStrategyObjectiveUserAssertedAssociationsHideStrategy() throws Exception
    {
        createAndVerifyTreeForStrategyObjectiveRelevancyTests(false, true, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
    }

    public void testRelevancyObjectiveStrategyUserAssertedAssociationsHideStrategy() throws Exception
    {
        createAndVerifyTreeForStrategyObjectiveRelevancyTests(false, true, StrategyObjectiveTreeOrderQuestion.OBJECTIVE_CONTAINS_STRATEGY_CODE);
    }

    private void createAndVerifyTreeForStrategyObjectiveRelevancyTests(final boolean defaultAssociations, final boolean hideStrategies, final String strategyContainsObjectiveCode) throws Exception
    {
        ResultsChainDiagram resultChainDiagram = ResultsChainDiagram.find(getProject(), getProject().createResultsChainDiagram());

        Strategy strategy = getProject().createStrategy();
        getProject().createAndAddFactorToDiagram(resultChainDiagram, strategy.getRef());

        ORef relevantActivityRef = getProject().addActivityToStrategy(strategy.getRef(), Strategy.TAG_ACTIVITY_IDS);
        Task relevantActivity = Task.find(getProject(), relevantActivityRef);
        ORef nearbyActivityRef = getProject().addActivityToStrategy(strategy.getRef(), Strategy.TAG_ACTIVITY_IDS);
        Task nearbyActivity = Task.find(getProject(), nearbyActivityRef);

        Objective objective = getProject().addObjective(strategy);

        BaseId relevantIndicatorId = getProject().addItemToIndicatorList(strategy.getRef(), Strategy.TAG_INDICATOR_IDS);
        ORef relevantIndicatorRef = new ORef(IndicatorSchema.getObjectType(), relevantIndicatorId);
        Indicator relevantIndicator = Indicator.find(getProject(), relevantIndicatorRef);
        BaseId nearbyIndicatorId = getProject().addItemToIndicatorList(strategy.getRef(), Strategy.TAG_INDICATOR_IDS);
        ORef nearbyIndicatorRef = new ORef(IndicatorSchema.getObjectType(), nearbyIndicatorId);
        Indicator nearbyIndicator = Indicator.find(getProject(), nearbyIndicatorRef);

        if (defaultAssociations)
        {
            // all activities under strategy are now marked relevant as default...so adjust for test case
            getProject().executeCommands(objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(nearbyActivity.getRef()));
            getProject().executeCommands(objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(relevantActivity.getRef()));
        }
        else
        {
            // all indicators marked as relevant by default...so remove 'nearby' indicator
            getProject().executeCommands(objective.createCommandsToEnsureIndicatorIsIrrelevant(nearbyIndicator.getRef()));
            // all activities under strategy are now marked relevant as default...so remove 'nearby' activity
            getProject().executeCommands(objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(nearbyActivity.getRef()));
            // finally, mark strategy as not relevant
            getProject().executeCommands(objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategy.getRef()));
        }

        CodeList rowCodes = new CodeList();
        rowCodes.add(ResultsChainDiagramSchema.OBJECT_NAME);
        if (!hideStrategies)
            rowCodes.add(StrategySchema.OBJECT_NAME);
        rowCodes.add(ObjectiveSchema.OBJECT_NAME);
        rowCodes.add(TaskSchema.ACTIVITY_NAME);
        rowCodes.add(IndicatorSchema.OBJECT_NAME);

        AbstractPlanningTreeNode rootNode = createAndBuildTree(rowCodes, strategyContainsObjectiveCode);
        Vector<AbstractPlanningTreeNode> resultsChainNodes = rootNode.getRawChildrenByReference();

        assertEquals("incorrect children count?", 1, resultsChainNodes.size());
        ORefList resultsChainNodeRefs = new ORefList();
        resultsChainNodeRefs.add(resultsChainNodes.get(0).getObjectReference());
        assertTrue("Should contain resultsChain?", resultsChainNodeRefs.contains(resultChainDiagram.getRef()));

        AbstractPlanningTreeNode resultsChainNode = findMatchingNode(resultChainDiagram.getRef(), resultsChainNodes);

        boolean strategiesContainObjectives = (strategyContainsObjectiveCode.equals(StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE));

        if (defaultAssociations)
        {
            if (hideStrategies)
            {
                assertEquals("incorrect child count for results chain?", 3, resultsChainNode.getChildCount());
                TreeTableNode objectiveNode = resultsChainNode.getChild(0);
                assertEquals("expected objective?", objective.getRef(), objectiveNode.getObjectReference());
                assertEquals("incorrect objective child count?", 2, objectiveNode.getChildCount());
                ORefList indicatorNodeRefs = new ORefList();
                indicatorNodeRefs.add(objectiveNode.getChild(0).getObjectReference());
                indicatorNodeRefs.add(objectiveNode.getChild(1).getObjectReference());
                assertTrue("Should contain nearby indicator?", indicatorNodeRefs.contains(nearbyIndicator.getRef()));
                assertTrue("Should contain relevant indicator?", indicatorNodeRefs.contains(relevantIndicator.getRef()));
                ORefList activityNodeRefs = new ORefList();
                activityNodeRefs.add(resultsChainNode.getChild(1).getObjectReference());
                activityNodeRefs.add(resultsChainNode.getChild(2).getObjectReference());
                assertTrue("Should contain nearby activity?", activityNodeRefs.contains(nearbyActivity.getRef()));
                assertTrue("Should contain relevant activity?", activityNodeRefs.contains(relevantActivity.getRef()));
            }
            else
            {
                if (strategiesContainObjectives)
                {
                    assertEquals("incorrect child count for results chain?", 1, resultsChainNode.getChildCount());
                    TreeTableNode strategyNode = resultsChainNode.getChild(0);
                    assertEquals("expected strategy?", strategy.getRef(), strategyNode.getObjectReference());
                    assertEquals("incorrect strategy child count?", 3, strategyNode.getChildCount());
                    TreeTableNode objectiveNode = strategyNode.getChild(0);
                    assertEquals("expected objective?", objective.getRef(), objectiveNode.getObjectReference());
                    assertEquals("incorrect objective child count?", 2, objectiveNode.getChildCount());
                    ORefList indicatorNodeRefs = new ORefList();
                    indicatorNodeRefs.add(objectiveNode.getChild(0).getObjectReference());
                    indicatorNodeRefs.add(objectiveNode.getChild(1).getObjectReference());
                    assertTrue("Should contain nearby indicator?", indicatorNodeRefs.contains(nearbyIndicator.getRef()));
                    assertTrue("Should contain relevant indicator?", indicatorNodeRefs.contains(relevantIndicator.getRef()));
                    ORefList activityNodeRefs = new ORefList();
                    activityNodeRefs.add(strategyNode.getChild(1).getObjectReference());
                    activityNodeRefs.add(strategyNode.getChild(2).getObjectReference());
                    assertTrue("Should contain nearby activity?", activityNodeRefs.contains(nearbyActivity.getRef()));
                    assertTrue("Should contain relevant activity?", activityNodeRefs.contains(relevantActivity.getRef()));
                }
                else
                {
                    assertEquals("incorrect child count for results chain?", 1, resultsChainNode.getChildCount());
                    TreeTableNode objectiveNode = resultsChainNode.getChild(0);
                    assertEquals("expected objective?", objective.getRef(), objectiveNode.getObjectReference());
                    assertEquals("incorrect objective child count?", 1, objectiveNode.getChildCount());
                    TreeTableNode strategyNode = objectiveNode.getChild(0);
                    assertEquals("expected strategy?", strategy.getRef(), strategyNode.getObjectReference());
                    assertEquals("incorrect strategy child count?", 4, strategyNode.getChildCount());
                    ORefList strategyChildNodeRefs = new ORefList();
                    strategyChildNodeRefs.add(strategyNode.getChild(0).getObjectReference());
                    strategyChildNodeRefs.add(strategyNode.getChild(1).getObjectReference());
                    strategyChildNodeRefs.add(strategyNode.getChild(2).getObjectReference());
                    strategyChildNodeRefs.add(strategyNode.getChild(3).getObjectReference());
                    assertTrue("Should contain nearby indicator?", strategyChildNodeRefs.contains(nearbyIndicator.getRef()));
                    assertTrue("Should contain relevant indicator?", strategyChildNodeRefs.contains(relevantIndicator.getRef()));
                    assertTrue("Should contain nearby activity?", strategyChildNodeRefs.contains(nearbyActivity.getRef()));
                    assertTrue("Should contain relevant activity?", strategyChildNodeRefs.contains(relevantActivity.getRef()));
                }
            }
        }
        else
        {
            if (hideStrategies)
            {
                assertEquals("incorrect child count for results chain?", 3, resultsChainNode.getChildCount());
                TreeTableNode objectiveNode = resultsChainNode.getChild(0);
                assertEquals("expected objective?", objective.getRef(), objectiveNode.getObjectReference());
                assertEquals("incorrect objective child count?", 2, objectiveNode.getChildCount());
                ORefList objectiveChildNodeRefs = new ORefList();
                objectiveChildNodeRefs.add(objectiveNode.getChild(0).getObjectReference());
                objectiveChildNodeRefs.add(objectiveNode.getChild(1).getObjectReference());
                assertTrue("Should contain relevant indicator?", objectiveChildNodeRefs.contains(relevantIndicator.getRef()));
                assertTrue("Should contain relevant activity?", objectiveChildNodeRefs.contains(relevantActivity.getRef()));
                ORefList otherChildNodeRefs = new ORefList();
                otherChildNodeRefs.add(resultsChainNode.getChild(1).getObjectReference());
                otherChildNodeRefs.add(resultsChainNode.getChild(2).getObjectReference());
                assertTrue("Should contain nearby indicator?", otherChildNodeRefs.contains(nearbyIndicator.getRef()));
                assertTrue("Should contain nearby activity?", otherChildNodeRefs.contains(nearbyActivity.getRef()));
            }
            else
            {
                assertEquals("incorrect child count for results chain?", 2, resultsChainNode.getChildCount());
                TreeTableNode objectiveNode = resultsChainNode.getChild(0);
                assertEquals("expected objective?", objective.getRef(), objectiveNode.getObjectReference());
                assertEquals("incorrect objective child count?", 2, objectiveNode.getChildCount());
                ORefList objectiveChildNodeRefs = new ORefList();
                objectiveChildNodeRefs.add(objectiveNode.getChild(0).getObjectReference());
                objectiveChildNodeRefs.add(objectiveNode.getChild(1).getObjectReference());
                assertTrue("Should contain relevant indicator?", objectiveChildNodeRefs.contains(relevantIndicator.getRef()));
                assertTrue("Should contain relevant activity?", objectiveChildNodeRefs.contains(relevantActivity.getRef()));
                TreeTableNode strategyNode = resultsChainNode.getChild(1);
                assertEquals("expected strategy?", strategy.getRef(), strategyNode.getObjectReference());
                assertEquals("incorrect strategy child count?", 4, strategyNode.getChildCount());
                ORefList strategyChildNodeRefs = new ORefList();
                strategyChildNodeRefs.add(strategyNode.getChild(0).getObjectReference());
                strategyChildNodeRefs.add(strategyNode.getChild(1).getObjectReference());
                strategyChildNodeRefs.add(strategyNode.getChild(2).getObjectReference());
                strategyChildNodeRefs.add(strategyNode.getChild(3).getObjectReference());
                assertTrue("Should contain nearby indicator?", strategyChildNodeRefs.contains(nearbyIndicator.getRef()));
                assertTrue("Should contain relevant indicator?", strategyChildNodeRefs.contains(relevantIndicator.getRef()));
                assertTrue("Should contain nearby activity?", strategyChildNodeRefs.contains(nearbyActivity.getRef()));
                assertTrue("Should contain relevant activity?", strategyChildNodeRefs.contains(relevantActivity.getRef()));
            }
        }
    }
    //endregion

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
		methodId = project.addItemToIndicatorList(indicatorId, MethodSchema.getObjectType(), Indicator.TAG_METHOD_IDS);

		activityId = project.addActivityToStrategyList(diagramStrategy1.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS);
		subtaskId = project.addSubtaskToActivity(getActivity().getRef(), Task.TAG_SUBTASK_IDS);
		
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

	private AbstractPlanningTreeNode createAndBuildTree(CodeList rowCodes, final String strategyObjectiveOrder) throws Exception
	{
		return createAndBuildTree(rowCodes, strategyObjectiveOrder, PlanningTreeTargetPositionQuestion.TARGET_NODES_CHILDREN_OF_DIAGRAM_OBJECTS_CODE);
	}

	public AbstractPlanningTreeNode createAndBuildTree(CodeList rowCodes, final String strategyObjectiveOrder, final String targetPositionCode) throws Exception
	{
		AbstractPlanningTreeNode rootNode = new PlanningTreeRootNodeAlwaysExpanded(getProject());
		ObjectTreeTableConfiguration configuration = new ObjectTreeTableConfiguration(getObjectManager(), BaseId.INVALID);
		configuration.setData(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, rowCodes.toString());
		configuration.setData(ObjectTreeTableConfiguration.TAG_STRATEGY_OBJECTIVE_ORDER, strategyObjectiveOrder);
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
	
	public Task getActivity()
	{
		return (Task) getProject().findObject(new ORef(TaskSchema.getObjectType(), activityId));
	}

    private DiagramFactor diagramStrategy1;
	private DiagramFactor diagramStrategy2;
	private DiagramFactor diagramCause;
	private BaseId objectiveId;
	private BaseId activityId;
	private BaseId indicatorId;
	private BaseId subtaskId;
	private BaseId taskId;
	private BaseId methodId;
}
