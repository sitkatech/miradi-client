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

package org.miradi.views.workplan;

import org.miradi.dialogs.RollupReportsRowColumnProvider;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.treetables.RollupReportsTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class RollupReportsManagementPanel extends PlanningTreeManagementPanel
{
	public RollupReportsManagementPanel(MainWindow mainWindowToUse,
			PlanningTreeTablePanel planningTreeTablePanelToUse,
			PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)
			throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanelToUse, planningTreePropertiesPanel);
	}
	
	@Override
	protected PlanningTreeTablePanel createPlanningTreeTablePanel(String uniqueTreeTableModelIdentifier, RowColumnProvider rowColumnProvider) throws Exception
	{
		PlanningTreeTableModel model = RollupReportsTreeTableModel.createRollupReportsTreeTableModel(getProject(), rowColumnProvider, UNIQUE_TREE_TABLE_IDENTIFIER);
		return ExportablePlanningTreeTablePanel.createPlanningTreeTablePanelWithoutButtonsForExporting(getMainWindow(), rowColumnProvider, model);
	}

	public static RollupReportsManagementPanel createRollUpReportsPanel(MainWindow mainWindowToUse) throws Exception
	{
		RowColumnProvider rowColumnProvider = new RollupReportsRowColumnProvider();
		PlanningTreeTableModel treeTableModel = RollupReportsTreeTableModel.createRollupReportsTreeTableModel(mainWindowToUse.getProject(), rowColumnProvider, UNIQUE_TREE_TABLE_IDENTIFIER);
		PlanningTreeTablePanel treeTablePanel = RollupReportsTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse, treeTableModel, rowColumnProvider);
		PlanningTreeMultiPropertiesPanel propertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new RollupReportsManagementPanel(mainWindowToUse, treeTablePanel, propertiesPanel);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Rollup Reports");
	}
	
	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "RollupReportsTreeTableModel";
}
