/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.util.HashSet;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.project.FactorDeleteHelper;
import org.conservationmeasures.eam.views.diagram.LinkDeletor;
import org.conservationmeasures.eam.views.diagram.LocationDoer;

public class DeleteGroupBoxDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		if (hasSelectedGroupBoxes())
			return true;
		
		return false;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			LinkDeletor linkDeletor = new LinkDeletor(getProject());
			FactorDeleteHelper deleteHelper = new FactorDeleteHelper(getDiagramView().getDiagramModel());
			HashSet<FactorCell> groupBoxCells = getDiagramView().getDiagramComponent().getOnlySelectedGroupBoxCells();
			for(FactorCell groupBoxCell : groupBoxCells)
			{
				removeAllChildrenFromGroupBox(groupBoxCell);
				ORefList referringGroupBoxDiagramLinks = groupBoxCell.getDiagramFactor().findObjectsThatReferToUs(DiagramLink.getObjectType());
				linkDeletor.deleteDiagramLinks(referringGroupBoxDiagramLinks);
				deleteHelper.deleteFactor(groupBoxCell.getDiagramFactor());	
			}
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}

	}
	
	private void removeAllChildrenFromGroupBox(FactorCell groupBoxCell) throws Exception
	{
		CommandSetObjectData clearChildrenList = new CommandSetObjectData(groupBoxCell.getDiagramFactorRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, new ORefList().toString());
		getProject().executeCommand(clearChildrenList);
	}

	private boolean hasSelectedGroupBoxes()
	{
		try
		{
			HashSet<FactorCell> groupBoxCells = getDiagramView().getDiagramComponent().getOnlySelectedGroupBoxCells();
			return (groupBoxCells.size() > 0);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
}
