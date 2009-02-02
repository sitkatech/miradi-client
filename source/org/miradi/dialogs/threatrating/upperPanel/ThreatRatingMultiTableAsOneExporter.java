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
package org.miradi.dialogs.threatrating.upperPanel;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.MultiTableCombinedAsOneExporter;

public class ThreatRatingMultiTableAsOneExporter extends MultiTableCombinedAsOneExporter
{
	public ThreatRatingMultiTableAsOneExporter()
	{
		super();
	}

	@Override
	public void clear()
	{
		super.clear();
	}
	
	public void addAsTopRowTable(AbstractTableExporter table)
	{
		super.addExportable(table);
	}
	
	public void setTargetSummaryRowTable(AbstractTableExporter table)
	{
		targetSummaryRowTable = table;
	}
	
	public void setOverallSummaryRowTable(AbstractTableExporter table)
	{
		overallProjectRatingSummaryTable = table;
	}
	
	@Override
	public int getRowCount()
	{
		final int SUMMARY_TABLES_ROW_COUNT = 1;
		
		return super.getRowCount() +  SUMMARY_TABLES_ROW_COUNT;
	}
	
	@Override
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		if (isTopRowTable(row))
			return super.getChoiceItemAt(row, column);

		if (isFirstBlankTableSummaryRow(column))
			return new EmptyChoiceItem();

		int columnWithinSummaryTable = convertToSummaryTableColumn(column);		
		if (isColumnWithinSummaryTable(columnWithinSummaryTable))
			return targetSummaryRowTable.getChoiceItemAt(0, columnWithinSummaryTable);
		
		return overallProjectRatingSummaryTable.getChoiceItemAt(0, 0);
	}

	private boolean isColumnWithinSummaryTable(int columnWithinSummaryTable)
	{
		return columnWithinSummaryTable < targetSummaryRowTable.getColumnCount();
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
	
	private boolean isFirstBlankTableSummaryRow(int column)
	{
		return column < ThreatNameColumnTableModel.COLUMN_COUNT;
	}

	private int convertToSummaryTableColumn(int column)
	{
		int BLANK_FIRST_COLUMN_COUNT = ThreatNameColumnTableModel.COLUMN_COUNT;
		
		return column - BLANK_FIRST_COLUMN_COUNT;
	}

	private boolean isTopRowTable(int row)
	{
		return row < super.getRowCount();
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		EAM.logError("getAllRefs is not implemented");
		return new ORefList();
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		EAM.logError("getAllTypes is not implemented");
		return new Vector<Integer>();
	}
			
	private AbstractTableExporter targetSummaryRowTable;
	private AbstractTableExporter overallProjectRatingSummaryTable;
	public static final int TARGET_SUMMARY_ROW_TABLE_INDEX = 0;
	public static final int OVERALL_PROJECT_RATING_ROW_TABLE_INDEX = 1;
}
