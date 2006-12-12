/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;
import org.martus.util.MultiCalendar;

public class BudgetTableModel extends AbstractTableModel
{
	public BudgetTableModel(Project projectToUse, IdList assignmentIdListToUse) throws Exception
	{
		project = projectToUse;
		assignmentIdList  = assignmentIdListToUse;
		
		totalsCalculator = new BudgetTotalsCalculator(project);
	
		setProjectDateRanges();
	}
	
	private void setProjectDateRanges() throws Exception
	{
		String startDate = project.getMetadata().getStartDate();
		projectStartDate = MultiCalendar.createFromGregorianYearMonthDay(2007, 1, 1);
		if (startDate.length() > 0 )
			projectStartDate = MultiCalendar.createFromIsoDateString(startDate);
		
		int year = projectStartDate.getGregorianYear();
		dateRanges = new DateRange[4];
		MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(year, 1, 1);
		MultiCalendar end = null;
		final int YEAR_QUARTER = 3;
		final int MINUS_A_DAY = -1;
		for (int i = 0; i < dateRanges.length; i++)
		{
			int endMonth  =  (i + 1) * YEAR_QUARTER;
			end = MultiCalendar.createFromGregorianYearMonthDay(year, endMonth, 1);
			
			dateRanges[i] = new DateRange(start, end);
			start = MultiCalendar.createFromGregorianYearMonthDay(end.getGregorianYear(), end.getGregorianMonth(), end.getGregorianDay());
			start.addDays(MINUS_A_DAY);
		}
	}

	public void setTask(Task taskToUse)
	{
		task = taskToUse;
		dataWasChanged();
	}
	
	public void dataWasChanged()
	{
		if(task == null)
			return;
		assignmentIdList = task.getAssignmentIdList();
		fireTableDataChanged();
	}
	
	public int getRowCount()
	{
		if (assignmentIdList != null)
			return (assignmentIdList.size() * 2) + 2;
		
		return 0;
	}
	
	public int getColumnCount()
	{
		return (dateRanges.length * 2) + EXTRA_COLUMN_COUNT;
	}
	
	public int getUnitTotalsColumnIndex()
	{
		return getColumnCount() - 2;	
	}
	
	private int getCostTotalsColumnIndex()
	{
		return getColumnCount() - 1;
	}
	
	public int getResourcesColumnIndex()
	{
		return 0;
	}

	public boolean isCellEditable(int row, int col) 
	{
		if (isUnitsTotalColumn(col))
			return false;
		
		if (isCostTotalsColumn(col))
			return false;
		
		if (isLabelColumn(col))
			return false;
		
		if (isTotalsRow(row))
			return false;
		
		if (isOdd(row))
			return false;
			
		return true;
	}
	
	public boolean isTotalsRow(int row)
	{
		if (row < (getRowCount() - 2))
			return false;
			
		return true;
	}

	public String getColumnName(int col)
	{
		if (isResourceColumn(col))
			return "Resources";
		
		if (isLabelColumn(col))
			return "";
		
		if (isUnitsTotalColumn(col))
			return "Unit Totals";
	
		if (isCostTotalsColumn(col))
			return "Cost Totals";
		
		if (isUnitsColumn(col))
			return dateRanges[covertToUnitsColumn(col)].getLabel();
		
		return "";
	}

	public Object getValueAt(int row, int col)
	{
		if (isResourceColumn(col))
			return getCurrentResource(row);
		
		if (isLabelColumn(col))
			return getResourceCellLabel(row, col);
		
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
		
		if (isUnitsColumn(col))
			setUnits(value, row, covertToUnitsColumn(col));
	}


	private boolean isLabelColumn(int col)
	{
		return 1 <= col && col <= STATIC_LABEL_COLUMN_COUNT;
	}
	
	private boolean isResourceColumn(int col)
	{
		return col == getResourcesColumnIndex();
	}

	private boolean isCostTotalsColumn(int col)
	{
		return col == getCostTotalsColumnIndex();
	}
	
	private boolean isUnitsTotalColumn(int col)
	{
		return col == getUnitTotalsColumnIndex();
	}
	
	private Object getCost(int row, int col)
	{
		try
		{
			if (!isOdd(row))
				return "";

			if (isTotalsRow(row))
				return Double.toString(getTotalCostForColumn(row, col));

			ProjectResource currentResource = getCurrentResource(row);
			if (currentResource == null)
				return "";

			final int BACK_ONE_ROW = 1;
			double units = new Double(getUnit(row - BACK_ONE_ROW, col)).doubleValue();
			double costPerUnit = currentResource.getCostPerUnit();
			return Double.toString(units * costPerUnit);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return "";
	}

	private double getTotalCostForColumn(int row, int col) throws Exception
	{
		if (getRowCount() <= 2)
			return 0.0;
		
		DateRange dateRange = dateRanges[covertToUnitsColumn(col)];
		double totalCostColumn = totalsCalculator.getTotalCost(assignmentIdList, dateRange);
		return totalCostColumn;
	}

	private boolean isUnitsColumn(int col)
	{
		if (col < (RESOURCE_COLUMN_COUNT + STATIC_LABEL_COLUMN_COUNT))
			return false;
		
		if (isOdd(col))
			return false;
		
		if (col  < (getColumnCount() - TOTALS_COLUMNS_COUNT ))
			return true;
		
		return false;
	}
	
	private boolean isCostColumn(int col)
	{
		if (col < (RESOURCE_COLUMN_COUNT + STATIC_LABEL_COLUMN_COUNT))
			return false;
		
		if (!isOdd(col))
			return false;
		
		if (col  < (getColumnCount() - TOTALS_COLUMNS_COUNT ))
			return true;
		
		return false;
	}

	private boolean isStaticLabelColum(int col)
	{
		return col == COST_UNIT_LABEL_COLUMN;
	}

	private int covertToUnitsColumn(int col)
	{
		return (col - (RESOURCE_COLUMN_COUNT + STATIC_LABEL_COLUMN_COUNT)) / 2;
	}
	
	private String getResourceCellLabel(int row, int col)
	{
		if (isTotalsRow(row))
			return "";
		
		if (isStaticLabelColum(col))
			return getStaticCellLabel(row, col); 
		
		ProjectResource resource = getCurrentResource(row);
		if (resource  == null)
			return "";
		
		if (col == 1)
			return resource.getData(ProjectResource.TAG_COST_PER_UNIT);
		
		if (col == 2)
			return resource.getData(ProjectResource.TAG_COST_UNIT);
			
		return "";
	}
	
	private String getStaticCellLabel(int row, int col)
	{
		if (isOdd(row))
			return "Cost";
		
		return "Units";
	}
	
	private String getTotalCost(int row)
	{
		if (isTotalsRow(row))
			return"";
		
		if (!isOdd(row))
			return "";
		
		try
		{
			DateRange combinedDateRange = getCombinedDateRange();
			Assignment assignment = getAssignment(getCorrectedRow(row));
			double totalCost = totalsCalculator.getTotalCost(assignment, combinedDateRange);
			return Double.toString(totalCost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		return "";
	}
	
	private Object getTotalUnits(int row)
	{
		if (isTotalsRow(row))
			return"";
		
		if (isOdd(row))
			return "";
		try
		{
			DateRange combinedDateRange = getCombinedDateRange();
			Assignment assignment = getAssignment(getCorrectedRow(row));
			double totalUnits = totalsCalculator.getTotalUnits(assignment, combinedDateRange);
			return Double.toString(totalUnits);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		return new String("");
	}

	private DateRange getCombinedDateRange() throws Exception
	{
		DateRange combinedDateRange = DateRange.combine(dateRanges[0], dateRanges[dateRanges.length - 1]);
		return combinedDateRange;
	}

	//FIXME budget code - dont return string just the value
	public String getUnit(int row, int col)
	{
		if (isOdd(row))
			return "";

		double units = 0.0;
		try
		{
			if (isTotalsRow(row))
				return Double.toString(getTotalColumnUnits(row, col));
			
			DateRangeEffortList effortList = getDateRangeEffortList(getCorrectedRow(row));
			units = effortList.getUnitsForDateRange(dateRanges[covertToUnitsColumn(col)]);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return Double.toString(units);
	}

	private double getTotalColumnUnits(int row, int col) throws Exception
	{
		if (getRowCount() <= 2)
			return 0.0;
		
		DateRange dateRange = dateRanges[covertToUnitsColumn(col)];
		double totalCostColumn = totalsCalculator.getTotalUnits(assignmentIdList, dateRange);
		return totalCostColumn;
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
		return (Assignment)project.findObject(ObjectType.ASSIGNMENT, getSelectedAssignment(row));
	}
	
	public BaseId getSelectedAssignment(int row)
	{
		return assignmentIdList.get(row);
	}
	
	public ProjectResource getCurrentResource(int row)
	{
		if (isTotalsRow(row))
			return null;
		
		BaseId assignmentId = getSelectedAssignment(getCorrectedRow(row));
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID);
		BaseId resourceId = new BaseId(stringId);
		
		ProjectResource resource = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId);
		
		return resource;
	}
	
	public int getCorrectedRow(int row)
	{
		return row /= 2;
	}

	boolean isOdd(int value)
	{
		return value % 2 != 0;
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
			
			BaseId  assignmentId = getSelectedAssignment(row);
			Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, resourceId.toString());
			project.executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
		
	}
	
	Project project;
	MultiCalendar projectStartDate;
	MultiCalendar projectEndDate;
	DateRange[] dateRanges;
	IdList assignmentIdList;
	Task task;
	BudgetTotalsCalculator totalsCalculator;
	
	private static final int COST_UNIT_LABEL_COLUMN = 3;
	private static final int RESOURCE_COLUMN_COUNT = 1;
	private static final int TOTALS_COLUMNS_COUNT = 2;
	private static final int STATIC_LABEL_COLUMN_COUNT = 3;
	private static final int EXTRA_COLUMN_COUNT = RESOURCE_COLUMN_COUNT + 
												  TOTALS_COLUMNS_COUNT + 
												  STATIC_LABEL_COLUMN_COUNT;	 
}
