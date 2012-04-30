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
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.project.ProjectForTesting;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CodeList;

public class TestTreeRebuilder extends TestCaseWithProject
{
	public TestTreeRebuilder(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		setupFactors();
	}
	
	@Override
	public void tearDown() throws Exception
	{
		// TODO Auto-generated method stub
		super.tearDown();
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
	
//FIXME urgent - The below commented code is from test classes.  They were testing old nodes.  Need
// to convert the test to test new TreeRebuilder. 
	
//	public void testPlanningTreeStrategyNode() throws Exception
//	{
//		ORefList activityRefs = getStrategy().getActivityRefs();
//		assertEquals("wrong activity count?", 1, activityRefs.size());
//		assertTrue("wrong type returned?", isActivity(activityRefs.get(0)));
//	}
//	
//	private boolean isActivity(ORef ref)
//	{
//		Task task = (Task) project.findObject(ref);
//		return task.isActivity();
//	}
	
	
//	public void testPlanningTreeActivityNode() throws Exception
//	{
//		ORefList taskRefs = getTask().getSubTaskRefs();
//		assertEquals("wrong subtask count?", 1, taskRefs.size());
//		assertEquals("wrong type returned?", Task.getObjectType(), taskRefs.get(0).getObjectType());
//	}
//
//	public void testSubtasks() throws Exception
//	{
//		AbstractProjectNode root = createCompleteTree();
//		ORefSet refsInTree = root.getAllRefsInTree();
//		assertTrue("Didn't add subtask to tree?", refsInTree.contains(getSubtask().getRef()));
//	}
//
	public void testMerging() throws Exception
	{
		CodeList rowCodes = new CodeList();
		rowCodes.add(TaskSchema.ACTIVITY_NAME);
		ObjectTreeTableConfiguration configuration = new ObjectTreeTableConfiguration(getObjectManager(), BaseId.INVALID);
		configuration.setData(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, rowCodes.toString());
		CustomTablePlanningTreeRowColumnProvider rowColumnProvider = new CustomTablePlanningTreeRowColumnProvider(getProject(), configuration);
		NormalTreeRebuilder rebuilder = new NormalTreeRebuilder(getProject(), rowColumnProvider);
		AbstractPlanningTreeNode rootNode = new PlanningTreeRootNodeAlwaysExpanded(getProject());
		rebuilder.rebuildTree(rootNode);
		assertEquals(1, rootNode.getChildCount());

		final TreeTableNode firstChild = rootNode.getChild(0);
		assertEquals(TaskSchema.getObjectType(), firstChild.getType());
		assertEquals(2, firstChild.getProportionShares());
		assertFalse("Full proportion task is allocated?", firstChild.areBudgetValuesAllocated());

	}
	
	
//	public void testPlanningTreeIndicatorNode() throws Exception
//	{
//		String relatedMethods = getIndicator().getPseudoData(Indicator.PSEUDO_TAG_RELATED_METHOD_OREF_LIST);
//		ORefList methodRefs = new ORefList(relatedMethods);
//		assertEquals("wrong method count?", 1, methodRefs.size());
//		assertTrue("wrong type returned?", isMethod(methodRefs.get(0)));
//	}
//	
//	private boolean isMethod(ORef ref)
//	{
//		Task task = (Task) project.findObject(ref);
//		return task.isMethod();
//	}

	
//	public void testMergeChildIntoList() throws Exception
//	{
//		Strategy strategy = getProject().createStrategy();
//		Task activity = getProject().createTask(strategy);
//		ORefList relevantActivityRefs = new ORefList(activity);
//		RelevancyOverrideSet relevantActivities = new RelevancyOverrideSet();
//		for (int index = 0; index < relevantActivityRefs.size(); ++index)
//		{
//			relevantActivities.add(new RelevancyOverride(relevantActivityRefs.get(index), true));
//		}
//		
//		Cause factor = getProject().createCause();
//		Objective objective = getProject().createObjective(factor);
//		getProject().fillObjectUsingCommand(objective, Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevantActivities.toString());
//		
//		PlanningTreeObjectiveNode objectiveNode = new PlanningTreeObjectiveNode(getProject(), getProject().getTestingDiagramObject(), objective.getRef(), new CodeList());	
//		assertEquals("Wrong objectice children count?", 1, objectiveNode.getChildCount());
//		
//		Vector<AbstractPlanningTreeNode> destination = new Vector<AbstractPlanningTreeNode>();
//		destination.add(objectiveNode);
//		
//		PlanningTreeTaskNode activityNode = new PlanningTreeTaskNode(getProject(), strategy.getRef(), activity.getRef(), new CodeList());
//		AbstractPlanningTreeNode.mergeChildIntoList(destination, activityNode);
//		
//		assertEquals("Activity node should not have been added since its a child of the objective?", 1, destination.size());
//	}
	
	private void setupFactors() throws Exception
	{
		ProjectForTesting project = getProject();
		
		projectMetadata = project.getMetadata();
		diagramStrategy1 = project.createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());		
		diagramStrategy2 = project.createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());		
		diagramCause = project.createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		diagramTarget = project.createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		
		stratToCauseLinkId = project.createDiagramLinkAndAddToDiagram(diagramStrategy1, diagramCause).getObjectId();		
		causeToTargetLinkId = project.createDiagramLinkAndAddToDiagram(diagramCause, diagramTarget).getObjectId();
		
		objectiveId = project.addItemToObjectiveList(diagramCause.getWrappedORef(), Cause.TAG_OBJECTIVE_IDS);
		indicatorId = project.addItemToIndicatorList(diagramCause.getWrappedORef(), Cause.TAG_INDICATOR_IDS);
		goalId = project.addItemToGoalList(diagramTarget.getWrappedORef(), Target.TAG_GOAL_IDS);
		taskId = project.addItemToIndicatorList(indicatorId, TaskSchema.getObjectType(), Indicator.TAG_METHOD_IDS);
		activityId = project.addActivityToStrateyList(diagramStrategy1.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS);
		subtaskId = project.addSubtaskToActivity(getTask().getRef(), Task.TAG_SUBTASK_IDS);
		
		IdList activityIds = new IdList(TaskSchema.getObjectType(), new BaseId[] {activityId});
		project.setObjectData(diagramStrategy2.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS, activityIds.toString());
		
		strategyResourceAssignmentRef = project.addResourceAssignment(getStrategy(), 1, 2001, 2001).getRef();
		indicatorResourceAssignmentRef = project.addResourceAssignment(getIndicator(), 2, 2002, 2002).getRef();
		subtaskResourceAssignmentRef = project.addResourceAssignment(getSubtask(), 4, 2004, 2004).getRef();
	}
//	
//	public AbstractProjectNode createCompleteTree() throws Exception
//	{
//		ChoiceQuestion rowChoiceQuestion= new CustomPlanningRowsQuestion(project);
//		HiddenConfigurableProjectRootNode root = new HiddenConfigurableProjectRootNode(project, rowChoiceQuestion.getAllCodes());
//		return root;
//	}
//	
//	public Goal getGoal()
//	{
//		return (Goal) project.findObject(new ORef(Goal.getObjectType(), goalId));
//	}
//	
//	public Objective getObjective()
//	{
//		return (Objective) project.findObject(new ORef(Objective.getObjectType(), objectiveId));
//	}
//	
	public Strategy getStrategy()
	{
		return (Strategy) getProject().findObject(diagramStrategy1.getWrappedORef());
	}
//	
//	public Strategy getStrategy2()
//	{
//		return (Strategy) project.findObject(diagramStrategy2.getWrappedORef());
//	}
//	
//	public Task getActivity()
//	{
//		return Task.find(project, new ORef(Task.getObjectType(), activityId));
//	}
//	
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
//	
//	public Target getTarget()
//	{
//		return (Target) project.findObject(diagramTarget.getWrappedORef());
//	}
//	
//	public Cause getThreat()
//	{
//		return (Cause) project.findObject(diagramCause.getWrappedORef());
//	}
//	
//	public ProjectMetadata getProjectMetadata()
//	{
//		return projectMetadata;
//	}
//	
//	public ResourceAssignment getStrategyResourceAssignment()
//	{
//		return ResourceAssignment.find(project, strategyResourceAssignmentRef);
//	}
//	
//	public ResourceAssignment getIndicatorResourceAssignment()
//	{
//		return ResourceAssignment.find(project, indicatorResourceAssignmentRef);
//	}
//	
//	public ResourceAssignment getSubtaskResourceAssignment()
//	{
//		return ResourceAssignment.find(project, subtaskResourceAssignmentRef);
//	}

	private ProjectMetadata projectMetadata;
	private DiagramFactor diagramStrategy1;
	private DiagramFactor diagramStrategy2;
	private DiagramFactor diagramCause;
	private DiagramFactor diagramTarget;
	private BaseId stratToCauseLinkId;
	private BaseId causeToTargetLinkId;
	private BaseId objectiveId;
	private BaseId goalId;
	private BaseId activityId;
	private BaseId indicatorId;
	private BaseId subtaskId;
	private BaseId taskId;
	private ORef strategyResourceAssignmentRef;
	private ORef indicatorResourceAssignmentRef;
	private ORef subtaskResourceAssignmentRef;
}
