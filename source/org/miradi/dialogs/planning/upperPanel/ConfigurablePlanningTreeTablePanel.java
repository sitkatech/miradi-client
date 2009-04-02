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

import org.miradi.actions.ActionCollapseAllNodes;
import org.miradi.actions.ActionCreatePlanningViewConfiguration;
import org.miradi.actions.ActionDeletePlanningViewConfiguration;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionExpandAllNodes;
import org.miradi.actions.ActionPlanningColumnsEditor;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.actions.ActionPlanningRowsEditor;
import org.miradi.actions.ActionRenamePlanningViewConfiguration;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.dialogs.planning.PlanningViewConfigurableControlPanel;
import org.miradi.main.MainWindow;

public class ConfigurablePlanningTreeTablePanel extends PlanningTreeTablePanel
{
	protected ConfigurablePlanningTreeTablePanel(MainWindow mainWindowToUse,
			PlanningTreeTable treeToUse, 
			PlanningTreeTableModel modelToUse, Class[] buttonActions) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions);

		customizationPanel = new PlanningViewConfigurableControlPanel(getProject());
		getButtonBox().add(customizationPanel);
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{
		return createPlanningTreeTablePanel(mainWindowToUse, getButtonActions());
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, Class[] buttonActions) throws Exception
	{
		PlanningTreeTableModel model = new ConfigurablePlanningTreeTableModel(mainWindowToUse.getProject());
		return createPlanningTreeTablePanel(mainWindowToUse, model, buttonActions);
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model, Class[] buttonActions) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);	
		
		return new ConfigurablePlanningTreeTablePanel(mainWindowToUse, treeTable, model, buttonActions);
	}
	
	private static Class[] getButtonActions()
	{
		return new Class[] {
			ActionExpandAllNodes.class,
			ActionCollapseAllNodes.class,
			ActionTreeNodeUp.class,
			ActionTreeNodeDown.class,
			ActionPlanningCreationMenu.class,
			ActionDeletePlanningViewTreeNode.class,
			
			ActionCreatePlanningViewConfiguration.class,
			ActionRenamePlanningViewConfiguration.class,
			ActionDeletePlanningViewConfiguration.class,
			ActionPlanningRowsEditor.class,
			ActionPlanningColumnsEditor.class, 
		};
	}
	
	@Override
	public void dispose()
	{
		super.dispose();

		customizationPanel.dispose();
	}

	private PlanningViewConfigurableControlPanel customizationPanel; 
}
