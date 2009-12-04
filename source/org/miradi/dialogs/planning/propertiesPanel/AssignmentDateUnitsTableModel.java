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
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objectdata.DateUnitListData;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.TableSettings;
import org.miradi.objects.Task;
import org.miradi.project.CurrencyFormat;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.project.ProjectTotalCalculator;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.CodeList;
import org.miradi.utils.ColumnTagProvider;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.OptionalDouble;

abstract public class AssignmentDateUnitsTableModel extends PlanningViewAbstractTreeTableSyncedTableModel implements ColumnTagProvider
{
	public AssignmentDateUnitsTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, providerToUse);

		provider = providerToUse;
		resourceRefsFilter = new ORefSet();
		treeModelIdentifierAsTag = treeModelIdentifierAsTagToUse;
		currencyFormatter = getProject().getCurrencyFormatterWithCommas();
		
		restoreDateUnits();
	}
	
	//TODO Should not be using TableSettings in this model.  TableSettings should be found outside in the table and
	//should be calling the model's restore passing in the dateUnits.
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
	public String getColumnName(int col)
	{
		try
		{
			DateUnit dateUnit = getDateUnit(col);
			return  getProject().getProjectCalendar().getShortDateUnitString(dateUnit);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return EAM.text("(Error)");
		}
	}

	@Override
	public boolean isColumnExpandable(int modelColumn)
	{
		try
		{
			if(isDayColumn(modelColumn))
				return false;
		
			return !isExpanded(modelColumn);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	@Override
	public boolean isColumnCollapsable(int modelColumn)
	{
		try
		{
			if(isDayColumn(modelColumn))
				return false;
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
	
	private DateUnitEffort getDateUnitEffort(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		DateUnitEffort dateUnitEffort = null;
		DateUnitEffortList effortList = assignment.getDateUnitEffortList();
		dateUnitEffort = effortList.getDateUnitEffortForSpecificDateUnit(dateUnit);
		
		return dateUnitEffort;
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		try
		{
			BaseObject baseObjectForRow = getBaseObjectForRow(row);
			if (!isOrCanReferToAssignments(baseObjectForRow.getRef()))
				return false;
			
			if (isSharedTask(baseObjectForRow))
				return false;

			if (!isEditableModel())
				return false;
		
			if (getAssignment(row) != null)
				return isAssignmentCellEditable(getAssignment(row), getDateUnit(column));
			
			if (hasConflictingValue(baseObjectForRow, getDateUnit(column)))
				return false;
			
			ORefList assignmentRefs = baseObjectForRow.getRefList(getAssignmentsTag());
			if (assignmentRefs.size() >  1)
				return canEditMultipleAssignments(baseObjectForRow, getDateUnit(column));
			
			if (assignmentRefs.size() == 1)
				return isAssignmentCellEditable(getSingleAssignmentForBaseObject(baseObjectForRow), getDateUnit(column));
			
			return assignmentRefs.isEmpty();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	private boolean isSharedTask(BaseObject baseObjectForRow)
	{
		if (!Task.is(baseObjectForRow))
			return false;
		
		Task task = (Task) baseObjectForRow;
		return task.isPartOfASharedTaskTree();
	}

	protected boolean canEditMultipleAssignments(BaseObject baseObjectForRow, DateUnit dateUnit) throws Exception
	{
		return false;
	}
	
	private boolean hasConflictingValue(BaseObject baseObjectForRow, DateUnit dateUnit) throws Exception
	{
		if (baseObjectForRow.getSubTaskRefs().size() == 0)
			return false;
		
		ORefList subTaskRefs = baseObjectForRow.getSubTaskRefs();
		TimePeriodCostsMap timePeriodCostsMap = baseObjectForRow.getTotalTimePeriodCostsMapForSubTasks(subTaskRefs, getAssignmentsTag());
		TimePeriodCosts timePeriodCosts = timePeriodCostsMap.calculateTimePeriodCosts(dateUnit);
		OptionalDouble totalCost = timePeriodCosts.calculateTotalCost(getProject());

		return totalCost.hasValue();
	}

	private Assignment getSingleAssignmentForBaseObject(BaseObject baseObjectForRow) throws Exception
	{
		ORefList assignmentRefsForRowObject = baseObjectForRow.getRefList(getAssignmentsTag());
		return Assignment.findAssignment(getProject(), assignmentRefsForRowObject.get(0));
	}

	private static boolean isOrCanReferToAssignments(ORef ref)
	{
		if (Assignment.isAssignment(ref))
			return true;
		
		return canReferToAssignments(ref);
	}

	public static boolean canReferToAssignments(ORef ref)
	{
		if (Indicator.is(ref))
			return true;
		
		if (Strategy.is(ref))
			return true;
		
		if (Task.is(ref))
			return true;
		
		return false;
	}

	private boolean isAssignmentCellEditable(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		if (!isAssignmentForModel(assignment))
			return false;
		
		return isHorizontallyEditable(assignment, dateUnit);
	}

	protected boolean isHorizontallyEditable(ORefList assignmentRefs, DateUnit dateUnit) throws Exception
	{
		for (int index = 0; index < assignmentRefs.size(); ++index)
		{
			Assignment assignment = Assignment.findAssignment(getProject(), assignmentRefs.get(index));
			if (!isHorizontallyEditable(assignment, dateUnit))
				return false;
		}
		
		return true;
	}
	
	private boolean isHorizontallyEditable(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		DateUnitEffort thisCellEffort = getDateUnitEffort(assignment, dateUnit);
		if(thisCellEffort != null)
			return true;
		
		return !getOptionalDoubleData(assignment, dateUnit).hasValue();
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

	private void setUnitsForColumn(Object value, int row, int column) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			Vector<Assignment> assignments = getAssignmentsToEdit(row);
			setAssignmentValues(assignments, column, divideValue(value, assignments.size()));	
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void setAssignmentValues(Vector<Assignment> assignments, int column, Object dividedValue) throws Exception
	{
		for (int index = 0; index < assignments.size(); ++index)
		{
			setAssignmentValue(dividedValue, column, assignments.get(index));
		}
	}
	
	private Object divideValue(Object value, int portionCount)
	{
		String valueAsString = value.toString().trim();
		if (valueAsString.equals(""))
			return value;
		
		double parsedValue = Double.parseDouble(valueAsString);
		return parsedValue  / portionCount;
	}

	private void setAssignmentValue(Object dividedValue, int column, Assignment assignment) throws Exception
	{
		DateUnit dateUnit = getDateUnit(column);
		String valueAsString = dividedValue.toString().trim();
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

	private void setUnits(Assignment assignment, DateUnit dateUnit, double units) throws Exception
	{
		DateUnitEffort effort = getDateUnitEffort(assignment, dateUnit);
		if (effort == null)
			effort = new DateUnitEffort(dateUnit, units);

		DateUnitEffortList effortList = assignment.getDateUnitEffortList();
		setUnits(assignment, effortList, effort, units);
	}
	
	private void setUnits(Assignment assignment, DateUnitEffortList effortList, DateUnitEffort effort, double units) throws Exception
	{
		effort.setUnitQuantity(units);
		effortList.setDateUnitEffort(effort);
		String newEffortListString = effortList.toString();
		String data = assignment.getData(assignment.TAG_DATEUNIT_EFFORTS);
		if(newEffortListString.equals(data))
			return;
		
		Command command = new CommandSetObjectData(assignment, Assignment.TAG_DATEUNIT_EFFORTS, newEffortListString);
		getProject().executeCommand(command);
	}

	private void clearSuperDateUnitColumns(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		// TODO: The following loop is very similar to getSuperDateUnits(),
		// so possibly should call that one and then loop through the results
		DateUnit superDateUnit = new DateUnit(dateUnit.getDateUnitCode());
		while(!superDateUnit.isProjectTotal())
		{
			superDateUnit = superDateUnit.getSuperDateUnit(getFiscalYearFirstMonth());
			DateUnitEffort dateUnitEffort = getDateUnitEffort(assignment, superDateUnit);
			if(dateUnitEffort == null)
				continue;
			
			clearUnits(assignment, superDateUnit);
		}
	}

	public void clearUnits(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		DateUnitEffortList effortList = assignment.getDateUnitEffortList();
		effortList.remove(dateUnit);
		String newEffortListString = effortList.toString();
		if(newEffortListString.equals(assignment.getData(assignment.TAG_DATEUNIT_EFFORTS)))
			return;
		
		Command command = new CommandSetObjectData(assignment.getType(), assignment.getId(), assignment.TAG_DATEUNIT_EFFORTS, newEffortListString);
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
	
	private Vector<Assignment> getAssignmentsToEdit(int row) throws Exception
	{
		Assignment assignmentForRow = getAssignment(row);
		if (assignmentForRow != null)
			return convertToVector(assignmentForRow);
		
		BaseObject baseObjectForRowColumn = getBaseObjectForRowColumn(row, 0);
		ORefList assignmentRefsForRowObject = baseObjectForRowColumn.getRefList(getAssignmentsTag());
		if (assignmentRefsForRowObject.size() > 0)
			return refsToAssignments(assignmentRefsForRowObject);
		
		return convertToVector(createAndAddNewAssignment(baseObjectForRowColumn));
	}
	
	private Vector<Assignment> refsToAssignments(ORefList assignmentRefs)
	{
		Vector<Assignment> assignments = new Vector();
		for (int index = 0; index < assignmentRefs.size(); ++index)
		{
			Assignment assignment = Assignment.findAssignment(getProject(), assignmentRefs.get(index));
			assignments.add(assignment);
		}
		
		return assignments;
	}
	
	private Vector<Assignment> convertToVector(Assignment assignment)
	{
		Vector<Assignment> singleList = new Vector();
		singleList.add(assignment);
		return singleList;
	}
	
	private Assignment createAndAddNewAssignment(BaseObject baseObjectForRowColumn) throws Exception
	{
		CommandCreateObject createAssignment = new CommandCreateObject(getAssignmentType());
		getProject().executeCommand(createAssignment);
		
		ORef assignmentRef = createAssignment.getObjectRef();
		CommandSetObjectData addAssignment = createAppendAssignmentCommand(baseObjectForRowColumn, assignmentRef);
		getProject().executeCommand(addAssignment);
		
		return Assignment.findAssignment(getProject(), assignmentRef);
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
	
	@Override
	public void updateFullTimeEmployeeDaysPerYearFraction(int row, int modelColumn, double fraction)
	{
		double fullTimeEmployeeDaysPerYear = getFullTimeEmployeeDaysPerYear(getProject());
		DateUnit dateUnit = getDateUnit(modelColumn);
		double value = calculateFullTimeEmployeeDays(dateUnit, fraction, fullTimeEmployeeDaysPerYear);
		
		setValueAt(new TaglessChoiceItem(value), row, modelColumn);
	}
	
	@Override
	public OptionalDouble getCellFraction(int row, int modelColumn)
	{
		ChoiceItem choiceItem = (ChoiceItem) getValueAt(row, modelColumn);
		String doubleAsString = choiceItem.getLabel();
		if (doubleAsString.length() == 0)
			return new OptionalDouble();
		
		double value = Double.parseDouble(doubleAsString);
		double fullTimeEmployeeDaysPerYear = getFullTimeEmployeeDaysPerYear(getProject());	
		double fraction = calculateFullTimeEmployeeFraction(getDateUnit(modelColumn), value, fullTimeEmployeeDaysPerYear);
		
		return new OptionalDouble(fraction);
	}

	public static double calculateFullTimeEmployeeFraction(DateUnit dateUnit, double assignedDayCount, double fullTimeEmployeeDaysPerYear)
	{
		int numberOfDateUnitsInOneYear = getNumberOfDateUnitsInYear(dateUnit);
		return (assignedDayCount * numberOfDateUnitsInOneYear) / fullTimeEmployeeDaysPerYear;
	}
	
	public static double calculateFullTimeEmployeeDays(DateUnit dateUnit, double fraction, double fullTimeEmployeeDaysPerYear)
	{
		int numberOfDateUnitsInOneYear = getNumberOfDateUnitsInYear(dateUnit);
		return (fraction * fullTimeEmployeeDaysPerYear) / numberOfDateUnitsInOneYear;
	}
	
	public static int getNumberOfDateUnitsInYear(DateUnit dateUnit)
	{
		final int MONTHS_PER_YEAR = 12;
		final int QUARTERS_PER_YEAR = 4;
		final int YEAR_PER_YEAR = 1;
		if (dateUnit.isMonth())
			return MONTHS_PER_YEAR;
	
		if (dateUnit.isQuarter())
			return QUARTERS_PER_YEAR;
	
		if (dateUnit.isYear())
			return YEAR_PER_YEAR;
		
		throw new RuntimeException(EAM.text("Should Now Allow Full Time Employee To Be Calculated For ") + "DateUnit = " + dateUnit);
	}
	
	public static double getFullTimeEmployeeDaysPerYear(Project project)
	{
		return Double.parseDouble(getRawFullTimeEmployeeDaysPerYear(project));
	}

	public static String getRawFullTimeEmployeeDaysPerYear(Project project)
	{
		return project.getMetadata().getFullTimeEmployeeDaysPerYear();
	}
		
	@Override
	public void respondToExpandOrCollapseColumnEvent(int column) throws Exception
	{
		DateUnit dateUnit = getDateUnit(column);
		if (isExpanded(column))
			respondToCollapseColumnEvent(dateUnit);
		else
			respondToExpandColumnEvent(dateUnit);
	}
	
	private void respondToCollapseColumnEvent(DateUnit dateUnit) throws Exception
	{
		setDeepestExpandedColumn(dateUnit.getSafeSuperDateUnit(getFiscalYearFirstMonth()));
	}

	private int getFiscalYearFirstMonth() throws Exception
	{
		return getProjectCalendar().getFiscalYearFirstMonth();
	}
	
	private void respondToExpandColumnEvent(DateUnit dateUnit) throws Exception
	{
		setDeepestExpandedColumn(dateUnit);
	}
	
	private void setDeepestExpandedColumn(DateUnit dateUnit) throws Exception
	{
		Vector<DateUnit> visibleDateUnits = new Vector();
		if (dateUnit != null)
		{
			visibleDateUnits.addAll(getSubDateUnits(dateUnit));
			visibleDateUnits.add(dateUnit);
			visibleDateUnits.addAll(dateUnit.getSuperDateUnitHierarchy(getFiscalYearFirstMonth()));
		}
		
		saveColumnDateUnits(visibleDateUnits);
	}
	
	private boolean isExpanded(int column) throws Exception
	{
		Vector<DateUnit> visibleDateUnits = getCopyOfDateUnits();
		visibleDateUnits.retainAll(getSubDateUnits(getDateUnit(column)));
		
		return visibleDateUnits.size() > 0;
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
	
	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
	}
	
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		OptionalDouble optionalDouble = getOptionalDoubleAt(row, column);
		if (optionalDouble.hasValue())
			return createFormattedChoiceItem(optionalDouble);
		
		return new EmptyChoiceItem();
	}
	
	protected ChoiceItem createFormattedChoiceItem(OptionalDouble optionalDouble)
	{
		return new ChoiceItem(optionalDouble.toUnformattedString(), optionalDouble.toUnformattedString());
	}
	
	private OptionalDouble getOptionalDoubleAt(int row, int column)
	{
		try	
		{
			BaseObject baseObject = getBaseObjectForRowColumn(row, column);
			DateUnit dateUnit = getDateUnit(column);
			
			return getOptionalDoubleData(baseObject, dateUnit);
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
	
	@Override
	public boolean isWorkUnitColumn(int column)
	{
		return getDateUnit(column) != null;
	}
	
	protected OptionalDouble getOptionalDoubleData(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		TimePeriodCosts timePeriodCosts = calculateTimePeriodCosts(baseObject, dateUnit);
		timePeriodCosts.filterProjectResources(getResourcesFilter());
		
		return calculateValue(timePeriodCosts);
	}
	
	protected TimePeriodCosts getProjectTotalTimePeriodCostFor(DateUnit dateUnit) throws Exception
	{
		ProjectTotalCalculator projectTotalCalculator = getProject().getProjectTotalCalculator();
		TimePeriodCostsMap totalProject = projectTotalCalculator.calculateProjectTotals();
		
		return totalProject.calculateTimePeriodCosts(dateUnit);
	}

	protected String getTreeModelIdentifierAsTag()
	{
		return treeModelIdentifierAsTag;
	}
	
	protected CurrencyFormat getCurrencyFormatter()
	{
		return currencyFormatter;
	}

	abstract public Color getCellBackgroundColor(int column);
	
	abstract protected OptionalDouble calculateValue(TimePeriodCosts timePeriodCosts);
	
	abstract protected boolean isAssignmentForModel(Assignment assignment);

    abstract protected boolean isEditableModel();
    
    abstract protected String getAssignmentsTag();
    
    abstract protected int getAssignmentType();
    
    abstract protected CommandSetObjectData createAppendAssignmentCommand(BaseObject baseObjectForRowColumn, ORef assignmentRef) throws Exception;
	
	private Vector<DateUnit> dateUnits;
	private RowColumnBaseObjectProvider provider;
	private String treeModelIdentifierAsTag;
	private CurrencyFormat currencyFormatter;
}
