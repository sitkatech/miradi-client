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

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;

public class GroupBoxLinkTableModel extends ObjectListTableModel
{
	public GroupBoxLinkTableModel(Project projectToUse, ORef parentRef, String annotationTag)
	{
		super(projectToUse, parentRef, annotationTag, DiagramLink.getObjectType(), COLUMN_TAGS);		
	}
	
	public String getValueToDisplay(ORef rowObjectRef, String tag)
	{
		DiagramLink diagramLink = DiagramLink.find(getProject(), rowObjectRef);
		BaseId diagramFactorId = new BaseId(diagramLink.getData(tag));
		ORef diagramFactorRef = new ORef(DiagramFactor.getObjectType(), diagramFactorId);
		DiagramFactor diagramFactor = DiagramFactor.find(getProject(), diagramFactorRef);
		
		return diagramFactor.getWrappedFactor().getLabel();
	}

	public static final String[] COLUMN_TAGS = new String[] {
		DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID,
		DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID,
	};
}
