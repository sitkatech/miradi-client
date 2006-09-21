/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.NonDraftInterventionSet;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;

class IndicatorTableModel extends AnnotationTableModel
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
			BaseId indicatorId = pool.getIds()[rowIndex];
			ConceptualModelNode[] modelNodes =  getChainManager().findNodesThatUseThisIndicator(indicatorId).toNodeArray();
			
			return ConceptualModelNode.getNodeLabelsAsString(modelNodes);
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_DIRECT_THREATS))
		{
			BaseId indicatorId = pool.getIds()[rowIndex];
			return getChainManager().getRelatedDirectThreatsAsString(indicatorId);
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_TARGETS))
		{
			BaseId indicatorId = pool.getIds()[rowIndex];
			return getChainManager().getRelatedTargetsAsString(indicatorId);
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_INTERVENTIONS))
		{
			BaseId indicatorId = pool.getIds()[rowIndex];
			ConceptualModelNodeSet modelNodes =  getChainManager().findAllNodesRelatedToThisIndicator(indicatorId);
			NonDraftInterventionSet directThreats = new NonDraftInterventionSet(modelNodes);
			
			return ConceptualModelNode.getNodeLabelsAsString(directThreats.toNodeArray());
		}
		
		return super.getValueAt(rowIndex, columnIndex);
	}

	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
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
