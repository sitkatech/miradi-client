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
package org.miradi.dialogs.planning.upperPanel;

import java.awt.Color;
import java.util.Collections;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Desire;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportShortStatusQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.IgnoreCaseStringComparator;
import org.miradi.utils.OptionalDouble;
import org.miradi.utils.Translation;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;
import org.miradi.views.summary.SummaryPlanningWorkPlanSubPanel;

public class PlanningViewMainTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewMainTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, RowColumnProvider rowColumnProviderToUse) throws Exception
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
		
		if (isWhoColumn(columnTag))
			return AppPreferences.RESOURCE_TABLE_BACKGROUND;
		
		if(isWhenColumn(columnTag))
			return AppPreferences.getWorkUnitsBackgroundColor();
		
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int modelColumn)
	{
		String columnTag = getColumnTag(modelColumn);
		if (isWhoColumn(columnTag))
			return isWhoCellEditable(row, modelColumn);
		
		if (isWhenColumn(columnTag))
			return isWhenCellEditable(row, modelColumn);
		
		return super.isCellEditable(row, modelColumn);
	}

	private boolean isWhoColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_WHO_TOTAL);
	}
	
	private boolean isWhenColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_WHEN_TOTAL);
	}
	
	@Override
	public boolean isWhenColumn(int modelColumn)
	{
		return isWhenColumn(getColumnTag(modelColumn));
	}
	
	private boolean isWhenCellEditable(int row, int modelColumn)
	{
		BaseObject baseObjectForRow = getBaseObjectForRowColumn(row, modelColumn);
		return isWhenEditable(baseObjectForRow);
	}
	
	public static boolean isWhenEditable(BaseObject baseObject)
	{
		try
		{
			if (!AssignmentDateUnitsTableModel.canOwnAssignments(baseObject.getRef()))
				return false;
			
			if (baseObject.getSubTaskRefs().hasRefs())
				return false;
			
			if (baseObject.getResourceAssignmentRefs().isEmpty())
				return true;
			
			if (baseObject.getResourceAssignmentRefs().size() > 1)
				return false;

			if (hasUsableNumberOfDateUnitEfforts(baseObject))
				return true;
				
			return false;
			
//FIXME we are temporarly restricting to objects that have no assignments
//Later we will descide to either remove this code or enable it.  This code was commented out since
//who editor can create resource assignments and we dont want to when editor to delete it.
//NOTE the commented method below as well.
//
//			if (hasMoreThanOneDateUnitEfforts(baseObject))
//				return false;
//			
//			OptionalDouble totalBudgetCost = baseObject.getTotalBudgetCost();
//			if (totalBudgetCost.hasNoValue())
//				return true;
//			
//			if (totalBudgetCost.getValue() > 0)
//				return false;
//			
//			return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	private static boolean hasUsableNumberOfDateUnitEfforts(BaseObject baseObject) throws Exception
	{
		ORefList assignmentRefs = baseObject.getResourceAssignmentRefs();
		ResourceAssignment assignment = ResourceAssignment.find(baseObject.getProject(), assignmentRefs.getFirstElement());
		DateUnitEffortList effortList = assignment.getDateUnitEffortList();
		
		TimePeriodCostsMap timePeriodCostsMap = assignment.getResourceAssignmentsTimePeriodCostsMap();
		OptionalDouble totalWorkUnits = timePeriodCostsMap.calculateTimePeriodCosts(new DateUnit()).getTotalWorkUnits();
		if (isGreaterThanZero(totalWorkUnits))
			return false;
		
		if (effortList.size() > 2)
			return false;
		
		return true;
	}

	private static boolean isGreaterThanZero(OptionalDouble totalWorkUnits)
	{
		return totalWorkUnits.hasValue() && totalWorkUnits.getValue() > 0.0;
	}
	
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		try
		{
			if (isWhenColumn(column))
				setWhenValue(getBaseObjectForRow(row), createCodeList(value));
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

	private void setWhenValue(BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			clearDateUnitEfforts(baseObjectForRow);
			if (datesAsCodeList.size() == 2)
				createResourceAssignment(baseObjectForRow, datesAsCodeList);

			if (datesAsCodeList.isEmpty() && areAllAssignmentsForObjectEmpty(baseObjectForRow))
				deleteResourceAssignment(baseObjectForRow);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void clearDateUnitEfforts(BaseObject baseObjectForRow) throws Exception
	{
		ORefList resourceAssignmentRefs = baseObjectForRow.getResourceAssignmentRefs();
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{			
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRefs.get(index));
			Command clearDateUnitEffortList = new CommandSetObjectData(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, new DateUnitEffortList().toString());
			getProject().executeCommand(clearDateUnitEffortList);
		}
	}

	private boolean areAllAssignmentsForObjectEmpty(BaseObject baseObjectForRow)
	{
		ORefList emptyAssignmentRefs = new ORefList();
		ORefList resourceAssignmentRefs = baseObjectForRow.getResourceAssignmentRefs();
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRefs.get(index));
			if (resourceAssignment.isEmpty())
				emptyAssignmentRefs.add(resourceAssignmentRefs.get(index));
		}
		
		return resourceAssignmentRefs.size() == emptyAssignmentRefs.size();
	}

	private void deleteResourceAssignment(BaseObject baseObjectForRow) throws Exception
	{	
		ORefList resourceAssignmentRefs = baseObjectForRow.getResourceAssignmentRefs();
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{			
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRefs.get(index));
			Vector<Command> removeAssignmentCommands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourceAssignment, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
			getProject().executeCommandsWithoutTransaction(removeAssignmentCommands);
		}
	}

	private void createResourceAssignment(BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);

		CommandCreateObject createResourceAssignment = new CommandCreateObject(ResourceAssignment.getObjectType());
		getProject().executeCommand(createResourceAssignment);

		ORef resourceAssignmentRef = createResourceAssignment.getObjectRef();
		CommandSetObjectData addEffortList = new CommandSetObjectData(resourceAssignmentRef, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
		getProject().executeCommand(addEffortList);

		CommandSetObjectData appendResourceAssignment = CommandSetObjectData.createAppendIdCommand(baseObjectForRow, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignmentRef);
		getProject().executeCommand(appendResourceAssignment);
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

	private boolean isWhoCellEditable(int row, int modelColumn)
	{
		try
		{
			BaseObject baseObjectForRow = getBaseObjectForRowColumn(row, modelColumn);
			if (!AssignmentDateUnitsTableModel.canOwnAssignments(baseObjectForRow.getRef()))
				return false;

			if (doAnySubtasksHaveAnyWorkUnitData(baseObjectForRow))
				return false;

			return doAllResourceAssignmentsHaveIdenticalWorkUnits(row, modelColumn);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;		
		}
	}

	private boolean doAnySubtasksHaveAnyWorkUnitData(BaseObject baseObjectForRow) throws Exception
	{
		TimePeriodCostsMap timePeriodCostsMap = baseObjectForRow.getTotalTimePeriodCostsMapForSubTasks(baseObjectForRow.getSubTaskRefs(), BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
		TimePeriodCosts wholeProjectTimePeriodCosts = timePeriodCostsMap.calculateTimePeriodCosts(new DateUnit());
		OptionalDouble totalSubtaskWorkUnitsForAllTimePeriods = wholeProjectTimePeriodCosts.getTotalWorkUnits();

		return totalSubtaskWorkUnitsForAllTimePeriods.hasValue();
	}
	
	private boolean doAllResourceAssignmentsHaveIdenticalWorkUnits(int row, int modelColumn) throws Exception
	{
			BaseObject baseObjectForRow = getBaseObjectForRowColumn(row, modelColumn);
			ORefList resourceAssignments = baseObjectForRow.getResourceAssignmentRefs();
			DateUnitEffortList expectedDateUnitEffortList = null;
			for (int index = 0; index < resourceAssignments.size(); ++index)
			{
				ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignments.get(index));
				DateUnitEffortList thisDateUnitEffortList = resourceAssignment.getDateUnitEffortList();
				if (expectedDateUnitEffortList == null)
					expectedDateUnitEffortList = thisDateUnitEffortList;
				
				if (!expectedDateUnitEffortList.equals(thisDateUnitEffortList))
					return false;
			}
			
			return true;
	}
	
	@Override
	public boolean isChoiceItemColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if(columnTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
			return true;
		
		if(columnTag.equals(Indicator.TAG_PRIORITY))
			return true;
		
		if(columnTag.equals(CustomPlanningColumnsQuestion.META_THREAT_RATING))
			return true;
		
		return false;
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
											CustomPlanningColumnsQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE,
											CustomPlanningColumnsQuestion.META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE,
											CustomPlanningColumnsQuestion.META_PROJECT_RESOURCE_BUDGET_DETAILS_COLUMN_CODE,
											CustomPlanningColumnsQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE,
											CustomPlanningColumnsQuestion.META_FUNDING_SOURCE_EXPENSE_COLUMN_CODE,
											CustomPlanningColumnsQuestion.META_BUDGET_DETAIL_COLUMN_CODE,
											CustomPlanningColumnsQuestion.META_FUNDING_SOURCE_BUDGET_DETAILS_COLUMN_CODE,
											CustomPlanningColumnsQuestion.META_ACCOUNTING_CODE_BUDGET_DETAILS_COLUMN_CODE,
											CustomPlanningColumnsQuestion.META_ACCOUNTING_CODE_EXPENSE_COLUMN_CODE,
		};
		
		columnsToShow.subtract(new CodeList(codesToOmit));
	}
	
	public int getColumnCount()
	{
		return columnsToShow.size();
	}

	public String getColumnTag(int column)
	{
		return columnsToShow.get(column);
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
		boolean isAsteriskColumn = isWhenColumn(columnTag) || isWhoColumn(columnTag);
		if (!isAsteriskColumn)
			return false;
		
		return SummaryPlanningWorkPlanSubPanel.hasDataOutsideOfProjectDateRange(getProject());
	}
	
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		BaseObject baseObject = getBaseObjectForRow(row);
		if(baseObject == null)
			return new EmptyChoiceItem();
		
		try
		{	
			String columnTag = getTagForCell(baseObject.getType(), column);
			if(isWhoColumn(columnTag))
				return appendedProjectResources(baseObject);
			if (columnTag.equals(CustomPlanningColumnsQuestion.META_THREAT_RATING))
				return getRatingChoiceItem(baseObject);
			
			if (! baseObject.doesFieldExist(columnTag))
				return new EmptyChoiceItem();

			String rawValue = "";
			if (baseObject.isPseudoField(columnTag))
				rawValue = baseObject.getPseudoData(columnTag);
			else
				rawValue = baseObject.getData(columnTag);
			
			if (rawValue == null)
				return new EmptyChoiceItem();
			
			if(columnTag.equals(Indicator.TAG_PRIORITY))
				return new PriorityRatingQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(ProjectResource.TAG_RESOURCE_TYPE))
				return getProject().getQuestion(ResourceTypeQuestion.class).findChoiceByCode(rawValue);
			
			if(columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return new ProgressReportShortStatusQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
				return new StrategyRatingSummaryQuestion().findChoiceByCode(rawValue);
			
			if(isWhenColumn(columnTag))
				return getFilteredWhen(baseObject);
			
			return new TaglessChoiceItem(rawValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new TaglessChoiceItem(EAM.text("[Error]"));
		}
	}
	
	private ChoiceItem getRatingChoiceItem(BaseObject baseObject) throws Exception
	{
		if (Cause.is(baseObject))
			return getThreatRatingChoiceItem((Cause) baseObject);
		
		if (Target.is(baseObject))
			return getTargetViabilityRating((AbstractTarget) baseObject);
		
		return new EmptyChoiceItem();
	}

	private ChoiceItem getThreatRatingChoiceItem(Cause threat) throws Exception
	{
		if (threat.isContributingFactor())
			return new EmptyChoiceItem();
		
		return getProject().getThreatRatingFramework().getThreatThreatRatingValue(threat.getRef());
	}
	
	private ChoiceItem getTargetViabilityRating(AbstractTarget abstractTarget)
	{
		ChoiceQuestion question = getProject().getQuestion(StatusQuestion.class);
		
		return question.findChoiceByCode(abstractTarget.getTargetViability());
	}

	private ChoiceItem getFilteredWhen(BaseObject baseObject) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = calculateTimePeriodCostsMap(baseObject);
		DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
		DateRange rolledUpResourceAssignmentsDateRange = totalTimePeriodCostsMap.getRolledUpDateRange(projectStartEndDateRange, getResourcesFilter());
		String rolledUpResourceAssignmentsWhen = getProject().getProjectCalendar().convertToSafeString(rolledUpResourceAssignmentsDateRange);
		
		return new TaglessChoiceItem(rolledUpResourceAssignmentsWhen);
	}
	
	@Override
	protected TimePeriodCostsMap getTotalTimePeriodCostsMap(BaseObject baseObject) throws Exception
	{		
		return baseObject.getResourceAssignmentsTimePeriodCostsMap();
	}
	
	private ChoiceItem appendedProjectResources(BaseObject baseObject) throws Exception
	{
		TimePeriodCosts timePeriodCosts = calculateTimePeriodCosts(baseObject, new DateUnit());
		timePeriodCosts.filterProjectResources(getResourcesFilter());
		ORefSet filteredResources = new ORefSet(timePeriodCosts.getResourceRefSet());
		
		Vector<String> sortedNames = getSortedResourceNames(filteredResources);		
		String appendedResources = createAppendedResourceNames(sortedNames);
		
		return new TaglessChoiceItem(appendedResources);
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

	private Vector<String> getSortedResourceNames(ORefSet filteredResources)
	{
		Vector<String> sortedNames = new Vector<String>();
		for(ORef resourceRef : filteredResources)
		{
			sortedNames.add(getWhoName(resourceRef));
		}
		
		Collections.sort(sortedNames, new IgnoreCaseStringComparator());
		return sortedNames;
	}
	
	private String getWhoName(ORef resourceRef)
	{
		if (resourceRef.isInvalid())
			return Translation.getNotSpecifiedText();

		ProjectResource projectResource = ProjectResource.find(getProject(), resourceRef);
		return projectResource.getWho();	
	}

	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
	}

	@Override
	public String getTagForCell(int nodeType, int column)
	{
		String columnTag = getColumnTag(column);
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
		if(Strategy.is(nodeType))
		{
			if(columnTag.equals(Indicator.TAG_PRIORITY))
				return Strategy.PSEUDO_TAG_RATING_SUMMARY;
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
			if (isWhoColumn(columnTag))
				return ResourceAssignment.PSEUDO_TAG_PROJECT_RESOURCE_LABEL;
			if (columnTag.equals(Indicator.PSEUDO_TAG_FACTOR))
				return ResourceAssignment.PSEUDO_TAG_OWNING_FACTOR_NAME;
		}
		if(Factor.isFactor(nodeType))
		{
			if (isDetailsColumn(column))
				return Factor.TAG_TEXT;
		}
		
		return columnTag;
	}

	private boolean isDetailsColumn(int column)
	{
		return getColumnTag(column).equals(Desire.TAG_FULL_TEXT);
	}
	
	private CodeList getVisibleColumnCodes(Project projectToUse) throws Exception
	{
		return getRowColumnProvider().getColumnListToShow();
	}
	
	public RowColumnProvider getRowColumnProvider()
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
	private RowColumnProvider rowColumnProvider;
}
