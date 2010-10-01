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

import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class PlanActionsAndMonitoringTab extends AbstractDashboardTab
{
	public PlanActionsAndMonitoringTab(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}

	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/2.html";
	}

	@Override
	protected TwoColumnPanel createLeftPanel()
	{
		TwoColumnPanel leftMainPanel = new TwoColumnPanel();
		
		createHeaderRow(leftMainPanel, EAM.text("2. Plan Actions and Monitoring"), "", getMainDescriptionFileName());
		
		return leftMainPanel;
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Plan Actions and Monitoring");
	}
}
