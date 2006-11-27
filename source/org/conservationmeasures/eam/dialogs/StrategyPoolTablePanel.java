/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class StrategyPoolTablePanel extends ObjectPoolTablePanel
{
	public StrategyPoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.MODEL_NODE, new StrategyPoolTableModel(projectToUse));
	}
}
