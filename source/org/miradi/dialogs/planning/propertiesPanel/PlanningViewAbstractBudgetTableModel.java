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
package org.miradi.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.utils.ColumnTagProvider;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;

abstract public class PlanningViewAbstractBudgetTableModel extends PlanningViewAbstractAssignmentTableModel implements ColumnTagProvider
{
	public PlanningViewAbstractBudgetTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		rebuildDateRanges();
		decimalFormatter = getProject().getDecimalFormatter();
	}
	
	@Override
	public void dataWasChanged() throws Exception
	{
		rebuildDateRanges();
		super.dataWasChanged();
		
	}

	private void rebuildDateRanges() throws Exception
	{
		dateRanges = getProjectCalendar().getQuarterlyDateRanges();
	}

	private ProjectCalendar getProjectCalendar() throws Exception
	{
		return getProject().getProjectCalendar();
	}

	public String getColumnName(int col)
	{
		try
		{
			return getProjectCalendar().getDateRangeName(dateRanges[col]);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return EAM.text("(Error)");
		}
	}

	public int getColumnCount()
	{
		return dateRanges.length;
	}
	
	public DateRange getDateRangeForColumn(int column)
	{
		return dateRanges[column];
	}

	public String getUnit(DateRangeEffortList effortList, DateRange dateRange) throws Exception
	{
		double units = effortList.getTotalUnitQuantity(dateRange);
		return decimalFormatter.format(units);
	}

	public ProjectResource getCurrentResource(Assignment assignment)
	{
		ORef resourceRef = assignment.getResourceRef();
		ProjectResource resource = (ProjectResource) getProject().findObject(resourceRef);
		
		return resource;
	}
	
	protected Object getUnits(int row, int column) throws Exception
	{
		DateRangeEffortList effortList = getDateRangeEffortList(row);
		DateRange dateRange = dateRanges[column];
		
		return getUnit(effortList, dateRange);
	}
	
	protected DateRangeEffortList getDateRangeEffortList(int row) throws Exception
	{
		return getAssignment(row).getDateRangeEffortList();
	}
		
	protected DateRangeEffort getDateRangeEffort(Assignment assignment, DateRange dateRange) throws Exception
	{
		DateRangeEffort dateRangeEffort = null;
		DateRangeEffortList effortList = assignment.getDateRangeEffortList();
		dateRangeEffort = effortList.getEffortForDateRange(dateRange);
		return dateRangeEffort;
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
	
	public boolean isCellEditable(int row, int column)
	{
		DateRange dateRange = getDateRangeForColumn(column);
		ProjectCalendar projectCalendar = getProject().getProjectCalendar();
		return projectCalendar.isDateRangeEditable(dateRange);
	}

	public Object getValueAt(int row, int column)
	{
		try
		{
			return getUnits(row, column);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	
		return "";
	}

	public void setValueAt(Object value, int row, int column)
	{
		try
		{
			setUnitsForColumn(value, row, column);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void setUnitsForColumn(Object value, int row, int column) throws Exception
	{
		DateRangeEffort effort = getDateRangeEffort(row, column);
		double units = 0;
		if (effort == null)
			effort = new DateRangeEffort("", units, dateRanges[column]);
	
		String valueAsString = value.toString().trim();
		if (! valueAsString.equals(""))
			units = Double.parseDouble(valueAsString);
	
		Assignment assignment = getAssignment(row);
		DateRangeEffortList effortList = getDateRangeEffortList(row);
		setUnits(assignment, effortList, effort, units);
	}

	public void setUnits(Assignment assignment, DateRangeEffortList effortList, DateRangeEffort effort, double units) throws Exception
	{
		effort.setUnitQuantity(units);
		effortList.setDateRangeEffort(effort);
		String newEffortListString = effortList.toString();
		if(newEffortListString.equals(assignment.getData(assignment.TAG_DATERANGE_EFFORTS)))
			return;
		
		Command command = new CommandSetObjectData(assignment.getType(), assignment.getId(), assignment.TAG_DATERANGE_EFFORTS, newEffortListString);
		getProject().executeCommand(command);
	}

	private DateRangeEffort getDateRangeEffort(int row, int column)	throws Exception
	{
		return getDateRangeEffort(getAssignment(row), dateRanges[column]);
	}

	protected DateRange[] dateRanges;
	protected DecimalFormat decimalFormatter;
}
