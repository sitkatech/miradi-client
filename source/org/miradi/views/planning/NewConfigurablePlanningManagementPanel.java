/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.Icon;

import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.NewActionPlanTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.strategicPlan.ActionPlanMultiPropertiesPanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class NewConfigurablePlanningManagementPanel extends PlanningTreeManagementPanel
{
	public NewConfigurablePlanningManagementPanel(MainWindow mainWindowToUse,
			PlanningTreeTablePanel planningTreeTablePanelToUse,
			PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)
			throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanelToUse, planningTreePropertiesPanel);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Tab|Custom 2");
	}

	@Override
	public Icon getIcon()
	{
		return IconManager.getNeedsAttentionIcon();
	}

	public static NewConfigurablePlanningManagementPanel createActionPlanPanel(MainWindow mainWindowToUse) throws Exception
	{
		PlanningTreeTablePanel actionPlanTreeTablePanel = NewActionPlanTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse);
		PlanningTreeMultiPropertiesPanel actionPlanPropertiesPanel = new ActionPlanMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new NewConfigurablePlanningManagementPanel(mainWindowToUse, actionPlanTreeTablePanel, actionPlanPropertiesPanel);
	}
}
