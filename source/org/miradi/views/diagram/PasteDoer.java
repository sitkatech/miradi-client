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
import org.miradi.dialogs.notify.NotifyDialog;
import org.miradi.dialogs.notify.NotifyDialogTemplateFactory;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.AbstractTransferableMiradiList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.utils.HtmlViewPanel;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.views.diagram.doers.AbstractPasteDoer;

public class PasteDoer extends AbstractPasteDoer
{
	@Override
	protected void doIt() throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try 
		{	
			AbstractTransferableMiradiList list = getTransferableMiradiList();
			if (list == null)
				return;
		
			DiagramObject diagramObjectRefBeingPastedInto = getDiagramObject();
			ORefList beforePasteDiagramFactors = diagramObjectRefBeingPastedInto.getAllDiagramFactorRefs();
			ORefList beforePasteDiagramLinks = diagramObjectRefBeingPastedInto.getAllDiagramLinkRefs();
			
			final String usersChoice = getUsersChoice(list);
				
			DiagramPaster diagramPaster = createDiagramPasterBaseOnUserChoice(list, usersChoice);
			if (pastingBetweenProjectsInDifferentDiagramType(list, diagramPaster))
			{
				EAM.notifyDialog(EAM.text("<HTML>When pasting between projects, can't paste from CM to RC or vice versa</HTML>"));
				return;
			}

			getProject().getDiagramClipboard().incrementPasteCount();
			paste(diagramPaster);
			
			possiblyNotifyUserIfDataWasLost(diagramPaster);
			
			if(!wasAnythingPasted(beforePasteDiagramFactors, beforePasteDiagramLinks))
				EAM.showHtmlMessageOkDialog(nothingPastedMessageFileName, EAM.text("Button|Paste"));
			else if(usersChoice.equals(AS_SHARED_BUTTON))
				NotifyDialog.notify(getMainWindow(), NotifyDialogTemplateFactory.pastedSharedFactors());
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

	private String getUsersChoice(AbstractTransferableMiradiList list) throws Exception
	{
		if (! atLeastOneClipboardMethodExistsInTargetProject(list))
			return AS_COPY_BUTTON;

		if (!isPasteInSameProject(list))
			return AS_COPY_BUTTON;
		
		if (isPastingInSameDiagramAsCopiedFrom(list))
			return AS_COPY_BUTTON;
		
		return AS_SHARED_BUTTON;
	}

	private boolean isPastingInSameDiagramAsCopiedFrom(AbstractTransferableMiradiList list)
	{
		ORef diagramObjecRefCopiedFrom = list.getDiagramObjectRefCopiedFrom();
		ORef diagramObjectRefBeingPastedInto = getDiagramObject().getRef();
		
		boolean pasteInSameDiagram = diagramObjecRefCopiedFrom.equals(diagramObjectRefBeingPastedInto);
		return pasteInSameDiagram && isPasteInSameProject(list);
	}

	private boolean isPasteInSameProject(AbstractTransferableMiradiList list)
	{
		return list.getProjectFileName().equals(getProject().getFilename());
	}
	
	private DiagramPaster createDiagramPasterBaseOnUserChoice(AbstractTransferableMiradiList list, String usersChoice) throws Exception
	{		
		if (usersChoice.equals(AS_SHARED_BUTTON))
			return new DiagramAsSharedPaster(getDiagramPanel(), getDiagramModel(), list);
		
		return new DiagramCopyPaster(getDiagramPanel(), getDiagramModel(), list);
	}

	private void possiblyNotifyUserIfDataWasLost(DiagramPaster diagramPaster) throws Exception
	{
		if (!diagramPaster.wasAnyDataLost())
			return;

		final String dialogTitle = EAM.text("Paste Between Projects");
		final String message = EAM.text("<html>Note that certain project data cannot be copied between projects. This includes:" +
				"<ol>" +
				"<li>Simple Threat Ratings </li>" +
				"<li>Work planning data that refers to other data which only exists in the old project." +
				"This would include work or expense assignments to Project Resources, Accounting Codes, " +
				"Funding Sources, and items in Category #1 or Category #2 lists. The Leader attribute " +
				"is also not copied.</li>" +
				"<li>Taxonomy classifications where the two projects do not share the same taxonomy classification lists. </li>" +
				"</ol></html>");

		HtmlViewPanel htmlViewPanel = HtmlViewPanelWithMargins.createFromTextString(getMainWindow(), dialogTitle, message);
		htmlViewPanel.showAsOkDialog();
	}
	
	private boolean pastingBetweenProjectsInDifferentDiagramType(AbstractTransferableMiradiList list, DiagramPaster diagramPaster)
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
	
	public boolean atLeastOneClipboardMethodExistsInTargetProject(AbstractTransferableMiradiList list) throws Exception
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
	
	private boolean wasAnythingPasted(ORefList beforePasteDiagramFactors, ORefList beforePasteDiagramLinks)
	{
		DiagramObject diagramObjectRefBeingPastedInto = getDiagramObject();
		ORefList afterPasteDiagramFactors = diagramObjectRefBeingPastedInto.getAllDiagramFactorRefs();
		if (!beforePasteDiagramFactors.equals(afterPasteDiagramFactors))
			return true;
		
		ORefList afterPasteDiagramLinks = diagramObjectRefBeingPastedInto.getAllDiagramLinkRefs();
		if (!beforePasteDiagramLinks.equals(afterPasteDiagramLinks))
			return true;
		
		return false;
	}

	private final static String AS_COPY_BUTTON = EAM.text("Button|Paste As Copies");
	private final static String AS_SHARED_BUTTON = EAM.text("Button|Paste Shared");
	private final static String nothingPastedMessageFileName = "NothingPastedMessage.html";
}
