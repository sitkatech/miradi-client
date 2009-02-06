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
package org.miradi.utils;

import java.awt.Dimension;

import javax.swing.JButton;

import org.miradi.actions.Actions;
import org.miradi.actions.EAMAction;

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
		//TODO adding text to the tool bar buttons.
		//comment out setText(null); above to see text 
		//setVerticalTextPosition(AbstractButton.BOTTOM);
	    //setHorizontalTextPosition(AbstractButton.CENTER);
		setToolTipText(action.getToolTipText());
		setName(buttonName);
		setFocusable(false);
		setOpaque(false);
		if (getDisabledIcon()==null)
			setDisabledIcon(action.getDisabledIcon());
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