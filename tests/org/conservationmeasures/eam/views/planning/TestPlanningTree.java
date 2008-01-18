/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.ProjectForTesting;

abstract public class TestPlanningTree extends EAMTestCase
{
	public TestPlanningTree(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		setupFactors();
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	private void setupFactors() throws Exception
	{
		projectMetadata = project.getMetadata();
		diagramStrategy = project.createDiagramFactorAndAddToDiagram(Strategy.getObjectType());		
		diagramCause = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		diagramTarget = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		stratToCauseLinkId = project.createDiagramLinkAndAddToDiagram(diagramStrategy, diagramCause).getObjectId();		
		causeToTargetLinkId = project.createDiagramLinkAndAddToDiagram(diagramCause, diagramTarget).getObjectId();
		
		objectiveId = project.addItemToObjectiveList(diagramCause.getWrappedORef(), Cause.TAG_OBJECTIVE_IDS);
		indicatorId = project.addItemToIndicatorList(diagramCause.getWrappedORef(), Cause.TAG_INDICATOR_IDS);
		goalId = project.addItemToGoalList(diagramTarget.getWrappedORef(), Target.TAG_GOAL_IDS);
		taskId = project.addItemToIndicatorList(indicatorId, Task.getObjectType(), Indicator.TAG_TASK_IDS);
		activityId = project.addActivityToStrateyList(diagramStrategy.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS);
		subtaskId = project.addSubtaskToActivity(getTask().getRef(), Task.TAG_SUBTASK_IDS);
	}
	
	public Goal getGoal()
	{
		return (Goal) project.findObject(new ORef(Goal.getObjectType(), goalId));
	}
	
	public Objective getObjective()
	{
		return (Objective) project.findObject(new ORef(Objective.getObjectType(), objectiveId));
	}
	
	public Strategy getStrategy()
	{
		return (Strategy) project.findObject(diagramStrategy.getWrappedORef());
	}
	
	public Indicator getIndicator()
	{
		return (Indicator) project.findObject(new ORef(Indicator.getObjectType(), indicatorId));
	}
	
	public Task getTask()
	{
		return (Task) project.findObject(new ORef(Task.getObjectType(), taskId));
	}
	
	public Task getSubtask()
	{
		return (Task) project.findObject(new ORef(Task.getObjectType(), subtaskId));
	}
	
	public Target getTarget()
	{
		return (Target) project.findObject(diagramTarget.getWrappedORef());
	}
	
	public Cause getThreat()
	{
		return (Cause) project.findObject(diagramCause.getWrappedORef());
	}
	
	public ProjectMetadata getProjectMetadata()
	{
		return projectMetadata;
	}
	
	ProjectForTesting project;
	ProjectMetadata projectMetadata;
	DiagramFactor diagramStrategy;		
	DiagramFactor diagramCause;
	DiagramFactor diagramTarget;
	
	BaseId stratToCauseLinkId;
	BaseId causeToTargetLinkId;
	
	BaseId indicatorId;
	BaseId objectiveId;
	BaseId goalId;
	BaseId taskId;
	BaseId activityId;
	BaseId subtaskId;
}
