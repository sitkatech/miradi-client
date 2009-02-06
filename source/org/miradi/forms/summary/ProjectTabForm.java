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
package org.miradi.forms.summary;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.objects.ProjectMetadata;
import org.miradi.views.summary.SummaryProjectPanel;

public class ProjectTabForm extends FieldPanelSpec
{
	public ProjectTabForm()
	{
		setHasBorder();
		setTranslatedTitle(SummaryProjectPanel.PANEL_DESCRIPTION);

		int type = ProjectMetadata.getObjectType();
		addLabelAndField(type, ProjectMetadata.TAG_PROJECT_NAME);
		addLabelAndField(type, ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		addLabelAndField(type, ProjectMetadata.PSEUDO_TAG_PROJECT_FILENAME);
		addBlankHorizontalLine();
		
		addLabelAndField(type, ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		addLabelAndField(type, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		addLabelAndField(type, ProjectMetadata.TAG_PROJECT_URL);
		addLabelAndField(type, ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		
		addLabelAndField(type, ProjectMetadata.TAG_PROJECT_STATUS);
		addLabelAndField(type, ProjectMetadata.TAG_NEXT_STEPS);
	}
}
