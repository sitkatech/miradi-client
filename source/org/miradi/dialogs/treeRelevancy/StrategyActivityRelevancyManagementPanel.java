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

package org.miradi.dialogs.treeRelevancy;

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.ObjectCollectionPanel;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.strategicPlan.StrategicPlanMultiPropertiesPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Desire;

public class StrategyActivityRelevancyManagementPanel extends ObjectManagementPanel
{
	private StrategyActivityRelevancyManagementPanel(MainWindow mainWindowToUse, ObjectCollectionPanel tablePanelToUse, AbstractObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(mainWindowToUse, tablePanelToUse, propertiesPanelToUse);
	}
	
	public static StrategyActivityRelevancyManagementPanel createManagementPanel(MainWindow mainWindow, Desire desire) throws Exception
	{
		RootRelevancyTreeTableNode rootNode = new RootRelevancyTreeTableNode(mainWindow.getProject(), desire);
		StrategyActivityRelevancyTreeTableModel treeTableModel = new StrategyActivityRelevancyTreeTableModel(rootNode); 
		StrategyActivityRelevancyTreeTable treeTable = new StrategyActivityRelevancyTreeTable(mainWindow, treeTableModel);
		StrategyActivityRelevancyTreeTablePanel treeTablePanel = StrategyActivityRelevancyTreeTablePanel.createTreeTablePanel(mainWindow, desire, treeTableModel, treeTable);
		PlanningTreeMultiPropertiesPanel multiPropertiesPanel = new StrategicPlanMultiPropertiesPanel(mainWindow, ORef.INVALID);
		
		return new StrategyActivityRelevancyManagementPanel(mainWindow, treeTablePanel, multiPropertiesPanel);
	}

	@Override
	public String getPanelDescription()
	{
		return "StrategyActivityRelevancyManagementPanel";
	}
}
