/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.progressReport;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.ProgressReport;
import org.miradi.project.Project;
import org.miradi.questions.ProgressReportStatusQuestion;

public class ProgressReportPropertiesPanel extends ObjectDataInputPanel
{
	public ProgressReportPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ProgressReport.getObjectType(), BaseId.INVALID);
			
		ProgressReportStatusQuestion progressReportStatusQuestion = new ProgressReportStatusQuestion();
		addField(createDateChooserField(ProgressReport.TAG_PROGRESS_DATE));
		addField(createChoiceField(ProgressReport.getObjectType(), ProgressReport.TAG_PROGRESS_STATUS, progressReportStatusQuestion));
		addField(createMultilineField(ProgressReport.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Progress Report Properties");
	}
}
