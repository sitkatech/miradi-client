/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;

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
		ORefList groupBoxDiagramFactorRefs = new ORefList();
		for (int i = 0; i < selectedCells.length; ++i)
		{ 
			ORef groupBoxDiagramFactorRef = selectedCells[i].getDiagramFactor().getOwningGroupBox();
			if (!groupBoxDiagramFactorRefs.contains(groupBoxDiagramFactorRef))
				groupBoxDiagramFactorRefs.add(groupBoxDiagramFactorRef);
		}
		
		return groupBoxDiagramFactorRefs;
	}

	protected void getCommandsToUpdateGroupBoxChildren() throws Exception
	{ 
		ORefList groupBoxChildrenToRemove = extractNonGroupBoxDiagramFactors();
		ORef groupBoxDiagramFactorRef = getGroupBoxRefsContainingSelectedDiagramFactors().getRefForType(DiagramFactor.getObjectType());
		DiagramFactor groupBoxDiagramFactor = DiagramFactor.find(getProject(), groupBoxDiagramFactorRef);
		ORefList groupBoxChildren = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		
		ORefList newSubtractChildrenRefs = ORefList.subtract(groupBoxChildren, groupBoxChildrenToRemove);
		CommandSetObjectData removeCommand = new CommandSetObjectData(groupBoxDiagramFactor.getRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, newSubtractChildrenRefs.toString());
		getProject().executeCommand(removeCommand);
	}
}
