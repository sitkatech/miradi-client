/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewFullSizeTable;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objects.BaseObject;

abstract public class TableWithTreeTableNodes extends PlanningViewFullSizeTable implements RowColumnBaseObjectProvider
{
	public TableWithTreeTableNodes(PlanningViewAbstractTreeTableSyncedTableModel modelToUse)
	{
		super(modelToUse);
	}

	public TreeTableNode getNodeForRow(int row)
	{
		return getSyncedModel().getNodeForRow(row);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row).getObject();
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
