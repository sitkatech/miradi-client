/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;

public class SummaryProjectPanel extends ObjectDataInputPanel
{
	public SummaryProjectPanel(Project projectToUse, ORef refToUse)
	{
		super(projectToUse, refToUse);
		
		addField(createStringField(ProjectMetadata.TAG_PROJECT_NAME));
		addField(createDateChooserField(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE));
		addField(createReadonlyTextField(ProjectMetadata.PSEUDO_TAG_PROJECT_FILENAME));
		addBlankHorizontalLine();
		
		addField(createStringField(ProjectMetadata.TAG_PROJECT_URL));
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_DESCRIPTION));
		
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_STATUS));
		addField(createMultilineField(ProjectMetadata.TAG_NEXT_STEPS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Project");
	}
}
