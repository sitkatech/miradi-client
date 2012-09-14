/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.utils;

import java.util.HashSet;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class MultiTableRowHeightController extends TableRowHeightController 
{
	public MultiTableRowHeightController(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		tables = new HashSet<TableWithRowHeightManagement>();
	}
	
	public void addTable(TableWithRowHeightManagement tableToAdd)
	{
		tables.add(tableToAdd);
		tableToAdd.setMultiTableRowHeightController(this);
		rowHeightChanged(tableToAdd.getRowHeight());
	}
	
	public void rowHeightChanged(int row, int newHeight)
	{
		if(isSetRowHeightInProgress)
			return;
		
		enableRowHeightInProgress();
		try
		{
			for(TableWithRowHeightManagement table : tables)
			{
				if(table.getRowHeight(row) != newHeight)
				{
					table.setRowHeight(row, newHeight);
					table.ensureSelectedRowVisible();
				}
			}
		}
		finally
		{
			disableRowHeightInProgress();
		}
	}
	
	public void saveNewRowHeight(int newHeight)
	{
		if(isSetRowHeightInProgress)
			return;
		
		enableRowHeightInProgress();
		try
		{
			for(TableWithRowHeightManagement table : tables)
			{
				table.saveRowHeight(newHeight);
			}
		}
		finally
		{
			disableRowHeightInProgress();
		}
	}

	private void disableRowHeightInProgress()
	{
		isSetRowHeightInProgress = false;
	}

	private void enableRowHeightInProgress()
	{
		isSetRowHeightInProgress = true;
	}

	
	public void rowHeightChanged(int newHeight)
	{
		if(getMainWindow().isRowHeightModeAutomatic())
			return;

		EAM.logVerbose("rowHeightChanged to " + newHeight);
		for(TableWithRowHeightManagement table : tables)
		{
			if(table.getRowHeight() != newHeight)
				table.setRowHeight(newHeight);
			
			//TODO why does this work (table is scolled to begenning even toug last row was selected) when placed 
			//outside of if.  an it fails to do the right thing when placed as part of the if above.  
			table.ensureSelectedRowVisible();
		}
		EAM.logVerbose("rowHeightChanged done");
	}
	
	@Override
	public void updateAutomaticRowHeights()
	{
		if(!getMainWindow().isRowHeightModeAutomatic())
			return;
		
		for(int row = 0; row < getRowCount(); ++row)
		{
			int tallestPreferred = getTallestPreferredRowHeight(row);
			setRowHeightInAllTables(row, tallestPreferred);
		}

		for(TableWithRowHeightManagement table : tables)
		{
			table.setVariableRowHeight();
		}
		
	}

	private int getRowCount()
	{
		int minimumRowCount = -1;
		for(TableWithRowHeightManagement table : tables)
		{
			int rowCount = table.getRowCount();
			if(minimumRowCount < 0)
				minimumRowCount = rowCount;
			minimumRowCount = Math.min(minimumRowCount, rowCount);
		}
		
		if(minimumRowCount < 0)
			minimumRowCount = 0;
		
		return minimumRowCount;
	}

	private int getTallestPreferredRowHeight(int row)
	{
		int tallest = 0;
		for(TableWithRowHeightManagement table : tables)
		{
			int rowHeight = table.getPreferredRowHeight(row);
			tallest = Math.max(tallest, rowHeight);
		}
		
		return tallest;
	}

	private void setRowHeightInAllTables(int row, int newRowHeight)
	{
		for(TableWithRowHeightManagement table : tables)
		{
			table.setRowHeight(row, newRowHeight);
		}
	}

	private HashSet<TableWithRowHeightManagement> tables;
	private boolean isSetRowHeightInProgress;
}
