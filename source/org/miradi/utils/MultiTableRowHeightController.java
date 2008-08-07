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
package org.miradi.utils;

import java.util.HashSet;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class MultiTableRowHeightController implements RowHeightListener
{
	public MultiTableRowHeightController(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		tables = new HashSet<TableWithRowHeightManagement>();
	}
	
	public void addTable(TableWithRowHeightManagement tableToAdd)
	{
		tables.add(tableToAdd);
		tableToAdd.addRowHeightListener(this);
		rowHeightChanged(tableToAdd.getRowHeight());
	}
	
	public void rowHeightChanged(int row, int newHeight)
	{
		for(TableWithRowHeightManagement table : tables)
		{
			if(table.getRowHeight(row) != newHeight)
				table.setRowHeight(row, newHeight);
			
			table.ensureSelectedRowVisible();
		}
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
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

	private MainWindow mainWindow;
	private HashSet<TableWithRowHeightManagement> tables;
}
