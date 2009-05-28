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

package org.miradi.dialogs.planning.propertiesPanel;

import java.util.Vector;

import javax.swing.Action;

import org.miradi.actions.ActionCreateExpense;
import org.miradi.actions.ActionDeleteExpense;
import org.miradi.main.MainWindow;

public class AssignmentExpensesTable extends AssignmentDateUnitsTable
{
	public AssignmentExpensesTable(MainWindow mainWindowToUse, AssignmentDateUnitsTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, modelToUse);
	}

	@Override
	public Vector<Action> getActionsForRightClickMenu(int tableColumn)
	{
		Vector<Action> actions = new Vector<Action>();
		actions.add(getActions().get(ActionCreateExpense.class));
		actions.add(getActions().get(ActionDeleteExpense.class));
		actions.add(null);
		actions.addAll(super.getActionsForRightClickMenu(tableColumn));
		return actions;
	}
}
