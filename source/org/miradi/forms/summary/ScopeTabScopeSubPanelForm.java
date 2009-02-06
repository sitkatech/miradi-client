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
import org.miradi.views.summary.ScopeAndVisionPanel;

public class ScopeTabScopeSubPanelForm extends FieldPanelSpec
{
	public ScopeTabScopeSubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(ScopeAndVisionPanel.PANEL_DESCRIPTION);
		
		int type = ProjectMetadata.getObjectType();
		
		addLabelAndField(type, ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		addLabelAndField(type, ProjectMetadata.TAG_PROJECT_SCOPE);
		addLabelAndField(type, ProjectMetadata.TAG_PROJECT_VISION);
		addLabelAndField(type, ProjectMetadata.TAG_SCOPE_COMMENTS);
	}
}
