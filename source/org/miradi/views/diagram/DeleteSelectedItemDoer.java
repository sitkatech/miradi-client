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
package org.miradi.views.diagram;

import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.project.FactorDeleteHelper;
import org.miradi.views.ViewDoer;

public class DeleteSelectedItemDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

		if (! isInDiagram())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		EAMGraphCell[] selectedRelatedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{	
			Vector<DiagramLink> diagramLinks = extractDiagramLinks(selectedRelatedCells);
			Vector<DiagramFactor> diagramFactors = extractDiagramFactors(selectedRelatedCells);
		
			deleteSelectedLinks(diagramLinks, diagramFactors);
			deleteSelectedFactors(diagramFactors);
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

	private void deleteSelectedFactors(Vector<DiagramFactor> diagramFactors) throws Exception
	{
		for(int i = 0; i < diagramFactors.size(); ++i)
		{
			deleteFactor(diagramFactors.get(i));
		}
	}

	private void deleteSelectedLinks(Vector<DiagramLink> diagramLinks, Vector<DiagramFactor> diagramFactorsAboutToBeDeleted) throws Exception
	{
		for(int i = 0; i < diagramLinks.size(); ++i)
		{
			deleteLink(diagramLinks.get(i), diagramFactorsAboutToBeDeleted);
		}
	}

	private void deleteFactor(DiagramFactor diagramFactor) throws Exception
	{
		DiagramComponent diagram = getDiagramView().getCurrentDiagramComponent();
		FactorDeleteHelper.createFactorDeleteHelper(diagram).deleteFactorAndDiagramFactor(diagramFactor);
	}

	private void deleteLink(DiagramLink diagramLink, Vector<DiagramFactor> diagramFactorsAboutToBeDeleted) throws Exception
	{
		LinkDeletor linkDeletor = new LinkDeletor(getProject());
		DiagramLink found = DiagramLink.find(getProject(), diagramLink.getRef());
		boolean wasAlreadyDeletedAsResultOfGroupBoxLinkDelete = (found == null);
		if (wasAlreadyDeletedAsResultOfGroupBoxLinkDelete)
			return;
		
		if (diagramLink.isGroupBoxLink())
			linkDeletor.deleteFactorLinksAndGroupBoxDiagramLinks(diagramFactorsAboutToBeDeleted, diagramLink);
		else
			linkDeletor.deleteDiagramLinkAndOrphandFactorLink(diagramFactorsAboutToBeDeleted, diagramLink);
	}

	private Vector<DiagramLink> extractDiagramLinks(EAMGraphCell[] selectedRelatedCells)
	{
		return extractType(selectedRelatedCells, DiagramLink.getObjectType());	
	}

	private Vector<DiagramFactor> extractDiagramFactors(EAMGraphCell[] selectedRelatedCells)
	{		
		return extractType(selectedRelatedCells, DiagramFactor.getObjectType());
	}
	
	private Vector extractType(EAMGraphCell[] selectedRelatedCells, int typeToExtract)
	{
		Vector filteredList = new Vector();
		for (int i = 0; i < selectedRelatedCells.length; ++i)
		{
			EAMGraphCell cell = selectedRelatedCells[i];
			if (cell.getDiagramFactorRef().getObjectType() == typeToExtract)
			{
				filteredList.add(cell.getDiagramFactor());
			}
			if (cell.getDiagramLinkRef().getObjectType() == typeToExtract)
			{
				filteredList.add(cell.getDiagramLink());
			}
		}
		
		return filteredList;
	}

	public static final String LINK_DELETE_NOTIFY_TEXT = EAM.text("The link(s) will be deleted from all Conceptual Model pages" +
	  															  " and Results Chains, not just this one. ");
}
