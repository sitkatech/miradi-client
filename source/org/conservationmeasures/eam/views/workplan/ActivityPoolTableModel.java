/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.LegacyObjectPoolTableModel;

public class ActivityPoolTableModel extends LegacyObjectPoolTableModel
{
	public ActivityPoolTableModel(Project projectToUse)
	{
		super(projectToUse.getPool(ObjectType.TASK), columnLabels);
		project = projectToUse;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(columnIndex == 1)
			return getStrategies(rowIndex);
		if(columnIndex == 2)
			return getResources(rowIndex);
		return super.getValueAt(rowIndex, columnIndex);
	}
	
	String getStrategies(int rowIndex)
	{
		Vector strategies = new Vector();
		BaseId thisTaskId = getTaskId(rowIndex);
		IdList nodeIds = project.getNodePool().getIdList();
		for(int i = 0; i < nodeIds.size(); ++i)
		{
			ModelNodeId nodeId = new ModelNodeId(nodeIds.get(i).asInt());
			ConceptualModelNode node = project.findNode(nodeId);
			if(node.isIntervention())
			{
				ConceptualModelIntervention intervention = (ConceptualModelIntervention)node;
				if(intervention.getActivityIds().contains(thisTaskId))
					strategies.add(intervention);
			}
		}
		return EAMBaseObject.toHtml((EAMBaseObject[])strategies.toArray(new EAMBaseObject[0]));
	}
	
	String getResources(int rowIndex)
	{
		Task task = (Task)project.findObject(ObjectType.TASK, getTaskId(rowIndex));
		ProjectResource[] resources = project.getTaskResources(task);
		return EAMBaseObject.toHtml(resources);
	}

	private BaseId getTaskId(int rowIndex)
	{
		IdList taskIds = project.getPool(ObjectType.TASK).getIdList();
		BaseId thisTaskId = taskIds.get(rowIndex);
		return thisTaskId;
	}
	
	static String[] columnLabels = {
		"Label",
		"Strategy",
		"ResourceIds",
	};
	
	Project project;
}
