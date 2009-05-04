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
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objectdata.DateUnitListData;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.CodeList;
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
			
			return getDateRange(dateUnit).toString();
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
		try
		{
			if (getAssignment(row) != null)
				return isAssignmentCellEditable(row, column);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		return false;
	}

	private boolean isAssignmentCellEditable(int row, int column) throws Exception
	{
		DateRange dateRange = getDateRange(column);
		Assignment assignment = getAssignment(row);
		DateRangeEffort thisCellEffort = getDateRangeEffort(assignment, dateRange);
		if(thisCellEffort != null)
			return true;
		
		return (assignment.getWorkUnits(dateRange) == 0);
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
		if(getValueAt(row, column).equals(value))
			return;
		
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
		getProject().executeBeginTransaction();
		try
		{
			Assignment assignment = getAssignment(row);
			DateUnit dateUnit = getDateUnit(column);

			String valueAsString = value.toString().trim();
			if (valueAsString.equals(""))
			{
				clearUnits(assignment, dateUnit);
			}
			else
			{
				double units = Double.parseDouble(valueAsString);
				setUnits(assignment, dateUnit, units);
			}
			
			clearSuperDateUnitColumns(assignment, dateUnit);
			
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void setUnits(Assignment assignment, DateUnit dateUnit, double units) throws Exception
	{
		DateRangeEffort effort = getDateRangeEffort(assignment, getDateRange(dateUnit));
		if (effort == null)
			effort = new DateRangeEffort("", units, getDateRange(dateUnit));

		DateRangeEffortList effortList = assignment.getDateRangeEffortList();
		setUnits(assignment, effortList, effort, units);
	}

	private void clearSuperDateUnitColumns(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		DateUnit superDateUnit = new DateUnit(dateUnit.getDateUnitCode());
		while(!superDateUnit.isBlank())
		{
			superDateUnit = superDateUnit.getSuperDateUnit();
			DateRange thisDateRange = getDateRange(superDateUnit);
			DateRangeEffort dateRangeEffort = getDateRangeEffort(assignment, thisDateRange);
			if(dateRangeEffort == null)
				continue;
			
			clearUnits(assignment, superDateUnit);
		}
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

	public void clearUnits(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		DateRangeEffortList effortList = assignment.getDateRangeEffortList();
		effortList.remove(getDateRange(dateUnit));
		String newEffortListString = effortList.toString();
		if(newEffortListString.equals(assignment.getData(assignment.TAG_DATERANGE_EFFORTS)))
			return;
		
		Command command = new CommandSetObjectData(assignment.getType(), assignment.getId(), assignment.TAG_DATERANGE_EFFORTS, newEffortListString);
		getProject().executeCommand(command);
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
		return getDateRange(dateUnit);
	}

	private DateRange getDateRange(DateUnit dateUnit) throws Exception
	{
		return getProjectCalendar().convertToDateRange(dateUnit);
	}
	
	public boolean isDateUnitColumnExpanded(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		if (dateUnit == null)
			return false;	
		
		try
		{
			Vector<DateUnit> currentDateUnits = getCopyOfDateUnits();
			if (hasSubDateUnits(dateUnit))
				return currentDateUnits.containsAll(getSubDateUnits(dateUnit));
			
			return currentDateUnits.contains(dateUnit);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
			
		}
	}
	
	public boolean isDayColumn(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		if (dateUnit == null)
			return false;
		
		return dateUnit.isDay();
	}
	
	public void respondToExpandOrCollapseColumnEvent(int column) throws Exception
	{
		Vector<DateUnit> currentDateUnits = getCopyOfDateUnits();
		DateUnit dateUnit = getDateUnit(column);
		Vector<DateUnit> subDateUnits = getSubDateUnits(dateUnit);					
		if (currentDateUnits.containsAll(subDateUnits))
		{
			recursivleyCollapseDateUnitAndItsSubDateUnits(currentDateUnits, dateUnit);
		}
		else
		{
			int indexToInsertSubDateUnits = currentDateUnits.indexOf(dateUnit);
			currentDateUnits.addAll(indexToInsertSubDateUnits, subDateUnits);
		}
		
		saveColumnDateUnits(currentDateUnits);
	}
	
	private void recursivleyCollapseDateUnitAndItsSubDateUnits(Vector<DateUnit> currentDateUnits, DateUnit dateUnit) throws Exception
	{
		if (!hasSubDateUnits(dateUnit))
			return;
		
		Vector<DateUnit> subDateUnits = getSubDateUnits(dateUnit);
		currentDateUnits.removeAll(subDateUnits);
		for(DateUnit thisDateUnit : subDateUnits)
		{
			recursivleyCollapseDateUnitAndItsSubDateUnits(currentDateUnits, thisDateUnit);
		}
	}
	
	private void saveColumnDateUnits(Vector<DateUnit> currentDateUnits) throws Exception
	{	
		CodeList thisDateUnits = DateUnitListData.convertToCodeList(currentDateUnits);
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getUniqueTableModelIdentifier());
		CommandSetObjectData setDateUnitsCommand = tableSettings.createCommandToUpdateDateUnitList(thisDateUnits);
		getProject().executeCommand(setDateUnitsCommand);
	}

	private Vector<DateUnit> getSubDateUnits(DateUnit dateUnit)	throws Exception
	{
		return getProjectCalendar().getSubDateUnits(dateUnit);
	}
	
	private boolean hasSubDateUnits(DateUnit dateUnit) throws Exception
	{
		return getProjectCalendar().hasSubDateUnits(dateUnit);
	}
	
	public Color getCellBackgroundColor(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		return AppPreferences.getWorkUnitsBackgroundColor(dateUnit);
	}

	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		Object valueAt = getValueAt(row, column);
		if (valueAt == null)
			return new EmptyChoiceItem();
		
		return new TaglessChoiceItem(valueAt);
	}
	
	public void updateColumnsToShow() throws Exception
	{
		restoreDateUnits();
	}
	
	public boolean isWorkUnitColumn(int column)
	{
		return getDateUnit(column) != null;
	}
	
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_TABLE_MODEL_IDENTIFIER;
	}

	private Vector<DateUnit> dateUnits;
	private RowColumnBaseObjectProvider provider;
	private static final String UNIQUE_TABLE_MODEL_IDENTIFIER = "WorkUnitsTableModel";
}
