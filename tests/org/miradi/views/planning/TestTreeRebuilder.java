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

import org.miradi.main.TestCaseWithProject;

public class TestTreeRebuilder extends TestCaseWithProject
{
	public TestTreeRebuilder(String name)
	{
		super(name);
	}
	
	public void testBasics()
	{
		//FIXME urgent - add basic tests here.
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
//	public void testMerging() throws Exception
//	{
//		CodeList rowCodes = new CodeList();
//		rowCodes.add(Task.ACTIVITY_NAME);
//		HiddenConfigurableProjectRootNode root = new HiddenConfigurableProjectRootNode(project, rowCodes);
//		assertEquals(1, root.getChildCount());
//
//		final TreeTableNode firstChild = root.getChild(0);
//		assertEquals(Task.getObjectType(), firstChild.getType());
//		assertEquals(2, firstChild.getProportionShares());
//		assertFalse("Full proportion task is allocated?", firstChild.areBudgetValuesAllocated());
//
//	}
	
	
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
	
//	private void setupFactors() throws Exception
//	{
//		projectMetadata = project.getMetadata();
//		diagramStrategy1 = project.createDiagramFactorAndAddToDiagram(Strategy.getObjectType());		
//		diagramStrategy2 = project.createDiagramFactorAndAddToDiagram(Strategy.getObjectType());		
//		diagramCause = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
//		diagramTarget = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());
//		
//		stratToCauseLinkId = project.createDiagramLinkAndAddToDiagram(diagramStrategy1, diagramCause).getObjectId();		
//		causeToTargetLinkId = project.createDiagramLinkAndAddToDiagram(diagramCause, diagramTarget).getObjectId();
//		
//		objectiveId = project.addItemToObjectiveList(diagramCause.getWrappedORef(), Cause.TAG_OBJECTIVE_IDS);
//		indicatorId = project.addItemToIndicatorList(diagramCause.getWrappedORef(), Cause.TAG_INDICATOR_IDS);
//		goalId = project.addItemToGoalList(diagramTarget.getWrappedORef(), Target.TAG_GOAL_IDS);
//		taskId = project.addItemToIndicatorList(indicatorId, Task.getObjectType(), Indicator.TAG_METHOD_IDS);
//		activityId = project.addActivityToStrateyList(diagramStrategy1.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS);
//		subtaskId = project.addSubtaskToActivity(getTask().getRef(), Task.TAG_SUBTASK_IDS);
//		
//		IdList activityIds = new IdList(Task.getObjectType(), new BaseId[] {activityId});
//		project.setObjectData(diagramStrategy2.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS, activityIds.toString());
//		
//		strategyResourceAssignmentRef = project.addResourceAssignment(getStrategy(), 1, 2001, 2001).getRef();
//		indicatorResourceAssignmentRef = project.addResourceAssignment(getIndicator(), 2, 2002, 2002).getRef();
//		subtaskResourceAssignmentRef = project.addResourceAssignment(getSubtask(), 4, 2004, 2004).getRef();
//	}
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
//	public Strategy getStrategy()
//	{
//		return (Strategy) project.findObject(diagramStrategy1.getWrappedORef());
//	}
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
//	public Indicator getIndicator()
//	{
//		return (Indicator) project.findObject(new ORef(Indicator.getObjectType(), indicatorId));
//	}
//	
//	public Task getTask()
//	{
//		return (Task) project.findObject(new ORef(Task.getObjectType(), taskId));
//	}
//	
//	public Task getSubtask()
//	{
//		return (Task) project.findObject(new ORef(Task.getObjectType(), subtaskId));
//	}
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
//	
}
