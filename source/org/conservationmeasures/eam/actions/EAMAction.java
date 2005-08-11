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

public abstract class EAMAction extends AbstractAction
{
	public EAMAction(String label)
	{
		this(label, "icons/blankicon.png");
	}
	
	public EAMAction(String label, String icon)
	{
		this(label, new ImageIcon(icon));
	}
	
	public EAMAction(String label, Icon icon)
	{
		super(label, icon);
	}

	public ImageIcon getIcon()
	{
		return (ImageIcon)getValue("icon");
	}

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

	public abstract void doAction(ActionEvent event) throws CommandFailedException;

}
