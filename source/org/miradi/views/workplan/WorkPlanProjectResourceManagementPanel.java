/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.workplan;

import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNodeAlwaysExpanded;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.treetables.BudgetCategoryTreeModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class WorkPlanProjectResourceManagementPanel extends WorkPlanBudgetCategoryManagementPanel
{
	public WorkPlanProjectResourceManagementPanel(MainWindow mainWindowToUse,
												  PlanningTreeTablePanel planningTreeTablePanelToUse,
												  PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel, WorkPlanManagementPanelConfiguration managementConfiguration)
			throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanelToUse, planningTreePropertiesPanel, managementConfiguration);
	}
	
	public static WorkPlanProjectResourceManagementPanel createManagementPanel(MainWindow mainWindowToUse, WorkPlanManagementPanelConfiguration managementConfiguration) throws Exception
	{
		PlanningTreeRootNodeAlwaysExpanded rootNode = new PlanningTreeRootNodeAlwaysExpanded(mainWindowToUse.getProject());
		WorkPlanCategoryTreeRowColumnProvider rowColumnProvider = managementConfiguration.getRowColumnProvider();
		BudgetCategoryTreeModel model = new BudgetCategoryTreeModel(mainWindowToUse.getProject(), rootNode, rowColumnProvider, managementConfiguration.getUniqueTreeTableIdentifier());
		PlanningTreeTablePanel treeTablePanel = WorkPlanProjectResourceTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse, model, rowColumnProvider, managementConfiguration.getButtonActions());
		PlanningTreeMultiPropertiesPanel propertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new WorkPlanProjectResourceManagementPanel(mainWindowToUse, treeTablePanel, propertiesPanel, managementConfiguration);
	}
}
