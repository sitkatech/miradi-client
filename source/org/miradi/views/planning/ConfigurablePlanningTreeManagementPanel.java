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
package org.miradi.views.planning;

import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

class ConfigurablePlanningTreeManagementPanel extends PlanningTreeManagementPanel
{
	public ConfigurablePlanningTreeManagementPanel(
			MainWindow mainWindowToUse,
			PlanningTreeTablePanel planningTreeTablePanel,
			PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)
			throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
	}
	
	@Override
	public String getPanelDescription()
	{
		return panelDescription;
	}
	
	public static PlanningTreeManagementPanel createConfigurablePlanningPanel(MainWindow mainWindowToUse) throws Exception
	{
		PlanningTreeTablePanel planningTreeTablePanel = PlanningTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse);
		PlanningTreeTable treeAsObjectPicker = (PlanningTreeTable)planningTreeTablePanel.getTree();
		PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID, treeAsObjectPicker);
		return new ConfigurablePlanningTreeManagementPanel(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
	}

	private final String panelDescription = EAM.text("Tab|Custom");
}