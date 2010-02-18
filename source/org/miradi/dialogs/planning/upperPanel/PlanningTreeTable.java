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

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionTreeCreateActivity;
import org.miradi.actions.ActionTreeCreateIndicator;
import org.miradi.actions.ActionTreeCreateMethod;
import org.miradi.actions.ActionTreeCreateObjective;
import org.miradi.actions.ActionTreeCreateTask;
import org.miradi.actions.ActionTreeShareActivity;
import org.miradi.actions.ActionTreeShareMethod;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.TreeTableWithPreferredScrollableViewportSize;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;

public class PlanningTreeTable extends TreeTableWithPreferredScrollableViewportSize implements RowColumnBaseObjectProvider
{
	public PlanningTreeTable(MainWindow mainWindowToUse, GenericTreeTableModel planningTreeModelToUse)
	{
		super(mainWindowToUse, planningTreeModelToUse);
	}
	
	@Override
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row).getObject();
	}

	@Override
	public void ensureSelectedRowVisible()
	{
		Rectangle rect = getCellRect(getSelectedRow(), 0, true);
		scrollRectToVisible(rect);
	}
	
	@Override
	public int getProportionShares(int row)
	{
		return getNodeForRow(row).getProportionShares();
	}
	
	@Override
	public boolean areBudgetValuesAllocated(int row)
	{
		return getNodeForRow(row).areBudgetValuesAllocated();
	}
	
	@Override
	protected Set<Class> getRelevantActions()
	{
		HashSet<Class> actions = new HashSet<Class>();
		actions.addAll(super.getRelevantActions());
		actions.add(ActionTreeCreateObjective.class);
		actions.add(ActionTreeCreateIndicator.class);
		actions.add(ActionTreeCreateActivity.class);
		actions.add(ActionTreeShareActivity.class);
		actions.add(ActionTreeCreateMethod.class);
		actions.add(ActionTreeShareMethod.class);
		actions.add(ActionTreeCreateTask.class);			
		actions.add(ActionDeletePlanningViewTreeNode.class);
		return actions;
	}
}
