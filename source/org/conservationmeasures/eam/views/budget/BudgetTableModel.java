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
		setProjectDateRanges();
	}
	
	public boolean isCellEditable(int row, int col) 
	{
		if (col == getTotalsColumnIndex())
			return false;
		
		if (isLabelColumn(col))
			return false;
		
		if (isOdd(row))
			return false;
			
		return true;
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
			return assignmentIdList.size() * 2;
		
		return 0;
	}
	
	public int getColumnCount()
	{
		return (dateRanges.length * 2) + EXTRA_COLUMN_COUNT;
	}
	
	public int getTotalsColumnIndex()
	{
		return getColumnCount() - 1;	
	}
	
	public int getResourcesColumnIndex()
	{
		return 0;
	}

	public String getColumnName(int col)
	{
		if (col == getResourcesColumnIndex())
			return "Resources";
		
		if (isLabelColumn(col))
			return "";
		
		if (col == getTotalsColumnIndex())
			return "Totals";
		
		if (isUnitsColumn(col))
			return dateRanges[getUnitsColumn(col)].getLabel();
		
		return "";
	}

	private boolean isLabelColumn(int col)
	{
		return 1 <= col && col <= STATIC_LABEL_COLUMN_COUNT;
	}

	private boolean isUnitsColumn(int col)
	{
		return ! isOdd(col + RESOURCE_COLUMN_COUNT + STATIC_LABEL_COLUMN_COUNT);
	}
	
	public Object getValueAt(int row, int col)
	{
		
		if (isStaticLabelColum(col))
			return getStaticCellLabel(row, col); 
		
		if (isOdd(row))
			return new String("");
			
		row = getCorrectedRow(row);
		
		if (col == getResourcesColumnIndex())
			return getSelectedResource(row);
		
		if (isLabelColumn(col))
			return getResourceCellLabel(row, col);
		
		if (col == getTotalsColumnIndex())
			return getTotalUnits(row);
		
		if (isUnitsColumn(col))
			return getUnitsFor(row, getUnitsColumn(col));
		
		return new String();
	}

	
	private boolean isStaticLabelColum(int col)
	{
		if (col == COST_UNIT_LABEL_COLUMN)
			return true;
		
		return false;
	}

	private int getUnitsColumn(int col)
	{
		return (col - (RESOURCE_COLUMN_COUNT + STATIC_LABEL_COLUMN_COUNT)) / 2;
	}
	
	private String getResourceCellLabel(int row, int col)
	{
		ProjectResource resource = getSelectedResource(row);
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
		if (!isOdd(row))
			return "Cost";
		return "Units";
	}


	
	private Object getTotalUnits(int row)
	{
		double totalUnits = 0.0;
		try
		{
			DateRangeEffortList effortList = getDateRangeEffortList(row);
			totalUnits = effortList.getTotalUnitQuantity();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}

		return Double.toString(totalUnits);
	}

	public String getUnitsFor(int row, int timeIndex)
	{
		double units = 0;
		try
		{
			DateRangeEffortList effortList = getDateRangeEffortList(row);
			if (effortList.size() <= timeIndex)
				return Double.toString(units);
			
			units = effortList.get(timeIndex).getUnitQuantity();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return Double.toString(units);
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
			if (effortList.size() <= timeIndex)
				return null;
			
			dateRangeEffort = effortList.get(timeIndex);
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
	
	public ProjectResource getSelectedResource(int row)
	{
		BaseId assignmentId = getSelectedAssignment(row);
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID);
		BaseId resourceId = new BaseId(stringId);
		
		ProjectResource resource = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId);
		
		return resource;
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
		if (col == getResourcesColumnIndex())
		{
			setResource(value, row);
			return;
		}
		
		if (isUnitsColumn(col))
			setUnits(value, row, getUnitsColumn(col));
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
			DateRangeEffort dateRangeEffort = getDateRangeEffort(row, timeIndex);
			DateRangeEffortList dateRangeEffortList = getDateRangeEffortList(row);
			double units = Double.parseDouble(value.toString());
			
			//FIXME budget code - take out daterange
			if (dateRangeEffort == null)
				dateRangeEffort = new DateRangeEffort("", units, dateRanges[timeIndex]);
			
			dateRangeEffort.setUnitQuantity(units);
			dateRangeEffortList.setDateRangeEffort(dateRangeEffort);
			Command command = new CommandSetObjectData(assignment.getType(), assignment.getId(), assignment.TAG_DATERANGE_EFFORTS, dateRangeEffortList.toString());
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
	
	
	private static final int COST_UNIT_LABEL_COLUMN = 3;
	private static final int RESOURCE_COLUMN_COUNT = 1;
	private static final int TOTALS_COLUMNS_COUNT = 1;
	private static final int STATIC_LABEL_COLUMN_COUNT = 3;
	private static final int EXTRA_COLUMN_COUNT = RESOURCE_COLUMN_COUNT + 
												  TOTALS_COLUMNS_COUNT + 
												  STATIC_LABEL_COLUMN_COUNT;	 
}
