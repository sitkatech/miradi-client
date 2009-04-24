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


import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objects.Assignment;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;

public class PlanningViewWorkPlanTableModel extends PlanningViewAbstractBudgetTableModel
{
	public PlanningViewWorkPlanTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
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
	
	private DateRangeEffort getDateRangeEffort(int row, int column) throws Exception
	{
		return getDateRangeEffort(getAssignment(row), dateRanges[column]);
	}
}
