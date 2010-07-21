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
package org.miradi.dialogs.planning.upperPanel;


import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.MainWindow;

public class ExportablePlanningTreeTablePanel extends PlanningTreeTablePanel
{
	protected ExportablePlanningTreeTablePanel(MainWindow mainWindowToUse,
											   PlanningTreeTable treeToUse, 
											   GenericTreeTableModel modelToUse,
											   Class[] buttonActions,
											   RowColumnProvider rowColumnProvider) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProvider);
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanelWithoutButtons(MainWindow mainWindowToUse, RowColumnProvider rowColumnProvider, String uniqueTreeTableModelIdentifier) throws Exception
	{
		PlanningTreeTableModel model = new ExportablePlanningTreeTableModel(mainWindowToUse.getProject(), rowColumnProvider, uniqueTreeTableModelIdentifier);
		return createPlanningTreeTablePanelWithoutButtonsForExporting(mainWindowToUse, rowColumnProvider, model);
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanelWithoutButtonsForExporting(MainWindow mainWindowToUse, RowColumnProvider rowColumnProvider, GenericTreeTableModel model) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);
		ExportablePlanningTreeTablePanel exportablePlanningTreeTablePanel = new ExportablePlanningTreeTablePanel(mainWindowToUse, treeTable, model, new Class[0], rowColumnProvider);
		exportablePlanningTreeTablePanel.rebuildEntireTreeTable();

		return exportablePlanningTreeTablePanel;
	}
}
