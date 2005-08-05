/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionOpenProject;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.MainWindowAction;

public class MainToolBar extends JToolBar
{
	public MainToolBar(MainWindow mainWindow)
	{
		setFloatable(false);
		addButtonForAction(new ActionNewProject(mainWindow));
		addButtonForAction(new ActionOpenProject(mainWindow));
		//addButtonForAction(new ActionStub("icons/print.gif"));
		addSeparator();
		addButtonForAction(new ActionUndo(mainWindow));
		addButtonForAction(new ActionRedo(mainWindow));
		addSeparator();
		addButtonForAction(new ActionCut(mainWindow));
		addButtonForAction(new ActionCopy(mainWindow));
		addButtonForAction(new ActionPaste(mainWindow));
		addButtonForAction(new ActionDelete(mainWindow));
	}
	
	void addButtonForAction(MainWindowAction action)
	{
		JButton button = new JButton(action);
		button.setText("");
		button.setToolTipText(action.getToolTipText());
		add(button);
	}

}

class ActionStub extends AbstractAction
{
	public ActionStub(String imagePath)
	{
		super(imagePath, new ImageIcon(imagePath));
	}

	public void actionPerformed(ActionEvent ae)
	{
		EAM.logWarning("ActionStub: " + getClass());
	}
}

