/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.NonDraftInterventionSet;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.AnnotationTableModel;

class ObjectiveTableModel extends AnnotationTableModel
{	
	ObjectiveTableModel(Project projectToUse)
	{
		super(projectToUse.getObjectivePool(), objectiveColumnTags);
		project = projectToUse;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		BaseId objectiveId = super.getEAMObjectRows()[rowIndex].getId();
		String columnTag = objectiveColumnTags[columnIndex];
		return getTableCellDisplayString(rowIndex, columnIndex, objectiveId, columnTag);
	}

	public String getTableCellDisplayString(int rowIndex, int columnIndex, BaseId objectiveId, String columnTag)
	{
		if(columnTag.equals(COLUMN_FACTORS))
		{
			ConceptualModelNode[] modelNodes =  getChainManager().findNodesThatUseThisObjective(objectiveId).toNodeArray();
			
			return EAMBaseObject.toHtml(modelNodes);
		}
		if(columnTag.equals(COLUMN_DIRECT_THREATS))
		{
			ConceptualModelNodeSet modelNodes =  getChainManager().findAllNodesRelatedToThisObjective(objectiveId);
			DirectThreatSet directThreats = new DirectThreatSet(modelNodes);
			
			return EAMBaseObject.toHtml(directThreats.toNodeArray());
		}
		if(columnTag.equals(COLUMN_TARGETS))
		{
			ConceptualModelNodeSet modelNodes =  getChainManager().findAllNodesRelatedToThisObjective(objectiveId);
			TargetSet directThreats = new TargetSet(modelNodes);
			
			return EAMBaseObject.toHtml(directThreats.toNodeArray());
		}
		if(columnTag.equals(COLUMN_INTERVENTIONS))
		{
			ConceptualModelNodeSet modelNodes =  getChainManager().findAllNodesRelatedToThisObjective(objectiveId);
			NonDraftInterventionSet directThreats = new NonDraftInterventionSet(modelNodes);
			
			return EAMBaseObject.toHtml(directThreats.toNodeArray());
		}

		return (String) super.getValueAt(rowIndex, columnIndex);
	}
	
	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}

	static final String[] objectiveColumnTags = {
		Objective.TAG_SHORT_LABEL, 
		Objective.TAG_LABEL,
		Objective.TAG_FULL_TEXT,
		COLUMN_FACTORS,
		COLUMN_DIRECT_THREATS,
		COLUMN_TARGETS,
		COLUMN_INTERVENTIONS,
		};
	
	Project project;
}
