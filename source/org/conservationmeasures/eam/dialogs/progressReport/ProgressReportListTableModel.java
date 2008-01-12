/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.progressReport;

import org.conservationmeasures.eam.dialogs.base.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ProgressReport;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class ProgressReportListTableModel extends ObjectListTableModel
{
	public ProgressReportListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Strategy.TAG_PROGRESS_REPORT_REFS, ProgressReport.getObjectType(), COLUMN_TAGS);
	}

	public static final String[] COLUMN_TAGS = new String[] {
		ProgressReport.TAG_LABEL,
		ProgressReport.TAG_PROGRESS_STATUS,
		ProgressReport.TAG_PROGRESS_DATE,
		ProgressReport.TAG_COMMENTS,
	};
}
