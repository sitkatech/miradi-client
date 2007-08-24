/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Arrays;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StratPlanObjective extends TreeTableNode
{
	public StratPlanObjective(Project projectToUse, Objective objectiveToUse)
	{
		project = projectToUse;
		if(objectiveToUse == null)
			EAM.logError("Attempted to create tree node for null objective");
		objective = objectiveToUse;
		strategies = new StratPlanStrategy[0];
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return objective;
	}
	
	public Object getValueAt(int column)
	{
		if(column == StrategicPlanTreeTableModel.labelColumn)
			return toString();
		return "";
	}

	public int getChildCount()
	{
		return strategies.length;
	}

	public TreeTableNode getChild(int index)
	{
		return strategies[index];
	}
	
	public ORef getObjectReference()
	{
		return objective.getRef();
	}
	
	public int getType()
	{
		return objective.getType();
	}

	public String toString()
	{
		return objective.toString();
	}

	public void rebuild()
	{
		ObjectManager objectManager = project.getObjectManager();
		ORefList strategyRefs = objectManager.getStrategyNodes(objective.getRef());
		
		strategies = new StratPlanStrategy[strategyRefs.size()];
		for (int i = 0; i < strategyRefs.size(); ++i)
		{
			Strategy strategy = (Strategy) objectManager.findObject(strategyRefs.get(i));
			strategies[i] = new StratPlanStrategy(project, strategy);
		}
		
		Arrays.sort(strategies, new IgnoreCaseStringComparator());
	}

	Project project;
	Objective objective;
	StratPlanStrategy[] strategies;
}
