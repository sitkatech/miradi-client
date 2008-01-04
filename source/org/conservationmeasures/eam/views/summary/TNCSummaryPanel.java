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
		addField(createNumericField(metadata.TAG_TNC_SIZE_IN_HECTARES));
		addField(createStringField(metadata.TAG_TNC_ECOREGION));
		addField(createReadonlyTextField(metadata.TAG_TNC_COUNTRY));
		addField(createStringField(metadata.TAG_TNC_OPERATING_UNITS));

		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("TNC");
	}
	
}
