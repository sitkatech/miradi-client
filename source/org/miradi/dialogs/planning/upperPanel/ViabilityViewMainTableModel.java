/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.Icon;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.icons.GoalIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.ProgressReportShortStatusQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.questions.TextAndIconChoiceItem;
import org.miradi.questions.TrendQuestion;
import org.miradi.questions.ViabilityModeQuestion;

public class ViabilityViewMainTableModel extends PlanningViewMainTableModel
{
	public ViabilityViewMainTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse) throws Exception
	{
		super(projectToUse, providerToUse, rowColumnProviderToUse);
	}
	
	@Override
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		BaseObject baseObject = getBaseObjectForRow(row);
		if (Target.is(baseObject))
			return getValueForTarget(baseObject, row, column);
		
		if (KeyEcologicalAttribute.is(baseObject))
			return getValueForKea((KeyEcologicalAttribute) baseObject, row, column);
		
		if (Indicator.is(baseObject))
			return getValueForIndicator(baseObject, row, column);
		
		if (Measurement.is(baseObject))
			return valueForMeasurement(baseObject, row, column);
		
		if (Goal.is(baseObject))
			return getValueForFutureResultAsGoal(baseObject, row, column);
		
		return super.getChoiceItemAt(row, column);
	}
	
	private ChoiceItem getValueForKea(KeyEcologicalAttribute kea, int row, int column)
	{
		String tag = COLUMN_TAGS_KEAS[column];
		String rawValue = kea.getData(tag);
		if (tag.equals(KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS))
			return getStatusQuestion().findChoiceByCode(rawValue);
		
		if (tag.equals(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE))
			return new KeyEcologicalAttributeTypeQuestion().findChoiceByCode(rawValue);
		
		if(tag.equals(KeyEcologicalAttribute.TAG_EMPTY))
			return new EmptyChoiceItem();
		
		return new TaglessChoiceItem(kea.getData(tag));
	}

	private ChoiceItem getValueForFutureResultAsGoal(BaseObject baseObject, int row, int column)
	{
		String tag = COLUMN_TAGS_FOR_FUTURE_RESULTS[column];
		ORefList objectHiearchy = getRowColumnObjectProvider().getObjectHiearchy(row, column);
		ORef indicatorRef = objectHiearchy.getRefForType(Indicator.getObjectType());
		Indicator indicatorAsParent = Indicator.find(getProject(), indicatorRef);
		String summaryData = indicatorAsParent.getData(Indicator.TAG_FUTURE_STATUS_SUMMARY);
		String statusData = indicatorAsParent.getData(Indicator.TAG_FUTURE_STATUS_RATING);
		TextAndIconChoiceItem textAndIconChoiceItem = new TextAndIconChoiceItem(summaryData, new GoalIcon());		
		if (tag.equals(StatusQuestion.POOR) && StatusQuestion.POOR.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(StatusQuestion.FAIR) && StatusQuestion.FAIR.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(StatusQuestion.GOOD) && StatusQuestion.GOOD.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(StatusQuestion.VERY_GOOD) && StatusQuestion.VERY_GOOD.equals(statusData))
			return textAndIconChoiceItem;
		
		return new EmptyChoiceItem();
	}

	private ChoiceItem getValueForIndicator(BaseObject baseObject, int row,	int column)
	{
		String tag = COLUMN_TAGS_FOR_INDICATORS[column];
		if (tag.equals(Indicator.PSEUDO_TAG_STATUS_VALUE) && isViabilityIndicator((Indicator) baseObject))
			return getStatusQuestion().findChoiceByCode(baseObject.getPseudoData(tag));
		
		if (tag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return new ProgressReportShortStatusQuestion().findChoiceByCode(baseObject.getPseudoData(tag));
		
		if(tag.equals(Indicator.TAG_EMPTY))
			return new EmptyChoiceItem();
		
		String data = baseObject.getData(tag);
		
		if(tag.equals(Indicator.TAG_RATING_SOURCE) && isViabilityIndicator((Indicator) baseObject))
			return new RatingSourceQuestion().findChoiceByCode(data);
		
		if (tag.equals(Indicator.TAG_INDICATOR_THRESHOLD))
		{
			int threasholdColumn = (column + 1) - getFirstIndexOfThreshold();
			String threashold = ((Indicator)baseObject).getThreshold().getStringMap().get(Integer.toString(threasholdColumn));
			
			return new TaglessChoiceItem(threashold);
		}
		
		return new TaglessChoiceItem(data);

	}

	private ChoiceItem getValueForTarget(BaseObject baseObject, int row, int column)
	{
		String tag = COLUMN_TAGS_FOR_TARGETS[column];
		String rawValue = baseObject.getData(tag);

		if(tag.equals(Target.TAG_VIABILITY_MODE))
			return new ViabilityModeQuestion().findChoiceByCode(rawValue);
		
		if (tag.equals(Target.PSEUDO_TAG_TARGET_VIABILITY))
			return getStatusQuestion().findChoiceByCode(rawValue);
		
		if(tag.equals(Target.TAG_EMPTY))
			return new EmptyChoiceItem();
		
		return new EmptyChoiceItem();

	}

	private ChoiceQuestion getStatusQuestion()
	{
		return StaticQuestionManager.getQuestion(StatusQuestion.class);
	}

	private ChoiceItem  valueForMeasurement(BaseObject baseObject, int row, int column)
	{
		String tag = COLUMN_TAGS_FOR_MEASUREMENTS[column];
		String statusData = baseObject.getData(Measurement.TAG_STATUS);
		String summaryData = baseObject.getData(Measurement.TAG_SUMMARY);
		TextAndIconChoiceItem textAndIconChoiceItem = new TextAndIconChoiceItem(summaryData, getTrendIcon(baseObject));		
		if (tag.equals(POOR) && StatusQuestion.POOR.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(FAIR) && StatusQuestion.FAIR.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(GOOD) && StatusQuestion.GOOD.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(VERY_GOOD) && StatusQuestion.VERY_GOOD.equals(statusData))
			return textAndIconChoiceItem;
		
		if (tag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return createStatusConfidenceChoiceItem(baseObject, tag);
		
		return new EmptyChoiceItem();

	}

	@Override
	public String getColumnName(int column)
	{
		String columnTag = getColumnTag(column);
		if(isChoiceItemColumn(columnTag))
			return getColumnChoiceItem(columnTag).getLabel();
		
		return EAM.fieldLabel(getObjectTypeForColumnLabel(columnTag), columnTag);
	}
	
	private int getObjectTypeForColumnLabel(String tag)
	{
		if(tag.equals(Target.TAG_VIABILITY_MODE))
			return Target.getObjectType();
			
		else if(tag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return Measurement.getObjectType();
			
		else if (tag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return Indicator.getObjectType();
		
		else if(tag.equals(Measurement.TAG_SUMMARY))
			return Measurement.getObjectType();
		
		return KeyEcologicalAttribute.getObjectType();
	}
	
	@Override
	public boolean isChoiceItemColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if(columnTag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return true;
		
		if(columnTag.equals(Indicator.TAG_RATING_SOURCE))
			return true;
		
		if(columnTag.equals(Measurement.TAG_STATUS))
			return true;

		if(columnTag.equals(AbstractTarget.TAG_VIABILITY_MODE))
			return true;

		if (isChoiceItemColumn(columnTag))
			return true;
		
		return super.isChoiceItemColumn(column);
	}
	
	private boolean isChoiceItemColumn(String columnTag)
	{
		ChoiceItem choiceItem = getStatusQuestion().findChoiceByCode(columnTag);
		return (choiceItem != null);
	}

	private ChoiceItem getColumnChoiceItem(String columnTag)
	{
		return getStatusQuestion().findChoiceByCode(columnTag);	
	}
	
	private ChoiceItem createStatusConfidenceChoiceItem(BaseObject baseObject, String tag)
	{
		String rawValue = baseObject.getData(tag);
		return getProject().getQuestion(StatusConfidenceQuestion.class).findChoiceByCode(rawValue);
	}

	private Icon getTrendIcon(BaseObject baseObject)
	{
		String trendData = baseObject.getData(Measurement.TAG_TREND);
		ChoiceQuestion trendQuestion = getProject().getQuestion(TrendQuestion.class);
		ChoiceItem findChoiceByCode = trendQuestion.findChoiceByCode(trendData);
		
		return findChoiceByCode.getIcon();
	}
	
	private int getFirstIndexOfThreshold()
	{
		for (int i = 0; i < COLUMN_TAGS_FOR_INDICATORS.length; ++i)
		{
			if (COLUMN_TAGS_FOR_INDICATORS[i].equals(Indicator.TAG_INDICATOR_THRESHOLD))
				return i;
		}
		
		throw new RuntimeException("Could not find Threshold in array.");
	}
	
	private boolean isViabilityIndicator(Indicator indicator)
	{
		return indicator.isViabilityIndicator();
	}
	
	public static final String POOR = StatusQuestion.POOR;
	public static final String FAIR = StatusQuestion.FAIR;
	public static final String GOOD = StatusQuestion.GOOD;
	public static final String VERY_GOOD = StatusQuestion.VERY_GOOD;
	
	public static final String[] COLUMN_TAGS_FOR_INDICATORS = {
		Indicator.TAG_EMPTY,
		Indicator.PSEUDO_TAG_STATUS_VALUE,
		Indicator.TAG_EMPTY,
		
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		
		Indicator.TAG_RATING_SOURCE,
		BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
	};
	
	public static final String[] COLUMN_TAGS_FOR_TARGETS = {
		Target.TAG_VIABILITY_MODE, 
		Target.PSEUDO_TAG_TARGET_VIABILITY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		Target.TAG_EMPTY,
		};
	
	public static final String[] COLUMN_TAGS_KEAS = {
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS, 
		KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		KeyEcologicalAttribute.TAG_EMPTY,
		};
	
	public static final String[] COLUMN_TAGS_FOR_MEASUREMENTS = { 
												Measurement.TAG_EMPTY,
												Measurement.TAG_EMPTY,
												Measurement.TAG_EMPTY,
												POOR,
												FAIR,
												GOOD,
												VERY_GOOD,
												Measurement.TAG_STATUS_CONFIDENCE,
												Measurement.TAG_EMPTY,};
	
	public static final String[] COLUMN_TAGS_FOR_FUTURE_RESULTS = {
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		
		StatusQuestion.POOR,
		StatusQuestion.FAIR,
		StatusQuestion.GOOD,
	    StatusQuestion.VERY_GOOD,

		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
	};
}
