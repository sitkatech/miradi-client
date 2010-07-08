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

package org.miradi.dialogs.planning.upperPanel;

import org.miradi.actions.ActionCreateCategoryOne;
import org.miradi.actions.ActionDeleteCategoryOne;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objects.BudgetCategoryOne;

public class CategoryOneTreeTablePanel extends PlanningTreeTablePanel
{
	protected CategoryOneTreeTablePanel(MainWindow mainWindowToUse,
			PlanningTreeTable treeToUse, 
			PlanningTreeTableModel modelToUse,
			Class[] buttonClasses,
			RowColumnProvider rowColumnProvider
	) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonClasses, rowColumnProvider);

	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model, RowColumnProvider rowColumnProvider) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);

		return new CategoryOneTreeTablePanel(mainWindowToUse, treeTable, model, getButtonActions(), rowColumnProvider);
	}
	
	@Override
	protected boolean doesCommandForceRebuild(CommandExecutedEvent event)
	{
		if (super.doesCommandForceRebuild(event))
			return true;
			
		return wasTypeCreatedOrDeleted(event, BudgetCategoryOne.getObjectType());
	}
	
	private static Class[] getButtonActions()
	{
		return new Class[] {
				ActionCreateCategoryOne.class, 
				ActionDeleteCategoryOne.class,
		};
	}
}
