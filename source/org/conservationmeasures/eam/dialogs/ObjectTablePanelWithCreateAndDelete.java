/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;

public class ObjectTablePanelWithCreateAndDelete extends ObjectTablePanel
{
	public ObjectTablePanelWithCreateAndDelete(Project projectToUse, int objectTypeToUse, ObjectTable tableToUse, MainWindowAction createAction, ObjectsAction deleteAction)
	{
		super(projectToUse, objectTypeToUse, tableToUse);

		addButton(new UiButton(createAction));
		addButton(deleteAction);
	}
	
}
