/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.ListSelectionModel;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ResourcePoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public ResourcePoolTablePanel(Project project, Actions actions)
	{
		super(project, ObjectType.PROJECT_RESOURCE, 
				new ResourcePoolTable(new ResourcePoolTableModel(project)),
				(MainWindowAction)actions.get(ActionCreateResource.class),
				(ObjectsAction)actions.get(ActionDeleteResource.class));

		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
}
