/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
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
		//TODO remove comment when done - coverted
		if (isResourceColumn(col))
			return getCurrentResource(getAssignmentForRow(getCorrectedRow(row)));
		
		if (isLabelColumn(col))
			return getResourceCellLabel(row, col);
		
		//TODO remove comment when done - coverted
		if (isAccountingCodeColumn(col))
			return getCurrentAccountingCode(getAssignmentForRow(getCorrectedRow(row)));

		//TODO remove comment when done - coverted
		if (isFundingSourceColumn(col))
			return getCurrentFundingSource(getAssignmentForRow(getCorrectedRow(row)));
		
		//TODO remove comment when done - coverted
		if (isCostColumn(col))
		{
			if (!isOdd(row))
				return "";
			
			try
			{
				final int BACK_ONE_ROW = 1;
				DateRangeEffortList effortList = getDateRangeEffortList(getCorrectedRow(row - BACK_ONE_ROW));
				DateRange dateRange = dateRanges[covertToUnitsColumn(col)];
				ProjectResource currentResource = getCurrentResource(getAssignmentForRow(getCorrectedRow(row)));

				return getCost(currentResource, effortList, dateRange);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		//TODO remove comment when done - coverted
		if (isUnitsColumn(col))
		{
			if (isOdd(row))
				return "";

			DateRangeEffortList effortList;
			try
			{
				effortList = getDateRangeEffortList(getCorrectedRow(row));
				DateRange dateRange = dateRanges[covertToUnitsColumn(col)];
				return getUnit(effortList, dateRange);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		//TODO remove comment when done - coverted			
		if (isUnitsTotalColumn(col))
		{
			if (isOdd(row))
				return "";
			
			return getTotalUnits(getAssignment(getCorrectedRow(row)));
		}
		
		//TODO remove comment when done - coverted
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
		if (isResourceColumn(col))
		{
			setResource(value, row);
			return;
		}
		
		if (isAccountingCodeColumn(col))
		{
			AccountingCode aCode = (AccountingCode)value;
			BaseId  assignmentId = getAssignmentForRow(row);

			setAccountingCode(aCode, assignmentId);
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

	private int covertToUnitsColumn(int col)
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
	
	private String getCostPerUnit(ProjectResource resource)
	{
		String raw = resource.getData(ProjectResource.TAG_COST_PER_UNIT);
		double costPerUnit = convertStringToDouble(raw);
		
		return currencyFormatter.format(costPerUnit);
	}

	private String getTotalCost(Assignment assignment)
	{		
		try
		{
			DateRange combinedDateRange = getCombinedDateRange();
			double totalCost = totalsCalculator.getTotalCost(assignment, combinedDateRange);
			return currencyFormatter.format(totalCost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		return "";
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
	
	public Object getCurrentAccountingCode(BaseId assignmentId)
	{
		//BaseId assignmentId = getAssignmentForRow(getCorrectedRow(row));
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ACCOUNTING_CODE);
		BaseId accountingId = new BaseId(stringId);
		
		AccountingCode accountingCode = (AccountingCode)project.findObject(ObjectType.ACCOUNTING_CODE, accountingId);
		return accountingCode;
	}
	
	public Object getCurrentFundingSource(BaseId assignmentId)
	{
		//BaseId assignmentId = getAssignmentForRow(getCorrectedRow(row));
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE);
		BaseId fundingId = new BaseId(stringId);
		
		FundingSource fundingSource = (FundingSource)project.findObject(ObjectType.FUNDING_SOURCE, fundingId);
		return fundingSource;
	}
	
	public ProjectResource getCurrentResource(BaseId assignmentId)
	{
		//BaseId assignmentId = getAssignmentForRow(getCorrectedRow(row));
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
			
			double units = 0;
			String valueAsString = value.toString().trim();
			if (! valueAsString.equals(""))
				units = Double.parseDouble(valueAsString);
			
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
			BaseId resourceId = projectResource.getId();
			
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
			BaseId fSourceId = fSource.getId();
			
			BaseId  assignmentId = getAssignmentForRow(row);
			Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE, fSourceId.toString());
			project.executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
	}
	
	public void setAccountingCode(AccountingCode aCode, BaseId  assignmentId)
	{
		try
		{
			BaseId aCodeId = aCode.getId();
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
	
	Project project;
	Task task;
	IdList assignmentIdList;
	
	private static final int FUNDING_SOURCE_COLUMN_INDEX = 1;
	private static final int ACCOUNTING_CODE_COLUMN_INDEX = 2;
	private static final int UNITS_LABEL_COLUMN_INDEX = 3;
	
}
