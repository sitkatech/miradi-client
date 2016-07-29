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

package org.miradi.dialogs.planning.upperPanel;

import org.miradi.actions.*;
import org.miradi.dialogs.tablerenderers.BasicTableCellEditorOrRendererFactory;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.UmbrellaView;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class WorkPlanUpperMultiTable extends PlanningUpperMultiTable
{
	public WorkPlanUpperMultiTable(MainWindow mainWindowToUse, PlanningTreeTable masterTreeToUse, PlanningTreeMultiTableModel model)
	{
		super(mainWindowToUse, masterTreeToUse, model);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		final int modelColumn = convertColumnIndexToModel(tableColumn);

		BasicTableCellEditorOrRendererFactory factory = (BasicTableCellEditorOrRendererFactory) super.getCellRenderer(row, tableColumn);

		Color background = getCastedModel().getCellBackgroundColor(row, modelColumn);

		int selectedRow = getSelectedRow();
		if (row == selectedRow)
			background = isCellEditable(selectedRow, tableColumn) ? Color.WHITE : EAM.READONLY_BACKGROUND_COLOR;

		factory.setCellBackgroundColor(background);

		return factory;
	}

	@Override
	protected ArrayList<Action> getActionsForBaseObject(BaseObject baseObject, Actions availableActions)
	{
		return getWorkPlanActionsForBaseObject(baseObject, availableActions);
	}

	@Override
	protected ArrayList<Action> getActionsForAssignments()
	{
		UmbrellaView currentView = getMainWindow().getCurrentView();
		if(currentView == null)
			return new ArrayList<Action>();

		return getActionsForAssignments(currentView);
	}

	public static ArrayList<Action> getActionsForAssignments(UmbrellaView currentView)
	{
		ArrayList<Action> actions = new ArrayList<Action>();

		if(currentView == null)
			return actions;

		currentView.addActionToListIfDoerAvailable(actions, ActionShowAllResourceAssignmentRows.class);
		currentView.addActionToListIfDoerAvailable(actions, ActionHideAllResourceAssignmentRows.class);
		currentView.addActionToListIfDoerAvailable(actions, ActionShowAllExpenseAssignmentRows.class);
		currentView.addActionToListIfDoerAvailable(actions, ActionHideAllExpenseAssignmentRows.class);

		actions.add(null);

		return actions;
	}

	public static ArrayList<Action> getWorkPlanActionsForBaseObject(BaseObject baseObject, Actions availableActions)
	{
		ArrayList<Action> actions = new ArrayList<Action>();

		if (Strategy.is(baseObject))
		{
			actions.add(availableActions.get(ActionTreeCreateActivity.class));
			actions.add(availableActions.get(ActionTreeCreateMonitoringActivity.class));
			actions.add(availableActions.get(ActionTreeCreateResourceAssignment.class));
			actions.add(availableActions.get(ActionTreeCreateExpenseAssignment.class));
			actions.add(null);
		}

		if (Task.is(baseObject))
		{
			if (((Task) baseObject).isActivity())
			{
				actions.add(availableActions.get(ActionTreeCreateActivity.class));
				actions.add(availableActions.get(ActionTreeCreateMonitoringActivity.class));
				actions.add(availableActions.get(ActionCreateTask.class));
			}
			else
			{
				actions.add(availableActions.get(ActionCreateSameLevelTask.class));
			}
			actions.add(availableActions.get(ActionTreeCreateResourceAssignment.class));
			actions.add(availableActions.get(ActionTreeCreateExpenseAssignment.class));
			actions.add(null);
		}

		return actions;
	}
}
