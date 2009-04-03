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
import org.miradi.actions.ActionExpandAllNodes;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.main.MainWindow;
import org.miradi.utils.CodeList;

public class WorkPlanTreeTablePanel extends PlanningTreeTablePanel
{
	protected WorkPlanTreeTablePanel(MainWindow mainWindowToUse,
									 PlanningTreeTable treeToUse,
									 PlanningTreeTableModel modelToUse,
									 Class[] buttonActions,
									 RowColumnProvider rowColumnProvider) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProvider);
	}

	protected CodeList getColumnsToShow() throws Exception
	{
		return new WorkPlanRowColumnProvider().getColumnListToShow();
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{
		PlanningTreeTableModel model = new WorkPlanTreeTableModel(mainWindowToUse.getProject());
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);
		WorkPlanRowColumnProvider rowColumnProvider = new WorkPlanRowColumnProvider();

		return new WorkPlanTreeTablePanel(mainWindowToUse, treeTable, model, getButtonActions(), rowColumnProvider);
	}

	private static Class[] getButtonActions()
	{
		return new Class[] {
				ActionExpandAllNodes.class, 
				ActionCollapseAllNodes.class, 
				ActionPlanningCreationMenu.class,			
		};
	}
}
