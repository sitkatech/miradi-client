/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.JButton;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;

public class ToolBarButton extends JButton implements LocationHolder
{
	public ToolBarButton(Actions actions, Class actionClass)
	{
		this(actions, actionClass, "");
	}

	public ToolBarButton(Actions actions, Class actionClass, String buttonName)
	{
		this(actions.get(actionClass), buttonName);
	}


	public ToolBarButton(EAMAction action, String buttonName)
	{
		super(action);
		setText("");
		setToolTipText(action.getToolTipText());
		setName(buttonName);
	}

	public boolean hasLocation()
	{
		return false;
	}
	
}