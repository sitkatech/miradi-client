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

import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;

public class MultiTableCombinedAsOneExporter extends AbstractTableExporter
{
	public MultiTableCombinedAsOneExporter()
	{
		clear();
	}

	public void clear()
	{
		tables = new Vector();
	}
	
	public void addAsMasterTable(AbstractTableExporter table)
	{
		tables.add(MASTER_TABLE_INDEX, table);
	}
	
	public void addExportable(AbstractTableExporter table)
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

	public int getDepth(int row, int column)
	{
		if (tables.size() == 0)
			return 0;
		
		return tables.get(0).getDepth(row, column);
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
	
	@Override
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(column);		
		return tableAndColumnHolder.getTable().getChoiceItemAt(row, tableAndColumnHolder.getColumn());
	}
	
	public String getStyleTagAt(int row, int column)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(column);		
		return tableAndColumnHolder.getTable().getStyleTagAt(row, column);
	}

	@Override
	public int getRowType(int row)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(0);
		return  tableAndColumnHolder.getTable().getRowType(row);
	}

	@Override
	public String getTextAt(int row, int column)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(column);
		Object value = tableAndColumnHolder.getTable().getTextAt(row, tableAndColumnHolder.getColumn());
		return getSafeValue(value);
	}
			
	public BaseObject getBaseObjectForRow(int row)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(0);
		return tableAndColumnHolder.getTable().getBaseObjectForRow(row);
	}

	protected TableAndColumnHolder getTableAndColumn(int column)
	{
		int columnWithinTable = column;
		int thisColumnCount = 0;
		for (int i = 0; i < tables.size(); ++i)
		{
			AbstractTableExporter thisTable = tables.get(i);
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
	
	protected class TableAndColumnHolder
	{
		public TableAndColumnHolder(AbstractTableExporter tableToUse, int columnToUse)
		{
			table = tableToUse;
			column = columnToUse;
		}
		
		public int getColumn()
		{
			return column;
		}
		
		public AbstractTableExporter getTable()
		{
			return table;
		}
		
		private AbstractTableExporter table;
		private int column;
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		return getMasterTable().getAllRefs(objectType);
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		return getMasterTable().getAllTypes();
	}
	
	private AbstractTableExporter getMasterTable()
	{
		return getTables().get(MASTER_TABLE_INDEX);
	}
	
	protected Vector<AbstractTableExporter> getTables()
	{
		return tables;
	}
	
	private Vector<AbstractTableExporter> tables;
	private static final int MASTER_TABLE_INDEX = 0;
}
