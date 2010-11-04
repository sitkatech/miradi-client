/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ImplementActionsAndMonitoringDashboardTab extends LeftSideRightSideSplitterContainerTab
{
	private ImplementActionsAndMonitoringDashboardTab(MainWindow mainWindowToUse, LeftSidePanelWithSelectableRows leftPanelToUse) throws Exception
	{
		super(mainWindowToUse, leftPanelToUse);
	}
	
	public static ImplementActionsAndMonitoringDashboardTab createTab(MainWindow mainWindowToUse) throws Exception
	{
		return new ImplementActionsAndMonitoringDashboardTab(mainWindowToUse, createLeftPanel(mainWindowToUse));
	}

	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/3.html";
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Implement Actions and Monitoring");
	}

	private static LeftSidePanelWithSelectableRows createLeftPanel(MainWindow mainWindowToUse)
	{
		return new ImplementActionsAndMonitoringDashboardLeftPanel(mainWindowToUse);
	}
}
