/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import org.conservationmeasures.eam.dialogs.fieldComponents.TreeNodeForRowProvider;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewFullSizeTable;
import org.conservationmeasures.eam.dialogs.treetables.RowBaseObjectProvider;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objects.BaseObject;

abstract public class TableWithTreeTableNodes extends PlanningViewFullSizeTable implements TreeNodeForRowProvider, RowBaseObjectProvider
{
	public TableWithTreeTableNodes(PlanningViewAbstractTreeTableSyncedTableModel modelToUse)
	{
		super(modelToUse);
	}

	public TreeTableNode getNodeForRow(int row)
	{
		return getSyncedModel().getNodeForRow(row);
	}
	
	public BaseObject getBaseObjectForRow(int row)
	{
		return getNodeForRow(row).getObject();
	}

	PlanningViewAbstractTreeTableSyncedTableModel getSyncedModel()
	{
		return (PlanningViewAbstractTreeTableSyncedTableModel)getModel();
	}

	public String getColumnTag(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getSyncedModel().getColumnTag(modelColumn);
	}
}
