/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.DirectThreatSet;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.NonDraftInterventionSet;
import org.conservationmeasures.eam.objects.TargetSet;
import org.conservationmeasures.eam.project.Project;

class IndicatorTableModel extends ObjectManagerTableModel
{
	IndicatorTableModel(Project projectToUse)
	{
		super(projectToUse.getIndicatorPool(), indicatorColumnTags);
		project = projectToUse;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(indicatorColumnTags[columnIndex].equals(COLUMN_FACTORS))
		{
			int indicatorId = pool.getIds()[rowIndex];
			ConceptualModelNode[] modelNodes =  project.findNodesThatUseThisIndicator(indicatorId).toNodeArray();
			
			return getNodeLabelsAsString(modelNodes);
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_DIRECT_THREATS))
		{
			int indicatorId = pool.getIds()[rowIndex];
			ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisIndicator(indicatorId);
			DirectThreatSet directThreats = new DirectThreatSet(modelNodes);
			
			return getNodeLabelsAsString(directThreats.toNodeArray());
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_TARGETS))
		{
			int indicatorId = pool.getIds()[rowIndex];
			ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisIndicator(indicatorId);
			TargetSet directThreats = new TargetSet(modelNodes);
			
			return getNodeLabelsAsString(directThreats.toNodeArray());
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_INTERVENTIONS))
		{
			int indicatorId = pool.getIds()[rowIndex];
			ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisIndicator(indicatorId);
			NonDraftInterventionSet directThreats = new NonDraftInterventionSet(modelNodes);
			
			return getNodeLabelsAsString(directThreats.toNodeArray());
		}
		
		return super.getValueAt(rowIndex, columnIndex);
	}
	
	String getNodeLabelsAsString(ConceptualModelNode[] modelNodes)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < modelNodes.length; ++i)
		{
			if(i > 0)
				result.append(", ");
			result.append(modelNodes[i].getLabel());
		}
		
		return result.toString();
	}

	public static final String COLUMN_FACTORS = "Factor(s)";
	public static final String COLUMN_DIRECT_THREATS = "Direct Threat(s)";
	public static final String COLUMN_TARGETS = "Target(s)";
	public static final String COLUMN_INTERVENTIONS = "Intervention(s)";
	
	static final String[] indicatorColumnTags = {
		Indicator.TAG_SHORT_LABEL, 
		Indicator.TAG_LABEL,
		Indicator.TAG_METHOD,
		COLUMN_FACTORS,
		COLUMN_DIRECT_THREATS,
		COLUMN_TARGETS,
		COLUMN_INTERVENTIONS,
		};

	Project project;
}
