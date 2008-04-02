/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.utils;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewFullSizeTable;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objects.BaseObject;

abstract public class TableWithTreeTableNodes extends PlanningViewFullSizeTable implements RowColumnBaseObjectProvider
{
	public TableWithTreeTableNodes(PlanningViewAbstractTreeTableSyncedTableModel modelToUse)
	{
		super(modelToUse);
	}

	public BaseObject getNodeForRow(int row)
	{
		return getSyncedModel().getNodeForRow(row);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row);
	}

	protected PlanningViewAbstractTreeTableSyncedTableModel getSyncedModel()
	{
		return (PlanningViewAbstractTreeTableSyncedTableModel)getModel();
	}

	public String getColumnTag(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getSyncedModel().getColumnTag(modelColumn);
	}
}
