/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewFullSizeTable;
import org.conservationmeasures.eam.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objects.BaseObject;

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
