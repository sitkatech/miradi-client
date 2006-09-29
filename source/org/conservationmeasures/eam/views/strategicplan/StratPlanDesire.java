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
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.project.Project;

public class StratPlanDesire extends StratPlanObject
{
	public StratPlanDesire(Project projectToUse, Desire desireToUse)
	{
		project = projectToUse;
		if(desireToUse == null)
			EAM.logError("Attempted to create tree node for null objective");
		desire = desireToUse;
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
		return desire.getType();
	}

	public BaseId getId()
	{
		return desire.getId();
	}

	public String toString()
	{
		return desire.getLabel();
	}

	public boolean canInsertActivityHere()
	{
		return false;
	}

	public void rebuild()
	{
		BaseId desireId = desire.getId();

		ConceptualModelNode[] interventionObjects = project.getNodePool().getInterventions();
		Vector strategyVector = new Vector();
		for(int i = 0; i < interventionObjects.length; ++i)
		{
			ConceptualModelIntervention intervention = (ConceptualModelIntervention)interventionObjects[i];
			if(intervention.isStatusDraft())
				continue;
			
			
			if(doesChainContainDesire(intervention, desireId))
				strategyVector.add(new StratPlanStrategy(project, intervention));
		}
		strategies = (StratPlanStrategy[])strategyVector.toArray(new StratPlanStrategy[0]);
	}
	
	private boolean doesChainContainDesire(ConceptualModelNode chainMember, BaseId desireId)
	{
		ConceptualModelNode[] chainNodes = project.getDiagramModel().getAllNodesInChain(chainMember).toNodeArray();
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
	Desire desire;
	StratPlanStrategy[] strategies;
}
