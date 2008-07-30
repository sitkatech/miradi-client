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

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class KeyEcologicalAttributeListManagementPanel extends ObjectListManagementPanel
{
	public KeyEcologicalAttributeListManagementPanel(MainWindow mainWindowToUse, FactorId nodeId, Actions actions) throws Exception
	{
		super(mainWindowToUse, new KeyEcologicalAttributeListTablePanel(mainWindowToUse.getProject(), actions, nodeId),
				new KeyEcologicalAttributePropertiesPanel(mainWindowToUse.getProject(), actions));
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION; 
	}
	
	public Icon getIcon()
	{
		return new KeyEcologicalAttributeIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|KEA"); 
}

