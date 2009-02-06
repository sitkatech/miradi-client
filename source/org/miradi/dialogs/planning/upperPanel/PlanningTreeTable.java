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

import java.awt.Dimension;
import java.awt.Rectangle;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;

public class PlanningTreeTable extends TreeTableWithStateSaving implements RowColumnBaseObjectProvider
{
	public PlanningTreeTable(MainWindow mainWindowToUse, PlanningTreeTableModel planningTreeModelToUse)
	{
		super(mainWindowToUse, planningTreeModelToUse);
		setAutoResizeMode(AUTO_RESIZE_OFF);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row).getObject();
	}

	public void ensureSelectedRowVisible()
	{
		Rectangle rect = getCellRect(getSelectedRow(), 0, true);
		scrollRectToVisible(rect);
	}
	
	public int getProportionShares(int row)
	{
		return getNodeForRow(row).getProportionShares();
	}
	
	public boolean areBudgetValuesAllocated(int row)
	{
		return getNodeForRow(row).areBudgetValuesAllocated();
	}

	public static final String UNIQUE_IDENTIFIER = "PlanningTreeTable";
}
