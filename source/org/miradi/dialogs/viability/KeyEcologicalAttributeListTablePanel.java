/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.viability;

import org.miradi.actions.ActionCreateKeyEcologicalAttribute;
import org.miradi.actions.ActionDeleteKeyEcologicalAttribute;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.ids.FactorId;
import org.miradi.main.MainWindow;

public class KeyEcologicalAttributeListTablePanel extends ObjectListTablePanel
{
	public KeyEcologicalAttributeListTablePanel(MainWindow mainWindowToUse, FactorId nodeId)
	{
		super(mainWindowToUse, new KeyEcologicalAttributeListTableModel(mainWindowToUse.getProject(), nodeId), 
				buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateKeyEcologicalAttribute.class,
		ActionDeleteKeyEcologicalAttribute.class
	};

}
