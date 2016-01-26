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

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.MainWindow;

import javax.swing.event.TableModelEvent;

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
