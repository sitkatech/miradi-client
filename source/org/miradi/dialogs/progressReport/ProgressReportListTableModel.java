/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.progressReport;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProgressReport;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ProgressReportStatusQuestion;

public class ProgressReportListTableModel extends ObjectListTableModel
{
	public ProgressReportListTableModel(Project projectToUse, ORef nodeRef, String annotationTag)
	{
		super(projectToUse, nodeRef, annotationTag, ProgressReport.getObjectType(), COLUMN_TAGS);
		
		progressReportQuestion = new ProgressReportStatusQuestion();
	}

	public boolean isChoiceItemColumn(int column)
	{
		if (getColumnTag(column).equals(ProgressReport.TAG_PROGRESS_STATUS))
			return true;
		
		return false;
	}

	public ChoiceQuestion getColumnQuestion(int column)
	{
		if (getColumnTag(column).equals(ProgressReport.TAG_PROGRESS_STATUS))
			return progressReportQuestion;
		
		return super.getColumnQuestion(column);
	}
	
	private ProgressReportStatusQuestion progressReportQuestion; 
	
	public static final String[] COLUMN_TAGS = new String[] {
		ProgressReport.TAG_PROGRESS_DATE,
		ProgressReport.TAG_PROGRESS_STATUS,
		ProgressReport.TAG_DETAILS,
	};
}
