/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.summary.IndicatorListTableModel;

public class IndicatorListTablePanel extends ObjectListTablePanel
{
	public IndicatorListTablePanel(Project projectToUse, ModelNodeId nodeId)
	{
		super(new IndicatorListTableModel(projectToUse, nodeId));
	}

}
