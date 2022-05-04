/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.base;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

abstract public class SingleBooleanColumnEditableModel extends EditableObjectTableModel
{
	public SingleBooleanColumnEditableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse)
	{
		super(projectToUse);
		
		rowColumnBaseObjectProvider = providerToUse;
	}
	
    @Override
	public Class<?> getColumnClass(int columnIndex) 
    {
    	return Boolean.class;
    }
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return true;
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return rowColumnBaseObjectProvider.getBaseObjectForRowColumn(row, column);
	}

	public int getColumnCount()
	{
		return 1;
	}
	
	public int getRowCount()
	{
		return rowColumnBaseObjectProvider.getRowCount();
	}

	public String getColumnTag(int modelColumn)
	{
		return "";
	}
	
	public Object getValueAt(int row, int column)
	{
		try
		{
			if (isRowSelected(row, column))
				return new Boolean(true);
			
			return new Boolean(false);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new Boolean(false);
		}
	}

	public ORef getRefForRow(int row)
	{
		BaseObject baseObjectForRow = getBaseObjectForRowColumn(row, SINGLE_COLUMN_INDEX);
		if (baseObjectForRow == null)
			return ORef.INVALID;
		
		return baseObjectForRow.getRef();
	}

	@Override
	public void setObjectRefs(ORefList hierarchyToSelectedRef)
	{
		//FIXME urgent: should this do something?
	}

	abstract protected boolean isRowSelected(int row, int column) throws Exception;

	private RowColumnBaseObjectProvider rowColumnBaseObjectProvider;
	protected static final int SINGLE_COLUMN_INDEX = 0;
}
