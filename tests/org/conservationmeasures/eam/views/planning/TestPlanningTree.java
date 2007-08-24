/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
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
		diagramStrategy = project.createDiagramFactorAndAddToDiagram(Strategy.getObjectType());		
		diagramCause = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		diagramTarget = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		stratToCauseLinkId = project.createDiagramFactorLink(diagramStrategy, diagramCause);		
		causeToTargetLinkId = project.createDiagramFactorLink(diagramCause, diagramTarget);
		
		objectiveId = project.addItemToObjectiveList(diagramCause.getWrappedORef(), Cause.TAG_OBJECTIVE_IDS);
		indicatorId = project.addItemToIndicatorList(diagramCause.getWrappedORef(), Cause.TAG_INDICATOR_IDS);
		goalId = project.addItemToGoalList(diagramTarget.getWrappedORef(), Target.TAG_GOAL_IDS);
		taskId = project.addItemToIndicatorList(indicatorId, Task.getObjectType(), Indicator.TAG_TASK_IDS);
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
	
	ProjectForTesting project;
	DiagramFactor diagramStrategy;		
	DiagramFactor diagramCause;
	DiagramFactor diagramTarget;
	
	DiagramFactorLinkId stratToCauseLinkId;
	DiagramFactorLinkId causeToTargetLinkId;
	
	BaseId indicatorId;
	BaseId objectiveId;
	BaseId goalId;
	BaseId taskId;
}
