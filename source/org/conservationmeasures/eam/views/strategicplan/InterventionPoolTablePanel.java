/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.dialogs.ObjectPoolTablePanel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class InterventionPoolTablePanel extends ObjectPoolTablePanel
{
	public InterventionPoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.MODEL_NODE, new InterventionPoolTableModel(projectToUse));
	}
}
