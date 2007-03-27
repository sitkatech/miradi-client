/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StratPlanObjective extends TreeTableNode
{
	public StratPlanObjective(Project projectToUse, Desire objectiveToUse)
	{
		project = projectToUse;
		if(objectiveToUse == null)
			EAM.logError("Attempted to create tree node for null objective");
		objective = objectiveToUse;
		strategies = new StratPlanStrategy[0];
		rebuild();
	}
	
	public EAMObject getObject()
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
		BaseId desireId = objective.getId();

		Factor[] interventionObjects = project.getFactorPool().getStrategies();
		Vector strategyVector = new Vector();
		for(int i = 0; i < interventionObjects.length; ++i)
		{
			Strategy intervention = (Strategy)interventionObjects[i];
			if(intervention.isStatusDraft())
				continue;
			
			
			if(doesChainContainDesire(intervention, desireId))
				strategyVector.add(new StratPlanStrategy(project, intervention));
		}
		strategies = (StratPlanStrategy[])strategyVector.toArray(new StratPlanStrategy[0]);
		Arrays.sort(strategies, new IgnoreCaseStringComparator());
	}

	// FIXME: I believe this method should be renamed doesChainContainObjective, (Kevin) 
	// and the call to getGoals() can be removed. kbs 2007-03-23
	private boolean doesChainContainDesire(Factor chainMember, BaseId desireId)
	{
		Factor[] chainNodes = project.getDiagramModel().getAllUpstreamDownstreamNodes(chainMember).toNodeArray();
		for(int i = 0; i < chainNodes.length; ++i)
		{
			if(chainNodes[i].getObjectives().contains(desireId))
				return true;

			if(chainNodes[i].getGoals().contains(desireId))
				return true;
		}
		
		return false;
	}

	Project project;
	Desire objective;
	StratPlanStrategy[] strategies;
}
