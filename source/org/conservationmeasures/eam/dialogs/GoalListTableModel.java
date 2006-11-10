/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;

public class GoalListTableModel extends ObjectListTableModel
{
	public GoalListTableModel(Project projectToUse, ModelNodeId nodeId)
	{
		super(projectToUse, ObjectType.MODEL_NODE, nodeId, ConceptualModelNode.TAG_GOAL_IDS, ObjectType.GOAL, Goal.TAG_LABEL);
	}
}
