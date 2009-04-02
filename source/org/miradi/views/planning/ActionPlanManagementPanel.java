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
/**
 * 
 */
package org.miradi.views.planning;

import javax.swing.Icon;

import org.miradi.actions.ActionCollapseAllNodes;
import org.miradi.actions.ActionExpandAllNodes;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.StrategicPlanTreeTableModel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

class ActionPlanManagementPanel extends PlanningTreeManagementPanel
{
	public ActionPlanManagementPanel(MainWindow mainWindowToUse,
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
	
	@Override
	public Icon getIcon()
	{
		return IconManager.getStrategyIcon();
	}

	public static ActionPlanManagementPanel createStrategicPlanPanel(MainWindow mainWindowToUse)
			throws Exception
	{
		PlanningTreeTableModel strategicPlanTreeTableModel = new StrategicPlanTreeTableModel(mainWindowToUse.getProject());
		Class[] buttonActions = new Class[] {
			ActionExpandAllNodes.class, 
			ActionCollapseAllNodes.class, 
			ActionPlanningCreationMenu.class,
			};
		PlanningTreeTablePanel strategicPlanTreeTablePanel = PlanningTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse, strategicPlanTreeTableModel, buttonActions);
		PlanningTreeTable treeAsObjectPicker = (PlanningTreeTable)strategicPlanTreeTablePanel.getTree();
		PlanningTreeMultiPropertiesPanel strategicPlanPropertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID, treeAsObjectPicker);
		return new ActionPlanManagementPanel(mainWindowToUse, strategicPlanTreeTablePanel, strategicPlanPropertiesPanel);
	}

	private final String panelDescription = EAM.text("Tab|Action Plan");
}