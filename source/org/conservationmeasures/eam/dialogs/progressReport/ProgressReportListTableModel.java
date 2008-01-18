/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.progressReport;

import org.conservationmeasures.eam.dialogs.base.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ProgressReport;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.ProgressReportStatusQuestion;

public class ProgressReportListTableModel extends ObjectListTableModel
{
	public ProgressReportListTableModel(Project projectToUse, ORef nodeRef, String annotationTag)
	{
		super(projectToUse, nodeRef, annotationTag, ProgressReport.getObjectType(), COLUMN_TAGS);
		
		progressReportQuestion = new ProgressReportStatusQuestion(ProgressReport.TAG_PROGRESS_STATUS);
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
		ProgressReport.TAG_PROGRESS_STATUS,
		ProgressReport.TAG_PROGRESS_DATE,
		ProgressReport.TAG_COMMENTS,
	};
}
