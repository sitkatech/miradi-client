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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import org.miradi.main.MainWindow;

import com.java.sun.jtreetable.JTreeTable;


public class SingleTableRowHeightController extends TableRowHeightController
{
	public SingleTableRowHeightController(MainWindow mainWindowToUse, TableWithRowHeightManagement tableToControl)
	{
		super(mainWindowToUse);
		table = tableToControl;
		
		table.asTable().addComponentListener(new ComponentEventHandler());
	}

	public void updateAutomaticRowHeights()
	{
		if(!isAutomaticRowHeightsEnabled())
			return;
		
		int rowCount = table.asTable().getRowCount();
		for(int row = 0; row < rowCount; ++row)
		{
			int height = getPreferredRowHeight(row);
			table.asTable().setRowHeight(row, height);
		}
		
		setVariableRowHeight();
	}
	
	private void setVariableRowHeight()
	{
		// FIXME: This will not work for regular tables
		((JTreeTable)table).getTree().setRowHeight(-1);
	}

	private int getPreferredRowHeight(int row)
	{
		return table.getPreferredRowHeight(row);
	}

	class ComponentEventHandler implements ComponentListener
	{
		public void componentShown(ComponentEvent e)
		{
			updateAutomaticRowHeights();
		}
		
		public void componentHidden(ComponentEvent e)
		{
		}

		public void componentMoved(ComponentEvent e)
		{
		}

		public void componentResized(ComponentEvent e)
		{
			updateAutomaticRowHeights();
		}

	}
	
	private TableWithRowHeightManagement table;
}
