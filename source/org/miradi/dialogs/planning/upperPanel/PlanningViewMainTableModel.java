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
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Desire;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.CodeList;

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
		
		if (columnTag.equals(BaseObject.PSEUDO_TAG_WHO_TOTAL))
			return AppPreferences.RESOURCE_TABLE_BACKGROUND;
		
		if (columnTag.equals(Indicator.PSEUDO_TAG_METHODS))
			return AppPreferences.INDICATOR_COLOR;
		
		if(columnTag.equals(BaseObject.PSEUDO_TAG_WHEN_TOTAL))
			return AppPreferences.WORKPLAN_TABLE_BACKGROUND;
		
		if(columnTag.equals(Task.PSEUDO_TAG_BUDGET_TOTAL))
			return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
		
		if(columnTag.equals(Assignment.PSEUDO_TAG_WORK_UNIT_TOTAL))
			return AppPreferences.WORKPLAN_TABLE_BACKGROUND;
			
		return null;
	}
	
	@Override
	public boolean isCurrencyColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if(columnTag.equals(Task.PSEUDO_TAG_BUDGET_TOTAL))
			return true;
		
		return false;
	}
	
	@Override
	public boolean isChoiceItemColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if(columnTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
			return true;
		
		if(columnTag.equals(Indicator.TAG_PRIORITY))
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
		if (columnsToShow.contains(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL))
			columnsToShow.removeCode(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL);
		
		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
			columnsToShow.removeCode(Measurement.META_COLUMN_TAG);
		
		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
			columnsToShow.removeCode(Indicator.META_COLUMN_TAG);
	}

	public int getColumnCount()
	{
		return columnsToShow.size();
	}

	@Override
	public String getColumnTag(int column)
	{
		return columnsToShow.get(column);
	}
	
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.FAKE, getColumnTag(column));
	}
	
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		BaseObject baseObject = getBaseObjectForRow(row);
		if(baseObject == null)
			return new EmptyChoiceItem();
		
		try
		{	
			String columnTag = getColumnTagForNode(baseObject.getType(), column);
			if (! baseObject.doesFieldExist(columnTag))
				return new EmptyChoiceItem();

			String rawValue = "";
			if (baseObject.isPseudoField(columnTag))
				rawValue = baseObject.getPseudoData(columnTag);
			else
				rawValue = baseObject.getData(columnTag);
			
			if(columnTag.equals(Indicator.TAG_PRIORITY))
				return new PriorityRatingQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return new ProgressReportStatusQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
				return new StrategyRatingSummaryQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(BaseObject.PSEUDO_TAG_BUDGET_TOTAL))
				return new TaglessChoiceItem(calculateProportion(row, rawValue));
				
			return new TaglessChoiceItem(rawValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new TaglessChoiceItem(EAM.text("[Error]"));
		}
	}
	
	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
	}

	private double calculateProportion(int row, String rawValue)
	{
		if(rawValue == null || rawValue.length() == 0)
			return 0.0;
		
		Double value = Double.parseDouble(rawValue);
		int totalShares = getBaseObjectForRow(row).getTotalShareCount();
		int proportionShares = getProportionShares(row);
		return value * proportionShares / totalShares;
	}

	private String getColumnTagForNode(int nodeType, int column)
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

		if(Target.is(nodeType))
		{
			if(isDetailsColumn(column))
				return Target.TAG_TEXT;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
			if (columnTag.equals(Factor.PSEUDO_TAG_TAXONOMY_CODE_VALUE))
				return Target.PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE;
		}
		
		if(Desire.isDesire(nodeType))
		{
			if (columnTag.equals(Factor.TAG_COMMENT))
				return Desire.TAG_COMMENTS;
		}
		
		if(Goal.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
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
		
		if(Assignment.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_WHO_TOTAL))
				return Assignment.PSEUDO_TAG_PROJECT_RESOURCE_LABEL;
			if (columnTag.equals(Indicator.PSEUDO_TAG_FACTOR))
				return Assignment.PSEUDO_TAG_OWNING_FACTOR_NAME;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_WHEN_TOTAL))
				return Assignment.PSEUDO_TAG_WHEN;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_BUDGET_TOTAL))
				return Assignment.PSEUDO_TAG_BUDGET_TOTAL;				
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

	private CodeList columnsToShow;
	private RowColumnProvider rowColumnProvider;
}
