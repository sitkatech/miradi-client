/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class EAMAction extends AbstractAction
{
	public EAMAction(String label, String icon)
	{
		this(label, new ImageIcon(icon));
	}
	
	public EAMAction(String label, Icon icon)
	{
		super(label, icon);
		if(label == "") 
			throw new RuntimeException("Actions must have a valid label");
			
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

}
