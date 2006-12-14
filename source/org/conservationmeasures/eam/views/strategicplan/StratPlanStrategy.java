/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StrategyTaxonomyQuestion;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StratPlanStrategy extends TreeTableNode
{
	public StratPlanStrategy(Project projectToUse, Strategy interventionToUse)
	{
		project = projectToUse;
		intervention = interventionToUse;
	}
	
	public EAMObject getObject()
	{
		return intervention;
	}
	
	public Strategy getIntervention()
	{
		return intervention;
	}
	
	public Object getValueAt(int column)
	{
		if(column == StrategicPlanTreeTableModel.labelColumn)
			return intervention.getLabel();
		String taxonomyCode = intervention.getData(Strategy.TAG_TAXONOMY_CODE);
		if (taxonomyCode.length()==0)
			return "";
		return new StrategyTaxonomyQuestion("").findChoiceByCode(taxonomyCode).getLabel();
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
	
	public ORef getObjectReference()
	{
		return intervention.getRef();
	}

	public int getType()
	{
		return intervention.getType();
	}
	
	public void rebuild() throws Exception
	{
	}

	
	Project project;
	Strategy intervention;
}

