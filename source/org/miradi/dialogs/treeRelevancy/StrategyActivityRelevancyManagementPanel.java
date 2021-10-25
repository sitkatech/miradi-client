/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.treeRelevancy;

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.ObjectCollectionPanel;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;

public class StrategyActivityRelevancyManagementPanel extends ObjectManagementPanel
{
	private StrategyActivityRelevancyManagementPanel(MainWindow mainWindowToUse, ObjectCollectionPanel tablePanelToUse, AbstractObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(mainWindowToUse, tablePanelToUse, propertiesPanelToUse);
	}
	
	public static StrategyActivityRelevancyManagementPanel createManagementPanel(MainWindow mainWindow, BaseObject baseObject) throws Exception
	{
		RootRelevancyTreeTableNode rootNode = new RootRelevancyTreeTableNode(mainWindow.getProject(), baseObject);
		StrategyActivityRelevancyTreeTableModel treeTableModel = new StrategyActivityRelevancyTreeTableModel(rootNode); 
		StrategyActivityRelevancyTreeTable treeTable = new StrategyActivityRelevancyTreeTable(mainWindow, treeTableModel);
		StrategyActivityRelevancyTreeTablePanel treeTablePanel = StrategyActivityRelevancyTreeTablePanel.createTreeTablePanel(mainWindow, baseObject, treeTableModel, treeTable);
		PlanningTreeMultiPropertiesPanel multiPropertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindow, ORef.INVALID);
		
		return new StrategyActivityRelevancyManagementPanel(mainWindow, treeTablePanel, multiPropertiesPanel);
	}

	@Override
	public String getPanelDescription()
	{
		return "StrategyActivityRelevancyManagementPanel";
	}
}
