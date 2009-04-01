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
import org.miradi.objects.Factor;
import org.miradi.objects.ScopeBox;
import org.miradi.project.Project;

public class ScopeBoxSubPanel extends ObjectDataInputPanel
{
	public ScopeBoxSubPanel(Project projectToUse)
	{
		super(projectToUse, ScopeBox.getObjectType());
		
		addField(createReadonlyTextField(ScopeBox.getObjectType(), ScopeBox.TAG_LABEL));
		addField(createReadonlyTextField(ScopeBox.getObjectType(), ScopeBox.TAG_TEXT));

		addField(createReadOnlyObjectList(ScopeBox.getObjectType(), Factor.PSEUDO_TAG_CONCEPTUAL_DIAGRAM_REFS));
		addField(createReadOnlyObjectList(ScopeBox.getObjectType(), Factor.PSEUDO_TAG_RESULTS_CHAIN_REFS));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return null;
	}
}
