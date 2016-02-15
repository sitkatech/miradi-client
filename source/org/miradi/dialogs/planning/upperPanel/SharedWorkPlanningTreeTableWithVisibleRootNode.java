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
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.MainWindow;

import javax.swing.event.TableModelEvent;
import java.util.HashSet;
import java.util.Set;

public class SharedWorkPlanningTreeTableWithVisibleRootNode extends PlanningTreeTableWithVisibleRootNode
{
	public SharedWorkPlanningTreeTableWithVisibleRootNode(MainWindow mainWindowToUse, GenericTreeTableModel planningTreeModelToUse)
	{
		super(mainWindowToUse, planningTreeModelToUse);
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
		updateColumnHeader();
	}

	@Override
	protected Set<Class> getRelevantActions()
	{
		HashSet<Class> relevantActions = new HashSet<Class>();
		relevantActions.addAll(super.getRelevantActions());
		relevantActions.add(ActionTreeCreateResourceAssignment.class);
		relevantActions.add(ActionTreeCreateExpenseAssignment.class);
		relevantActions.add(ActionExpandToMenu.class);
		relevantActions.add(ActionExpandToStrategy.class);
		relevantActions.add(ActionExpandToTask.class);

		return relevantActions;
	}

	private void updateColumnHeader()
	{
		SharedWorkPlanTreeTableModel treeTableModel = (SharedWorkPlanTreeTableModel) getTreeTableModel();
		if (treeTableModel != null)
		{
			String columnName = treeTableModel.getColumnName(0);

			if (treeTableModel.treeHasSubTasks())
				columnName += SharedWorkPlanTreeTableModel.HAS_HIDDEN_SUB_TASKS_DOUBLE_ASTERISK;

			this.columnModel.getColumn(0).setHeaderValue(columnName);
		}
	}
}
