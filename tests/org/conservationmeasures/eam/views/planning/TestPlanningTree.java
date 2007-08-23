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
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
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
		diagramStrategy1 = project.createDiagramFactorAndAddToDiagram(Strategy.getObjectType());		
		diagramCause1 = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		diagramTarget1 = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		strat1ToCause1LinkId = project.createDiagramFactorLink(diagramStrategy1, diagramCause1);		
		cause1ToTarget1LinkId = project.createDiagramFactorLink(diagramCause1, diagramTarget1);
		
		objectiveId = project.addItemToObjectiveList(diagramCause1.getWrappedORef(), Cause.TAG_OBJECTIVE_IDS);
		indicatorId = project.addItemToIndicatorList(diagramCause1.getWrappedORef(), Cause.TAG_INDICATOR_IDS);
		goalId = project.addItemToGoalList(diagramTarget1.getWrappedORef(), Target.TAG_GOAL_IDS);
	}
	
	public Goal getGoal()
	{
		return (Goal) project.findObject(new ORef(Goal.getObjectType(), goalId));
	}
	
	ProjectForTesting project;
	DiagramFactor diagramStrategy1;		
	DiagramFactor diagramCause1;
	DiagramFactor diagramTarget1;
	
	DiagramFactorLinkId strat1ToCause1LinkId;
	DiagramFactorLinkId cause1ToTarget1LinkId;
	
	BaseId indicatorId;
	BaseId objectiveId;
	BaseId goalId;
}
