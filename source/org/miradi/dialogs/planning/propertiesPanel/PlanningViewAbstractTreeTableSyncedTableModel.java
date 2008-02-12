/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

import com.java.sun.jtreetable.TreeTableModelAdapter;

abstract public class PlanningViewAbstractTreeTableSyncedTableModel extends EditableObjectTableModel
{
	public PlanningViewAbstractTreeTableSyncedTableModel(Project projectToUse, TreeTableModelAdapter adapterToUse) throws Exception
	{
		super(projectToUse);
		
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
		
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row).getObject();
	}
	
	public TreeTableModelAdapter getTreeTableModelAdapter()
	{
		return adapter;
	}

	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
	}
	
	protected Project project;
	private TreeTableModelAdapter adapter;
}
