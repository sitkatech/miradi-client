/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanStrategyRoot extends TreeTableNode
{
	public WorkPlanStrategyRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return null;
	}
	
	public TreeTableNode getChild(int index)
	{
		return strategies[index];
	}

	public int getChildCount()
	{
		return strategies.length;
	}

	public ORef getObjectReference()
	{
		return EAM.WORKPLAN_STRATEGY_ROOT;
	}
	
	public int getType()
	{
		return getObjectReference().getObjectType();
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	public String toString()
	{
		return STRATEGIC_LABEL;
	}
	
	public void rebuild()
	{
		strategies = getWorkPlanStrategies(project);
	}

	static public WorkPlanStrategyNode[] getWorkPlanStrategies(Project project)
	{
		Factor[] interventionObjects = project.getStrategyPool().getNonDraftStrategies();
		Vector strategyVector = new Vector();
		for(int i = 0; i < interventionObjects.length; ++i)
		{
			Strategy intervention = (Strategy)interventionObjects[i];
			WorkPlanStrategyNode workPlanStrategy = new WorkPlanStrategyNode(project, intervention);
			strategyVector.add(workPlanStrategy);
		}
		WorkPlanStrategyNode strategies[] = (WorkPlanStrategyNode[])strategyVector.toArray(new WorkPlanStrategyNode[0]);
		Arrays.sort(strategies, new IgnoreCaseStringComparator());
		return strategies;
	}
	

	public BaseId getId()
	{
		return null;
	}
	
	Project project;
	WorkPlanStrategyNode[] strategies;
	private static final String STRATEGIC_LABEL = "Strategies";
}
