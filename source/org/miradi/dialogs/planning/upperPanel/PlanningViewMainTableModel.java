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
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportShortStatusQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.OptionalDouble;
import org.miradi.utils.Translation;
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

	public Color getCellBackgroundColor(int column)
	{
		String columnTag = getColumnTag(column);
		
		if (isWhoColumn(columnTag))
			return AppPreferences.RESOURCE_TABLE_BACKGROUND;
		
		if (columnTag.equals(Indicator.PSEUDO_TAG_METHODS))
			return AppPreferences.INDICATOR_COLOR;
		
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
		
		return super.isCellEditable(row, modelColumn);
	}

	private boolean isWhoColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_WHO_TOTAL);
	}
	
	private boolean isWhenColumn(String columnTag)
	{
		return columnTag.equals(BaseObject.PSEUDO_TAG_WHEN_TOTAL);
	}
	
	private boolean isWhoCellEditable(int row, int modelColumn)
	{
		try
		{
			BaseObject baseObjectForRow = getBaseObjectForRowColumn(row, modelColumn);
			if (!AssignmentDateUnitsTableModel.canReferToAssignments(baseObjectForRow.getRef()))
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
		if (!SummaryPlanningWorkPlanSubPanel.hasDataOutsideOfProjectDateRange(getProject()))
			return false;
		
		if (isWhenColumn(columnTag))
			return true;
		
		return isWhoColumn(columnTag);
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
				return getThreatRatingChoiceItem(baseObject);
			
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
	
	private ChoiceItem getThreatRatingChoiceItem(BaseObject baseObject) throws Exception
	{
		if (!Cause.is(baseObject))
			return new EmptyChoiceItem();
		
		Cause threat = (Cause) baseObject;
		if (threat.isContributingFactor())
			return new EmptyChoiceItem();
		
		return getProject().getThreatRatingFramework().getThreatThreatRatingValue(threat.getRef());
	}

	private ChoiceItem getFilteredWhen(BaseObject baseObject) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = calculateTimePeriodCostsMap(baseObject);
		DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
		DateRange rolledUpResourceAssignmentsDateRange = totalTimePeriodCostsMap.getRolledUpDateRange(projectStartEndDateRange, getResourcesFilter());
		String rolledUpResourceAssignmentsWhen = convertToSafeString(rolledUpResourceAssignmentsDateRange);
		
		return new TaglessChoiceItem(rolledUpResourceAssignmentsWhen);
	}
	
	@Override
	protected TimePeriodCostsMap getTotalTimePeriodCostsMap(BaseObject baseObject) throws Exception
	{		
		return baseObject.getResourceAssignmentsTimePeriodCostsMap();
	}
	
	public String convertToSafeString(DateRange combinedDateRange)
	{
		if (combinedDateRange == null)
			return "";
		
		return  getProject().getProjectCalendar().getDateRangeName(combinedDateRange);
	}
	
	private ChoiceItem appendedProjectResources(BaseObject baseObject) throws Exception
	{
		TimePeriodCosts timePeriodCosts = calculateTimePeriodCosts(baseObject, new DateUnit());
		timePeriodCosts.filterProjectResources(getResourcesFilter());
		ORefSet filteredResources = new ORefSet(timePeriodCosts.getResourceRefSet());
		
		boolean isFirstIteration = true; 
		String appendedResources = "";
		for(ORef resourceRef : filteredResources)
		{
			if (!isFirstIteration)
				appendedResources += ", ";
			
			appendedResources += getWhoName(resourceRef);
			isFirstIteration = false;	
		}
		
		return new TaglessChoiceItem(appendedResources);
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
		if (AbstractTarget.isAbstractTarget(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
			if(isDetailsColumn(column))
				return AbstractTarget.TAG_TEXT;	
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
			if(isDetailsColumn(column))
				return Cause.TAG_TEXT;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}

		if(ThreatReductionResult.is(nodeType))
		{
			if(isDetailsColumn(column))
				return ThreatReductionResult.TAG_TEXT;
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
			if (isDetailsColumn(column))
				return Strategy.TAG_TEXT;
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
