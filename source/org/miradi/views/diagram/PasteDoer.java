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

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.dialogs.base.ConfirmDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.views.diagram.doers.AbstractPasteDoer;

public class PasteDoer extends AbstractPasteDoer
{
	public void doIt() throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try 
		{	
			TransferableMiradiList list = getTransferableMiradiList();
			if (list == null)
				return;
		
			DiagramObject diagramObjectRefBeingPastedInto = getDiagramObject();
			ORefList beforePasteDiagramFactors = diagramObjectRefBeingPastedInto.getAllDiagramFactorRefs();
			ORefList beforePasteDiagramLinks = diagramObjectRefBeingPastedInto.getAllDiagramLinkRefs();
			
			final String usersChoice = getUsersChoice(list);
			if (usersChoice.equals(CANCEL_BUTTON))
				return;
				
			DiagramPaster diagramPaster = createDiagramPasterBaseOnUserChoice(list, usersChoice);
			if (pastingBetweenProjectsInDifferentDiagramType(list, diagramPaster))
			{
				EAM.notifyDialog(EAM.text("<HTML>When pasting between projects, can't paste from CM to RC or vice versa</HTML>"));
				return;
			}

			getProject().getDiagramClipboard().incrementPasteCount();
			paste(diagramPaster);
			possiblyNotifyUserIfDataWasLost(diagramPaster);
			notifiyIfNothingWasPasted(beforePasteDiagramFactors, beforePasteDiagramLinks);
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		} 
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private DiagramObject getDiagramObject()
	{
		return getDiagramPanel().getDiagramObject();
	}

	private String getUsersChoice(TransferableMiradiList list) throws Exception
	{
		if (! atLeastOneClipboardMethodExistsInTargetProject(list))
			return AS_COPY_BUTTON;

		if (!isPasteInSameProject(list))
			return AS_COPY_BUTTON;
		
		if (isPastingInSameDiagramAsCopiedFrom(list))
			return AS_COPY_BUTTON;
		
		String confirmationTextToUse = "<html><div class='WizardText'>" + EAM.text(
				"This will paste shared copies of the factors, " +
				"meaning that any changes made in this diagram will also be reflected " +
				"in other diagrams that these factors appear. " +
				"In other words, each factor will only exist once in this project, " +
				"but will be visible in multiple diagram pages. " +
				"Normally, this is what you want.<br><br>" +
				"If you really wanted to create duplicate copies of the factors, " +
				"separate from the originals, " +
				"you must cancel this operation, " +
				"return to the diagram where these factors originated, " +
				"and paste them there. " +
				"Then use the &lt;Cut&gt; command to move them to the clipboard, " +
				"return to this diagram, and paste the new copies here.");
		if(ConfirmDialog.confirm(getMainWindow(), EAM.text("Confirm Paste Shared Factors"), confirmationTextToUse, AS_SHARED_BUTTON))
			return AS_SHARED_BUTTON;
		
		return CANCEL_BUTTON;
	}

	private boolean isPastingInSameDiagramAsCopiedFrom(TransferableMiradiList list)
	{
		ORef diagramObjecRefCopiedFrom = list.getDiagramObjectRefCopiedFrom();
		ORef diagramObjectRefBeingPastedInto = getDiagramObject().getRef();
		
		boolean pasteInSameDiagram = diagramObjecRefCopiedFrom.equals(diagramObjectRefBeingPastedInto);
		return pasteInSameDiagram && isPasteInSameProject(list);
	}

	private boolean isPasteInSameProject(TransferableMiradiList list)
	{
		return list.getProjectFileName().equals(getProject().getFilename());
	}
	
	private DiagramPaster createDiagramPasterBaseOnUserChoice(TransferableMiradiList list, String usersChoice) throws Exception
	{		
		if (usersChoice.equals(AS_SHARED_BUTTON))
			return new DiagramAsSharedPaster(getDiagramPanel(), getDiagramModel(), list);
		
		return new DiagramCopyPaster(getDiagramPanel(), getDiagramModel(), list);
	}

	private void possiblyNotifyUserIfDataWasLost(DiagramPaster diagramPaster) throws Exception
	{
		if (!diagramPaster.wasAnyDataLost())
			return;
		
		EAM.notifyDialog(EAM.text("Some of the data could not be moved to this project because " +
								  "it refers to other data that only exists in the old project"));
	}
	
	private boolean pastingBetweenProjectsInDifferentDiagramType(TransferableMiradiList list, DiagramPaster diagramPaster)
	{
		if (isPasteInSameProject(list))
			return false;
		
		if (diagramPaster.isPastingInSameDiagramType())
			return false;
		
		return true;
	}

	protected void paste(DiagramPaster diagramPaster) throws Exception
	{
		diagramPaster.pasteFactorsAndLinks(getLocation());
		diagramPaster.selectNewlyPastedItems();

	}
	
	public boolean atLeastOneClipboardMethodExistsInTargetProject(TransferableMiradiList list) throws Exception
	{
		ORefList factorRefs = list.getFactorRefs();
		for (int i = 0; i < factorRefs.size(); ++i)
		{
			BaseObject foundObject = getProject().findObject(factorRefs.get(i));
			if (foundObject != null)
				return true;
		}
		
		return false;
	}
	
	private void notifiyIfNothingWasPasted(ORefList beforePasteDiagramFactors, ORefList beforePasteDiagramLinks) throws Exception
	{
		DiagramObject diagramObjectRefBeingPastedInto = getDiagramObject();
		ORefList afterPasteDiagramFactors = diagramObjectRefBeingPastedInto.getAllDiagramFactorRefs();
		if (!beforePasteDiagramFactors.equals(afterPasteDiagramFactors))
			return;
		
		ORefList afterPasteDiagramLinks = diagramObjectRefBeingPastedInto.getAllDiagramLinkRefs();
		if (!beforePasteDiagramLinks.equals(afterPasteDiagramLinks))
			return;
		
		EAM.showHtmlMessageOkDialog(messageFileName, "Paste");
	}

	private final static String AS_COPY_BUTTON = EAM.text("Button|Paste As Copies");
	private final static String AS_SHARED_BUTTON = EAM.text("Button|Paste Shared");
	private final static String messageFileName = "NothingPastedMessage.html";
}
