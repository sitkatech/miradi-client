/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectChainObject;
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
		strategies = getStrategyNodes(objective);
	}

	static public StratPlanStrategy[] getStrategyNodes(Objective objective)
	{
		Project project = objective.getProject();
		ObjectiveId objectiveId = (ObjectiveId)objective.getId();

		Factor[] strategyObjects = project.getStrategyPool().getNonDraftStrategies();
		Vector strategyVector = new Vector();
		for(int i = 0; i < strategyObjects.length; ++i)
		{
			Strategy strategy = (Strategy)strategyObjects[i];
			if(doesChainContainObjective(strategy, objectiveId))
				strategyVector.add(new StratPlanStrategy(project, strategy));
		}
		StratPlanStrategy[] strategies = (StratPlanStrategy[])strategyVector.toArray(new StratPlanStrategy[0]);
		Arrays.sort(strategies, new IgnoreCaseStringComparator());
		return strategies;
	}

	static private boolean doesChainContainObjective(Factor chainMember, ObjectiveId objectiveId)
	{
		ProjectChainObject chainObject = new ProjectChainObject();
		chainObject.buildUpstreamDownstreamChain(chainMember);
		Factor[] chainNodes = chainObject.getFactorsArray();
		for(int i = 0; i < chainNodes.length; ++i)
		{
			if(chainNodes[i].getObjectives().contains(objectiveId))
				return true;
		}
		
		return false;
	}

	Project project;
	Objective objective;
	StratPlanStrategy[] strategies;
}
