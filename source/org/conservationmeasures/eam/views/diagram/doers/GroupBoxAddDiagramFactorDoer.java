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

public class GroupBoxAddDiagramFactorDoer extends AbstractGroupBoxDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		if (!containsOnlyOneGroupBox(selected))
			return false;
		
		return true;
	}
	
	protected void getCommandsToUpdateGroupBoxChildren() throws Exception
	{
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		ORefList nonGroupBoxDiagramFactorRefs = extractNonGroupBoxDiagramFactors(selected);
		DiagramFactor groupBoxDiagramFactor = getGroupBox(selected);
		ORefList groupBoxChildrenRefs = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		for (int i = 0; i < nonGroupBoxDiagramFactorRefs.size(); ++i)
		{
			ORef diagramfactorRef = nonGroupBoxDiagramFactorRefs.get(i);
			if (!groupBoxChildrenRefs.contains(diagramfactorRef))
			{
				
				CommandSetObjectData appendCommand = CommandSetObjectData.createAppendORefCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, diagramfactorRef);
				getProject().executeCommand(appendCommand);
			}
		}
	}
}
