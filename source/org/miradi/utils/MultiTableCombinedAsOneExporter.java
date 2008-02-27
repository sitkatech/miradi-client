/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.util.Vector;

import org.miradi.objects.BaseObject;

public class MultiTableCombinedAsOneExporter implements ExportableTableInterface
{
	public MultiTableCombinedAsOneExporter()
	{
		clear();
	}

	public void clear()
	{
		tables = new Vector();
	}
	
	public void addTable(ExportableTableInterface table)
	{
		tables.add(table);
	}
	
	public int getColumnCount()
	{
		int combinedCount = 0;
		for (int i = 0;  i < tables.size(); ++i)
		{
			combinedCount += tables.get(i).getColumnCount();
		}
		
		return combinedCount;
	}

	public int getDepth(int row)
	{
		if (tables.size() == 0)
			return 0;
		
		return tables.get(0).getDepth(row);
	}

	public int getMaxDepthCount()
	{
		if (tables.size() == 0)
			return 0;
		
		return tables.get(0).getMaxDepthCount();	
	}

	public int getRowCount()
	{
		if (tables.size() > 0) 
			return tables.get(0).getRowCount();
		
		return 0;
	}

	public String getHeaderFor(int column)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(column);
		return tableAndColumnHolder.getTable().getHeaderFor(tableAndColumnHolder.getColumn());
	}
	
	public Object getValueAt(int row, int column)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(column);
		Object value = tableAndColumnHolder.getTable().getValueAt(row, tableAndColumnHolder.getColumn());
		if (value == null)
			return "";
		
		return value.toString();
	}
	
	public BaseObject getObjectForRow(int row)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(0);
		return tableAndColumnHolder.getTable().getObjectForRow(row);
	}

	private TableAndColumnHolder getTableAndColumn(int column)
	{
		int columnWithinTable = column;
		int thisColumnCount = 0;
		for (int i = 0; i < tables.size(); ++i)
		{
			ExportableTableInterface thisTable = tables.get(i);
			thisColumnCount += thisTable.getColumnCount();
			if (thisColumnCount <= column)
			{
				columnWithinTable -= thisTable.getColumnCount();
				continue;
			}
			
			return new TableAndColumnHolder(thisTable, columnWithinTable);
		}
		
		throw new RuntimeException("Error occurred while exporting table.");
	}
	
	private class TableAndColumnHolder
	{
		public TableAndColumnHolder(ExportableTableInterface tableToUse, int columnToUse)
		{
			table = tableToUse;
			column = columnToUse;
		}
		
		public int getColumn()
		{
			return column;
		}
		
		public ExportableTableInterface getTable()
		{
			return table;
		}
		
		private ExportableTableInterface table;
		private int column;
	}
		
	private Vector<ExportableTableInterface> tables;
}
