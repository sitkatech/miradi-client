/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionOpenProject;
import org.conservationmeasures.eam.actions.ActionPaste;

public class MainToolBar extends JToolBar
{
	public MainToolBar(MainWindow mainWindow)
	{
		setFloatable(false);
		add(new ActionNewProject(mainWindow));
		add(new ActionOpenProject(mainWindow));
		add(new ActionStub("icons/print.gif"));
		add(new Separator());
		add(new ActionCut(mainWindow));
		add(new ActionCopy(mainWindow));
		add(new ActionPaste(mainWindow));
		add(new ActionDelete(mainWindow));
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

