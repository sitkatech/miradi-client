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

import org.miradi.dialogfields.editors.TimeframeEditorComponent;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.*;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateRange;

import java.awt.*;

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
		columnsToShow = new CodeList(getVisibleColumnCodes());
		omitColumnTagsRepresentedByColumnTables();
		fireTableStructureChanged();
	}

	@Override
	public Color getCellBackgroundColor(int row, int column)
	{
		String columnTag = getColumnTag(column);
		
		if (isTimeframeColumn(columnTag))
			return AppPreferences.RESOURCE_TABLE_BACKGROUND;
		
		if(isAssignedWhoColumn(columnTag) || isAssignedWhenColumn(columnTag))
			return AppPreferences.getWorkUnitsBackgroundColor();
		
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int modelColumn)
	{
		String columnTag = getColumnTag(modelColumn);

		if (isTimeframeColumn(columnTag))
			return isTimeframeCellEditable(row, modelColumn);
		
		if (isAssignedWhoColumn(columnTag))
			return new WhoAssignedStateLogic().isWhoCellEditable(getBaseObjectForRowColumn(row, modelColumn));

		if (isAssignedWhenColumn(columnTag))
			return false;

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

	private boolean isTimeframeColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_TIMEFRAME_TOTAL);
	}

	private boolean isAssignedWhoColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_ASSIGNED_WHO_TOTAL) || columnTag.equals(ResourceAssignment.PSEUDO_TAG_PROJECT_RESOURCE_LABEL);
	}
	
	private boolean isAssignedWhenColumn(String columnTag)
	{
		return columnTag.equals(CustomPlanningColumnsQuestion.META_ASSIGNED_WHEN_TOTAL);
	}

	@Override
	public boolean isTimeframeColumn(int modelColumn)
	{
		return isTimeframeColumn(getColumnTag(modelColumn));
	}

	private boolean isTimeframeCellEditable(int row, int modelColumn)
	{
		BaseObject baseObjectForRow = getBaseObjectForRowColumn(row, modelColumn);
		return baseObjectForRow.isTimeframeEditable();
	}

	@Override
	public boolean isAssignedWhenColumn(int modelColumn)
	{
		return isAssignedWhenColumn(getColumnTag(modelColumn));
	}

	@Override
	public boolean isAssignedWhoColumn(int modelColumn)
	{
		return isAssignedWhoColumn(getColumnTag(modelColumn));
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

			if (isTimeframeColumn(column))
			{
				TimeframeEditorComponent.setTimeframeValue(getProject(), baseObjectForRow, TimeframeEditorComponent.createCodeList(value));
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
	public boolean isAppendedLabelsOnSingleLineColumn(int column)
	{
		String columnTag = getColumnTag(column);

		if (columnTag.equals(Factor.PSEUDO_TAG_INDICATORS))
			return true;

		if (columnTag.equals(Desire.PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
			return true;

		if (columnTag.equals(Desire.PSEUDO_TAG_RELEVANT_ACTIVITY_REFS))
			return true;

		if (columnTag.equals(Factor.PSEUDO_TAG_ACTIVITIES))
			return true;

		if (columnTag.equals(Strategy.PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
			return true;

		if (columnTag.equals(Task.PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
			return true;

		if (columnTag.equals(Indicator.PSEUDO_TAG_METHODS))
			return true;

		if (columnTag.equals(WorkPlanColumnConfigurationQuestion.INDICATORS_COLUMN_CODE))
			return true;

		return false;
	}

	@Override
	public boolean isProgressColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if(columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
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

		if (isAssignedWhoColumn(columnTag))
			return EAM.text("People");

		return EAM.fieldLabel(ObjectType.FAKE, columnTag);
	}
	
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		BaseObject baseObject = getBaseObjectForRow(row);
		if(baseObject == null)
			return new EmptyChoiceItem();
		
		try
		{	
			String columnTag = getTagForCell(baseObject.getType(), column);

			if(isAssignedWhoColumn(columnTag))
				return getAssignedProjectResourcesAsChoiceItem(baseObject);

			if (columnTag.equals(CustomPlanningColumnsQuestion.META_CURRENT_RATING))
				return getRatingChoiceItem(baseObject);
			
			if (! baseObject.doesFieldExist(columnTag))
				return new EmptyChoiceItem();

			String rawValue;

			if(columnTag.equals(BaseObject.PSEUDO_TAG_TIMEFRAME_TOTAL))
				rawValue = getProject().getTimePeriodCostsMapsCache().getTimeframeTotalAsString(baseObject);
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
				return createAppendedRelevantIndicatorLabels(baseObject);
			
			if (Desire.isDesire(baseObject.getRef()) && columnTag.equals(Desire.PSEUDO_TAG_RELEVANT_ACTIVITY_REFS))
				return createAppendedRelevantActivityLabels(baseObject);

			if (Indicator.is(baseObject.getRef()) && columnTag.equals(Indicator.PSEUDO_TAG_RELEVANT_ACTIVITY_REFS))
				return createAppendedRelevantActivityLabels(baseObject);

			if (Strategy.is(baseObject.getRef()) && columnTag.equals(Strategy.PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
				return createAppendedRelevantIndicatorLabels(baseObject);

			if (Task.is(baseObject.getRef()) && columnTag.equals(Task.PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
				return createAppendedRelevantIndicatorLabels(baseObject);

			if(isTimeframeColumn(columnTag))
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
	
	private ChoiceItem createAppendedRelevantActivityLabels(BaseObject baseObject) throws Exception
	{
		return createLabelsOnSingleLine(baseObject, baseObject.getRelevantActivityRefs());
	}

	protected ChoiceItem createAppendedRelevantIndicatorLabels(BaseObject baseObject) throws Exception
	{
		return createLabelsOnSingleLine(baseObject, baseObject.getRelevantIndicatorRefList());
	}
	
	private ChoiceItem createLabelsOnSingleLine(BaseObject baseObject, ORefList refs)
	{
		String labelsOnASingleLine = baseObject.getLabelsAsMultiline(refs);
		
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
		TimePeriodCostsMap totalTimePeriodCostsMap = calculateTimePeriodPlannedCostsMap(baseObject);
		return getFilteredWhen(totalTimePeriodCostsMap, new ORefSet());
	}

	private ChoiceItem getFilteredWhenForAssignments(BaseObject baseObject) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = calculateTimePeriodAssignedCostsMap(baseObject);
		return getFilteredWhen(totalTimePeriodCostsMap, getResourcesFilter());
	}

	private ChoiceItem getFilteredWhen(TimePeriodCostsMap totalTimePeriodCostsMap, ORefSet resourcesToFilterBy) throws Exception
	{
		DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
		DateRange rolledUpDateRangeForResources = totalTimePeriodCostsMap.getRolledUpDateRange(projectStartEndDateRange, resourcesToFilterBy);
		String rolledUpResourcesWhen = getProject().getProjectCalendar().convertToSafeString(rolledUpDateRangeForResources);

		return new TaglessChoiceItem(rolledUpResourcesWhen);
	}

	@Override
	protected TimePeriodCostsMap getTotalTimePeriodPlannedCostsMap(BaseObject baseObject) throws Exception
	{
		return baseObject.getTimeframesTimePeriodCostsMap();
	}
	
	@Override
	protected TimePeriodCostsMap getTotalTimePeriodAssignedCostsMap(BaseObject baseObject) throws Exception
	{
		return baseObject.getResourceAssignmentsTimePeriodCostsMap();
	}

	private ChoiceItem getAssignedProjectResourcesAsChoiceItem(BaseObject baseObject) throws Exception
	{
		TimePeriodCosts timePeriodCosts = calculateTimePeriodAssignedCosts(baseObject, new DateUnit());
		return baseObject.getAssignedProjectResourcesAsChoiceItem(timePeriodCosts, BaseObject.TAG_ASSIGNED_LEADER_RESOURCE, getResourcesFilter());
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
		if (isIndicatorsColumn(column))
		{
			columnTag = Factor.PSEUDO_TAG_INDICATORS;
		}
		if(ConceptualModelDiagram.is(nodeType))
		{
			if(isDetailsColumn(column))
				return ConceptualModelDiagram.TAG_DETAIL;
		}
		if(ResultsChainDiagram.is(nodeType))
		{
			if(isDetailsColumn(column))
				return ResultsChainDiagram.TAG_DETAIL;
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
		if(Strategy.is(nodeType))
		{
			if (columnTag.equals(Factor.PSEUDO_TAG_INDICATORS))
				return Strategy.PSEUDO_TAG_RELEVANT_INDICATOR_REFS;
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
			if (columnTag.equals(Factor.PSEUDO_TAG_INDICATORS))
				return Task.PSEUDO_TAG_RELEVANT_INDICATOR_REFS;
		}
		if(Indicator.is(nodeType))
		{
			if (isDetailsColumn(column))
				return Indicator.TAG_DETAIL;

			if (columnTag.equals(Factor.PSEUDO_TAG_ACTIVITIES))
				return Indicator.PSEUDO_TAG_RELEVANT_ACTIVITY_REFS;
		}
		if(Method.is(nodeType))
		{
			if (isDetailsColumn(column))
				return Method.TAG_DETAILS;
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
		if(Timeframe.is(nodeType))
		{
			if (columnTag.equals(Indicator.PSEUDO_TAG_FACTOR))
				return Timeframe.PSEUDO_TAG_OWNING_FACTOR_NAME;
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

	private boolean isIndicatorsColumn(int column)
	{
		return getColumnTag(column).equals(WorkPlanColumnConfigurationQuestion.INDICATORS_COLUMN_CODE);
	}

	private CodeList getVisibleColumnCodes() throws Exception
	{
		return getRowColumnProvider().getColumnCodesToShow();
	}
	
	protected PlanningTreeRowColumnProvider getRowColumnProvider()
	{
		return rowColumnProvider;
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
				
	private static final String UNIQUE_MODEL_IDENTIFIER = "PlanningViewMainTableModel";
	
	private CodeList columnsToShow;
	private PlanningTreeRowColumnProvider rowColumnProvider;
}
