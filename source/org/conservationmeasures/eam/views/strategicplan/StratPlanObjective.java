/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class StratPlanObjective extends StratPlanObject
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

	public Object getChild(int index)
	{
		return strategies[index];
	}
	
	public int getType()
	{
		return objective.getType();
	}

	public BaseId getId()
	{
		return objective.getId();
	}

	public String toString()
	{
		return objective.getLabel();
	}

	public boolean canInsertActivityHere()
	{
		return false;
	}

	public void rebuild()
	{
		ConceptualModelNode[] interventionObjects = project.getNodePool().getInterventions();
		Vector strategyVector = new Vector();
		for(int i = 0; i < interventionObjects.length; ++i)
		{
			ConceptualModelIntervention intervention = (ConceptualModelIntervention)interventionObjects[i];
			if(intervention.isStatusDraft())
				continue;
			
			
			if(doesChainContainObjective(intervention))
				strategyVector.add(new StratPlanStrategy(project, intervention));
		}
		strategies = (StratPlanStrategy[])strategyVector.toArray(new StratPlanStrategy[0]);
	}
	
	boolean doesChainContainObjective(ConceptualModelNode chainMember)
	{
		BaseId objectiveId = objective.getId();
		ConceptualModelNode[] chainNodes = project.getDiagramModel().getAllNodesInChain(chainMember).toNodeArray();
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
