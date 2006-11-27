/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.project.Project;

abstract public class ObjectListTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public ObjectListTablePanel(Project projectToUse, int objectTypeToUse, ObjectTableModel model, MainWindowAction createAction, ObjectsAction deleteAction)
	{
		super(projectToUse, objectTypeToUse, new ObjectListTable(model), createAction, deleteAction);
	}

	public void dispose()
	{
		super.dispose();
	}


}
