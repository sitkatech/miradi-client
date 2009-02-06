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
package org.miradi.dialogs.indicator;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportStatusQuestion;


public class IndicatorPoolTableModel extends ObjectPoolTableModel
{
	public IndicatorPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.INDICATOR, COLUMN_TAGS);
	}
		
	public ChoiceQuestion getColumnQuestion(int column)
	{
		if (getColumnTag(column).equals(Indicator.TAG_PRIORITY))
			return getProject().getQuestion(PriorityRatingQuestion.class);
		if (getColumnTag(column).equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return getProject().getQuestion(ProgressReportStatusQuestion.class);
		
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
