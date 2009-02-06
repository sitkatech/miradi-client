/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
