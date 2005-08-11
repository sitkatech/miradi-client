/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
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
		this(mainWindowToUse, label, new ImageIcon(icon));
	}
	
	public MainWindowAction(MainWindow mainWindowToUse, String label, Icon icon)
	{
		super(label, icon);
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
			EAM.errorDialog(EAM.text("An internal error prevented this operation"));
		}
		
	}
	
	public ImageIcon getIcon()
	{
		return (ImageIcon)getValue("icon");
	}
	
	public abstract void doAction(ActionEvent event) throws CommandFailedException;
	public String getToolTipText()
	{
		return "";
	}
	
	public boolean shouldBeEnabled()
	{
		return false;
	}
	
	public void updateEnabledState()
	{
		setEnabled(shouldBeEnabled());
	}
	
	MainWindow mainWindow;
}