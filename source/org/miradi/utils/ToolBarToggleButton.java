/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.actions.ActionToggleSpellChecker;
import org.miradi.actions.Actions;
import org.miradi.actions.MiradiAction;

public class ToolBarToggleButton extends MinimumSizeToggleButton
{
	public ToolBarToggleButton(Actions actions, Class<ActionToggleSpellChecker> actionClass)
	{
		this(actions.get(actionClass), "");
	}

	public ToolBarToggleButton(MiradiAction action, String buttonName)
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
	
}
