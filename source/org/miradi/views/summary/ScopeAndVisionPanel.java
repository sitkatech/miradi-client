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

public class ScopeAndVisionPanel extends ObjectDataInputPanel
{
	public ScopeAndVisionPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);

		addField(createStringField(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE));
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_SCOPE));
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_VISION));
		addField(createMultilineField(ProjectMetadata.TAG_SCOPE_COMMENTS));
		
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Scope and Vision");
	}

}
