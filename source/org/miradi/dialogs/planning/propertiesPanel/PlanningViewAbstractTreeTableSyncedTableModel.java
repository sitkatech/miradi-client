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
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.ExportableTableInterface;

abstract public class PlanningViewAbstractTreeTableSyncedTableModel extends EditableObjectTableModel implements ExportableTableInterface
{
	public PlanningViewAbstractTreeTableSyncedTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse);
		
		project = projectToUse;
		objectProvider = providerToUse;
	}
	
	public int getRowCount()
	{
		return objectProvider.getRowCount();
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
		
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return objectProvider.getBaseObjectForRowColumn(row, column);
	}
	
	public RowColumnBaseObjectProvider getTreeTableModelAdapter()
	{
		return objectProvider;
	}

	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
	}
	
	public BaseObject getBaseObjectForRow(int row)
	{
		return getBaseObjectForRowColumn(row, 0);
	}
	
	public int getDepth(int row)
	{
		return 0;
	}
	
	public int getMaxDepthCount()
	{
		return 0;
	}
	
	public String getHeaderFor(int column)
	{
		return getColumnName(column);
	}
	
	protected Project project;
	private RowColumnBaseObjectProvider objectProvider;
}
