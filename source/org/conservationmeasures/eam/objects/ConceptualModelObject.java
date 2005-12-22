/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.annotations.Goals;
import org.conservationmeasures.eam.annotations.Indicator;
import org.conservationmeasures.eam.annotations.Objectives;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.project.IdAssigner;

public class ConceptualModelObject
{
	public ConceptualModelObject(NodeType nodeType)
	{
		type = nodeType;
		
		id = IdAssigner.INVALID_ID;
		indicator = new Indicator();
		setNodePriority(ThreatPriority.createPriorityNotUsed());
		objectives = new Objectives();
		goals = new Goals();
	}
	
	public void setId(int idToUse)
	{
		id = idToUse;
	}

	public int getId()
	{
		return id;
	}

	public NodeType getType()
	{
		return type;
	}
	
	public void setType(NodeType typeToUse)
	{
		type = typeToUse;
	}

	public Indicator getIndicator()
	{
		return indicator;
	}
	
	public void setIndicator(Indicator indicatorToUse)
	{
		indicator = indicatorToUse;
	}

	public ThreatPriority getThreatPriority()
	{
		return threatPriority;
	}
	
	public void setNodePriority(ThreatPriority priorityToUse)
	{
		threatPriority = priorityToUse;
	}
	
	public Objectives getObjectives()
	{
		return objectives;
	}

	public void setObjectives(Objectives objectivesToUse)
	{
		objectives = objectivesToUse;
	}
	
	public Goals getGoals()
	{
		return goals;
	}

	public void setGoals(Goals goalsToUse)
	{
		goals = goalsToUse;
	}

	public boolean isIntervention()
	{
		return false;
	}
	
	public boolean isFactor()
	{
		return false;
	}
	
	public boolean isTarget()
	{
		return false;
	}
	
	public boolean isIndirectFactor()
	{
		return false;
	}
	
	public boolean isDirectThreat()
	{
		return false;
	}
	
	public boolean isStress()
	{
		return false;
	}
	
	public boolean canHaveObjectives()
	{
		return false;
	}

	public boolean canHaveGoal()
	{
		return false;
	}

	public boolean canHavePriority()
	{
		return false;
	}
	
	private int id;
	private NodeType type;
	private Indicator indicator;
	private ThreatPriority threatPriority;
	private Objectives objectives;
	private Goals goals;
}
