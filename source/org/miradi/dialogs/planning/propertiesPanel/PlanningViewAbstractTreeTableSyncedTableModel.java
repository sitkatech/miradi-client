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
