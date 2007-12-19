/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;

public class GroupBoxRemoveDiagramFactorDoer extends AbstractGroupBoxDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		if (!areAllSelectedChildrenOfGroupBox())
			return false;
		
		return true;
	}
	
	private boolean areAllSelectedChildrenOfGroupBox()
	{
		EAMGraphCell[] selectedCells = getSelectedCells();
		for (int i = 0; i < selectedCells.length; ++i)
		{
			if (!isChildOfGroupBox(selectedCells[i]))
				return false;
		}
		
		return true;
	}

	private boolean isChildOfGroupBox(EAMGraphCell cell)
	{
		if (!cell.isFactor())
			return false;
		
		ORef owningGroupBox = cell.getDiagramFactor().getOwningGroupBox();
		return !owningGroupBox.isInvalid();
	}

	protected void getCommandsToUpdateGroupBoxChildren() throws Exception
	{
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		ORefList nonGroupBoxDiagramFactorRefs = extractNonGroupBoxDiagramFactors(selected);
		for (int i = 0; i < nonGroupBoxDiagramFactorRefs.size(); ++i)
		{
			ORef diagramfactorRef = nonGroupBoxDiagramFactorRefs.get(i);
			ORefList diagramFactorReferers = DiagramFactor.findObjectsThatReferToUs(getProject().getObjectManager(), DiagramFactor.getObjectType(), diagramfactorRef);
			ORef groupBoxDiagramFactorRef = diagramFactorReferers.getRefForType(DiagramFactor.getObjectType());
			if (groupBoxDiagramFactorRef.isInvalid())
				continue;
			
			DiagramFactor groupBoxDiagramFactor = DiagramFactor.find(getProject(), groupBoxDiagramFactorRef);
			if (groupBoxDiagramFactor.getGroupBoxChildrenRefs().contains(diagramfactorRef))
			{
				CommandSetObjectData removeCommand = CommandSetObjectData.createRemoveORefCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, diagramfactorRef);
				getProject().executeCommand(removeCommand);
			}
		}
	}
}
