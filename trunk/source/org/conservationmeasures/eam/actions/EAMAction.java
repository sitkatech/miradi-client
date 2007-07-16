/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;

public abstract class EAMAction extends AbstractAction
{
	public EAMAction(String label, String icon)
	{
		this(label, new MiradiResourceImageIcon(icon));
	}
	
	public EAMAction(String label, Icon icon)
	{
		super(label, icon);
		if(label.length() == 0) 
			throw new RuntimeException("Actions must have a valid label");
			
	}

	public abstract void doAction() throws CommandFailedException;
	
	public boolean isObjectAction()
	{
		return false;
	}

	public Icon getDisabledIcon()
	{
		return null;
	}
	
	public Icon getIcon()
	{
		Object icon = getValue("icon");
		if(icon == null)
			icon = getValue("SmallIcon");
		return (Icon)icon;
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

	public static final String DEMO_INDICATOR = " - demo";
}
