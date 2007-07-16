/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;

public class BudgetTableModel extends AbstractBudgetTableModel
{
	public BudgetTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse, new IdList());
	}
	
	public BaseId getAssignmentForRow(int row)
	{
		return assignmentIdList.get(row);
	}

	public int getRowCount()
	{
		if (assignmentIdList != null)
			return (assignmentIdList.size() * 2);
		
		return 0;
	}
	
	public int getColumnCount()
	{
		return (dateRanges.length * 2) + TOTAL_ROW_HEADER_COLUMN_COUNT + TOTALS_COLUMN_COUNT;
	}
	
	public boolean isCellEditable(int row, int col) 
	{
		if (isUnitsTotalColumn(col))
			return false;
		
		if (isCostTotalsColumn(col))
			return false;
		
		if (isLabelColumn(col))
			return false;
		
		if (isYearlyTotalColumn(col))
			return false;
		
		if (isOdd(row))
			return false;
			
		return true;
	}
	
	public String getColumnName(int col)
	{
		if (isResourceColumn(col))
			return "Resources";
		
		if (isUnitsLabelColumn(col))
			return "Unit";
		
		if (isCostPerUnitLabelColumn(col))
			return "Cost Per Unit";
		
		if (isLabelColumn(col))
			return "";
		
		if (isUnitsTotalColumn(col))
			return "Unit Totals";
	
		if (isFundingSourceColumn(col))
			return "Funding Source";
		
		if (isAccountingCodeColumn(col))
			return "Accounting Code";
		
		if (isCostTotalsColumn(col))
			return "Cost Totals";
		
		if (isUnitsColumn(col))
			
			return "<html><center>" + getDateRangeAsString(col) + "<br>Units</center></html>";
		
		if (isCostColumn(col))
			return "<html><center>" + getDateRangeAsString(col) + "<br>Cost</center></html>";
		
		return "";
	}

	private String getDateRangeAsString(int col)
	{
		return dateRanges[convertColumn(col)].toString();
	}

	public Object getValueAt(int row, int col)
	{
		BaseId assignmentForRow = getAssignmentForRow(getCorrectedRow(row));
		if (isResourceColumn(col))
			return getCurrentResource(assignmentForRow);
		
		if (isLabelColumn(col))
			return getResourceCellLabel(row, col);
		
		if (isAccountingCodeColumn(col))
			return getCurrentAccountingCode(assignmentForRow);

		if (isFundingSourceColumn(col))
			return getCurrentFundingSource(assignmentForRow);
		
		if (isCostColumn(col))
		{
			if (!isOdd(row))
				return "";
			
			try
			{
				final int BACK_ONE_ROW = 1;
				int correctedRow = getCorrectedRow(row - BACK_ONE_ROW);
				Assignment assignment = getAssignment(correctedRow);
				DateRangeEffortList effortList = getDateRangeEffortList(assignment);
				DateRange dateRange = dateRanges[convertColumn(col)];
				ProjectResource currentResource = getCurrentResource(assignmentForRow);

				return getCost(currentResource, effortList, dateRange);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		if (isUnitsColumn(col))
		{
			if (isOdd(row))
				return "";

			DateRangeEffortList effortList;
			try
			{
				effortList = getDateRangeEffortList(getAssignment(getCorrectedRow(row)));
				DateRange dateRange = dateRanges[convertColumn(col)];
				return getUnit(effortList, dateRange);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		if (isUnitsTotalColumn(col))
		{
			if (isOdd(row))
				return "";
			
			return getTotalUnits(getAssignment(getCorrectedRow(row)));
		}
		
		if (isCostTotalsColumn(col))
		{
			if (!isOdd(row))
				return "";

			return getTotalCost(getAssignment(getCorrectedRow(row)));
		}
		
		return new String("");
	}
	
	public void setValueAt(Object value, int row, int col)
	{
		if (isOdd(row))
			return;
		
		row = getCorrectedRow(row); 
		if (value == null)
		{
			EAM.logDebug("value in setValueAt is null");
			return;
		}
		BaseId assignmentId = getAssignmentForRow(row);
		if (isResourceColumn(col))
		{
			ProjectResource projectResource = (ProjectResource)value;
			setResource(projectResource, assignmentId);
			return;
		}
		
		if (isAccountingCodeColumn(col))
		{
			AccountingCode accountingCode = (AccountingCode)value;
			setAccountingCode(accountingCode, assignmentId);
			return;
		}
		
		if (isFundingSourceColumn(col))
		{
			FundingSource fSource = (FundingSource)value;
			setFundingSource(fSource, assignmentId);
			return;
		}
		
		if (isUnitsColumn(col))
			setUnitsForColumn(value, row, col);
	}

	public int convertColumn(int col)
	{
		return (col - (TOTAL_ROW_HEADER_COLUMN_COUNT)) / 2;
	}
	
	private String getResourceCellLabel(int row, int col)
	{
		ProjectResource resource = getCurrentResource(getAssignmentForRow(getCorrectedRow(row)));
		if (resource  == null)
			return "";
		
		if (col == getUnitsLabelColumnIndex() && !isOdd(row))
			return resource.getData(ProjectResource.TAG_COST_UNIT);
		
		if (col == getCostPerUnitLabelColumnIndex() && isOdd(row))
			return getCostPerUnit(resource);
			
		return "";
	}
	
	public Assignment getAssignment(int row)
	{
		return (Assignment)project.findObject(ObjectType.ASSIGNMENT, getAssignmentForRow(row));
	}
				
	public boolean isYearlyTotalColumn(int col)
	{
		if (col < TOTAL_ROW_HEADER_COLUMN_COUNT)
			return false;
		//TODO only dealing with QUARTERS,  this method has to be revised
		//if dealing with anything other than QUARTERS
		final int QUARTERLY_TOTAL_COUNT = 10;
		int convertedCol = col - (TOTAL_ROW_HEADER_COLUMN_COUNT - 1);
		
		if ((convertedCol + 1) % QUARTERLY_TOTAL_COUNT == 0)
			return true;
		if (convertedCol % QUARTERLY_TOTAL_COUNT == 0)
			return true;
		
		return false;
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

	public boolean doubleRowed()
	{
		return true;
	}	
	
	public int getUnitsLabelColumnIndex()
	{
		return UNITS_LABEL_COLUMN_INDEX;
	}
	
	protected boolean isLabelColumn(int col)
	{
		return 3 <= col && col < TOTAL_ROW_HEADER_COLUMN_COUNT;
	}
	
	public int getAccountingCodeColumnIndex()
	{
		return ACCOUNTING_CODE_COLUMN_INDEX;
	}
	
	public int getFundingSourceColumnIndex()
	{
		return FUNDING_SOURCE_COLUMN_INDEX;
	}
	
	
	private static final int FUNDING_SOURCE_COLUMN_INDEX = 1;
	private static final int ACCOUNTING_CODE_COLUMN_INDEX = 2;
	private static final int UNITS_LABEL_COLUMN_INDEX = 3;
	
}
