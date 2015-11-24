/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning.upperPanel;

import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.AssignmentDateUnitsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.ProjectResourceLeaderAtTopSorter;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportShortStatusQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.OptionalDouble;
import org.miradi.utils.Translation;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;
import org.miradi.views.summary.SummaryPlanningWorkPlanSubPanel;

public class PlanningViewMainTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewMainTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse) throws Exception
	{
		super(projectToUse, providerToUse);
		
		rowColumnProvider = rowColumnProviderToUse;
		
		updateColumnsToShow();
	}

	public void updateColumnsToShow() throws Exception
	{
		columnsToShow = new CodeList(getVisibleColumnCodes(project));
		omitColumnTagsRepresentedByColumnTables();
		fireTableStructureChanged();
	}

	@Override
	public Color getCellBackgroundColor(int column)
	{
		String columnTag = getColumnTag(column);
		
		if (isPlannedWhoColumn(columnTag) || isAssignedWhoColumn(columnTag))
			return AppPreferences.RESOURCE_TABLE_BACKGROUND;
		
		if(isPlannedWhenColumn(columnTag) || isAssignedWhenColumn(columnTag))
			return AppPreferences.getWorkUnitsBackgroundColor();
		
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int modelColumn)
	{
		String columnTag = getColumnTag(modelColumn);

		if (isPlannedWhoColumn(columnTag))
			return new WhoPlannedStateLogic(getProject()).isWhoCellEditable(getBaseObjectForRowColumn(row, modelColumn));
		
		if (isPlannedWhenColumn(columnTag))
			return isPlannedWhenCellEditable(row, modelColumn);
		
		if (isAssignedWhoColumn(columnTag))
			return new WhoAssignedStateLogic(getProject()).isWhoCellEditable(getBaseObjectForRowColumn(row, modelColumn));

		if (isAssignedWhenColumn(columnTag))
			return isAssignedWhenCellEditable(row, modelColumn);

		if (isCodeListColumn(modelColumn))
			return false;
		
		BaseObject baseObject = getBaseObjectForRow(row);
		String tagForCell = getTagForCell(baseObject.getType(), modelColumn);
		if (!baseObject.doesFieldExist(tagForCell))
			return false;
		
		if (baseObject.isPseudoField(tagForCell))
			return false;
		
		return true;
	}
	
	@Override
	public boolean isFormattedColumn(int modelColumn)
	{
		if (isDetailsColumn(modelColumn))
			return true;
		
		if (isCommentsColumn(modelColumn))
			return true;
		
		return super.isFormattedColumn(modelColumn);
	}

	private boolean isPlannedWhoColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_PLANNED_WHO_TOTAL);
	}

	private boolean isPlannedWhenColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_PLANNED_WHEN_TOTAL);
	}

	private boolean isAssignedWhoColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_ASSIGNED_WHO_TOTAL);
	}
	
	private boolean isAssignedWhenColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_ASSIGNED_WHEN_TOTAL);
	}

	@Override
	public boolean isPlannedWhenColumn(int modelColumn)
	{
		return isPlannedWhenColumn(getColumnTag(modelColumn));
	}

	private boolean isPlannedWhenCellEditable(int row, int modelColumn)
	{
		BaseObject baseObjectForRow = getBaseObjectForRowColumn(row, modelColumn);
		return isPlannedWhenEditable(baseObjectForRow);
	}

	public static boolean isPlannedWhenEditable(BaseObject baseObject)
	{
		try
		{
			if (!AssignmentDateUnitsTableModel.canOwnAssignments(baseObject.getRef()))
				return false;

			if (hasSubTasksWithResourcePlans(baseObject))
				return false;

			if (baseObject.getResourcePlanRefs().isEmpty())
				return true;

			if (hasResourcePlansWithDifferentDateUnitEffortLists(baseObject))
				return false;

			if (hasResourcePlansWithUsableNumberOfDateUnitEfforts(baseObject))
				return true;

			return false;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	private static boolean hasSubTasksWithResourcePlans(BaseObject baseObject) throws Exception
	{
		ORefList subTaskRefs = baseObject.getSubTaskRefs();
		for (int index = 0; index < subTaskRefs.size(); ++index)
		{
			Task task = Task.find(baseObject.getProject(), subTaskRefs.get(index));
			if (task.getResourcePlanRefs().hasRefs())
				return true;
		}

		return false;
	}

	private static boolean hasResourcePlansWithDifferentDateUnitEffortLists(BaseObject baseObject) throws Exception
	{
		ORefList resourcePlanRefs = baseObject.getResourcePlanRefs();
		HashSet<DateUnitEffortList> dateUnitEffortLists = new HashSet<DateUnitEffortList>();
		for (int index = 0; index < resourcePlanRefs.size(); ++index)
		{
			ResourcePlan resourcePlan = ResourcePlan.find(baseObject.getProject(), resourcePlanRefs.get(index));
			dateUnitEffortLists.add(resourcePlan.getDateUnitEffortList());
			if (dateUnitEffortLists.size() > 1)
				return true;
		}

		return false;
	}

	private static boolean hasResourcePlansWithUsableNumberOfDateUnitEfforts(BaseObject baseObject) throws Exception
	{
		ORefList resourcePlanRefs = baseObject.getResourcePlanRefs();
		ResourcePlan resourcePlan = ResourcePlan.find(baseObject.getProject(), resourcePlanRefs.getFirstElement());
		DateUnitEffortList effortList = resourcePlan.getDateUnitEffortList();

		TimePeriodCostsMap timePeriodCostsMap = resourcePlan.getResourcePlansTimePeriodCostsMap();
		OptionalDouble totalWorkUnits = timePeriodCostsMap.calculateTimePeriodCosts(new DateUnit()).getTotalWorkUnits();
		if (totalWorkUnits.hasNonZeroValue())
			return false;

		if (effortList.size() > 2)
			return false;

		return true;
	}

	@Override
	public boolean isAssignedWhenColumn(int modelColumn)
	{
		return isAssignedWhenColumn(getColumnTag(modelColumn));
	}
	
	private boolean isAssignedWhenCellEditable(int row, int modelColumn)
	{
		BaseObject baseObjectForRow = getBaseObjectForRowColumn(row, modelColumn);
		return isAssignedWhenEditable(baseObjectForRow);
	}
	
	public static boolean isAssignedWhenEditable(BaseObject baseObject)
	{
		try
		{
			if (!AssignmentDateUnitsTableModel.canOwnAssignments(baseObject.getRef()))
				return false;
			
			if (hasSubTasksWithResourceAssignments(baseObject))
				return false;
			
			if (baseObject.getResourceAssignmentRefs().isEmpty())
				return true;
			
			if (hasResourceAssignmentsWithDifferentDateUnitEffortLists(baseObject))
				return false;

			if (hasResourceAssignmentsWithUsableNumberOfDateUnitEfforts(baseObject))
				return true;
				
			return false;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	private static boolean hasSubTasksWithResourceAssignments(BaseObject baseObject) throws Exception
	{
		ORefList subTaskRefs = baseObject.getSubTaskRefs();
		for (int index = 0; index < subTaskRefs.size(); ++index)
		{
			Task task = Task.find(baseObject.getProject(), subTaskRefs.get(index));
			if (task.getResourceAssignmentRefs().hasRefs())
				return true;
		}
		
		return false;
	}
	
	private static boolean hasResourceAssignmentsWithDifferentDateUnitEffortLists(BaseObject baseObject) throws Exception
	{
		ORefList resourceAssignmentRefs = baseObject.getResourceAssignmentRefs();
		HashSet<DateUnitEffortList> dateUnitEffortLists = new HashSet<DateUnitEffortList>();
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{			
			ResourceAssignment resourceAssignment = ResourceAssignment.find(baseObject.getProject(), resourceAssignmentRefs.get(index));
			dateUnitEffortLists.add(resourceAssignment.getDateUnitEffortList());
			if (dateUnitEffortLists.size() > 1)
				return true;
		}	
		
		return false;
	}

	private static boolean hasResourceAssignmentsWithUsableNumberOfDateUnitEfforts(BaseObject baseObject) throws Exception
	{
		ORefList assignmentRefs = baseObject.getResourceAssignmentRefs();
		ResourceAssignment assignment = ResourceAssignment.find(baseObject.getProject(), assignmentRefs.getFirstElement());
		DateUnitEffortList effortList = assignment.getDateUnitEffortList();
		
		TimePeriodCostsMap timePeriodCostsMap = assignment.getResourceAssignmentsTimePeriodCostsMap();
		OptionalDouble totalWorkUnits = timePeriodCostsMap.calculateTimePeriodCosts(new DateUnit()).getTotalWorkUnits();
		if (totalWorkUnits.hasNonZeroValue())
			return false;
		
		if (effortList.size() > 2)
			return false;
		
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		try
		{
			final BaseObject baseObjectForRow = getBaseObjectForRow(row);
			if (baseObjectForRow == null || baseObjectForRow.getRef().isInvalid())
				return;
			
			if (value == null)
				return;
			
			if (isPlannedWhenColumn(column))
			{
				setWhenPlannedValue(baseObjectForRow, createCodeList(value));
			}
			else if (isAssignedWhenColumn(column))
			{
				setWhenAssignedValue(baseObjectForRow, createCodeList(value));
			}
			else if (isChoiceItemColumn(column))
			{
				ChoiceItem choiceItem = (ChoiceItem) value;
				setValueUsingCommand(baseObjectForRow.getRef(), getTagForCell(baseObjectForRow.getType(), column), choiceItem.getCode());
			}
			else
			{
				setValueUsingCommand(baseObjectForRow.getRef(), getTagForCell(baseObjectForRow.getType(), column), value.toString());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
 		super.setValueAt(value, row, column);	
	}

	private CodeList createCodeList(Object rawValue) throws Exception
	{
		return new CodeList(rawValue.toString());
	}

	private void setWhenPlannedValue(BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			clearResourcePlanDateUnitEfforts(baseObjectForRow);
			ORefList resourcePlanRefs = baseObjectForRow.getResourcePlanRefs();
			if (datesAsCodeList.hasData() && resourcePlanRefs.isEmpty())
				createResourcePlan(baseObjectForRow, datesAsCodeList);
			
			if (datesAsCodeList.hasData() && resourcePlanRefs.hasRefs())
				updateResourcePlans(resourcePlanRefs, datesAsCodeList);
			
			if (datesAsCodeList.isEmpty() && resourcePlanRefs.size() == 1)
				deleteEmptyResourcePlan(resourcePlanRefs.getFirstElement());
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void updateResourcePlans(ORefList resourcePlanRefs, CodeList datesAsCodeList) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		for (int index = 0; index < resourcePlanRefs.size(); ++index)
		{
			setResourcePlanDateUnitEffortList(resourcePlanRefs.get(index), dateUnitEffortList);
		}
	}

	private void clearResourcePlanDateUnitEfforts(BaseObject baseObjectForRow) throws Exception
	{
		ORefList resourcePlanRefs = baseObjectForRow.getResourcePlanRefs();
		for (int index = 0; index < resourcePlanRefs.size(); ++index)
		{
			ResourcePlan resourcePlan = ResourcePlan.find(getProject(), resourcePlanRefs.get(index));
			Command clearDateUnitEffortList = new CommandSetObjectData(resourcePlan, ResourceAssignment.TAG_DATEUNIT_EFFORTS, new DateUnitEffortList().toString());
			getProject().executeCommand(clearDateUnitEffortList);
		}
	}

	private void deleteEmptyResourcePlan(ORef resourcePlanRef) throws Exception
	{
		ResourcePlan resourcePlan = ResourcePlan.find(getProject(), resourcePlanRef);
		if (resourcePlan.isEmpty())
		{
			CommandVector removePlanCommands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourcePlan, BaseObject.TAG_RESOURCE_PLAN_IDS);
			getProject().executeCommands(removePlanCommands);
		}
	}

	private void createResourcePlan(BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		CommandCreateObject createResourcePlan = new CommandCreateObject(ResourcePlanSchema.getObjectType());
		getProject().executeCommand(createResourcePlan);

		ORef resourcePlanRef = createResourcePlan.getObjectRef();
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		setResourcePlanDateUnitEffortList(resourcePlanRef, dateUnitEffortList);

		CommandSetObjectData appendResourcePlan = CommandSetObjectData.createAppendIdCommand(baseObjectForRow, BaseObject.TAG_RESOURCE_PLAN_IDS, resourcePlanRef);
		getProject().executeCommand(appendResourcePlan);
	}

	private void setResourcePlanDateUnitEffortList(ORef resourcePlanRef, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		CommandSetObjectData addEffortList = new CommandSetObjectData(resourcePlanRef, ResourcePlan.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
		getProject().executeCommand(addEffortList);
	}

	private void setWhenAssignedValue(BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			clearResourceAssignmentDateUnitEfforts(baseObjectForRow);
			ORefList resourceAssignmentRefs = baseObjectForRow.getResourceAssignmentRefs();
			if (datesAsCodeList.hasData() && resourceAssignmentRefs.isEmpty())
				createResourceAssignment(baseObjectForRow, datesAsCodeList);

			if (datesAsCodeList.hasData() && resourceAssignmentRefs.hasRefs())
				updateResourceAssignments(resourceAssignmentRefs, datesAsCodeList);

			if (datesAsCodeList.isEmpty() && resourceAssignmentRefs.size() == 1)
				deleteEmptyResourceAssignment(resourceAssignmentRefs.getFirstElement());
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void updateResourceAssignments(ORefList resourceAssignmentRefs, CodeList datesAsCodeList) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{			
			setResourceAssignmentDateUnitEffortList(resourceAssignmentRefs.get(index), dateUnitEffortList);
		}
	}

	private void clearResourceAssignmentDateUnitEfforts(BaseObject baseObjectForRow) throws Exception
	{
		ORefList resourceAssignmentRefs = baseObjectForRow.getResourceAssignmentRefs();
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{			
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRefs.get(index));
			Command clearDateUnitEffortList = new CommandSetObjectData(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, new DateUnitEffortList().toString());
			getProject().executeCommand(clearDateUnitEffortList);
		}
	}

	private void deleteEmptyResourceAssignment(ORef resourceAssignmentRef) throws Exception
	{	
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRef);
		if (resourceAssignment.isEmpty())
		{
			CommandVector removeAssignmentCommands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourceAssignment, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
			getProject().executeCommands(removeAssignmentCommands);
		}
	}

	private void createResourceAssignment(BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		CommandCreateObject createResourceAssignment = new CommandCreateObject(ResourceAssignmentSchema.getObjectType());
		getProject().executeCommand(createResourceAssignment);

		ORef resourceAssignmentRef = createResourceAssignment.getObjectRef();
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		setResourceAssignmentDateUnitEffortList(resourceAssignmentRef, dateUnitEffortList);

		CommandSetObjectData appendResourceAssignment = CommandSetObjectData.createAppendIdCommand(baseObjectForRow, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignmentRef);
		getProject().executeCommand(appendResourceAssignment);
	}

	private void setResourceAssignmentDateUnitEffortList(ORef resourceAssignmentRef, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		CommandSetObjectData addEffortList = new CommandSetObjectData(resourceAssignmentRef, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
		getProject().executeCommand(addEffortList);
	}

	private DateUnitEffortList createDateUnitEffortList(CodeList datesAsCodeList)
	{
		final int NO_VALUE = 0;
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < datesAsCodeList.size(); ++index)
		{
			DateUnit dateUnit = new DateUnit(datesAsCodeList.get(index));
			if (dateUnitEffortList.getDateUnitEffortForSpecificDateUnit(dateUnit) == null)
				dateUnitEffortList.add(new DateUnitEffort(dateUnit, NO_VALUE));
		}

		return dateUnitEffortList;
	}

	@Override
	public Class getCellQuestion(int row, int modelColumn)
	{
		if (isProjectResourceTypeColumn(modelColumn))
			return ResourceTypeQuestion.class;
		
		if (isPriorityColumn(modelColumn))
			return PriorityRatingQuestion.class;
		
		return null;
	}
	
	@Override
	public boolean isChoiceItemColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if(isPriorityColumn(column))
			return true;
		
		if(columnTag.equals(CustomPlanningColumnsQuestion.META_CURRENT_RATING))
			return true;
		
		if(isProjectResourceTypeColumn(column))
			return true;
		
		return false;
	}
	
	private boolean isProjectResourceTypeColumn(int column)
	{
		return getColumnTag(column).equals(ProjectResource.TAG_RESOURCE_TYPE);
	}
	
	private boolean isPriorityColumn(int column)
	{
		return getColumnTag(column).equals(Indicator.TAG_PRIORITY);
	}
	
	@Override
	public boolean isProgressColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if(columnTag.equals(Strategy.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return true;
		if(columnTag.equals(Objective.PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE))
			return true;
		
		return false;
	}

	private void omitColumnTagsRepresentedByColumnTables()
	{
		String[] codesToOmit = new String[]{
											Measurement.META_COLUMN_TAG,
											Indicator.META_COLUMN_TAG,
											WorkPlanColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_PROJECT_RESOURCE_BUDGET_DETAILS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_FUNDING_SOURCE_EXPENSE_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_BUDGET_DETAIL_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_FUNDING_SOURCE_BUDGET_DETAILS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_ACCOUNTING_CODE_BUDGET_DETAILS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_ACCOUNTING_CODE_EXPENSE_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_ANALYSIS_WORK_UNITS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_ANALYSIS_EXPENSES_CODE,
											WorkPlanColumnConfigurationQuestion.META_ANALYSIS_BUDGET_DETAILS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_ONE_WORK_UNITS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_ONE_EXPENSE_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_ONE_BUDGET_DETAILS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_TWO_WORK_UNITS_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_TWO_EXPENSE_COLUMN_CODE,
											WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_TWO_BUDGET_DETAILS_COLUMN_CODE,
		};
		
		columnsToShow.subtract(new CodeList(codesToOmit));
	}
	
	public int getColumnCount()
	{
		return columnsToShow.size();
	}

	public String getColumnTag(int modelColumn)
	{
		return columnsToShow.get(modelColumn);
	}
	
	@Override
	public String getColumnName(int column)
	{
		String columnTag = getColumnTag(column);
		String columnName = EAM.fieldLabel(ObjectType.FAKE, columnTag);
		if (doesColumnHeaderNeedAsterisk(columnTag))
			columnName += HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK;
		
		return columnName;
	}
	
	private boolean doesColumnHeaderNeedAsterisk(String columnTag)
	{
		boolean isAsteriskColumn = isPlannedWhenColumn(columnTag) || isPlannedWhoColumn(columnTag) || isAssignedWhenColumn(columnTag) || isAssignedWhoColumn(columnTag);
		if (!isAsteriskColumn)
			return false;

		if (isPlannedWhenColumn(columnTag) || isPlannedWhoColumn(columnTag))
			return SummaryPlanningWorkPlanSubPanel.hasPlannedDataOutsideOfProjectDateRange(getProject());

		if (isAssignedWhenColumn(columnTag) || isAssignedWhoColumn(columnTag))
			return SummaryPlanningWorkPlanSubPanel.hasAssignedDataOutsideOfProjectDateRange(getProject());

		return false;
	}
	
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		BaseObject baseObject = getBaseObjectForRow(row);
		if(baseObject == null)
			return new EmptyChoiceItem();
		
		try
		{	
			String columnTag = getTagForCell(baseObject.getType(), column);

			if(isPlannedWhoColumn(columnTag))
				return getPlannedProjectResourcesAsChoiceItem(baseObject);

			if(isAssignedWhoColumn(columnTag))
				return getAssignedProjectResourcesAsChoiceItem(baseObject);

			if (columnTag.equals(CustomPlanningColumnsQuestion.META_CURRENT_RATING))
				return getRatingChoiceItem(baseObject);
			
			if (! baseObject.doesFieldExist(columnTag))
				return new EmptyChoiceItem();

			String rawValue;

			if(columnTag.equals(BaseObject.PSEUDO_TAG_PLANNED_WHEN_TOTAL))
				rawValue = getProject().getTimePeriodCostsMapsCache().getPlannedWhenTotalAsString(baseObject);
			else if (columnTag.equals(BaseObject.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL))
				rawValue = getProject().getTimePeriodCostsMapsCache().getAssignedWhenTotalAsString(baseObject);
			else if (baseObject.isPseudoField(columnTag))
				rawValue = baseObject.getPseudoData(columnTag);
			else
				rawValue = baseObject.getData(columnTag);
			
			if (rawValue == null)
				return new EmptyChoiceItem();
			
			if(columnTag.equals(Indicator.TAG_PRIORITY))
				return new PriorityRatingQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(ProjectResource.TAG_RESOURCE_TYPE))
				return StaticQuestionManager.getQuestion(ResourceTypeQuestion.class).findChoiceByCode(rawValue);
			
			if(columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return new ProgressReportShortStatusQuestion().findChoiceByCode(rawValue);
			
			if (Desire.isDesire(baseObject.getRef()) && columnTag.equals(Desire.PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
				return createAppendedRelevantIndicatorLabels((Desire)baseObject);
			
			if (Desire.isDesire(baseObject.getRef()) && columnTag.equals(Desire.PSEUDO_TAG_RELEVANT_ACTIVITY_REFS))
				return createAppendedRelevantActivityLabels((Desire)baseObject);
			
			if(isPlannedWhenColumn(columnTag))
				return getFilteredWhenForPlans(baseObject);

			if(isAssignedWhenColumn(columnTag))
				return getFilteredWhenForAssignments(baseObject);

			return new TaglessChoiceItem(rawValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new TaglessChoiceItem(EAM.text("[Error]"));
		}
	}
	
	private ChoiceItem createAppendedRelevantActivityLabels(Desire desire) throws Exception
	{
		return createLabelsOnSingleLine(desire, desire.getRelevantActivityRefs());
	}

	private ChoiceItem createAppendedRelevantIndicatorLabels(Desire desire) throws Exception
	{
		return createLabelsOnSingleLine(desire, desire.getRelevantIndicatorRefList());
	}
	
	private ChoiceItem createLabelsOnSingleLine(Desire desire, ORefList refs)
	{
		String labelsOnASingleLine = desire.getLabelsAsMultiline(refs);
		
		return new TaglessChoiceItem(labelsOnASingleLine);
	}

	private ChoiceItem getRatingChoiceItem(BaseObject baseObject) throws Exception
	{
		if (Cause.is(baseObject))
			return getThreatRatingChoiceItem((Cause) baseObject);
		
		if (Target.is(baseObject))
			return getTargetViabilityRating((AbstractTarget) baseObject);
		
		if (Strategy.is(baseObject))
			return getStrategyRating((Strategy) baseObject);
		
		return new EmptyChoiceItem();
	}

	private ChoiceItem getStrategyRating(Strategy strategy)
	{
		return strategy.getStrategyRating();
	}

	private ChoiceItem getThreatRatingChoiceItem(Cause threat) throws Exception
	{
		if (threat.isContributingFactor())
			return new EmptyChoiceItem();
		
		return getProject().getThreatRatingFramework().getThreatThreatRatingValue(threat.getRef());
	}
	
	private ChoiceItem getTargetViabilityRating(AbstractTarget abstractTarget)
	{
		ChoiceQuestion question = StaticQuestionManager.getQuestion(StatusQuestion.class);
		
		return question.findChoiceByCode(abstractTarget.getTargetViability());
	}

	private ChoiceItem getFilteredWhenForPlans(BaseObject baseObject) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = calculateTimePeriodPlannedCostsMap(baseObject, getRowColumnProvider().getWorkPlanBudgetMode());
		return getFilteredWhen(totalTimePeriodCostsMap);
	}

	private ChoiceItem getFilteredWhenForAssignments(BaseObject baseObject) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = calculateTimePeriodAssignedCostsMap(baseObject, getRowColumnProvider().getWorkPlanBudgetMode());
		return getFilteredWhen(totalTimePeriodCostsMap);
	}

	private ChoiceItem getFilteredWhen(TimePeriodCostsMap totalTimePeriodCostsMap) throws Exception
	{
		DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
		DateRange rolledUpDateRangeForResources = totalTimePeriodCostsMap.getRolledUpDateRange(projectStartEndDateRange, getResourcesFilter());
		String rolledUpResourcesWhen = getProject().getProjectCalendar().convertToSafeString(rolledUpDateRangeForResources);

		return new TaglessChoiceItem(rolledUpResourcesWhen);
	}

	@Override
	protected TimePeriodCostsMap getTotalTimePeriodPlannedCostsMap(BaseObject baseObject) throws Exception
	{		
		return baseObject.getResourcePlansTimePeriodCostsMap();
	}
	
	@Override
	protected TimePeriodCostsMap getTotalTimePeriodAssignedCostsMap(BaseObject baseObject) throws Exception
	{
		return baseObject.getResourceAssignmentsTimePeriodCostsMap();
	}

	private ChoiceItem getPlannedProjectResourcesAsChoiceItem(BaseObject baseObject) throws Exception
	{
		TimePeriodCosts timePeriodCosts = calculateTimePeriodPlannedCosts(baseObject, new DateUnit(), getRowColumnProvider().getWorkPlanBudgetMode());
		return getProjectResourcesAsChoiceItem(timePeriodCosts, baseObject, BaseObject.TAG_PLANNED_LEADER_RESOURCE);
	}

	private ChoiceItem getAssignedProjectResourcesAsChoiceItem(BaseObject baseObject) throws Exception
	{
		TimePeriodCosts timePeriodCosts = calculateTimePeriodAssignedCosts(baseObject, new DateUnit(), getRowColumnProvider().getWorkPlanBudgetMode());
		return getProjectResourcesAsChoiceItem(timePeriodCosts, baseObject, BaseObject.TAG_ASSIGNED_LEADER_RESOURCE);
	}

	private ChoiceItem getProjectResourcesAsChoiceItem(TimePeriodCosts timePeriodCosts, BaseObject baseObject, String leaderResourceTag) throws Exception
	{
		timePeriodCosts.retainWorkUnitDataRelatedToAnyOf(getResourcesFilter());
		ORefSet filteredResources = new ORefSet(timePeriodCosts.getWorkUnitsRefSetForType(ProjectResourceSchema.getObjectType()));

		ORefSet unspecifiedBaseObjectRefs = getInvalidRefs(filteredResources);
		filteredResources.removeAll(unspecifiedBaseObjectRefs);
		Vector<ProjectResource> sortedProjectResources = toProjectResources(filteredResources);
		ORef leaderResourceRef = ORef.INVALID;
		if (baseObject.doesFieldExist(leaderResourceTag))
		{
			leaderResourceRef = baseObject.getRef(leaderResourceTag);
			Collections.sort(sortedProjectResources, new ProjectResourceLeaderAtTopSorter(leaderResourceRef));
		}

		final ORefList sortedProjectResourceRefs = new ORefList(sortedProjectResources);
		sortedProjectResourceRefs.addAll(new ORefList(unspecifiedBaseObjectRefs));
		Vector<String> sortedNames = getResourceNames(sortedProjectResourceRefs, leaderResourceRef);
		String appendedResources = createAppendedResourceNames(sortedNames);

		return new TaglessChoiceItem(appendedResources);
	}

	public ORefSet getInvalidRefs(ORefSet oRefSet)
	{
		ORefSet invalidRefs = new ORefSet();
		for(ORef ref : oRefSet)
		{
			if (ref.isInvalid())
				invalidRefs.add(ref);
		}
		
		return invalidRefs;
	}
	
	private Vector<ProjectResource> toProjectResources(ORefSet resourcesRefs) throws Exception
	{
		Vector<ProjectResource> resources = new Vector<ProjectResource>();
		for(ORef resourceRef : resourcesRefs)
		{
			final ProjectResource projectResource = ProjectResource.find(getProject(), resourceRef);
			if (projectResource == null)
			{
				EAM.logError("Could not find Project Resource object for ref:" + resourceRef);
				continue;
			}
			
			resources.add(projectResource);
		}
		
		return resources;
	}

	private String createAppendedResourceNames(Vector<String> sortedNames)
	{
		boolean isFirstIteration = true; 
		String appendedResources = "";
		for(String resourceName : sortedNames)
		{
			if (!isFirstIteration)
				appendedResources += ", ";
			
			appendedResources += resourceName;
			isFirstIteration = false;	
		}
		return appendedResources;
	}

	private Vector<String> getResourceNames(ORefList filteredResources, ORef leaderResourceRef)
	{
		Vector<String> names = new Vector<String>();
		for(ORef resourceRef : filteredResources)
		{
			names.add(getWhoName(resourceRef, leaderResourceRef));
		}

		return names;
	}

	private String getWhoName(ORef resourceRef, ORef leaderResourceRef)
	{
		if (resourceRef.isInvalid())
			return Translation.getNotSpecifiedText();

		ProjectResource projectResource = ProjectResource.find(getProject(), resourceRef);
		final String who = projectResource.getWho();
		if (leaderResourceRef.equals(resourceRef))
			return who + "*";
		
		return who;	
	}

	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
	}

	@Override
	public String getTagForCell(int nodeType, int column)
	{
		String columnTag = getColumnTag(column);
		if (isCommentsColumn(column))
		{
			columnTag = Factor.TAG_COMMENTS;
		}
		
		if(ProjectMetadata.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
		if(ConceptualModelDiagram.is(nodeType))
		{
			if(isDetailsColumn(column))
				return ConceptualModelDiagram.TAG_DETAIL;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
		if(ResultsChainDiagram.is(nodeType))
		{
			if(isDetailsColumn(column))
				return ResultsChainDiagram.TAG_DETAIL;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
		if(AbstractTarget.isAbstractTarget(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
		if(Target.is(nodeType))
		{
			if (columnTag.equals(Factor.PSEUDO_TAG_TAXONOMY_CODE_VALUE))
				return Target.PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE;
		}		
		if(Desire.isDesire(nodeType))
		{
			if (columnTag.equals(Factor.TAG_COMMENTS))
				return Desire.TAG_COMMENTS;
			
			if (columnTag.equals(Factor.PSEUDO_TAG_INDICATORS))
				return Desire.PSEUDO_TAG_RELEVANT_INDICATOR_REFS;
			
			if (columnTag.equals(Strategy.PSEUDO_TAG_ACTIVITIES))
				return Desire.PSEUDO_TAG_RELEVANT_ACTIVITY_REFS;
		}
		if(Goal.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return Goal.PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS))
				return Goal.PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS;
		}
		if(Cause.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
		if(ThreatReductionResult.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
		if(Objective.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return Objective.PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS))
				return Objective.PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS;
		}
		if(Task.is(nodeType))
		{
			if(isDetailsColumn(column))
				return Task.TAG_DETAILS;
		}
		if(Indicator.is(nodeType))
		{
			if (isDetailsColumn(column))
				return Indicator.TAG_DETAIL;
		}
		if(Measurement.is(nodeType))
		{
			if (isDetailsColumn(column))
				return Measurement.TAG_DETAIL;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
		if(ResourceAssignment.is(nodeType))
		{
			if (isAssignedWhoColumn(columnTag))
				return ResourceAssignment.PSEUDO_TAG_PROJECT_RESOURCE_LABEL;
			if (columnTag.equals(Indicator.PSEUDO_TAG_FACTOR))
				return ResourceAssignment.PSEUDO_TAG_OWNING_FACTOR_NAME;
		}
		if(ResourcePlan.is(nodeType))
		{
			if (isPlannedWhoColumn(columnTag))
				return ResourcePlan.PSEUDO_TAG_PROJECT_RESOURCE_LABEL;
			if (columnTag.equals(Indicator.PSEUDO_TAG_FACTOR))
				return ResourcePlan.PSEUDO_TAG_OWNING_FACTOR_NAME;
		}
		if(Factor.isFactor(nodeType))
		{
			if (isDetailsColumn(column))
				return Factor.TAG_TEXT;
		}
		if (SubTarget.is(nodeType))
		{
			if (isDetailsColumn(column))
				return SubTarget.TAG_DETAIL;
		}
		if (FutureStatus.is(nodeType))
		{
			if (isCommentsColumn(column))
				return FutureStatusSchema.TAG_FUTURE_STATUS_COMMENTS;
			if (isDetailsColumn(column))
				return FutureStatusSchema.TAG_FUTURE_STATUS_DETAIL;
		}

		return columnTag;
	}
	
	private boolean isCommentsColumn(int column)
	{
		if (getColumnTag(column).equals(Factor.TAG_COMMENTS))
			return true;
		
		return getColumnTag(column).equals(WorkPlanColumnConfigurationQuestion.COMMENTS_COLUMN_CODE);
	}

	private boolean isDetailsColumn(int column)
	{
		if (getColumnTag(column).equals(WorkPlanColumnConfigurationQuestion.DETAILS_COLUMN_CODE))
			return true;
		
		return getColumnTag(column).equals(Desire.TAG_FULL_TEXT);
	}
	
	private CodeList getVisibleColumnCodes(Project projectToUse) throws Exception
	{
		return getRowColumnProvider().getColumnCodesToShow();
	}
	
	private PlanningTreeRowColumnProvider getRowColumnProvider()
	{
		return rowColumnProvider;
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
				
	private static final String UNIQUE_MODEL_IDENTIFIER = "PlanningViewMainTableModel";
	
	private static final String HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK = "*";

	private CodeList columnsToShow;
	private PlanningTreeRowColumnProvider rowColumnProvider;
}
