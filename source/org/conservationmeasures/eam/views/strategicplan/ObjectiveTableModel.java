/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.DirectThreatSet;
import org.conservationmeasures.eam.objects.NonDraftInterventionSet;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.TargetSet;
import org.conservationmeasures.eam.project.Project;

class ObjectiveTableModel extends AnnotationTableModel
{	
	ObjectiveTableModel(Project projectToUse)
	{
		super(projectToUse.getObjectivePool(), objectiveColumnTags);
		project = projectToUse;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(objectiveColumnTags[columnIndex].equals(COLUMN_FACTORS))
		{
			BaseId objectiveId = pool.getIds()[rowIndex];
			ConceptualModelNode[] modelNodes =  project.findNodesThatUseThisObjective(objectiveId).toNodeArray();
			
			return getNodeLabelsAsString(modelNodes);
		}
		if(objectiveColumnTags[columnIndex].equals(COLUMN_DIRECT_THREATS))
		{
			BaseId objectiveId = pool.getIds()[rowIndex];
			ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisObjective(objectiveId);
			DirectThreatSet directThreats = new DirectThreatSet(modelNodes);
			
			return getNodeLabelsAsString(directThreats.toNodeArray());
		}
		if(objectiveColumnTags[columnIndex].equals(COLUMN_TARGETS))
		{
			BaseId objectiveId = pool.getIds()[rowIndex];
			ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisObjective(objectiveId);
			TargetSet directThreats = new TargetSet(modelNodes);
			
			return getNodeLabelsAsString(directThreats.toNodeArray());
		}
		if(objectiveColumnTags[columnIndex].equals(COLUMN_INTERVENTIONS))
		{
			BaseId objectiveId = pool.getIds()[rowIndex];
			ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisObjective(objectiveId);
			NonDraftInterventionSet directThreats = new NonDraftInterventionSet(modelNodes);
			
			return getNodeLabelsAsString(directThreats.toNodeArray());
		}
		
		return super.getValueAt(rowIndex, columnIndex);
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
