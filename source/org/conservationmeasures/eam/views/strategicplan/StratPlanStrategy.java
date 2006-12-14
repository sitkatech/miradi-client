/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.TaxonomyItem;
import org.conservationmeasures.eam.objecthelpers.TaxonomyLoader;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;
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
		
		return getTaxonomyLabel();
	}

	private String getTaxonomyLabel()
	{
		try
		{
		String taxonomyCode = intervention.getData(Strategy.TAG_TAXONOMY_CODE);
		TaxonomyItem[] taxonomyItems = TaxonomyLoader.load(TaxonomyLoader.STRATEGY_TAXONOMIES_FILE);
		for(int i = 0; i < taxonomyItems.length; i++)
		{
			if(taxonomyItems[i].getTaxonomyCode().equals(taxonomyCode))
			{
				if (i == 0) 
					return "";
				return taxonomyItems[i].getTaxonomyDescription();
			}
		}
		return intervention.getData(Strategy.TAG_TAXONOMY_CODE);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "";
		}

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

