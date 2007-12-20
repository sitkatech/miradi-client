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

public class GroupBoxAddDiagramFactorDoer extends AbstractGroupBoxDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		if (!isAtLeastOneGroupBoxSelected())
			return false;
		
		if (hasOwnedSelectedDiagramFactors())
			return false;
		
		return true;
	}
	
	private boolean hasOwnedSelectedDiagramFactors()
	{
		ORefList selectedDiagramFactorsRefs = extractNonGroupBoxDiagramFactors();
		for (int i = 0; i < selectedDiagramFactorsRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), selectedDiagramFactorsRefs.get(i));
			ORef owningGroupBox = diagramFactor.getOwningGroupBox();
			if (!owningGroupBox.isInvalid())
				return true;
		}
		
		return false;
	}

	protected void getCommandsToUpdateGroupBoxChildren() throws Exception
	{
		ORefList nonGroupBoxDiagramFactorRefs = extractNonGroupBoxDiagramFactors();
		DiagramFactor groupBoxDiagramFactor = getSingleSelectedGroupBox();
		ORefList groupBoxChildrenRefs = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		
		ORefList diagramFactorRefsToAdd = ORefList.subtract(nonGroupBoxDiagramFactorRefs, groupBoxChildrenRefs);
		CommandSetObjectData appendCommand = CommandSetObjectData.createAppendORefListCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, diagramFactorRefsToAdd);
		getProject().executeCommand(appendCommand);
	}
}
