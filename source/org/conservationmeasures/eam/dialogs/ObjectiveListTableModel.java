/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class ObjectiveListTableModel extends ObjectListTableModel
{
	public ObjectiveListTableModel(Project projectToUse, ModelNodeId nodeId)
	{
		super(projectToUse, ObjectType.MODEL_NODE, nodeId, Factor.TAG_OBJECTIVE_IDS, ObjectType.OBJECTIVE, Objective.TAG_LABEL);
	}
}
