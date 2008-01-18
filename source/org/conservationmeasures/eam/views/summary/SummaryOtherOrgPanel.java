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

public class SummaryOtherOrgPanel extends ObjectDataInputPanel
{
	public SummaryOtherOrgPanel(Project projectToUse, ORef refToUse)
	{
		super(projectToUse, refToUse);
		
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_MANAGING_OFFICE));
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_REGIONAL_OFFICE));
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER));
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));

		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Other Org");
	}
}
