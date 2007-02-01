/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import java.text.DecimalFormat;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.utils.DateRange;

abstract public class AbstractBudgetTableModel extends AbstractTableModel
{
	public int getResourcesColumnIndex()
	{
		return RESOURCES_COLUMN_INDEX;
	}
	
	public int getUnitsAndCostLabelColumnIndex()
	{
		return UNITS_AND_COST_LABEL_COLUMN_INDEX;
	}
	
	public int getCostPerUnitLabelColumnIndex()
	{
		return COST_PER_UNIT_COLUMN_INDEX;
	}
	
	public int getUnitTotalsColumnIndex()
	{
		return getColumnCount() - 2;	
	}
	
	public int getCostTotalsColumnIndex()
	{
		return getColumnCount() - 1;
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
	
	public Object getCost(ProjectResource currentResource, DateRangeEffortList effortList, DateRange dateRange)
	{
		try
		{
			if (currentResource == null)
				return "";
			
			String unit = getUnit(effortList, dateRange);
			double units = convertStringToDouble(unit);
			double costPerUnit = currentResource.getCostPerUnit();
			return currencyFormatter.format(units * costPerUnit);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return "";
	}
	
	public String getUnit(DateRangeEffortList effortList, DateRange dateRange)
	{
		double units = 0.0;
		try
		{
			units = effortList.getTotalUnitQuantity(dateRange);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return decimalFormatter.format(units);
	}
	
//	TODO this method should possibly be renamed and made available 
	//for the whole app.
	public static double convertStringToDouble(String raw)
	{
		if (raw.length() == 0)
			return 0;
		
		double newDouble = 0;
		try
		{
			 newDouble = new Double(raw).doubleValue();
		}
		catch (NumberFormatException e)
		{
			EAM.logException(e);
		}
		
		return newDouble; 
	}
	
	public Object getTotalUnits(Assignment assignment)
	{
		try
		{
			DateRange combinedDateRange = getCombinedDateRange();
			double totalUnits = totalsCalculator.getTotalUnits(assignment, combinedDateRange);
			return decimalFormatter.format(totalUnits);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		return "";
	}

	public DateRange getCombinedDateRange() throws Exception
	{
		return DateRange.combine(dateRanges[0], dateRanges[dateRanges.length - 1]);
	}
	
	abstract public boolean isYearlyTotalColumn(int col);
	
	abstract public BaseId getAssignmentForRow(int row);
	
	abstract public int getUnitsLabelColumnIndex();
	
	abstract public boolean doubleRowed();
	
	abstract protected boolean isUnitsColumn(int col);
	
	abstract protected boolean isLabelColumn(int col);

	abstract public int getAccountingCodeColumnIndex();
	
	abstract public int getFundingSourceColumnIndex();
	
	private static final int RESOURCES_COLUMN_INDEX = 0;
	private static final int COST_PER_UNIT_COLUMN_INDEX = 4;	
	private static final int UNITS_AND_COST_LABEL_COLUMN_INDEX = 5;
	
	static final int TOTAL_ROW_HEADER_COLUMN_COUNT = 5;
	
	static final int TOTALS_COLUMN_COUNT = 2;
	
	DecimalFormat currencyFormatter;
	DecimalFormat decimalFormatter;
	DateRange[] dateRanges;
	BudgetTotalsCalculator totalsCalculator;
}
