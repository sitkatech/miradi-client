/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
		return PANEL_DESCRIPTION; 
	}

	public static final String PANEL_DESCRIPTION = EAM.text("Scope and Vision");
}
