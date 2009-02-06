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
package org.miradi.dialogs.base;

import org.miradi.actions.Actions;
import org.miradi.actions.EAMAction;
import org.miradi.actions.ObjectsAction;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.main.MainWindow;

public class ObjectTablePanelWithCreateAndDelete extends ObjectTablePanel
{
	public ObjectTablePanelWithCreateAndDelete(MainWindow mainWindowToUse, ObjectTable tableToUse)
	{
		super(mainWindowToUse, tableToUse);
	}
	
	public ObjectTablePanelWithCreateAndDelete(MainWindow mainWindowToUse, ObjectTable tableToUse, Actions actions, Class[] buttonActionClasses)
	{
		this(mainWindowToUse, tableToUse);

		for (int i=0; i<buttonActionClasses.length; ++i)
		{
			EAMAction action = actions.get(buttonActionClasses[i]);
			if (action.isObjectAction())
				addButton((ObjectsAction) action);
			else
				addButton(new PanelButton(action));
		}
	}
	
}
