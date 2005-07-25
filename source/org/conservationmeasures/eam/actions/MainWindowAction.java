/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public abstract class MainWindowAction extends AbstractAction
{
	public MainWindowAction(MainWindow mainWindowToUse, String label)
	{
		this(mainWindowToUse, label, "icons/blankicon.png");
	}
	
	public MainWindowAction(MainWindow mainWindowToUse, String label, String icon)
	{
		super(label, new ImageIcon(icon));
		mainWindow = mainWindowToUse;
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			doAction(event);
		}
		catch (CommandFailedException e)
		{
			EAM.logException(e);
			getMainWindow().errorDialog(EAM.text("An internal error prevented this operation"));
		}
		
	}
	
	public abstract void doAction(ActionEvent event) throws CommandFailedException;
	
	MainWindow mainWindow;
}