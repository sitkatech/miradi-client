/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.dialogs.ObjectPoolTablePanel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ActivityPoolTablePanel extends ObjectPoolTablePanel
{
	public ActivityPoolTablePanel(Project project)
	{
		super(project, ObjectType.TASK, new ActivityPoolTableModel(project));
	}
}
