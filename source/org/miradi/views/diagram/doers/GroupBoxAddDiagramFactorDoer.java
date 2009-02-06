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

import java.util.Vector;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;
import org.miradi.views.diagram.LinkCreator;
import org.miradi.views.diagram.LinkDeletor;

public class GroupBoxAddDiagramFactorDoer extends AbstractGroupBoxDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		if (!isExactlyOneGroupBoxSelected())
			return false;
		
		if (isOwnedByDifferentGroupBoxThanSelected())
			return false;
		
		return true;
	}
	
	private boolean isOwnedByDifferentGroupBoxThanSelected()
	{
		ORefList nonGroupBoxSelectedRefs = getSelectedNonGroupBoxDiagramFactors();
		DiagramFactor selectedGroupBox = getSingleSelectedGroupBox();
		for (int i = 0; i < nonGroupBoxSelectedRefs.size(); ++i)
		{
			DiagramFactor groupChildFactor = DiagramFactor.find(getProject(), nonGroupBoxSelectedRefs.get(i));
			if (groupChildFactor.getOwningGroupBoxRef().isInvalid())
				continue;
			
			if (!groupChildFactor.getOwningGroupBoxRef().equals(selectedGroupBox.getRef()))
				return true;	
		}
		
		return false;
	}

	public static boolean hasOwnedSelectedDiagramFactors(Project project, ORefList selectedDiagramFactorsRefs)
	{
		for (int i = 0; i < selectedDiagramFactorsRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, selectedDiagramFactorsRefs.get(i));
			ORef owningGroupBox = diagramFactor.getOwningGroupBoxRef();
			if (!owningGroupBox.isInvalid())
				return true;
		}
		
		return false;
	}

	protected void updateGroupBoxChildrenUsingCommands() throws Exception
	{
		ORefList nonGroupBoxDiagramFactorRefs = getSelectedNonGroupBoxDiagramFactors();
		DiagramFactor groupBoxDiagramFactor = getSingleSelectedGroupBox();
		
		if (!areSameTypeAsInGroup(groupBoxDiagramFactor, nonGroupBoxDiagramFactorRefs))
		{
			EAM.notifyDialog(EAM.text(WARNING_TEXT));
			return;
		}
		
		getDiagramView().getCurrentDiagramComponent().clearSelection();
		removeAnyGroupBoxToNonGroupBoxLinks(groupBoxDiagramFactor, nonGroupBoxDiagramFactorRefs);
		ORefList groupBoxChildrenRefs = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		
		ORefList diagramFactorRefsToAdd = ORefList.subtract(nonGroupBoxDiagramFactorRefs, groupBoxChildrenRefs);
		CommandSetObjectData appendCommand = CommandSetObjectData.createAppendORefListCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, diagramFactorRefsToAdd);
		getProject().executeCommand(appendCommand);
		
		ensureNewlyAddedDiagramFactorIsLinked(groupBoxDiagramFactor);
	}

	private boolean areSameTypeAsInGroup(DiagramFactor groupBoxDiagramFactor, ORefList nonGroupBoxDiagramFactorRefs)
	{
		ORef  firstChildRef = groupBoxDiagramFactor.getGroupBoxChildrenRefs().getRefForType(DiagramFactor.getObjectType());
		if (firstChildRef.isInvalid())
			return true;
		
		DiagramFactor child = DiagramFactor.find(getProject(), firstChildRef);
		for (int i = 0; i < nonGroupBoxDiagramFactorRefs.size(); ++i)
		{
			DiagramFactor thisDiagramFactor = DiagramFactor.find(getProject(), nonGroupBoxDiagramFactorRefs.get(i));
			if (thisDiagramFactor.getWrappedType() != child.getWrappedType())
				return false;
		}
		
		return true;
	}

	private void removeAnyGroupBoxToNonGroupBoxLinks(DiagramFactor groupBoxDiagramFactor, ORefList nonGroupBoxDiagramFactorRefs) throws Exception
	{
		ORefList diagramLinkReferrers = groupBoxDiagramFactor.findObjectsThatReferToUs(DiagramLink.getObjectType());
		LinkDeletor linkDeletor = new LinkDeletor(getProject());
		for (int linkIndex = 0; linkIndex < diagramLinkReferrers.size(); ++linkIndex)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkReferrers.get(linkIndex));
			for (int factorIndex = 0; factorIndex < nonGroupBoxDiagramFactorRefs.size(); ++factorIndex)
			{
				if (diagramLink.isToOrFrom(nonGroupBoxDiagramFactorRefs.get(factorIndex)))
				{
					linkDeletor.deleteFactorLinksAndGroupBoxDiagramLinks(new Vector<DiagramFactor>(), diagramLink);
				}
			}
			
		}
	}

	private void ensureNewlyAddedDiagramFactorIsLinked(DiagramFactor groupBoxDiagramFactor) throws Exception
	{
		LinkCreator linkCreator = new LinkCreator(getProject());
		ORefList diagramLinkReferrers = groupBoxDiagramFactor.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int i = 0; i < diagramLinkReferrers.size(); ++i)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkReferrers.get(i));
			DiagramFactor fromDiagramFactor = DiagramFactor.find(getProject(), diagramLink.getFromDiagramFactorRef());
			DiagramFactor toDiagramFactor = DiagramFactor.find(getProject(), diagramLink.getToDiagramFactorRef());
			linkCreator.createGroupBoxChildrenDiagramLinks(getDiagramView().getDiagramModel(), fromDiagramFactor, toDiagramFactor);
		}
	}
	
	public final static String WARNING_TEXT = "Group boxes cannot contain factors of more than one type";
}
