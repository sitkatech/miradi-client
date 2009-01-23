/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
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
	
	protected DateRange[] dateRanges;
	protected DecimalFormat decimalFormatter;
}
