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
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ScopeBox;
import org.miradi.project.Project;

public class ScopeBoxPoolSubPanel extends ObjectDataInputPanel
{
	public ScopeBoxPoolSubPanel(Project projectToUse)
	{
		super(projectToUse, ScopeBox.getObjectType());
		
		setLayout(new OneColumnGridLayout());
		
		reloadSubPanels();
		
		updateFieldsFromProject();
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		
		reloadSubPanels();
	}

	private void reloadSubPanels()
	{
		removeAll();
		
		ORefList scopeBoxRefs = getProject().getScopeBoxPool().getRefList();
		for (int index = 0; index < scopeBoxRefs.size(); ++index)
		{
			ScopeBoxSubPanel scopeBoxSubPanel = new ScopeBoxSubPanel(getProject());
			addSubPanelWithoutTitledBorder(scopeBoxSubPanel);
			scopeBoxSubPanel.setObjectRef(scopeBoxRefs.get(index));
		}
		
		invalidate();
	}

	@Override
	public String getPanelDescription()
	{
		return null;
	}
}
