/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.views.Doer;

public abstract class EAMAction extends AbstractAction
{
	public EAMAction(String label, String icon)
	{
		this(label, new MiradiResourceImageIcon(icon));
	}
	
	public EAMAction(String label, Icon icon)
	{
		super(label, icon);
	}
	
	public abstract void doAction() throws CommandFailedException;
	public abstract Doer getDoer();
	
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

	public boolean isAvailableWithoutProject()
	{
		return false;
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
