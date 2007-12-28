/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.DiagramGroupBoxCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.views.diagram.InsertFactorDoer;

public class InsertGroupBoxDoer extends InsertFactorDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
				
		return true;
	}
	
	protected void doExtraSetup(DiagramFactor groupBoxDiagramFactor, FactorCell[] selectedFactorCells) throws Exception
	{
		super.doExtraSetup(groupBoxDiagramFactor, selectedFactorCells);
		if (!containsAllAcceptableDiagramFactors(selectedFactorCells))
			return;
		
		ORefList selectedDiagramFactorRefs = extractDiagramFactorRefs(selectedFactorCells);
		if (GroupBoxAddDiagramFactorDoer.hasOwnedSelectedDiagramFactors(getProject(), selectedDiagramFactorRefs))
			return;
		
		CommandSetObjectData appendCommand = CommandSetObjectData.createAppendORefListCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, selectedDiagramFactorRefs);
		getProject().executeCommand(appendCommand);
	}
	
	private ORefList extractDiagramFactorRefs(FactorCell[] factorCells)
	{
		ORefList diagramFactorRefs = new ORefList();
		for (int i = 0; i < factorCells.length; ++i)
		{
			diagramFactorRefs.add(factorCells[i].getDiagramFactorRef());
		}
		
		return diagramFactorRefs;
	}
	
	private boolean containsAllAcceptableDiagramFactors(FactorCell[] selectedFactorCells)
	{
		for (int i = 0; i < selectedFactorCells.length; ++i)
		{
			if (!AbstractGroupBoxDoer.isAcceptableFactor(selectedFactorCells[i].getWrappedType()))
					return false;
		}
		
		return true;
	}
	
	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramGroupBoxCell.class, true);
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Group Box");
	}

	public int getTypeToInsert()
	{
		return GroupBox.getObjectType();
	}
}
