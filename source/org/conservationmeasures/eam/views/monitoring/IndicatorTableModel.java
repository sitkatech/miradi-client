/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.NonDraftInterventionSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.AnnotationTableModel;

public class IndicatorTableModel extends AnnotationTableModel
{
	IndicatorTableModel(Project projectToUse)
	{
		super(projectToUse.getIndicatorPool(), indicatorColumnTags);
		project = projectToUse;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		BaseId indicatorId = getPool().getIds()[rowIndex];
		if(indicatorColumnTags[columnIndex].equals(COLUMN_FACTORS))
		{
			ConceptualModelNode[] modelNodes =  getChainManager().findNodesThatUseThisIndicator(indicatorId).toNodeArray();
			
			return ConceptualModelNode.getNodeLabelsAsHtml(modelNodes);
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_DIRECT_THREATS))
		{
			return getChainManager().getRelatedDirectThreatsAsHtml(indicatorId);
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_TARGETS))
		{
			return getChainManager().getRelatedTargetsAsHtml(indicatorId);
		}
		if(indicatorColumnTags[columnIndex].equals(COLUMN_INTERVENTIONS))
		{
			ConceptualModelNodeSet modelNodes =  getChainManager().findAllNodesRelatedToThisIndicator(indicatorId);
			NonDraftInterventionSet directThreats = new NonDraftInterventionSet(modelNodes);
			
			return ConceptualModelNode.getNodeLabelsAsHtml(directThreats.toNodeArray());
		}
		if(indicatorColumnTags[columnIndex].equals(Indicator.TAG_RESOURCE_IDS))
		{
			Indicator indicator = (Indicator)project.findObject(ObjectType.INDICATOR, indicatorId);
			ProjectResource[] resources = getResourcesForIndicator(project, indicator);
			return ProjectResource.getResourcesAsHtml(resources);
		}
		
		return super.getValueAt(rowIndex, columnIndex);
	}

	public static ProjectResource[] getResourcesForIndicator(Project project, Indicator indicator)
	{
		ResourcePool resourcePool = project.getResourcePool();
		IdList resourceIds = indicator.getResourceIdList();
		ProjectResource[] resources = new ProjectResource[resourceIds.size()];
		for(int i = 0; i < resourceIds.size(); ++i)
			resources[i] = resourcePool.find(resourceIds.get(i));
		return resources;
	}
	
	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
	static final String[] indicatorColumnTags = {
		Indicator.TAG_SHORT_LABEL, 
		Indicator.TAG_LABEL,
		Indicator.TAG_METHOD,
		Indicator.TAG_RESOURCE_IDS,
		COLUMN_FACTORS,
		COLUMN_DIRECT_THREATS,
		COLUMN_TARGETS,
		COLUMN_INTERVENTIONS,
		};

	Project project;
}
