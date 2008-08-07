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

import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.miradi.dialogs.fieldComponents.PanelTable;
import org.miradi.main.MainWindow;

abstract public class TableWithRowHeightSaver extends PanelTable implements TableWithRowHeightManagement
{
	public TableWithRowHeightSaver(MainWindow mainWindowToUse, TableModel model)
	{
		super(mainWindowToUse, model);
		
		rowHeightSaver = new TableRowHeightSaver();
		rowHeightSaver.manage(getMainWindow(), this, getUniqueTableIdentifier());
	}
	
	public boolean allowUserToSetRowHeight()
	{
		return true;
	}
	
	public void addRowHeightListener(RowHeightListener listener)
	{
		rowHeightSaver.addRowHeightListener(listener);
	}
	
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if(rowHeightSaver == null)
			return;
		
		rowHeightSaver.rowHeightChanged(rowHeight);
	}
	
	public void setRowHeight(int row, int rowHeight)
	{
		super.setRowHeight(row, rowHeight);
		if(rowHeightSaver == null)
			return;
		
		rowHeightSaver.rowHeightChanged(row, rowHeight);
	}
	
	public Rectangle getCellRect(int row, int column, boolean includeSpacing)
	{
		Rectangle cellRect = super.getCellRect(row, column, includeSpacing);
		if(!includeSpacing && getMainWindow().isRowHeightModeManual())
		{
			cellRect.height -= TableRowHeightSaver.ROW_RESIZE_MARGIN;
		}
		return cellRect;
	}
	
	public JTable asTable()
	{
		return this;
	}
	

	abstract public String getUniqueTableIdentifier();
	
	private TableRowHeightSaver rowHeightSaver;
}
