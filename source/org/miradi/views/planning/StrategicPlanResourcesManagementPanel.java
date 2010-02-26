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
import org.miradi.dialogs.planning.ProjectResourceCoreRowColumnProvider;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.ProjectResourceTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.ProjectResourceTreeTablePanel;
import org.miradi.dialogs.strategicPlan.StrategicPlanMultiPropertiesPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class StrategicPlanResourcesManagementPanel extends ProjectResourceManagementPanel
{
	public StrategicPlanResourcesManagementPanel(MainWindow mainWindowToUse,
			PlanningTreeTablePanel planningTreeTablePanel,
			PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)
			throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
	}

	protected PlanningTreeTablePanel createPlanningTreeTablePanel(String uniqueTreeTableModelIdentifier, RowColumnProvider rowColumnProvider) throws Exception
	{
		PlanningTreeTableModel model = ProjectResourceTreeTableModel.createProjectResourceTreeTableModel(getProject(), rowColumnProvider.getColumnListToShow());
		return ExportablePlanningTreeTablePanel.createPlanningTreeTablePanelWithoutButtons(getMainWindow(), rowColumnProvider, model);
	}
	
	public static PlanningTreeManagementPanel createProjectResourcesPanelWithoutBudgetColumns(MainWindow mainWindowToUse) throws Exception
	{
		RowColumnProvider rowColumnProvider = new ProjectResourceCoreRowColumnProvider();
		PlanningTreeTableModel treeTableModel = ProjectResourceTreeTableModel.createProjectResourceTreeTableModel(mainWindowToUse.getProject(), rowColumnProvider.getColumnListToShow());
		PlanningTreeTablePanel treeTablePanel = ProjectResourceTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse, treeTableModel, rowColumnProvider);
		PlanningTreeMultiPropertiesPanel propertiesPanel = new StrategicPlanMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new StrategicPlanResourcesManagementPanel(mainWindowToUse, treeTablePanel, propertiesPanel);
	}	
}
