/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.text.DecimalFormat;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.AccountingCodeId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FundingSourceId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;

public class BudgetTableModel extends AbstractBudgetTableModel
{
	public BudgetTableModel(Project projectToUse, IdList assignmentIdListToUse) throws Exception
	{
		project = projectToUse;
		assignmentIdList  = assignmentIdListToUse;
		totalsCalculator = new BudgetTotalsCalculator(project);
		dateRanges = new ProjectCalendar(project).getQuarterlyDateDanges();
		currencyFormatter = project.getCurrencyFormatter();
		decimalFormatter = project.getDecimalFormatter();
	}
	
	public BaseId getAssignmentForRow(int row)
	{
		return assignmentIdList.get(row);
	}

	public void setTask(Task taskToUse)
	{
		task = taskToUse;
		dataWasChanged();
	}
	
	public void dataWasChanged()
	{
		if(task == null)
			assignmentIdList = new IdList();
		else
			assignmentIdList = task.getAssignmentIdList();
		
		fireTableDataChanged();
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
			return dateRanges[covertToUnitsColumn(col)].toString();
		
		return "";
	}

	public Object getValueAt(int row, int col)
	{
		if (isResourceColumn(col))
			return getCurrentResource(row);
		
		if (isLabelColumn(col))
			return getResourceCellLabel(row, col);
		
		if (isAccountingCodeColumn(col))
			return getCurrentAccountingCode(row);
		
		if (isFundingSourceColumn(col))
			return getCurrentFundingSource(row);
		
		if (isCostColumn(col))
			return getCost(row, col);
		
		if (isUnitsColumn(col))
			return getUnit(row, col);
			
		if (isUnitsTotalColumn(col))
			return getTotalUnits(row);
		
		if (isCostTotalsColumn(col))
			return getTotalCost(row);
		
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
		if (isResourceColumn(col))
		{
			setResource(value, row);
			return;
		}
		
		if (isAccountingCodeColumn(col))
		{
			setAccountingCode(value, row);
			return;
		}
		
		if (isFundingSourceColumn(col))
		{
			setFundingSource(value, row);
			return;
		}
		
		if (isUnitsColumn(col))
			setUnits(value, row, covertToUnitsColumn(col));
	}

	private Object getCost(int row, int col)
	{
		try
		{
			if (!isOdd(row))
				return "";

			ProjectResource currentResource = getCurrentResource(row);
			if (currentResource == null)
				return "";

			final int BACK_ONE_ROW = 1;
			double units = new Double(getUnit(row - BACK_ONE_ROW, col)).doubleValue();
			double costPerUnit = currentResource.getCostPerUnit();
			return currencyFormatter.format(units * costPerUnit);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return "";
	}

	private int covertToUnitsColumn(int col)
	{
		return (col - (TOTAL_ROW_HEADER_COLUMN_COUNT)) / 2;
	}
	
	private String getResourceCellLabel(int row, int col)
	{
		if (isStaticLabelColum(col))
			return getStaticCellLabel(row, col); 

		ProjectResource resource = getCurrentResource(row);
		if (resource  == null)
			return "";
		
		if (col == getUnitsLabelColumnIndex() && !isOdd(row))
			return resource.getData(ProjectResource.TAG_COST_UNIT);
		
		if (col == getCostPerUnitLabelColumnIndex() && isOdd(row))
			return getCostPerUnit(resource);
			
		return "";
	}
	
	private String getCostPerUnit(ProjectResource resource)
	{
		String raw = resource.getData(ProjectResource.TAG_COST_PER_UNIT);
		double costPerUnit = new Double(raw).doubleValue();
		
		return currencyFormatter.format(costPerUnit);
	}

	private String getStaticCellLabel(int row, int col)
	{
		if (isOdd(row))
			return "Cost";
		
		return "Units";
	}
	
	private String getTotalCost(int row)
	{
		if (!isOdd(row))
			return "";
		
		try
		{
			DateRange combinedDateRange = getCombinedDateRange();
			Assignment assignment = getAssignment(getCorrectedRow(row));
			double totalCost = totalsCalculator.getTotalCost(assignment, combinedDateRange);
			return currencyFormatter.format(totalCost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		return "";
	}
	
	private Object getTotalUnits(int row)
	{
		if (isOdd(row))
			return "";
		try
		{
			DateRange combinedDateRange = getCombinedDateRange();
			Assignment assignment = getAssignment(getCorrectedRow(row));
			double totalUnits = totalsCalculator.getTotalUnits(assignment, combinedDateRange);
			return decimalFormatter.format(totalUnits);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		return new String("");
	}

	private DateRange getCombinedDateRange() throws Exception
	{
		return DateRange.combine(dateRanges[0], dateRanges[dateRanges.length - 1]);
	}

	//FIXME budget code - dont return string just the value
	public String getUnit(int row, int col)
	{
		if (isOdd(row))
			return "";

		double units = 0.0;
		try
		{
			DateRangeEffortList effortList = getDateRangeEffortList(getCorrectedRow(row));
			units = effortList.getTotalUnitQuantity(dateRanges[covertToUnitsColumn(col)]);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return decimalFormatter.format(units);
	}

	private DateRangeEffortList getDateRangeEffortList(int row) throws Exception
	{
		Assignment assignment = getAssignment(row);
		String dREffortListAsString = assignment.getData(Assignment.TAG_DATERANGE_EFFORTS);
		DateRangeEffortList dREffortList = new DateRangeEffortList(dREffortListAsString);
		return dREffortList;
	}
	
	public DateRangeEffort getDateRangeEffort(int row, int timeIndex)
	{
		DateRangeEffort dateRangeEffort = null;
		try
		{
			DateRangeEffortList effortList = getDateRangeEffortList(row);
			DateRange dateRange = dateRanges[timeIndex];
			dateRangeEffort = effortList.getEffortForDateRange(dateRange);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return dateRangeEffort;
	}
	
	public Assignment getAssignment(int row)
	{
		return (Assignment)project.findObject(ObjectType.ASSIGNMENT, getAssignmentForRow(row));
	}
	
	public Object getCurrentAccountingCode(int row)
	{
		BaseId assignmentId = getAssignmentForRow(getCorrectedRow(row));
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ACCOUNTING_CODE);
		BaseId accountingId = new BaseId(stringId);
		
		AccountingCode accountingCode = (AccountingCode)project.findObject(ObjectType.ACCOUNTING_CODE, accountingId);
		return accountingCode;
	}
	
	public Object getCurrentFundingSource(int row)
	{
		BaseId assignmentId = getAssignmentForRow(getCorrectedRow(row));
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE);
		BaseId fundingId = new BaseId(stringId);
		
		FundingSource fundingSource = (FundingSource)project.findObject(ObjectType.FUNDING_SOURCE, fundingId);
		return fundingSource;
	}
	
	public ProjectResource getCurrentResource(int row)
	{
		BaseId assignmentId = getAssignmentForRow(getCorrectedRow(row));
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID);
		BaseId resourceId = new BaseId(stringId);
		
		ProjectResource resource = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId);
		
		return resource;
	}
	
	private void setUnits(Object value, int row, int timeIndex)
	{
		try
		{
			Assignment assignment = getAssignment(row);
			DateRangeEffort effort = getDateRangeEffort(row, timeIndex);
			DateRangeEffortList effortList = getDateRangeEffortList(row);
			double units = Double.parseDouble(value.toString());
			
			//FIXME budget code - take out daterange
			if (effort == null)
				effort = new DateRangeEffort("", units, dateRanges[timeIndex]);
			
			effort.setUnitQuantity(units);
			effortList.setDateRangeEffort(effort);
			Command command = new CommandSetObjectData(assignment.getType(), assignment.getId(), assignment.TAG_DATERANGE_EFFORTS, effortList.toString());
			project.executeCommand(command);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void setResource(Object value, int row)
	{
		try
		{
			ProjectResource projectResource = (ProjectResource)value;
			ProjectResourceId resourceId = (ProjectResourceId)(projectResource).getId();
			
			BaseId  assignmentId = getAssignmentForRow(row);
			Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, resourceId.toString());
			project.executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
		
	}
	
	public void setFundingSource(Object value, int row)
	{
		try
		{
			FundingSource fSource = (FundingSource)value;
			FundingSourceId fSourceId = (FundingSourceId)(fSource).getId();
			
			BaseId  assignmentId = getAssignmentForRow(row);
			Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE, fSourceId.toString());
			project.executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
	}
	
	public void setAccountingCode(Object value, int row)
	{
		try
		{
			AccountingCode aCode = (AccountingCode)value;
			AccountingCodeId aCodeId = (AccountingCodeId)(aCode).getId();
			
			BaseId  assignmentId = getAssignmentForRow(row);
			Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ACCOUNTING_CODE, aCodeId.toString());
			project.executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
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
	
	protected boolean isFundingSourceColumn(int col)
	{
		return col == getFundingSourceColumnIndex();
	}

	protected boolean isAccountingCodeColumn(int col)
	{
		return col == getAccountingCodeColumnIndex();
	}

	
	
	DecimalFormat currencyFormatter;
	DecimalFormat decimalFormatter;
	
	Project project;
	DateRange[] dateRanges;
	
	Task task;
	BudgetTotalsCalculator totalsCalculator;
	IdList assignmentIdList;
	
	private static final int FUNDING_SOURCE_COLUMN_INDEX = 1;
	private static final int ACCOUNTING_CODE_COLUMN_INDEX = 2;
	private static final int UNITS_LABEL_COLUMN_INDEX = 3;
	
}
