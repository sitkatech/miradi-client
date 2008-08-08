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

import javax.swing.Icon;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;

public class ThreatStressRatingMultiTableAsOneExporter extends MultiTableCombinedAsOneExporter
{
	public ThreatStressRatingMultiTableAsOneExporter()
	{
		super();
	}

	@Override
	public void clear()
	{
		super.clear();

		summaryRowTables = new Vector<AbstractTableExporter>();
	}
	
	public void addAsTopRowTable(AbstractTableExporter table)
	{
		super.addExportable(table);
	}
	
	public void addAsSummaryRowTable(int index, AbstractTableExporter table)
	{
		summaryRowTables.add(index, table);
	}
		
	@Override
	public int getRowCount()
	{
		final int SUMMARY_TABLES_ROW_COUNT = 1;
		
		return super.getRowCount() +  SUMMARY_TABLES_ROW_COUNT;
	}
	
	@Override
	public Icon getIconAt(int row, int column)
	{
		if (isTopRowTable(row))
			return super.getIconAt(row, column);
		
		return null;
	}

	@Override
	public int getRowType(int row)
	{
		if (isTopRowTable(row))
			return super.getRowType(row);
		
		return ObjectType.FAKE;
	}
	
	public BaseObject getBaseObjectForRow(int row)
	{
		if (isTopRowTable(row))
			return getBaseObjectForRow(row);
		
		return null;
	}
	
	@Override
	public String getTextAt(int row, int column)
	{	
		if (isTopRowTable(row))
			return super.getTextAt(row, column);
		
		AbstractTableExporter summaryTable = summaryRowTables.get(0);
		int columnWithinSummaryTable = convertToSummaryTableColumn(column);
		if (columnWithinSummaryTable < summaryTable.getColumnCount() )
			return summaryTable.getTextAt(0, columnWithinSummaryTable);
		
		return summaryRowTables.get(1).getTextAt(0, 0);
	}
	

	private int convertToSummaryTableColumn(int column)
	{
		int BLANK_FIRST_COLUMN_COUNT = 1;
		
		return column - BLANK_FIRST_COLUMN_COUNT;
	}

	private boolean isTopRowTable(int row)
	{
		return row < getTables().get(0).getRowCount();
	}
			
	private Vector<AbstractTableExporter> summaryRowTables;
}
