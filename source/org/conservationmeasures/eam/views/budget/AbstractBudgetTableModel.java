/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;

abstract public class AbstractBudgetTableModel extends AbstractTableModel
{
	public int getAccountingCodeColumnIndex()
	{
		return ACCOUNTING_CODE_COLUMN_INDEX;
	}
	
	public int getFundingSourceColumnIndex()
	{
		return FUNDING_SOURCE_COLUMN_INDEX;
	}
	
	public int getResourcesColumnIndex()
	{
		return RESOURCES_COLUMN_INDEX;
	}
	
	public int getRowTotalsLabelColumnIndex()
	{
		return ROW_TOTALS_LABEL_COLUMN_INDEX;
	}
	
	public int getUnitsAndCostLabelColumnIndex()
	{
		return UNITS_AND_COST_LABEL_COLUMN_INDEX;
	}
	
	public int getCostPerUnitLabelColumnIndex()
	{
		return COST_PER_UNIT_COLUMN_INDEX;
	}
	
	public int getUnitsLabelColumnIndex()
	{
		return UNITS_LABEL_COLUMN_INDEX;
	}
	
	public int getUnitTotalsColumnIndex()
	{
		return getColumnCount() - 2;	
	}
	
	public int getCostTotalsColumnIndex()
	{
		return getColumnCount() - 1;
	}
	
	protected boolean isFundingSourceColumn(int col)
	{
		return col == getFundingSourceColumnIndex();
	}

	protected boolean isAccountingCodeColumn(int col)
	{
		return col == getAccountingCodeColumnIndex();
	}

	protected boolean isLabelColumn(int col)
	{
		return 3 <= col && col < TOTAL_ROW_HEADER_COLUMN_COUNT;
	}

	protected boolean isResourceColumn(int col)
	{
		return col == getResourcesColumnIndex();
	}

	protected boolean isCostTotalsColumn(int col)
	{
		return col == getCostTotalsColumnIndex();
	}

	protected boolean isUnitsTotalColumn(int col)
	{
		return col == getUnitTotalsColumnIndex();
	}

	protected boolean isUnitsColumn(int col)
	{
		if (col < (TOTAL_ROW_HEADER_COLUMN_COUNT))
			return false;
		
		if (!isOdd(col))
			return false;
		
		if (col  < (getColumnCount() - TOTALS_COLUMN_COUNT ))
			return true;
		
		return false;
	}

	public boolean isCostColumn(int col)
	{
		if (col < (TOTAL_ROW_HEADER_COLUMN_COUNT))
			return false;
		
		if (isOdd(col))
			return false;
		
		if (col  < (getColumnCount() - TOTALS_COLUMN_COUNT ))
			return true;
		
		return false;
	}

	protected boolean isStaticLabelColum(int col)
	{
		return col == getUnitsAndCostLabelColumnIndex();
	}

	protected boolean isCostPerUnitLabelColumn(int col)
	{
		return col == getCostPerUnitLabelColumnIndex();
	}

	protected boolean isUnitsLabelColumn(int col)
	{
		return col == getUnitsLabelColumnIndex();
	}

	protected boolean isOdd(int value)
	{
		return value % 2 != 0;
	}

	public int getCorrectedRow(int row)
	{
		return row /= 2;
	}

	abstract public boolean isTotalsRow(int row);
	
	abstract public BaseId getAssignmentForRow(int row);
	
	private static final int RESOURCES_COLUMN_INDEX = 0;
	private static final int FUNDING_SOURCE_COLUMN_INDEX = 1;
	private static final int ACCOUNTING_CODE_COLUMN_INDEX = 2;
	private static final int UNITS_LABEL_COLUMN_INDEX = 3;
	private static final int COST_PER_UNIT_COLUMN_INDEX = 4;	
	private static final int UNITS_AND_COST_LABEL_COLUMN_INDEX = 5;
	private static final int ROW_TOTALS_LABEL_COLUMN_INDEX = 6;

	static final int TOTAL_ROW_HEADER_COLUMN_COUNT = 7;
	
	static final int TOTALS_ROW_COUNT = 2;
	static final int TOTALS_COLUMN_COUNT = 2;
	
}
