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

import java.util.Vector;

import org.miradi.main.MainWindow;


public class SingleTableRowHeightController extends TableRowHeightController
{
	public SingleTableRowHeightController(MainWindow mainWindowToUse, TableWithRowHeightManagement tableToControl)
	{
		super(mainWindowToUse);
		table = tableToControl;
		listenForTableSizeChanges(table);
	}
	
	public void updateAutomaticRowHeights()
	{
		if(controller != null)
			controller.updateAutomaticRowHeights();
		
		if(!isAutomaticRowHeightsEnabled())
			return;
		
 		int rowCount = table.getRowCount();
 		Vector<Integer> oldRowHeights = new Vector<Integer>();
 		Vector<Integer> newRowHeights = new Vector<Integer>();
 		for(int row = 0; row < rowCount; ++row)
 		{
 			oldRowHeights.add(table.getRowHeight(row));
			newRowHeights.add(getPreferredRowHeight(row));
		}
 		
 		if(oldRowHeights.equals(newRowHeights))
 			return;

 		for(int row = 0; row < rowCount; ++row)
 		{
			table.setRowHeight(row, newRowHeights.get(row));
 		}
 		
		table.setVariableRowHeight();
	}
	
	public void setMultiTableRowHeightController(MultiTableRowHeightController listener)
	{
		controller = listener;
	}

	private int getPreferredRowHeight(int row)
	{
		return table.getPreferredRowHeight(row);
	}

	private boolean isAutomaticRowHeightsEnabled()
	{
		if(controller != null)
			return false;
		if(!getMainWindow().isRowHeightModeAutomatic())
			return false;
	
		return true;
	}
	
	public static final int ABOUT_TWO_INCHES = 96*2;

	private TableWithRowHeightManagement table;
	private MultiTableRowHeightController controller;
}
