/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.actions.ActionCreateExpense;
import org.miradi.actions.ActionDeleteExpense;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.MainWindow;
import org.miradi.views.umbrella.ObjectPicker;

public class ExpenseAssignmentEditorComponent extends AbstractAssignmentEditorComponent
{
	public ExpenseAssignmentEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse, objectPickerToUse);
	}
	
	@Override
	protected void createTables() throws Exception
	{
		abstractAssignmentSummaryTableModel = new ExpenseAssignmentMainTableModel(getProject());
		abstractSummaryTable = new ExpenseAssignmentDetailsMainTable(getMainWindow(), abstractAssignmentSummaryTableModel);
	}
	
	@Override
	protected void addButtons(OneRowPanel box)
	{
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionCreateExpense.class), getPicker()));
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionDeleteExpense.class), abstractSummaryTable));
	}
}
