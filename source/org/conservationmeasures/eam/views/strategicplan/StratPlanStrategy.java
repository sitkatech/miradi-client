/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StratPlanStrategy extends TreeTableNode
{
	public StratPlanStrategy(Project projectToUse, ConceptualModelIntervention interventionToUse)
	{
		project = projectToUse;
		intervention = interventionToUse;
	}
	
	public ConceptualModelIntervention getIntervention()
	{
		return intervention;
	}
	
	public Object getValueAt(int column)
	{
		if(column == StrategicPlanTreeTableModel.labelColumn)
			return intervention.getLabel();
		
		return "";
	}

	public int getChildCount()
	{
		return 0;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}
	
	public String toString()
	{
		return intervention.toString();
	}

	public int getType()
	{
		return intervention.getType();
	}
	
	Project project;
	ConceptualModelIntervention intervention;
}

