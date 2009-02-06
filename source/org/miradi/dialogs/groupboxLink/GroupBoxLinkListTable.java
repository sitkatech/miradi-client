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

import org.miradi.dialogs.base.ObjectListTable;
import org.miradi.dialogs.base.ObjectTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;

public class GroupBoxLinkListTable extends ObjectListTable
{
	public GroupBoxLinkListTable(MainWindow mainWindowToUse, ObjectTableModel modelToUse)
	{
		super(mainWindowToUse, modelToUse);
	}

	@Override
	public ORefList[] getSelectedHierarchies()
	{
		ORefList selectedHierarchy = super.getSelectedHierarchies()[0];
		DiagramLink diagramLink = getNonGroupBoxDiagramLink(selectedHierarchy);
		if (diagramLink == null)
			return new ORefList[] {selectedHierarchy};
			
		ORefList newSelectedHierarchyRefList = new ORefList();
		ORef fromRef = diagramLink.getFromDiagramFactorRef();
		DiagramFactor fromDiagramFactor = DiagramFactor.find(getProject(), fromRef);
		
		ORef toRef = diagramLink.getToDiagramFactorRef();
		DiagramFactor toDiagramFactor = DiagramFactor.find(getProject(), toRef);
		
		newSelectedHierarchyRefList.add(toDiagramFactor.getWrappedORef());
		newSelectedHierarchyRefList.add(fromDiagramFactor.getWrappedORef());
		newSelectedHierarchyRefList.add(diagramLink.getWrappedRef());
		newSelectedHierarchyRefList.addAll(selectedHierarchy);
	
		return new ORefList[] {newSelectedHierarchyRefList};
	}

	private DiagramLink getNonGroupBoxDiagramLink(ORefList selectedHierarchy)
	{
		ORefList diagramLinkRefs = selectedHierarchy.filterByType(DiagramLink.getObjectType());
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRefs.get(index));
			if (!diagramLink.isGroupBoxLink())
				return diagramLink;
		}
		
		return null;
	}
}
