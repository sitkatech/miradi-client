/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.util.HashSet;
import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
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
		String deleteDiagramReferrerChoice = confirmReferringDiagramLinksBeingDeleted(selectedRelatedCells);
		if (isCancelChoice(deleteDiagramReferrerChoice))
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{	
			Vector<DiagramLink> diagramLinks = extractDiagramLinks(selectedRelatedCells);
			Vector<DiagramFactor> diagramFactors = extractDiagramFactors(selectedRelatedCells);
		
			deleteSelectedLinks(deleteDiagramReferrerChoice, diagramLinks, diagramFactors);
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

	private void deleteSelectedLinks(String deleteDiagramReferrerChoice, Vector<DiagramLink> diagramLinks, Vector<DiagramFactor> diagramFactorsAboutToBeDeleted) throws Exception
	{
		for(int i = 0; i < diagramLinks.size(); ++i)
		{
			deleteLink(deleteDiagramReferrerChoice, diagramLinks.get(i), diagramFactorsAboutToBeDeleted);
		}
	}

	private void deleteFactor(DiagramFactor diagramFactor) throws Exception
	{
		DiagramComponent diagram = getDiagramView().getCurrentDiagramComponent();
		new FactorDeleteHelper(diagram).deleteFactorAndDiagramFactor(diagramFactor);
	}

	private void deleteLink(String deleteDiagramReferrerChoice, DiagramLink diagramLink, Vector<DiagramFactor> diagramFactorsAboutToBeDeleted) throws Exception
	{
		LinkDeletor linkDeletor = new LinkDeletor(getProject());
		DiagramLink found = DiagramLink.find(getProject(), diagramLink.getRef());
		boolean wasAlreadyDeletedAsResultOfGroupBoxLinkDelete = (found == null);
		if (wasAlreadyDeletedAsResultOfGroupBoxLinkDelete)
			return;
		
		if (diagramLink.isGroupBoxLink())
			deleteGroupBoxDiagramLink(deleteDiagramReferrerChoice, diagramLink,	diagramFactorsAboutToBeDeleted, linkDeletor);
		else 
			deleteNonGroupBoxDiagramLink(deleteDiagramReferrerChoice, diagramLink, diagramFactorsAboutToBeDeleted, linkDeletor);
	}

	private void deleteGroupBoxDiagramLink(String deleteDiagramReferrerChoice, DiagramLink diagramLink,	Vector<DiagramFactor> diagramFactorsAboutToBeDeleted, LinkDeletor linkDeletor) throws Exception
	{
		if (isDeleteFromAllDiagramsChoice(deleteDiagramReferrerChoice))
			linkDeletor.deleteFactorLinksAndGroupBoxDiagramLinksAndReferringDiagramLinks(diagramFactorsAboutToBeDeleted, diagramLink);
		else
			linkDeletor.deleteFactorLinksAndGroupBoxDiagramLinks(diagramFactorsAboutToBeDeleted, diagramLink);
	}

	private void deleteNonGroupBoxDiagramLink(String deleteDiagramReferrerChoice, DiagramLink diagramLink, Vector<DiagramFactor> diagramFactorsAboutToBeDeleted, LinkDeletor linkDeletor) throws Exception
	{
		if (isDeleteFromAllDiagramsChoice(deleteDiagramReferrerChoice))
			linkDeletor.deleteDiagramLinkAndFactorLinkAndDiagramLinkReferrers(diagramFactorsAboutToBeDeleted, diagramLink);
		else 
			linkDeletor.deleteDiagramLinkAndFactorLink(diagramFactorsAboutToBeDeleted, diagramLink);
	}

	private boolean isDeleteFromAllDiagramsChoice(String deleteDiagramReferrerChoice)
	{
		return deleteDiagramReferrerChoice.equals(FROM_ALL_DIAGRAMS_CHOICE);
	}
	
	private boolean isCancelChoice(String deleteDiagramReferrerChoice)
	{
		return deleteDiagramReferrerChoice.equals(CANCEL_CHOICE);
	}	


	private String confirmReferringDiagramLinksBeingDeleted(EAMGraphCell[] selectedRelatedCells)
	{
		Vector<DiagramLink> diagramLinks = extractDiagramLinks(selectedRelatedCells);
		Vector<DiagramLink> diagramLinksWithGroupBoxes = getDiagramLinksAndGroupboxChildrenLinks(diagramLinks);
		HashSet<DiagramObject> diagramObjects = getDiagramNamesAffectedByThisDelete(diagramLinksWithGroupBoxes, extractDiagramFactorsRefs(selectedRelatedCells));
		HashSet<DiagramObject> singleItemSet = new HashSet();
		singleItemSet.add(getDiagramView().getCurrentDiagramObject());
		if (diagramObjects.size() <= 1)
			return JUST_THIS_DIAGRAM_CHOICE;

		String userChoiceResult = EAM.choiceDialog(EAM.text(""), new String[]{createDiagramNamesList(diagramObjects)}, new String[]{CANCEL_CHOICE, JUST_THIS_DIAGRAM_CHOICE, FROM_ALL_DIAGRAMS_CHOICE});
		if (userChoiceResult.length() == 0)
			return CANCEL_CHOICE;
		
		return userChoiceResult;
	}

	private String createDiagramNamesList(HashSet<DiagramObject> diagramObjects)
	{
		String notifyDiaglogText = LINK_DELETE_NOTIFY_TEXT;
		for(DiagramObject diagramObject : diagramObjects)
		{
			notifyDiaglogText += " \n - " + diagramObject.toString();			
		}
		return notifyDiaglogText;
	}

	private HashSet<DiagramObject> getDiagramNamesAffectedByThisDelete(Vector<DiagramLink> diagramLinks, ORefList diagramFactorRefs)
	{	
		HashSet<DiagramObject> diagramObjects = new HashSet(); 
		for (int i = 0; i < diagramLinks.size(); ++i)
		{
			DiagramLink diagramLink = diagramLinks.get(i);
			FactorLink factorLink = diagramLink.getUnderlyingLink();
			if(factorLink == null)
			{
				EAM.logWarning("DiagramLink without FactorLink: " + diagramLink.getRef());
				continue;
			}
			
			ORef fromDiagramFactorRef =  diagramLink.getFromDiagramFactorRef();
			ORef toDiagramFactorRef = diagramLink.getToDiagramFactorRef();
			ORefList diagramLinkRefs = factorLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
			boolean containsBothFromAndTo = !diagramFactorRefs.contains(fromDiagramFactorRef) && !diagramFactorRefs.contains(toDiagramFactorRef);
			boolean hasMoreThanOneRefferer = diagramLinkRefs.size() > 1;
			if (hasMoreThanOneRefferer && containsBothFromAndTo)
				diagramObjects.addAll(getAllDiagramsThatRefer(factorLink));
		}
		
		return diagramObjects;
	}
	
	private HashSet<DiagramObject> getAllDiagramsThatRefer(FactorLink factorLink)
	{
		HashSet<DiagramObject> diagramObjects = new HashSet();
		ORefList diagramRefs = DiagramObject.getDiagramRefsContainingLink(getProject(), factorLink.getRef());
		for (int i = 0; i < diagramRefs.size(); ++i)
		{
			DiagramObject diagramObject = (DiagramObject) getProject().findObject(diagramRefs.get(i));
			diagramObjects.add(diagramObject);
		}
		
		return diagramObjects;
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
	
	private ORefList extractDiagramFactorsRefs(EAMGraphCell[] selectedRelatedCells)
	{
		Vector<DiagramFactor> diagramFactors = extractDiagramFactors(selectedRelatedCells);
		ORefList diagramFactorRefList = new ORefList(diagramFactors.toArray(new DiagramFactor[0]));
		
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
	
	public static final String LINK_DELETE_NOTIFY_TEXT = EAM.text("The link(s) will be deleted from all Conceptual Model pages" +
	  															  " and Results Chains, not just this one. ");
	
	public static final String JUST_THIS_DIAGRAM_CHOICE = "Just This Diagram";
	public static final String FROM_ALL_DIAGRAMS_CHOICE = "From All Diagrams";
	public static final String CANCEL_CHOICE = "Cancel";
}
