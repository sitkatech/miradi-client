/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import java.text.DecimalFormat;

import javax.swing.table.AbstractTableModel;

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

abstract public class AbstractBudgetTableModel extends AbstractTableModel
{
	
	public AbstractBudgetTableModel(Project projectToUse, IdList assignmentIdListToUse) throws Exception
	{
		project = projectToUse;
		assignmentIdList  = assignmentIdListToUse;
		totalsCalculator = new BudgetTotalsCalculator(project);
		dateRanges = new ProjectCalendar(project).getQuarterlyDateDanges();
		currencyFormatter = project.getCurrencyFormatter();
		decimalFormatter = project.getDecimalFormatter();
		assignmentIdList = new IdList();
	}
	
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
		
	public boolean isResourceColumn(int col)
	{
		return col == getResourcesColumnIndex();
	}

	public boolean isCostTotalsColumn(int col)
	{
		return col == getCostTotalsColumnIndex();
	}

	public boolean isUnitsTotalColumn(int col)
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

	public boolean isUnitsLabelColumn(int col)
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
	
	public Object getCurrentFundingSource(BaseId assignmentId)
	{
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE);
		BaseId fundingId = new BaseId(stringId);
		
		FundingSource fundingSource = (FundingSource)project.findObject(ObjectType.FUNDING_SOURCE, fundingId);
		return fundingSource;
	}
	
	public ProjectResource getCurrentResource(BaseId assignmentId)
	{
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID);
		BaseId resourceId = new BaseId(stringId);
		
		ProjectResource resource = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId);
		
		return resource;
	}
	
	public Object getCurrentAccountingCode(BaseId assignmentId)
	{
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ACCOUNTING_CODE);
		BaseId accountingId = new BaseId(stringId);
		
		AccountingCode accountingCode = (AccountingCode)project.findObject(ObjectType.ACCOUNTING_CODE, accountingId);
		return accountingCode;
	}
	
	public String getTotalCost(Assignment assignment)
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
	
	public String getCostPerUnit(ProjectResource resource)
	{
		String raw = resource.getData(ProjectResource.TAG_COST_PER_UNIT);
		double costPerUnit = convertStringToDouble(raw);
		
		return currencyFormatter.format(costPerUnit);
	}
	
	public void setFundingSource(FundingSource fSource, BaseId  assignmentId)
	{
		try
		{
			BaseId fSourceId = fSource.getId();
			Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_FUNDING_SOURCE, fSourceId.toString());
			project.executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
	}
	
	public void setResource(ProjectResource projectResource, BaseId  assignmentId)
	{
		try
		{
			BaseId resourceId = projectResource.getId();
			Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, resourceId.toString());
			project.executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
	}
	
	public void setUnits(Assignment assignment, DateRangeEffortList effortList, DateRangeEffort effort, double units) throws Exception
	{
		effort.setUnitQuantity(units);
		effortList.setDateRangeEffort(effort);
		Command command = new CommandSetObjectData(assignment.getType(), assignment.getId(), assignment.TAG_DATERANGE_EFFORTS, effortList.toString());
		project.executeCommand(command);
	}
	
	public DateRangeEffortList getDateRangeEffortList(Assignment assignment) throws Exception
	{
		String dREffortListAsString = assignment.getData(Assignment.TAG_DATERANGE_EFFORTS);
		DateRangeEffortList dREffortList = new DateRangeEffortList(dREffortListAsString);
		return dREffortList;
	}
	
	public DateRangeEffort getDateRangeEffort(Assignment assignment, DateRange dateRange) throws Exception
	{
		DateRangeEffort dateRangeEffort = null;
		DateRangeEffortList effortList = getDateRangeEffortList(assignment);
		dateRangeEffort = effortList.getEffortForDateRange(dateRange);
		return dateRangeEffort;
	}
	
	public boolean isAccountingCodeColumn(int col)
	{
		return col == getAccountingCodeColumnIndex();
	}

	public boolean isFundingSourceColumn(int col)
	{
		return col == getFundingSourceColumnIndex();
	}
	
	public void setTask(Task taskToUse)
	{
		if (isAlreadyCurrentTask(taskToUse))
			return;
			
		task = taskToUse;
		dataWasChanged();
	}
	
	public void dataWasChanged()
	{
		if (task != null)
			possiblyFireTableDataChangedDueToNewTask();
		else
			possiblyFireTableDataChanged();	
	}
	
	protected boolean isAlreadyCurrentTask(Task taskToUse)
	{
		return task != null && taskToUse != null && task.getId().equals(taskToUse.getId());
	}
	
	protected boolean isAlreadyCurrentAssignmentIdList()
	{
		return assignmentIdList.equals(task.getAssignmentIdList());
	}

	private void possiblyFireTableDataChangedDueToNewTask()
	{
		if (isAlreadyCurrentAssignmentIdList())
			return;
		
		assignmentIdList = task.getAssignmentIdList();
		fireTableDataChanged();
	}

	private void possiblyFireTableDataChanged()
	{
		if (assignmentIdList.size() == 0)
			return;
		
		assignmentIdList = new IdList();
		fireTableDataChanged();
	}
	
	abstract public boolean isYearlyTotalColumn(int col);
	
	abstract public BaseId getAssignmentForRow(int row);
	
	abstract public int getUnitsLabelColumnIndex();
	
	abstract public boolean doubleRowed();
	
	abstract protected boolean isUnitsColumn(int col);
	
	abstract protected boolean isLabelColumn(int col);

	abstract public int getAccountingCodeColumnIndex();
	
	abstract public int getFundingSourceColumnIndex();
	
	//abstract public void setTask(Task taskToUse);
	
//	abstract public void dataWasChanged();
	
	private static final int RESOURCES_COLUMN_INDEX = 0;
	private static final int COST_PER_UNIT_COLUMN_INDEX = 4;	
	private static final int UNITS_AND_COST_LABEL_COLUMN_INDEX = 5;
	
	static final int TOTAL_ROW_HEADER_COLUMN_COUNT = 5;
	
	static final int TOTALS_COLUMN_COUNT = 2;
	
	DecimalFormat currencyFormatter;
	DecimalFormat decimalFormatter;
	DateRange[] dateRanges;
	BudgetTotalsCalculator totalsCalculator;
	Project project;
	Task task;
	IdList assignmentIdList;
}
