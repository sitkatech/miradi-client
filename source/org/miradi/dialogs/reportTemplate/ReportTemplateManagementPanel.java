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
package org.miradi.dialogs.reportTemplate;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectPoolManagementPanel;
import org.miradi.icons.ReportTemplateIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ReportTemplateManagementPanel extends ObjectPoolManagementPanel
{
	public ReportTemplateManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, new ReportTemplatePoolTablePanel(mainWindowToUse), new ReportTemplatePropertiesPanel(mainWindowToUse.getProject()));
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new ReportTemplateIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Report Templates"); 
}
