/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.JButton;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;

public class ToolBarButton extends JButton
{
	public ToolBarButton(Actions actions, Class actionClass)
	{
		this(actions.get(actionClass));
	}

	public ToolBarButton(EAMAction action)
	{
		super(action);
		setText("");
		setToolTipText(action.getToolTipText());
	}
	
}