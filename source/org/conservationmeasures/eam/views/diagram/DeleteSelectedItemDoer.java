/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.FactorDeleteHelper;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteSelectedItemDoer extends ViewDoer
{
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
		if (! confirmIfReferringLinksBeingDeleted(selectedRelatedCells))
			return;
		
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
		
		DiagramLink diagramLink = cell.getDiagramLink();
		if (!diagramLink.isGroupBoxLink())
			new LinkDeletor(getProject()).deleteFactorLinkAndDiagramLink(factorRefsAboutToBeDeleted, diagramLink);
		else
			new LinkDeletor(getProject()).deleteFactorLinksAndGroupBoxDiagramLinks(factorRefsAboutToBeDeleted, diagramLink);
	}	

	private boolean confirmIfReferringLinksBeingDeleted(EAMGraphCell[] selectedRelatedCells)
	{
		Vector<DiagramLink> diagramLinks = extractDiagramLinks(selectedRelatedCells);
		Vector<DiagramLink> diagramLinksWithGroupBoxes = getDiagramLinksAndGroupboxChildrenLinks(diagramLinks);
		Vector diagramNames = getDiagramNamesAffectedByThisDelete(diagramLinksWithGroupBoxes, extractDiagramFactors(selectedRelatedCells));
		if (diagramNames.size() <= 1)
			return true;

		String notifyDiaglogText = LINK_DELETE_NOTIFY_TEXT; 
		for (int i = 0 ; i < diagramNames.size(); ++i)
		{
			notifyDiaglogText += " \n - " + diagramNames.get(i);
		}
		
		return EAM.confirmDeletRetainDialog(new String[]{notifyDiaglogText});
	}

	private Vector getDiagramNamesAffectedByThisDelete(Vector<DiagramLink> diagramLinks, ORefList diagramFactorRefs)
	{	
		Vector diagramNames = new Vector(); 
		for (int i = 0; i < diagramLinks.size(); ++i)
		{
			DiagramLink diagramLink = diagramLinks.get(i);
			FactorLink factorLink = diagramLink.getUnderlyingLink();
			ORef fromDiagramFactorRef =  new ORef(DiagramFactor.getObjectType(), diagramLink.getFromDiagramFactorId());
			ORef toDiagramFactorRef = new ORef(DiagramFactor.getObjectType(), diagramLink.getToDiagramFactorId());
			ORefList diagramLinkRefs = factorLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
			boolean containsBothFromAndTo = !diagramFactorRefs.contains(fromDiagramFactorRef) && !diagramFactorRefs.contains(toDiagramFactorRef);
			boolean hasMoreThanOneRefferer = diagramLinkRefs.size() > 1;
			if (hasMoreThanOneRefferer && containsBothFromAndTo)
				diagramNames.addAll(getAllDiagramsThatRefer(diagramNames, factorLink));
		}
		
		return diagramNames;
	}
	
	private Vector getAllDiagramsThatRefer(Vector existingDiagramNames, FactorLink factorLink)
	{
		Vector diagramNames = new Vector();
		ORefList diagramRefs = DiagramObject.getDiagramRefsContainingLink(getProject(), factorLink.getRef());
		for (int i = 0; i < diagramRefs.size(); ++i)
		{
			DiagramObject diagramObject = (DiagramObject) getProject().findObject(diagramRefs.get(i));
			String diagramObjectLabel = diagramObject.toString();
			if (existingDiagramNames.contains(diagramObjectLabel))
				continue;
	
			diagramNames.add(diagramObjectLabel);
		}
		
		return diagramNames;
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
	
	private ORefList extractDiagramFactors(EAMGraphCell[] selectedRelatedCells)
	{
		ORefList diagramFactorRefList = new ORefList();
		for (int i = 0; i < selectedRelatedCells.length; ++i)
		{
			EAMGraphCell cell = selectedRelatedCells[i];
			if (!cell.isFactor())
				continue;
			
			FactorCell factorCell = (FactorCell) cell;
			diagramFactorRefList.add(factorCell.getDiagramFactorRef());
		}
		
		return diagramFactorRefList;
	}
	
	private Vector<DiagramLink> getDiagramLinksAndGroupboxChildrenLinks(Vector<DiagramLink> diagramLinks)
	{
		Vector<DiagramLink> diagramLinksWithPossibleGroupBoxLinks = new Vector();
		for (int i = 0; i < diagramLinks.size(); ++i)
		{
			DiagramLink diagramLink = diagramLinks.get(i);
			ORefList selfOrChildren = diagramLink.getSelfOrChildren();
			if (selfOrChildren.contains(diagramLink.getRef()))
				diagramLinksWithPossibleGroupBoxLinks.add(diagramLink);
			else 
				diagramLinksWithPossibleGroupBoxLinks.addAll(convertToDiagramLinks(selfOrChildren));
		}
		
		return diagramLinksWithPossibleGroupBoxLinks;
	}
	
	private Vector<DiagramLink> convertToDiagramLinks(ORefList diagramLinkRefs)
	{
		Vector<DiagramLink> diagramLinks = new Vector();
		for (int i = 0; i < diagramLinkRefs.size(); ++i)
		{
			diagramLinks.add(DiagramLink.find(getProject(), diagramLinkRefs.get(i)));
		}
		
		return diagramLinks;
	}
	
	private Vector<DiagramLink> extractDiagramLinks(EAMGraphCell[] selectedRelatedCells)
	{
		Vector<DiagramLink> diagramLinks = new Vector();
		for (int i = 0; i < selectedRelatedCells.length; ++i)
		{
			EAMGraphCell cell = selectedRelatedCells[i];
			if (!cell.isFactorLink())
				continue;
			
			LinkCell linkCell = (LinkCell) cell;
			diagramLinks.add(linkCell.getDiagramLink());
		}
		
		return diagramLinks;
	}
	
	public static final String LINK_DELETE_NOTIFY_TEXT = EAM.text("The link(s) will be deleted from all Conceptual Model pages" +
	  															  " and Results Chains, not just this one. ");
}
