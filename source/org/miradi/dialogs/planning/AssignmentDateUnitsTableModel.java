/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.planning;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objectdata.DateUnitListData;
import org.miradi.objecthelpers.*;
import org.miradi.objects.*;
import org.miradi.project.CurrencyFormat;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.*;

import java.util.Vector;

abstract public class AssignmentDateUnitsTableModel extends PlanningViewAbstractTreeTableSyncedTableModel implements ModelColumnTagProvider
{
	public AssignmentDateUnitsTableModel(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse, RowColumnBaseObjectProvider rowColumnBaseObjectProviderToUse, String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, rowColumnBaseObjectProviderToUse, rowColumnProviderToUse);

		rowColumnBaseObjectProvider = rowColumnBaseObjectProviderToUse;
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

			if(isMonthColumn(modelColumn) && getProjectCalendar().shouldHideDayColumns())
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
	public boolean isColumnCollapsible(int modelColumn)
	{
		try
		{
			if(isDayColumn(modelColumn))
				return false;

			if(isMonthColumn(modelColumn) && getProjectCalendar().shouldHideDayColumns())
				return false;

			return isExpanded(modelColumn);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	@Override
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
	
	public String getColumnTag(int modelColumn)
	{
		return getDateUnit(modelColumn).getDateUnitCode();
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		try
		{
			BaseObject baseObjectForRow = getBaseObjectForRow(row);

			if (!Assignment.isAssignment(baseObjectForRow.getRef()))
				return false;

			if (!isEditableModel())
				return false;

			Assignment assignment = getAssignment(row);
			if (assignment != null)
				return isAssignmentCellEditable(assignment, getDateUnit(column));

			if (hasConflictingValue(baseObjectForRow, getDateUnit(column)))
				return false;
			
			ORefList assignmentRefs = baseObjectForRow.getSafeRefListData(getAssignmentsTag());
			if (assignmentRefs.size() >  1)
				return false;

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
	
	private boolean hasConflictingValue(BaseObject baseObjectForRow, DateUnit dateUnit) throws Exception
	{
		if (baseObjectForRow.getChildTaskRefs().size() == 0)
			return false;
		
		TimePeriodCostsMap timePeriodCostsMap = getTimePeriodCostsMapsCache().getTotalTimePeriodAssignedCostsMapForSubTasks(baseObjectForRow, getAssignmentsTag());
		TimePeriodCosts timePeriodCosts = timePeriodCostsMap.calculateTimePeriodCosts(dateUnit);
		OptionalDouble totalCost = timePeriodCosts.calculateTotalCost(getProject());

		return totalCost.hasValue();
	}

	private Assignment getSingleAssignmentForBaseObject(BaseObject baseObjectForRow) throws Exception
	{
		ORefList assignmentRefsForRowObject = baseObjectForRow.getSafeRefListData(getAssignmentsTag());
		return Assignment.findAssignment(getProject(), assignmentRefsForRowObject.get(0));
	}

	private boolean isAssignmentCellEditable(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		if (!isAssignmentForModel(assignment))
			return false;

		return isHorizontallyEditable(assignment, dateUnit);
	}

	private boolean isHorizontallyEditable(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		DateUnitEffort thisCellEffort = getDateUnitEffort(assignment, dateUnit);
		if(thisCellEffort != null)
			return true;
		
		return !getUnfilteredOptionalDoubleData(assignment, dateUnit).hasValue();
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		// TODO: Compare with code in divideValue. Why does this not need trim and if?
		final String newValueAsString = value.toString();
		final String oldValueAsString = getChoiceItemAt(row, column).getCode();
		if(oldValueAsString.equals(newValueAsString))
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
			setAssignmentValues(assignments, column, value);
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
			double units = DoubleUtilities.toDoubleFromDataFormat(valueAsString);
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
		String data = assignment.getData(assignment.TAG_DATEUNIT_DETAILS);
		if(newEffortListString.equals(data))
			return;
		
		Command command = new CommandSetObjectData(assignment, Assignment.TAG_DATEUNIT_DETAILS, newEffortListString);
		getProject().executeCommand(command);
	}

	private void clearSuperDateUnitColumns(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		Vector<DateUnit> superHierarchy = getSuperDateUnitsHierarchy(dateUnit);
		for(DateUnit superDateUnit : superHierarchy)
		{
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
		if(newEffortListString.equals(assignment.getData(assignment.TAG_DATEUNIT_DETAILS)))
			return;
		
		Command command = new CommandSetObjectData(assignment.getType(), assignment.getId(), assignment.TAG_DATEUNIT_DETAILS, newEffortListString);
		getProject().executeCommand(command);
	}

	@Override
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getRowColumnBaseObjectProvider().getBaseObjectForRowColumn(row, column);
	}

	@Override
	public int getRowCount()
	{
		return getRowColumnBaseObjectProvider().getRowCount();
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
		ORefList assignmentRefsForRowObject = baseObjectForRowColumn.getSafeRefListData(getAssignmentsTag());
		if (assignmentRefsForRowObject.size() > 0)
		{
			final Vector<Assignment> allAssignments = refsToAssignments(assignmentRefsForRowObject);
			return getFilteredAssignments(allAssignments);
		}
		
		return convertToVector(createAndAddNewAssignment(baseObjectForRowColumn));
	}
	
	private Vector<Assignment> getFilteredAssignments(Vector<Assignment> refsToAssignments)
	{
		Vector<Assignment> assignments = new Vector<Assignment>();
		for(Assignment assignment : refsToAssignments)
		{
			if (shouldIncludeAssignment(assignment))
				assignments.add(assignment);
		}
		return assignments;
	}

	private Vector<Assignment> refsToAssignments(ORefList assignmentRefs)
	{
		Vector<Assignment> assignments = new Vector<Assignment>();
		for (int index = 0; index < assignmentRefs.size(); ++index)
		{
			Assignment assignment = Assignment.findAssignment(getProject(), assignmentRefs.get(index));
			assignments.add(assignment);
		}
		
		return assignments;
	}

	private boolean shouldIncludeAssignment(Assignment assignment)
	{
		if (ExpenseAssignment.is(assignment))
			return true;
		
		ORefSet resourcesFilter = getResourcesFilter();
		if (resourcesFilter.isEmpty())
			return true;
		
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), assignment.getRef());
		return resourcesFilter.contains(resourceAssignment.getResourceRef());
	}
	
	private Vector<Assignment> convertToVector(Assignment assignment)
	{
		Vector<Assignment> singleList = new Vector<Assignment>();
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

	public RowColumnBaseObjectProvider getRowColumnBaseObjectProvider()
	{
		return rowColumnBaseObjectProvider;
	}

	private Vector<DateUnit> getDateUnits()
	{
		return dateUnits;
	}

	protected OptionalDouble calculateRollupValue(int row, int column)
	{		
		try	
		{
			DateUnit dateUnit = getDateUnit(column);
			TimePeriodCosts timePeriodCosts = getProjectTotalTimePeriodCostFor(dateUnit);
			ORefList objectHierarchy = getRowColumnBaseObjectProvider().getObjectHierarchy(row, column);
			ORefSet refsToRetain = new ORefSet(objectHierarchy);
			removeMetadataRootNodeRefInPlace(refsToRetain);
			retainDataRelatedToAllOf(timePeriodCosts, refsToRetain);
			
			return calculateValue(timePeriodCosts);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new OptionalDouble();
		}
	}

	private void removeMetadataRootNodeRefInPlace(ORefSet refsToRetain)
	{
		refsToRetain.remove(getProject().getMetadata().getRef());
	}

	protected void retainDataRelatedToAllOf(TimePeriodCosts timePeriodCosts, ORefSet objectHierarchy)
	{
		throw new RuntimeException("Model needs to override retainDataRelatedToAnyOf");
	}
	
	public Vector<DateUnit> getCopyOfDateUnits()
	{
		return new Vector<DateUnit>(getDateUnits());
	}
	
	public boolean isDayColumn(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		if (dateUnit == null)
			return false;
		
		return dateUnit.isDay();
	}

	public boolean isMonthColumn(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		if (dateUnit == null)
			return false;

		return dateUnit.isMonth();
	}

	@Override
	public void updateFullTimeEmployeeDaysPerYearFraction(int row, int modelColumn, double fraction) throws Exception
	{
		double fullTimeEmployeeDaysPerYear = getFullTimeEmployeeDaysPerYear(getProject());
		DateUnit dateUnit = getDateUnit(modelColumn);
		double value = calculateFullTimeEmployeeDays(dateUnit, fraction, fullTimeEmployeeDaysPerYear);
		
		setValueAt(new TaglessChoiceItem(value), row, modelColumn);
	}
	
	@Override
	public OptionalDouble getCellFraction(int row, int modelColumn) throws Exception
	{
		ChoiceItem choiceItem = (ChoiceItem) getValueAt(row, modelColumn);
		String doubleAsString = choiceItem.getLabel();
		if (doubleAsString.length() == 0)
			return new OptionalDouble();
		
		double value = DoubleUtilities.toDoubleFromDataFormat(doubleAsString);
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
		
		throw new RuntimeException("Should Now Allow Full Time Employee To Be Calculated For DateUnit = " + dateUnit);
	}
	
	private static double getFullTimeEmployeeDaysPerYear(Project project) throws Exception
	{
		String rawFullTimeEmployeeDaysPerYear = getRawFullTimeEmployeeDaysPerYear(project);
		return DoubleUtilities.toDoubleFromDataFormat(rawFullTimeEmployeeDaysPerYear);
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
	
	private void respondToCollapseColumnEvent(DateUnit dateUnitToCollapse) throws Exception
	{
		Vector<DateUnit> visibleDateUnits = new Vector<DateUnit>();

		if (dateUnitToCollapse != null)
		{
			if (dateUnitToCollapse.isProjectTotal())
				visibleDateUnits.add(dateUnitToCollapse);
			else
				visibleDateUnits.addAll(getCollapsedDateUnits(dateUnitToCollapse));
		}

		saveColumnDateUnits(visibleDateUnits);
	}

	private void respondToExpandColumnEvent(DateUnit dateUnitToExpand) throws Exception
	{
		Vector<DateUnit> visibleDateUnits = new Vector<DateUnit>();

		if (dateUnitToExpand != null)
		{
			if (dateUnitToExpand.isProjectTotal())
				visibleDateUnits.addAll(getProjectTotalDateUnits(dateUnitToExpand));
			else
				visibleDateUnits.addAll(getExpandedDateUnits(dateUnitToExpand));
		}

		saveColumnDateUnits(visibleDateUnits);
	}

	private Vector<DateUnit> getProjectTotalDateUnits(DateUnit dateUnit) throws Exception
	{
		Vector<DateUnit> projectTotalDateUnits = new Vector<DateUnit>();

		projectTotalDateUnits.addAll(getSubDateUnits(dateUnit));
		projectTotalDateUnits.add(dateUnit);
		projectTotalDateUnits.addAll(getSuperDateUnitsHierarchy(dateUnit));

		return projectTotalDateUnits;
	}

	private Vector<DateUnit> getCollapsedDateUnits(DateUnit dateUnitToCollapse) throws Exception
	{
		Vector<DateUnit> visibleDateUnits = new Vector<DateUnit>();

		Vector<DateUnit> currentDateUnits = getDateUnits();

		for (DateUnit dateUnit : currentDateUnits)
		{
			if (!dateUnitToCollapse.contains(dateUnit) || dateUnitToCollapse.equals(dateUnit))
				visibleDateUnits.add(dateUnit);
		}

		return visibleDateUnits;
	}

	private Vector<DateUnit> getExpandedDateUnits(DateUnit dateUnitToExpand) throws Exception
	{
		Vector<DateUnit> visibleDateUnits = new Vector<DateUnit>();

		Vector<DateUnit> currentDateUnits = getDateUnits();
		for (DateUnit dateUnit : currentDateUnits)
		{
			if (dateUnit.equals(dateUnitToExpand))
				visibleDateUnits.addAll(getSubDateUnits(dateUnitToExpand));

			visibleDateUnits.add(dateUnit);
		}

		return visibleDateUnits;
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
	
	private Vector<DateUnit> getSuperDateUnitsHierarchy(DateUnit dateUnit) throws Exception
	{
		return getProjectCalendar().getSuperDateUnitsHierarchy(dateUnit);
	}

	private Vector<DateUnit> getSubDateUnits(DateUnit dateUnit)	throws Exception
	{
		return getProjectCalendar().getSubDateUnits(dateUnit);
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
		String formattedValue = FloatingPointFormatter.formatEditableValue(optionalDouble);
		
		return new ChoiceItem(formattedValue, formattedValue);
	}

	protected OptionalDouble getOptionalDoubleAt(int row, int column)
	{
		try	
		{
			BaseObject baseObject = getBaseObjectForRowColumn(row, column);
			if (baseObject == null)
				return new OptionalDouble();
			
			DateUnit dateUnit = getDateUnit(column);
			
			return getOptionalDoubleDataFilteredByResource(baseObject, dateUnit);
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
	public boolean isDateUnitColumn(int column)
	{
		return getDateUnit(column) != null;
	}

	private OptionalDouble getOptionalDoubleDataFilteredByResource(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		ORefSet resourcesFilter = getResourcesFilter();
		TimePeriodCosts timePeriodCosts = calculateTimePeriodAssignedCosts(baseObject, dateUnit);
		timePeriodCosts.retainWorkUnitDataRelatedToAnyOf(resourcesFilter);
		
		return calculateValue(timePeriodCosts);
	}
	
	private OptionalDouble getUnfilteredOptionalDoubleData(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		TimePeriodCosts timePeriodCosts = calculateTimePeriodAssignedCosts(baseObject, dateUnit);

		return calculateValue(timePeriodCosts);
	}

	private TimePeriodCosts getProjectTotalTimePeriodCostFor(DateUnit dateUnit) throws Exception
	{
		TimePeriodCostsMap totalProject = getTimePeriodCostsMapsCache().calculateProjectAssignedTotals();
		
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

	abstract protected OptionalDouble calculateValue(TimePeriodCosts timePeriodCosts) throws Exception;
	
	abstract protected boolean isAssignmentForModel(Assignment assignment);

    protected boolean isEditableModel()
    {
    	return false;
    }
    
    abstract protected String getAssignmentsTag();
    
    abstract protected int getAssignmentType();
    
    protected CommandSetObjectData createAppendAssignmentCommand(BaseObject baseObjectForRowColumn, ORef assignmentRef) throws Exception
    {
		throw new RuntimeException("Cannot create assignment in this table.");
    }
	
	private Vector<DateUnit> dateUnits;
	private RowColumnBaseObjectProvider rowColumnBaseObjectProvider;
	private String treeModelIdentifierAsTag;
	private CurrencyFormat currencyFormatter;
}
