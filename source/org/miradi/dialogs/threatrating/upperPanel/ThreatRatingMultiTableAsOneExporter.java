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
package org.miradi.dialogs.threatrating.upperPanel;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.MultiTableCombinedAsOneExporter;
import org.miradi.utils.TableExporter;

public class ThreatRatingMultiTableAsOneExporter extends MultiTableCombinedAsOneExporter
{
	public ThreatRatingMultiTableAsOneExporter(Project projectToUse)
	{
		super(projectToUse);
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
	
	public void setTargetSummaryRowTable(TableExporter table)
	{
		targetSummaryRowTable = table;
	}
	
	public void setOverallSummaryRowTable(TableExporter table)
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
	public ChoiceItem getModelChoiceItemAt(int row, int modelColumn)
	{
		if (isTopRowTable(row))
			return super.getModelChoiceItemAt(row, modelColumn);

		if (isFirstBlankTableSummaryRow(modelColumn))
			return new EmptyChoiceItem();
		
		if (isTargetSummaryRowHeader(modelColumn))
			return new TaglessChoiceItem(ThreatRatingMultiTablePanel.getTargetSummaryRowHeaderLabel());

		int columnWithinSummaryTable = convertToSummaryTableColumn(modelColumn);		
		if (isColumnWithinSummaryTable(columnWithinSummaryTable))
			return targetSummaryRowTable.getChoiceItemAt(0, columnWithinSummaryTable);
		
		return overallProjectRatingSummaryTable.getChoiceItemAt(0, 0);
	}

	private boolean isTargetSummaryRowHeader(int modelColumn)
	{
		return modelColumn == ThreatNameColumnTableModel.THREAT_NAME_COLUMN_INDEX;
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
		return column < ThreatNameColumnTableModel.THREAT_NAME_COLUMN_INDEX;
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
			
	private TableExporter targetSummaryRowTable;
	private TableExporter overallProjectRatingSummaryTable;
	public static final int TARGET_SUMMARY_ROW_TABLE_INDEX = 0;
	public static final int OVERALL_PROJECT_RATING_ROW_TABLE_INDEX = 1;
}
