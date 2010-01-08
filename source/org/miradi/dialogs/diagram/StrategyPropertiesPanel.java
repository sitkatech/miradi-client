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
package org.miradi.dialogs.diagram;


import org.miradi.dialogs.assignment.AssignmentsPropertiesPanel;
import org.miradi.dialogs.expense.ExpensesPropertiesPanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.ObjectPicker;

public class StrategyPropertiesPanel extends AbstractStrategyPropertiesPanel
{
	public StrategyPropertiesPanel(MainWindow mainWindow, ObjectPicker picker) throws Exception
	{
		super(mainWindow.getProject(), picker);
	}

	@Override
	protected void addBudgetSubPanels(ObjectPicker picker) throws Exception
	{
		addSubPanelWithTitledBorder(new AssignmentsPropertiesPanel(getMainWindow(), Task.getObjectType(), picker));
		addSubPanelWithTitledBorder(new ExpensesPropertiesPanel(getMainWindow(), Task.getObjectType(), picker));
	}
}
