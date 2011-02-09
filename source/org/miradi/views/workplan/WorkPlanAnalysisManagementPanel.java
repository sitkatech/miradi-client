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

package org.miradi.views.workplan;

import javax.swing.Icon;

import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.treenodes.NewPlanningRootNode;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.treetables.NewBudgetCategoryTreeModel;
import org.miradi.dialogs.treetables.WorkPlanAnalysisTreeTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningTreeRowColumnProvider;

public class WorkPlanAnalysisManagementPanel extends PlanningTreeManagementPanel
{
	public WorkPlanAnalysisManagementPanel(MainWindow mainWindowToUse,
			PlanningTreeTablePanel planningTreeTablePanelToUse,
			PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel, AnalysisManagementConfiguration analysisManagementConfiguration)
			throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanelToUse, planningTreePropertiesPanel, analysisManagementConfiguration.getUniqueTreeTableIdentifier());
		
		mangementConfiguration = analysisManagementConfiguration;

	}
	
	public static WorkPlanAnalysisManagementPanel createManagementPanel(MainWindow mainWindowToUse, AnalysisManagementConfiguration analysisManagementConfiguration) throws Exception
	{
		WorkPlanCategoryTreeRowColumnProvider rowColumnProvider = analysisManagementConfiguration.getRowColumnProvider();
		WorkPlanAnalysisTreeTableModel model = WorkPlanAnalysisTreeTableModel.createCategoryTreeTableModel(mainWindowToUse.getProject(), rowColumnProvider, analysisManagementConfiguration.getUniqueTreeTableIdentifier());
		PlanningTreeTablePanel treeTablePanel = WorkPlanCategoryTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse, model, rowColumnProvider, analysisManagementConfiguration.getButtonActions());
		PlanningTreeMultiPropertiesPanel propertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new WorkPlanAnalysisManagementPanel(mainWindowToUse, treeTablePanel, propertiesPanel, analysisManagementConfiguration);
	}

	@Override
	protected PlanningTreeTablePanel createPlanningTreeTablePanel(String uniqueTreeTableModelIdentifier, PlanningTreeRowColumnProvider rowColumnProvider) throws Exception
	{
		WorkPlanAnalysisTreeTableModel model = WorkPlanAnalysisTreeTableModel.createCategoryTreeTableModel(getProject(), mangementConfiguration.getRowColumnProvider(), getMangementConfiguration().getUniqueTreeTableIdentifier());
		return ExportablePlanningTreeTablePanel.createPlanningTreeTablePanelWithoutButtonsForExporting(getMainWindow(), rowColumnProvider, model);
	}
	
	public static WorkPlanCategoryManagementPanel createManagementPanel(MainWindow mainWindowToUse, AbstractManagementConfiguration managementConfiguration) throws Exception
	{
		NewPlanningRootNode rootNode = new NewPlanningRootNode(mainWindowToUse.getProject());
		WorkPlanCategoryTreeRowColumnProvider rowColumnProvider = managementConfiguration.getRowColumnProvider();
		NewBudgetCategoryTreeModel model = new NewBudgetCategoryTreeModel(mainWindowToUse.getProject(), rootNode, rowColumnProvider, managementConfiguration.getUniqueTreeTableIdentifier());
		PlanningTreeTablePanel treeTablePanel = WorkPlanCategoryTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse, model, rowColumnProvider, managementConfiguration.getButtonActions());
		PlanningTreeMultiPropertiesPanel propertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new WorkPlanCategoryManagementPanel(mainWindowToUse, treeTablePanel, propertiesPanel, managementConfiguration);
	}

	@Override
	public String getPanelDescription()
	{
		return getMangementConfiguration().getPanelDescription();
	}

	private AbstractManagementConfiguration getMangementConfiguration()
	{
		return mangementConfiguration;
	}
	
	@Override
	public Icon getIcon()
	{
		return getMangementConfiguration().getIcon();
	}

	private AbstractManagementConfiguration mangementConfiguration;
}
