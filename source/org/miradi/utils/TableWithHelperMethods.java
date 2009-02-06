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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

abstract public class TableWithHelperMethods extends TableWithSunBugWorkarounds
{
	public TableWithHelperMethods()
	{
	}

	public TableWithHelperMethods(TableModel model)
	{
		super(model);
	}

	public void setForcedPreferredScrollableViewportHeight(int viewportheight)
	{
		forcedPreferredScrollableViewportHeight = viewportheight;
	}
	
	public void setForcedPreferredScrollableViewportWidth(int viewportWidth)
	{
		forcedPreferredScrollableViewportWidth = viewportWidth;
	}
	
	public Dimension getPreferredScrollableViewportSize()
	{
		int preferredHeight = super.getPreferredScrollableViewportSize().height;
		if (forcedPreferredScrollableViewportHeight != 0)
			preferredHeight = forcedPreferredScrollableViewportHeight;
		
		int preferredWidth = super.getPreferredScrollableViewportSize().width;
		if (forcedPreferredScrollableViewportWidth != 0)
			preferredWidth = forcedPreferredScrollableViewportWidth;
		
		return new Dimension(preferredWidth, preferredHeight);
	}
	
	public void setColumnWidthToHeaderWidth(int tableColumn)
	{
		setColumnWidth(tableColumn, getColumnHeaderWidth(this, tableColumn));
	}
	
	public int getColumnHeaderWidth(int tableColumn) 
	{
		return getColumnHeaderWidth(this, tableColumn);
	}

	public static int getColumnHeaderWidth(JTable table, int tableColumn) 
	{
		TableColumn columnToAdjust = table.getColumnModel().getColumn(tableColumn);
		String padding = "    ";
		String value = (String)columnToAdjust.getHeaderValue() + padding;
		return getRenderedWidth(table, tableColumn, value);
	}
	
	public int getRenderedWidth(int tableColumn, String value) 
	{
		return getRenderedWidth(this, tableColumn, value);
	}
	
	public static int getRenderedWidth(JTable table, int tableColumn, String value)
	{
		TableColumn columnToAdjust = table.getColumnModel().getColumn(tableColumn);
		TableCellRenderer renderer = columnToAdjust.getHeaderRenderer();
		if(renderer == null)
		{
			JTableHeader header = table.getTableHeader();
			renderer = header.getDefaultRenderer();
		}
		Component c = renderer.getTableCellRendererComponent(table, value, true, true, -1, tableColumn);
		int width = c.getPreferredSize().width;
		return width;
	}
	
	public void setColumnWidth(int tableColumn, int width) 
	{
		TableColumn columnToAdjust = getTableColumn(tableColumn);
		
		columnToAdjust.setPreferredWidth(width);
		columnToAdjust.setWidth(width);
	}

	public TableColumn getTableColumn(int tableColumn)
	{
		return getColumnModel().getColumn(tableColumn);
	}

	public void resizeTable()
	{
		resizeTable(getModel().getRowCount());
	}
	
	public void resizeTable(int rowCount)
	{
		Dimension d = getPreferredScrollableViewportSize();
		int constantRowHeight = getRowHeight() + getRowMargin() ;
		d.height = rowCount * constantRowHeight;
		int headerWidth = getHeaderWidth();
		if(headerWidth < maxGridWidthPixels  && !useMaxWidth)
			d.width = headerWidth;
		else
			d.width = maxGridWidthPixels;
		setPreferredScrollableViewportSize(d);
	}
	
	public int getHeaderWidth()
	{
		int width = 0;
		for(int tableColumn = 0; tableColumn < getColumnCount(); ++tableColumn)
		{
			width += getTableColumn(tableColumn).getWidth();
		}
		return width;
	}
	
	public void stopCellEditing()
	{
		TableCellEditor editor = getCellEditor();
		if (editor == null)
			return;
	
		editor.stopCellEditing();
	}

	public void ensureSelectedRowVisible()
	{
		ensureSelectedRowVisible(this);
	}
	
	public static void ensureSelectedRowVisible(JTable table)
	{
		Rectangle cellRect = table.getCellRect(table.getSelectedRow(), 0 , true);
		table.scrollRectToVisible(cellRect);
	}
	
	private	int maxGridWidthPixels;
	private	boolean useMaxWidth;
	private int forcedPreferredScrollableViewportWidth;
	private int forcedPreferredScrollableViewportHeight;
}
