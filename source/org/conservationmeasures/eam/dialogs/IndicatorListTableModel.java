/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class IndicatorListTableModel extends ObjectListTableModel
{
	public IndicatorListTableModel(Project projectToUse, FactorId nodeId)
	{
		super(projectToUse, ObjectType.MODEL_NODE, nodeId, Factor.TAG_INDICATOR_IDS, ObjectType.INDICATOR, Indicator.TAG_LABEL);
	}
}
