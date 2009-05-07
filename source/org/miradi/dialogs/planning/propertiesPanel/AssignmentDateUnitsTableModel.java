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
import org.miradi.questions.ColumnConfigurationQuestion;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.CodeList;
import org.miradi.utils.ColumnTagProvider;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;
import org.miradi.utils.OptionalDouble;

abstract public class AssignmentDateUnitsTableModel extends PlanningViewAbstractTreeTableSyncedTableModel implements ColumnTagProvider
{
	public AssignmentDateUnitsTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse, providerToUse);

		provider = providerToUse;
		restoreDateUnits();
	}
	
	public void restoreDateUnits() throws Exception
	{
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getUniqueTableModelIdentifier());
		Vector<DateUnit> dateUnitsToUse = tableSettings.getDateUnitList();
		
		if (!areAllDateUnitsValid(dateUnitsToUse))
			dateUnitsToUse.clear();
		
		if (dateUnitsToUse.isEmpty())
			dateUnitsToUse.add(new DateUnit());
		
		dateUnits = dateUnitsToUse;
		
		fireTableStructureChanged();
	}
	
	private boolean areAllDateUnitsValid(Vector<DateUnit> dateUnitsToUse)
	{
		for(DateUnit dateUnit : dateUnitsToUse)
		{
			if(!dateUnit.isValid())
				return false;
		}
		return true;
	}

	private ProjectCalendar getProjectCalendar() throws Exception
	{
		return getProject().getProjectCalendar();
	}
	
	@Override
	public String getColumnGroupCode(int modelColumn)
	{
		return ColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE;
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

	public boolean isColumnExpandable(int modelColumn)
	{
		try
		{
			return !isExpanded(modelColumn);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	public boolean isColumnCollapsable(int modelColumn)
	{
		try
		{
			return isExpanded(modelColumn);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
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
		dateRangeEffort = effortList.getDateRangeEffortForSpecificDateRange(dateRange);
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
				return isAssignmentCellEditable(getAssignment(row), getDateRange(column));
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		return false;
	}
	
	protected boolean isAssignmentCellEditable(Assignment assignment, DateRange dateRange) throws Exception
	{
		if (!isCorrectType(assignment))
			return false;
		
		DateRangeEffort thisCellEffort = getDateRangeEffort(assignment, dateRange);
		if(thisCellEffort != null)
			return true;
		
		return hasValue(assignment, dateRange);
	}

	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
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
	
	private void setUnits(Assignment assignment, DateRangeEffortList effortList, DateRangeEffort effort, double units) throws Exception
	{
		effort.setUnitQuantity(units);
		effortList.setDateRangeEffort(effort);
		String newEffortListString = effortList.toString();
		String data = assignment.getData(assignment.TAG_DATERANGE_EFFORTS);
		if(newEffortListString.equals(data))
			return;
		
		Command command = new CommandSetObjectData(assignment, Assignment.TAG_DATERANGE_EFFORTS, newEffortListString);
		getProject().executeCommand(command);
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
		BaseObject baseObjectForRowColumn = getBaseObjectForRowColumn(row, 0);
		if (Assignment.isAssignment(baseObjectForRowColumn))
			return (Assignment) baseObjectForRowColumn;
		
		return null;
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
	
	protected DateRange getDateRange(int column) throws Exception
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
		boolean isExpanded = isExpanded(column);
		DateUnit dateUnit = getDateUnit(column);
		Vector<DateUnit> visibleDateUnits = getCopyOfDateUnits();
		if (isExpanded)
		{
			recursivleyCollapseDateUnitAndItsSubDateUnits(visibleDateUnits, dateUnit);
		}
		else
		{
			int indexToInsertSubDateUnits = visibleDateUnits.indexOf(dateUnit);
			visibleDateUnits.addAll(indexToInsertSubDateUnits, getSubDateUnits(dateUnit));
		}
		
		saveColumnDateUnits(visibleDateUnits);
	}

	private boolean isExpanded(int column) throws Exception
	{
		Vector<DateUnit> visibleDateUnits = getCopyOfDateUnits();
		return visibleDateUnits.containsAll(getSubDateUnits(getDateUnit(column)));
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
	
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		OptionalDouble optionalDouble = getOptionalDoubleAt(row, column);
		if (optionalDouble.hasValue())
		{
			return new TaglessChoiceItem(optionalDouble);
		}
		
		return new EmptyChoiceItem();
	}
	
	private OptionalDouble getOptionalDoubleAt(int row, int column)
	{
		try	
		{
			BaseObject baseObject = getBaseObjectForRowColumn(row, column);
			DateRange dateRange = getDateRange(column);
			
			return getOptionalDoubleData(baseObject, dateRange);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	
		return new OptionalDouble();
	}

	public void updateColumnsToShow() throws Exception
	{
		restoreDateUnits();
	}
	
	public boolean isWorkUnitColumn(int column)
	{
		return getDateUnit(column) != null;
	}

	abstract public String getUniqueTableModelIdentifier();
	
	abstract public Color getCellBackgroundColor(int column);
	
	abstract protected OptionalDouble getOptionalDoubleData(BaseObject baseObject, DateRange dateRange) throws Exception;
	
	abstract protected boolean isCorrectType(Assignment assignment);

	abstract protected boolean hasValue(Assignment assignment, DateRange dateRange) throws Exception;
	
	private Vector<DateUnit> dateUnits;
	private RowColumnBaseObjectProvider provider;
}
