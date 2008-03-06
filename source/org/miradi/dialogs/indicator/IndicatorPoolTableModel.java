/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.indicator;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.PriorityRatingQuestion;


public class IndicatorPoolTableModel extends ObjectPoolTableModel
{
	public IndicatorPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.INDICATOR, COLUMN_TAGS);
	}
		
	public ChoiceQuestion getColumnQuestion(int column)
	{
		if (getColumnTag(column).equals(Indicator.TAG_PRIORITY))
			return new PriorityRatingQuestion();
		
		return null;
	}

	private static final String[] COLUMN_TAGS = new String[] {
		Indicator.TAG_SHORT_LABEL,
		Indicator.TAG_LABEL,
		Indicator.TAG_PRIORITY,
		BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
		Indicator.PSEUDO_TAG_FACTOR,
		Indicator.PSEUDO_TAG_METHODS,
		Indicator.PSEUDO_TAG_TARGETS,
		Indicator.PSEUDO_TAG_DIRECT_THREATS,
	};

}
