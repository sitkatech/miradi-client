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

package org.miradi.dialogs.planning.upperPanel;

import org.miradi.actions.ActionCollapseAllRows;
import org.miradi.actions.ActionCreateCustomFromCurrentTreeTableConfiguration;
import org.miradi.actions.ActionExpandAllRows;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.StrategicRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.PlanningRootNode;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.MainWindow;
import org.miradi.views.planning.RowManager;


public class NewActionPlanTreeTablePanel extends PlanningTreeTablePanel
{
	protected NewActionPlanTreeTablePanel(MainWindow mainWindowToUse,
			PlanningTreeTable treeToUse, 
			PlanningTreeTableModel modelToUse,
			Class[] buttonActions, 
			RowColumnProvider rowColumnProvider) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProvider);
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{
		TreeTableNode rootNode = new PlanningRootNode(mainWindowToUse.getProject(), RowManager.getStrategicPlanRows());
		PlanningTreeTableModel model = new NewActionPlanTreeTableModel(mainWindowToUse.getProject(), rootNode);
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);
		StrategicRowColumnProvider rowColumnProvider = new StrategicRowColumnProvider();

		return new ActionPlanTreeTablePanel(mainWindowToUse, treeTable, model, getButtonActions(), rowColumnProvider);
	}

	private static Class[] getButtonActions()
	{
		return new Class[] { ActionExpandAllRows.class,
				ActionCollapseAllRows.class, 
				ActionPlanningCreationMenu.class,
				ActionCreateCustomFromCurrentTreeTableConfiguration.class, 
				};
	}

}
