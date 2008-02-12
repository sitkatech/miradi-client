/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

public class ProjectScopePanel extends ObjectDataInputPanel
{
	public ProjectScopePanel(Project projectToUse, ProjectMetadata metadata)
	{
		super(projectToUse, metadata.getType(), metadata.getId());

		addField(createStringField(metadata.TAG_SHORT_PROJECT_SCOPE));
		addField(createMultilineField(metadata.TAG_PROJECT_SCOPE));
		addField(createMultilineField(metadata.TAG_PROJECT_VISION));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Project Scope Properties");
	}

}
