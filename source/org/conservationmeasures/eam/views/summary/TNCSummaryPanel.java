/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.TncOperatingUnitsQuestion;

public class TNCSummaryPanel extends ObjectDataInputPanel
{
	public TNCSummaryPanel(Project projectToUse, ProjectMetadata metadata)
	{
		super(projectToUse, metadata.getType(), metadata.getId());

		addField(createReadonlyTextField(metadata.TAG_TNC_WORKBOOK_VERSION_NUMBER));
		addField(createReadonlyTextField(metadata.TAG_TNC_WORKBOOK_VERSION_DATE));
		addField(createReadonlyTextField(metadata.TAG_TNC_DATABASE_DOWNLOAD_DATE));
		addField(createMultilineField(metadata.TAG_TNC_LESSONS_LEARNED));
		addField(createMultilineField(metadata.TAG_TNC_PLANNING_TEAM_COMMENT));
		addField(createStringField(metadata.TAG_TNC_ECOREGION));
		addField(createReadonlyTextField(metadata.TAG_TNC_COUNTRY));
		addField(createReadonlyTextField(metadata.LEGACY_TAG_TNC_OPERATING_UNITS));
		addField(createMultiCodeField(ProjectMetadata.getObjectType(), new TncOperatingUnitsQuestion(ProjectMetadata.TAG_TNC_OPERATING_UNITS), 1));

		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("TNC");
	}
	
}
