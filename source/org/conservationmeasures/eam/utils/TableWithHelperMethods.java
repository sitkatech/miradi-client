/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.utils;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
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

	public void setColumnWidthToHeaderWidth(int column)
	{
		setColumnWidth(column, getColumnHeaderWidth(this, column));
	}
	
	public int getColumnHeaderWidth(int column) 
	{
		return getColumnHeaderWidth(this, column);
	}

	public static int getColumnHeaderWidth(JTable table, int column) 
	{
		TableColumn columnToAdjust = table.getColumnModel().getColumn(column);
		String padding = "    ";
		String value = (String)columnToAdjust.getHeaderValue() + padding;
		return getRenderedWidth(table, column, value);
	}
	
	public int getRenderedWidth(int column, String value) 
	{
		return getRenderedWidth(this, column, value);
	}
	
	public static int getRenderedWidth(JTable table, int column, String value)
	{
		TableColumn columnToAdjust = table.getColumnModel().getColumn(column);
		TableCellRenderer renderer = columnToAdjust.getHeaderRenderer();
		if(renderer == null)
		{
			JTableHeader header = table.getTableHeader();
			renderer = header.getDefaultRenderer();
		}
		Component c = renderer.getTableCellRendererComponent(table, value, true, true, -1, column);
		int width = c.getPreferredSize().width;
		return width;
	}
	
	public void setColumnWidth(int column, int width) 
	{
		TableColumn columnToAdjust = getColumnModel().getColumn(column);
		columnToAdjust.setPreferredWidth(width);
		columnToAdjust.setWidth(width);
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
		for(int i = 0; i < getModel().getColumnCount(); ++i)
		{
			width += getColumnModel().getColumn(i).getWidth();
		}
		return width;
	}
	
	int maxGridWidthPixels;
	boolean useMaxWidth;
}
