/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.text.JTextComponent;

import org.bushe.swing.action.ActionList;
import org.miradi.actions.Actions;

public class TextAreaHtmlContextMenuListener extends TextAreaContextMenuListener
{
	public TextAreaHtmlContextMenuListener(JPopupMenu menuToUse, JTextComponent textFieldToUse, Actions actionsToUse, ActionList actionListToUse)
	{
		super(menuToUse, textFieldToUse, actionsToUse);
		
		actionList = actionListToUse;
	}
	
	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent ev)
	{
		super.popupMenuWillBecomeVisible(ev);
		
		getMenu().addSeparator();
		for (int index = 0; index < actionList.size(); ++index)
		{
			final Object rawObject = actionList.get(index);
			if (rawObject == null)
			{
				getMenu().addSeparator();
			}
			else
			{
				AbstractAction action = (AbstractAction) rawObject;
				getMenu().add(new JMenuItem(action));
			}
		}
	}
	
	private ActionList actionList;
}
