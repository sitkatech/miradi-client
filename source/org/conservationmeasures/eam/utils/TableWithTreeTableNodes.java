/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import org.conservationmeasures.eam.dialogs.fieldComponents.TreeNodeForRowProvider;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewFullSizeTable;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;

abstract public class TableWithTreeTableNodes extends PlanningViewFullSizeTable implements TreeNodeForRowProvider
{
	public TableWithTreeTableNodes(PlanningViewAbstractTreeTableSyncedTableModel modelToUse)
	{
		super(modelToUse);
		nodeProvider = modelToUse;
	}

	public TreeTableNode getNodeForRow(int row)
	{
		return nodeProvider.getNodeForRow(row);
	}

	TreeNodeForRowProvider nodeProvider;
}
