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

import java.awt.Color;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.ColumnTagProvider;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;

public class WorkUnitsTableModel extends PlanningViewAbstractTreeTableSyncedTableModel implements ColumnTagProvider
{
	public WorkUnitsTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse, providerToUse);

		provider = providerToUse;
		restoreDateUnits();
	}
	
	public void restoreDateUnits() throws Exception
	{
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getUniqueTableModelIdentifier());
		Vector<DateUnit> dateUnitsToUse = tableSettings.getDateUnitList();
		if (dateUnitsToUse.isEmpty())
			dateUnitsToUse.add(new DateUnit());
		
		dateUnits = dateUnitsToUse;
		
		fireTableStructureChanged();
	}
	
	private ProjectCalendar getProjectCalendar() throws Exception
	{
		return getProject().getProjectCalendar();
	}

	@Override
	public String getColumnName(int col)
	{
		try
		{
			DateUnit dateUnit = getDateUnit(col);
			if (dateUnit.isBlank())
				return EAM.text("Total");
			
			String name = dateUnit.toString();

			if(dateUnit.isQuarter())
				return name.substring(4);

			if(dateUnit.isMonth())
				return "M " + dateUnit.getMonth();
			
			if(dateUnit.isDay())
				return Integer.toString(dateUnit.getDay());
			
			return name;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return EAM.text("(Error)");
		}
	}

	public DateUnit getDateUnit(int column)
	{
		return getDateUnits().get(column);
	}

	public int getColumnCount()
	{
		return getDateUnits().size();
	}
	
	public DateRange getDateRangeForColumn(int column) throws Exception
	{
		return getDateRange(column);
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
	
	@Override
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		if (getAssignment(row) != null)
			isAssignmentCellEditable(column);
		
		return false;
	}

	private boolean isAssignmentCellEditable(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		if (getProject().getMetadata().isBudgetTimePeriodYearly())
			return dateUnit.isYear();
		
		return dateUnit.isQuarter();
	}

	public Object getValueAt(int row, int column)
	{
		try	
		{
			BaseObject baseObject = getBaseObjectForRowColumn(row, column);
			DateRange dateRange = getDateRange(column);
			
			return baseObject.getWorkUnits(dateRange);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	
		return "";
	}

	@Override
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
			effort = new DateRangeEffort("", units, getDateRange(column));
	
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
		return getDateRangeEffort(getAssignment(row), getDateRange(column));
	}	
	
	@Override
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
	}

	@Override
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getProvider().getBaseObjectForRowColumn(row, column);
	}

	@Override
	public int getRowCount()
	{
		return getProvider().getRowCount();
	}
	
	public Assignment getAssignment(int row)
	{
		return (Assignment) getBaseObjectForRowColumn(row, 0);
	}
	
	public RowColumnBaseObjectProvider getProvider()
	{
		return provider;
	}

	private Vector<DateUnit> getDateUnits()
	{
		return dateUnits;
	}
	
	public Vector<DateUnit> getCopyOfDateUnits()
	{
		return new Vector(getDateUnits());
	}
	
	private DateRange getDateRange(int column) throws Exception
	{
		DateUnit dateUnit = getDateUnit(column);
		return getProjectCalendar().convertToDateRange(dateUnit);
	}

	public Color getCellBackgroundColor(int column)
	{
		return null;
	}

	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		return null;
	}
	
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_TABLE_MODEL_IDENTIFIER;
	}

	private Vector<DateUnit> dateUnits;
	private RowColumnBaseObjectProvider provider;
	private static final String UNIQUE_TABLE_MODEL_IDENTIFIER = "WorkUnitsTableModel";
}
