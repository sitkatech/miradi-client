/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Box;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.martus.swing.UiButton;

public class ObjectTablePanelWithCreateAndDelete extends ObjectTablePanel
{
	public ObjectTablePanelWithCreateAndDelete(Project projectToUse, int objectTypeToUse, ObjectTable tableToUse, MainWindowAction createAction, ObjectsAction deleteAction)
	{
		super(projectToUse, objectTypeToUse, tableToUse);
		buttons = Box.createVerticalBox();
		add(buttons, BorderLayout.AFTER_LINE_ENDS);

		addButton(new UiButton(createAction));
		addButton(deleteAction);
	}
	
	public void addButton(ObjectsAction action)
	{
		addButton(new ObjectsActionButton(action, table));
	}
	
	public void addButton(UiButton button)
	{
		buttons.add(button);
	}
	
	Box buttons;
}
