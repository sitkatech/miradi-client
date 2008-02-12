/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

public class SummaryOtherOrgPanel extends ObjectDataInputPanel
{
	public SummaryOtherOrgPanel(Project projectToUse, ORef refToUse)
	{
		super(projectToUse, refToUse);
		
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_MANAGING_OFFICE));
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_REGIONAL_OFFICE));

		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Other Org");
	}
}
