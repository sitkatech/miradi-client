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

import java.awt.Color;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.viability.ViabilityTreeModel;
import org.miradi.dialogs.viability.nodes.ViabilityMeasurementNode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.ProgressReportShortStatusQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TaglessChoiceItem;


//FIXME - urgent - TargetViability -  this is not hooked anywhere yet
public class TargetViabilityTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public TargetViabilityTableModel(Project projectToUse, RowColumnBaseObjectProvider adapterToUse) throws Exception
	{
		super(projectToUse, adapterToUse);
		
		statusQuestion = new StatusQuestion();
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}
	
	public String getColumnTag(int column)
	{
		return columnTags[column];
	}
	
	@Override
	public String getColumnName(int column)
	{
		String columnTag = getColumnTag(column);
		if(isChoiceItemColumn(column))
			return getColumnChoiceItem(columnTag).getLabel();
		
		return columnTag;
	}
	
	public boolean isChoiceItemColumn(String columnTag)
	{
		ChoiceItem choiceItem = statusQuestion.findChoiceByCode(columnTag);
		return (choiceItem != null);
	}

	private ChoiceItem getColumnChoiceItem(String columnTag)
	{
		return statusQuestion.findChoiceByCode(columnTag);	
	}
	
	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
	}

	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		String tag = getColumnTag(column);
		BaseObject baseObject = getBaseObjectForRow(row);
		if (Indicator.is(baseObject))
		{
			return getIndicatorData(column, tag, (Indicator)baseObject);
		}
		
		return new EmptyChoiceItem();

	}

	public ChoiceItem getIndicatorData(int column, String tag, Indicator indicator)
	{
		if (!indicator.getStoredFieldTags().contains(tag))
			return new EmptyChoiceItem();
		
		if (tag.equals(Indicator.PSEUDO_TAG_STATUS_VALUE) && isViabilityIndicator(indicator))
			return new StatusQuestion().findChoiceByCode(indicator.getPseudoData(tag));
		
		if (tag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return new ProgressReportShortStatusQuestion().findChoiceByCode(indicator.getPseudoData(tag));
		
		if(tag.equals(Indicator.TAG_EMPTY))
			return new EmptyChoiceItem();
		
		String data = indicator.getData(tag);
		
		if(tag.equals(Indicator.TAG_RATING_SOURCE) && isViabilityIndicator(indicator))
			return new RatingSourceQuestion().findChoiceByCode(data);
		
		if (tag.equals(Indicator.TAG_INDICATOR_THRESHOLD))
		{
			int threasholdColumn = (column + 1) - getFirstIndexOfThreshold();
			String threashold = indicator.getThreshold().getStringMap().get(Integer.toString(threasholdColumn));
			
			return new TaglessChoiceItem(threashold);
		}
		
		return new EmptyChoiceItem();
	}
	

	private boolean isViabilityIndicator(BaseObject baseObject)
	{
		if (!Indicator.is(baseObject))
			return false;
		return ((Indicator) baseObject).isViabilityIndicator();
	}
	
	private int getFirstIndexOfThreshold()
	{
		for (int i = 0; i < columnTags.length; ++i)
		{
			if (columnTags[i].equals(Indicator.TAG_INDICATOR_THRESHOLD))
				return i;
		}
		
		throw new RuntimeException("Could not find Threshold in array.");
	}

	@Override
	public Color getCellBackgroundColor(int column)
	{
		return Color.WHITE;
	}
	
	public void updateColumnsToShow() throws Exception
	{
		fireTableStructureChanged();
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
				
	private StatusQuestion statusQuestion;
	private static final String UNIQUE_MODEL_IDENTIFIER = "TargetViabilityTableModel";

	public static String[] columnTags = { 
		 Target.TAG_VIABILITY_MODE,
		 ViabilityTreeModel.VIRTUAL_TAG_STATUS,
		 KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE,
		 ViabilityMeasurementNode.POOR,
		 ViabilityMeasurementNode.FAIR,
		 ViabilityMeasurementNode.GOOD,
		 ViabilityMeasurementNode.VERY_GOOD,
		 Measurement.TAG_STATUS_CONFIDENCE,
		 BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,};
}
