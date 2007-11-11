/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.dialogs.tablerenderers.RowBaseObjectProvider;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

import com.java.sun.jtreetable.TreeTableModelAdapter;

abstract public class PlanningViewAbstractTreeTableSyncedTableModel extends AbstractTableModel implements ColumnTagProvider, RowBaseObjectProvider
{
	public PlanningViewAbstractTreeTableSyncedTableModel(Project projectToUse, TreeTableModelAdapter adapterToUse) throws Exception
	{
		project = projectToUse;
		adapter = adapterToUse;
	}
	
	public int getRowCount()
	{
		return adapter.getRowCount();
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
		
	public TreeTableNode getNodeForRow(int row)
	{
		return (TreeTableNode)adapter.nodeForRow(row);
	}
		
	public BaseObject getBaseObjectForRow(int row)
	{
		return getNodeForRow(row).getObject();
	}

	protected Project project;
	private TreeTableModelAdapter adapter;
}
