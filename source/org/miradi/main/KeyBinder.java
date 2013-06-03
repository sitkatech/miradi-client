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
package org.miradi.main;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyBinder
{
	public static final int KEY_MODIFIER_NONE = 0;
	public static final int KEY_MODIFIER_CTRL = KeyEvent.CTRL_DOWN_MASK;
	
	public static void bindKey(JComponent component, int key, int keyModifier, Action contextMenuAction)
	{
		String thisName = (String)contextMenuAction.getValue(Action.NAME);
		component.getActionMap().put(thisName, contextMenuAction);
		component.getInputMap().put(KeyStroke.getKeyStroke(key, keyModifier), thisName);
	}

}
