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
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;

public class GroupBoxRemoveDiagramFactorDoer extends AbstractGroupBoxDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		if (!areAllSelectedChildrenOfSameGroupBox())
			return false;
		
		return true;
	}
	
	private boolean areAllSelectedChildrenOfSameGroupBox()
	{
		return getGroupBoxRefsContainingSelectedDiagramFactors().size() == 1;
	}

	private ORefList getGroupBoxRefsContainingSelectedDiagramFactors()
	{
		FactorCell[] selectedCells = getSelectedCells();
		ORefSet groupBoxDiagramFactorRefs = new ORefSet();
		for (int i = 0; i < selectedCells.length; ++i)
		{ 
			ORef groupBoxDiagramFactorRef = selectedCells[i].getDiagramFactor().getOwningGroupBoxRef();
			if (groupBoxDiagramFactorRef.isInvalid())
				continue;
			
			groupBoxDiagramFactorRefs.add(groupBoxDiagramFactorRef);
		}
		
		return new ORefList(groupBoxDiagramFactorRefs);
	}

	protected void updateGroupBoxChildrenUsingCommands() throws Exception
	{ 
		ORefList groupBoxChildrenToRemove = getSelectedNonGroupBoxDiagramFactors();
		ORef groupBoxDiagramFactorRef = getGroupBoxRefsContainingSelectedDiagramFactors().getRefForType(DiagramFactor.getObjectType());
		DiagramFactor groupBoxDiagramFactor = DiagramFactor.find(getProject(), groupBoxDiagramFactorRef);
		ORefList groupBoxChildren = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		
		ORefList newSubtractChildrenRefs = ORefList.subtract(groupBoxChildren, groupBoxChildrenToRemove);
		CommandSetObjectData removeCommand = new CommandSetObjectData(groupBoxDiagramFactor.getRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, newSubtractChildrenRefs.toString());
		getProject().executeCommand(removeCommand);
		
		updateGroupBoxLinks(groupBoxDiagramFactor, groupBoxChildrenToRemove);
	}

	private void updateGroupBoxLinks(DiagramFactor groupBoxDiagramFactor, ORefList groupBoxChildrenToRemove) throws Exception
	{
		for (int i = 0; i < groupBoxChildrenToRemove.size(); ++i)
		{
			DiagramFactor childOfGroupBoxDiagramFactor = DiagramFactor.find(getProject(), groupBoxChildrenToRemove.get(i));
			removeGroupBoxDiagramLinkForDiagramFactor(groupBoxDiagramFactor, childOfGroupBoxDiagramFactor);
		}
	}

	private void removeGroupBoxDiagramLinkForDiagramFactor(DiagramFactor groupBoxDiagramFactor, DiagramFactor removedChildDiagramFactor) throws Exception
	{	
		ORefList removedChildDiagramLinkReferrerRefs = removedChildDiagramFactor.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int linkIndex = 0; linkIndex < removedChildDiagramLinkReferrerRefs.size(); ++linkIndex)
		{
			DiagramLink removedChildDiagramLink = DiagramLink.find(getProject(), removedChildDiagramLinkReferrerRefs.get(linkIndex));
			removeChildLinkFromGroupBoxLinks(groupBoxDiagramFactor, removedChildDiagramLink);			
		}
	}

	private void removeChildLinkFromGroupBoxLinks(DiagramFactor groupBoxDiagramFactor, DiagramLink removedChildDiagramLink) throws Exception
	{
		ORefList referringGroupBoxDiagramLinkRefs = removedChildDiagramLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int groupBoxLinkIndex = 0; groupBoxLinkIndex < referringGroupBoxDiagramLinkRefs.size(); ++groupBoxLinkIndex)
		{
			DiagramLink groupBoxDiagramLink = DiagramLink.find(getProject(), referringGroupBoxDiagramLinkRefs.get(groupBoxLinkIndex));
			if (groupBoxDiagramLink.isToOrFrom(groupBoxDiagramFactor))
			{
				CommandSetObjectData removeDiagramLinkCommand = CommandSetObjectData.createRemoveORefCommand(groupBoxDiagramLink, DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, removedChildDiagramLink.getRef());
				getProject().executeCommand(removeDiagramLinkCommand);
			}	
		}
	}
}
