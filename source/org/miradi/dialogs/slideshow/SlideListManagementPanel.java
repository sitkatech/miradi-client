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
package org.miradi.dialogs.slideshow;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.SlideShowIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class SlideListManagementPanel extends ObjectListManagementPanel
{
	public SlideListManagementPanel(MainWindow mainWindowToUse, ORef nodeRef, Actions actions) throws Exception
	{
		super(mainWindowToUse, new SlideListTablePanel(mainWindowToUse, nodeRef),
				new SlidePropertiesPanel(mainWindowToUse.getProject(), BaseId.INVALID));
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new SlideShowIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Slide Show"); 
}

