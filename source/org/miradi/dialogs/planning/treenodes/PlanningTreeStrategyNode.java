/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeStrategyNode extends AbstractPlanningTreeNode
{
	public PlanningTreeStrategyNode(Project projectToUse, ORef strategyRef, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		strategy = (Strategy)project.findObject(strategyRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		ORefList activityRefs = strategy.getActivityRefs();
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
