/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;

public class TNCSummaryPanel extends ObjectDataInputPanel
{
	public TNCSummaryPanel(Project projectToUse, ProjectMetadata metadata)
	{
		super(projectToUse, metadata.getType(), metadata.getId());

		ObjectDataInputField workbookVersionNumber = createStringField(metadata.TAG_TNC_WORKBOOK_VERSION_NUMBER);
		workbookVersionNumber.setEditable(false);
		addField(EAM.text("Label|CAP Workbook Version Number"), workbookVersionNumber);

		ObjectDataInputField workbookVersionDate = createDateField(metadata.TAG_TNC_WORKBOOK_VERSION_DATE);
		workbookVersionDate.setEditable(false);
		addField(EAM.text("Label|CAP Workbook Version Date"), workbookVersionDate);

		ObjectDataInputField databaseDownloadDate = createDateField(metadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		databaseDownloadDate.setEditable(false);
		addField(EAM.text("Label|ConPro Database Download Date"), databaseDownloadDate);

		ObjectDataInputField lessonsLearned = createMultilineField(metadata.TAG_TNC_LESSONS_LEARNED);
		addField(EAM.text("Label|Lessons Learned"), lessonsLearned);

		updateFieldsFromProject();
	}
	
	public String getPanelDescriptionText()
	{
		return EAM.text("TNC");
	}
	
}
