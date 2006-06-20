/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionModifyObjective;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.DirectThreatSet;
import org.conservationmeasures.eam.objects.NonDraftInterventionSet;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ObjectivePool;
import org.conservationmeasures.eam.objects.TargetSet;
import org.conservationmeasures.eam.project.Project;

public class ObjectiveManagementPanel extends ObjectManagementPanel
{
	public ObjectiveManagementPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, new ObjectiveTableModel(mainWindowToUse.getProject()), buttonActionClasses);
	}
	
	public Objective getSelectedObjective()
	{
		int row = table.getSelectedRow();
		if(row < 0)
			return null;
		
		ObjectivePool pool = getProject().getObjectivePool();
		int objectiveId = pool.getIds()[row];
		Objective objective = pool.find(objectiveId);
		return objective;
	}

	static class ObjectiveTableModel extends ObjectManagerTableModel
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
				int objectiveId = pool.getIds()[rowIndex];
				ConceptualModelNode[] modelNodes =  project.findNodesThatUseThisObjective(objectiveId).toNodeArray();
				
				return getNodeLabelsAsString(modelNodes);
			}
			if(objectiveColumnTags[columnIndex].equals(COLUMN_DIRECT_THREATS))
			{
				int objectiveId = pool.getIds()[rowIndex];
				ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisObjective(objectiveId);
				DirectThreatSet directThreats = new DirectThreatSet(modelNodes);
				
				return getNodeLabelsAsString(directThreats.toNodeArray());
			}
			if(objectiveColumnTags[columnIndex].equals(COLUMN_TARGETS))
			{
				int objectiveId = pool.getIds()[rowIndex];
				ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisObjective(objectiveId);
				TargetSet directThreats = new TargetSet(modelNodes);
				
				return getNodeLabelsAsString(directThreats.toNodeArray());
			}
			if(objectiveColumnTags[columnIndex].equals(COLUMN_INTERVENTIONS))
			{
				int objectiveId = pool.getIds()[rowIndex];
				ConceptualModelNodeSet modelNodes =  project.findAllNodesRelatedToThisObjective(objectiveId);
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

	static final String[] objectiveColumnTags = {
		Objective.TAG_SHORT_LABEL, 
		Objective.TAG_LABEL,
		Objective.TAG_FULL_TEXT,
		COLUMN_FACTORS,
		COLUMN_DIRECT_THREATS,
		COLUMN_TARGETS,
		};

	Project project;
}

	static final Class[] buttonActionClasses = {
		ActionCreateObjective.class, 
		ActionModifyObjective.class, 
		ActionDeleteObjective.class, 
		};

}
