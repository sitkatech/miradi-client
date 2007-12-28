/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.LinkCreator;

public class GroupBoxAddDiagramFactorDoer extends AbstractGroupBoxDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		if (!isAtLeastOneGroupBoxSelected())
			return false;
		
		if (hasOwnedSelectedDiagramFactors(getProject(), extractNonGroupBoxDiagramFactors()))
			return false;
		
		return true;
	}
	
	public static boolean hasOwnedSelectedDiagramFactors(Project project, ORefList selectedDiagramFactorsRefs)
	{
		for (int i = 0; i < selectedDiagramFactorsRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, selectedDiagramFactorsRefs.get(i));
			ORef owningGroupBox = diagramFactor.getOwningGroupBox();
			if (!owningGroupBox.isInvalid())
				return true;
		}
		
		return false;
	}

	protected void updateGroupBoxChildrenUsingCommands() throws Exception
	{
		ORefList nonGroupBoxDiagramFactorRefs = extractNonGroupBoxDiagramFactors();
		DiagramFactor groupBoxDiagramFactor = getSingleSelectedGroupBox();
		ORefList groupBoxChildrenRefs = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		
		ORefList diagramFactorRefsToAdd = ORefList.subtract(nonGroupBoxDiagramFactorRefs, groupBoxChildrenRefs);
		CommandSetObjectData appendCommand = CommandSetObjectData.createAppendORefListCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, diagramFactorRefsToAdd);
		getProject().executeCommand(appendCommand);
		
		ensureNewlyAddedDiagramFactorIsLinked(groupBoxDiagramFactor);
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
}
