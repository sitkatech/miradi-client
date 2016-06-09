/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.planning;

import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.ConfigurablePlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

import javax.swing.*;

public class ConfigurablePlanningManagementPanel extends PlanningTreeManagementPanel
{
	public ConfigurablePlanningManagementPanel(MainWindow mainWindowToUse,
			PlanningTreeTablePanel planningTreeTablePanelToUse,
			PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)
			throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanelToUse, planningTreePropertiesPanel);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Tab|Custom");
	}

	@Override
	public Icon getIcon()
	{
		return IconManager.getPlanningIcon();
	}

	public static ConfigurablePlanningManagementPanel createActionPlanPanel(MainWindow mainWindowToUse) throws Exception
	{
		PlanningTreeTablePanel actionPlanTreeTablePanel = ConfigurablePlanningTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse);
		PlanningTreeMultiPropertiesPanel actionPlanPropertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new ConfigurablePlanningManagementPanel(mainWindowToUse, actionPlanTreeTablePanel, actionPlanPropertiesPanel);
	}
}
