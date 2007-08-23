/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestPlanningTree extends EAMTestCase
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
		DiagramFactor diagramStrategy1 = project.createDiagramFactorAndAddToDiagram(Strategy.getObjectType());		
		DiagramFactor diagramCause1 = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor diagramTarget1 = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());

		project.createDiagramFactorLink(diagramStrategy1, diagramCause1);		
		project.createDiagramFactorLink(diagramCause1, diagramTarget1);
		
		project.addItemToObjectiveList(diagramCause1.getId(), Objective.getObjectType(), Cause.TAG_OBJECTIVE_IDS);
		project.addItemToIndicatorList(diagramCause1.getId(), Indicator.getObjectType(), Cause.TAG_INDICATOR_IDS);
		project.addItemToGoalList(diagramTarget1.getId(), Goal.getObjectType(), Target.TAG_GOAL_IDS);
	}
	
	public void testPlanningTreeGoalNodes()
	{
		//TODO finish project
		//already asserted fail
	}
	
	ProjectForTesting project;
}
