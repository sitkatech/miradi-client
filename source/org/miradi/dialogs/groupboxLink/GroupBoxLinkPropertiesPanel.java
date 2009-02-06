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
package org.miradi.dialogs.groupboxLink;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.diagram.DiagramLinkColorSubPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;

public class GroupBoxLinkPropertiesPanel extends ObjectDataInputPanel
{
	public GroupBoxLinkPropertiesPanel(Project projectToUse)
	{
		super(projectToUse, DiagramLink.getObjectType());
		
		DiagramLinkColorSubPanel diagramLinkColorSubPanel = new DiagramLinkColorSubPanel(getProject(), DiagramLink.getObjectType());
		addSubPanel(diagramLinkColorSubPanel);
		add(diagramLinkColorSubPanel);
		
		updateFieldsFromProject();
	}
	
	@Override
	public void setObjectRef(ORef refToUse)
	{
		super.setObjectRef(refToUse);
		
		if (!DiagramLink.is(refToUse))
			return;
		
		DiagramLink childLink = DiagramLink.find(getProject(), refToUse);
		ORefList groupBoxLinkReferrers = childLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int i = 0; i < groupBoxLinkReferrers.size(); ++i)
		{
			setObjectRef(groupBoxLinkReferrers.get(i));
		}
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Group Box Link Properties");
	}
}
