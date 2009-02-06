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
package org.miradi.dialogs.viability.nodes;

import javax.swing.Icon;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TextAndIconChoiceItem;
import org.miradi.questions.TrendQuestion;

public class ViabilityMeasurementNode extends TreeTableNode
{
	public ViabilityMeasurementNode(ViabilityIndicatorNode parent, Measurement measurementToUse)
	{
		measurement = measurementToUse;
		parentNode = parent;
	}
	
	public BaseObject getObject()
	{
		return measurement;
	}
	
	public Measurement getMeasurement()
	{
		return (Measurement) getObject();
	}

	public ORef getObjectReference()
	{
		return measurement.getRef();
	}
	
	public int getType()
	{
		return measurement.getType();
	}

	@Override
	public String toRawString()
	{
		return measurement.toString();
	}

	public int getChildCount()
	{
		return 0;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}
	
	public TreeTableNode getParentNode()
	{
		return parentNode;
	}

	//TODO: this method could be pulled up to the supper
	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		String statusData = getObject().getData(Measurement.TAG_STATUS);
		String summaryData = getObject().getData(Measurement.TAG_SUMMARY);
		TextAndIconChoiceItem textAndIconChoiceItem = new TextAndIconChoiceItem(summaryData, getTrendIcon());		
		if (tag.equals(POOR) && StatusQuestion.POOR.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(FAIR) && StatusQuestion.FAIR.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(GOOD) && StatusQuestion.GOOD.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(VERY_GOOD) && StatusQuestion.VERY_GOOD.equals(statusData))
			return textAndIconChoiceItem;
		
		if (tag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return createStatusConfidenceChoiceItem(tag);
		
		return new EmptyChoiceItem();
	}

	private Object createStatusConfidenceChoiceItem(String tag)
	{
		String rawValue = getObject().getData(tag);
		return getProject().getQuestion(StatusConfidenceQuestion.class).findChoiceByCode(rawValue);
	}

	public Icon getTrendIcon()
	{
		String trendData = getObject().getData(Measurement.TAG_TREND);
		ChoiceQuestion trendQuestion = getProject().getQuestion(TrendQuestion.class);
		ChoiceItem findChoiceByCode = trendQuestion.findChoiceByCode(trendData);
		
		return findChoiceByCode.getIcon();
	}
	
	private Project getProject()
	{
		return getObject().getProject();
	}
	
	public void rebuild() throws Exception
	{
	}
	
	public static final String POOR = StatusQuestion.POOR;
	public static final String FAIR = StatusQuestion.FAIR;
	public static final String GOOD = StatusQuestion.GOOD;
	public static final String VERY_GOOD = StatusQuestion.VERY_GOOD;
	
	public static final String[] COLUMN_TAGS = {Measurement.TAG_EMPTY, 
												Measurement.TAG_EMPTY,
												Measurement.TAG_EMPTY,
												Measurement.TAG_EMPTY,
												POOR,
												FAIR,
												GOOD,
												VERY_GOOD,
												Measurement.TAG_STATUS_CONFIDENCE,
												Measurement.TAG_EMPTY,};
	
	private Measurement measurement;
	private ViabilityIndicatorNode parentNode;	
}
