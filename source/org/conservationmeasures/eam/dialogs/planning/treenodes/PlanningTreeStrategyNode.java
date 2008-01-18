/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeStrategyNode extends AbstractPlanningTreeNode
{
	public PlanningTreeStrategyNode(Project projectToUse, ORef strategyRef) throws Exception
	{
		super(projectToUse);
		strategy = (Strategy)project.findObject(strategyRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		ORefList activityRefs = strategy.getActivities();
		createAndAddTaskNodes(activityRefs);
	}

	public BaseObject getObject()
	{
		return strategy;
	}

	boolean shouldSortChildren()
	{
		return false;
	}

	Strategy strategy;
}
