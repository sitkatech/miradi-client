/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.dialogs.fieldComponents.TreeNodeForRowProvider;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

import com.java.sun.jtreetable.TreeTableModelAdapter;

abstract public class PlanningViewAbstractTreeTableSyncedTableModel extends AbstractTableModel implements ColumnTagProvider, TreeNodeForRowProvider
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
		
	protected Project project;
	private TreeTableModelAdapter adapter;
}
