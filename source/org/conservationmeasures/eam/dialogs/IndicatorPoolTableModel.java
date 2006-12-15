/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;


public class IndicatorPoolTableModel extends ObjectPoolTableModel
{
	public IndicatorPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.INDICATOR, COLUMN_TAGS);
	}
	
	public Object getValueAt(int row, int column)
	{
		String rawValue = (String)super.getValueAt(row, column);
		String tag = COLUMN_TAGS[column];
		if(tag.equals(Indicator.TAG_PRIORITY))
			return new PriorityRatingQuestion(tag).findChoiceByCode(rawValue).getLabel();
		if(tag.equals(Indicator.TAG_STATUS))
			return new IndicatorStatusRatingQuestion(tag).findChoiceByCode(rawValue).getLabel();
		
		return rawValue;
	}



	private static final String[] COLUMN_TAGS = new String[] {
		Indicator.TAG_SHORT_LABEL,
		Indicator.TAG_LABEL,
		Indicator.PSEUDO_TAG_FACTOR,
		Indicator.TAG_PRIORITY,
		Indicator.TAG_STATUS,
	};

}
