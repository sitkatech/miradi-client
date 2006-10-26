/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;

public class TNCSummaryPanel extends MetadataEditingPanel
{
	public TNCSummaryPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		
		add(new UiLabel(EAM.text("Label|Lessons Learned:")));
		lessonsLearned = createFieldComponent(ProjectMetadata.TAG_TNC_LESSONS_LEARNED, 50);
		add(lessonsLearned);
		
		add(new UiLabel(EAM.text("Label|CAP Workbook Version Number")));
		workbookVersionNumber = createFieldComponent(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_NUMBER, 20);
		workbookVersionNumber.setEditable(false);
		add(workbookVersionNumber);

		add(new UiLabel(EAM.text("Label|CAP Workbook Version Date")));
		workbookVersionDate = createFieldComponent(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_DATE, 10);
		workbookVersionDate.setEditable(false);
		add(workbookVersionDate);

		add(new UiLabel(EAM.text("Label|CAP Database Download Date")));
		databaseDownloadDate = createFieldComponent(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE, 10);
		databaseDownloadDate.setEditable(false);
		add(databaseDownloadDate);
	}
	
	UiTextField lessonsLearned;
	UiTextField workbookVersionNumber;
	UiTextField workbookVersionDate;
	UiTextField databaseDownloadDate;
}
