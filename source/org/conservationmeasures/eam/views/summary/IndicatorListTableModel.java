/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class IndicatorListTableModel extends ObjectListTableModel
{
	public IndicatorListTableModel(Project projectToUse, ModelNodeId nodeId)
	{
		super(projectToUse, ObjectType.MODEL_NODE, nodeId, ConceptualModelNode.TAG_INDICATOR_IDS, ObjectType.INDICATOR, Indicator.TAG_LABEL);
	}
}
