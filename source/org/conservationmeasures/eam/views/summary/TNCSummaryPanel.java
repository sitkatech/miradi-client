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
	}
	
	UiTextField lessonsLearned;
}
