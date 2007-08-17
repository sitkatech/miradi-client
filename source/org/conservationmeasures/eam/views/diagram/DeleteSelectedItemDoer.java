/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.project.FactorDeleteHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteSelectedItemDoer extends ViewDoer
{
	public DeleteSelectedItemDoer()
	{
		super();
	}
	
	public DeleteSelectedItemDoer(Project project)
	{
		setProject(project);
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

		if (! isDiagramView())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		EAMGraphCell[] selectedRelatedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORefList factorRefsAboutToBeDeleted = extractFactors(selectedRelatedCells);
			for(int i = 0; i < selectedRelatedCells.length; ++i)
			{
				EAMGraphCell cell = selectedRelatedCells[i];
				deleteFactor(cell);
				deleteLink(cell, factorRefsAboutToBeDeleted);
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

	private ORefList extractFactors(EAMGraphCell[] selectedRelatedCells)
	{
		ORefList factorRefList = new ORefList();
		for (int i = 0; i < selectedRelatedCells.length; ++i)
		{
			EAMGraphCell cell = selectedRelatedCells[i];
			if (!cell.isFactor())
				continue;
			
			FactorCell factorCell = (FactorCell) cell;
			factorRefList.add(factorCell.getWrappedORef());
		}
		
		return factorRefList;
	}

	private void deleteFactor(EAMGraphCell cell) throws Exception
	{
		if(!cell.isFactor())
			return;
		
		DiagramModel model = getDiagramView().getDiagramModel();
		new FactorDeleteHelper(model).deleteFactor((FactorCell)cell);
	}

	private void deleteLink(EAMGraphCell cell, ORefList factorRefsAboutToBeDeleted) throws Exception
	{
		if(!cell.isFactorLink())
			return;
		
		new LinkDeletor(getProject()).deleteFactorLink(cell.getDiagramFactorLink().getDiagramLinkageId(), factorRefsAboutToBeDeleted);
	}
}
