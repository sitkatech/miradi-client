/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Dimension;

import javax.swing.AbstractButton;
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
		setText(null);
		//FIXME adding text to the tool bar buttons.
		//comment out setText(null); above to see text 
		//setVerticalTextPosition(AbstractButton.BOTTOM);
	    //setHorizontalTextPosition(AbstractButton.CENTER);
		setToolTipText(action.getToolTipText());
		setName(buttonName);
	}

	public boolean hasLocation()
	{
		return false;
	}
	
	public Dimension getPreferredSize()
	{
		return getMinimumSize();
	}

	public Dimension getMaximumSize()
	{
		return getMinimumSize();
	}
	
	public Dimension getMinimumSize()
	{
		Dimension originalMinimumSize = super.getMinimumSize();
		int realMinimum = originalMinimumSize.height;
		return new Dimension(realMinimum, realMinimum);
	}
	
}