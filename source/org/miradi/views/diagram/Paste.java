/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.text.ParseException;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.ORef;

public class Paste extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isInDiagram())
			return false;
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		if(contents == null)
			return false;

		return contents.isDataFlavorSupported(TransferableMiradiList.miradiListDataFlavor);
	}

	public void doIt() throws CommandFailedException
	{
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		getProject().executeCommand(new CommandBeginTransaction());
		try 
		{	
			Transferable contents = clipboard.getContents(null);
			if(!contents.isDataFlavorSupported(TransferableMiradiList.miradiListDataFlavor))
				return;

			TransferableMiradiList list = (TransferableMiradiList)contents.getTransferData(TransferableMiradiList.miradiListDataFlavor);
			final String usersChoice = getUsersChoice(list);
			if (usersChoice.equals(CANCEL_BUTTON))
				return;
				
			DiagramPaster diagramPaster = createDiagramPasterBaseOnUserChoice(list, usersChoice);
			if (! diagramPaster.canPaste())
			{
				EAM.notifyDialog(EAM.text("<HTML>These factors cannot be pasted as shared into this diagram.<BR><BR>" +
										  "Contributing Factors and Direct Threats cannot be pasted as shared into a Results Chain.<BR>" +
										  "Intermediate Results and Threat Reduction Results cannot be pasted as shared into a Conceptual Model page.</HTML>"));
				return;
			}

			clipboard.incrementPasteCount();
			paste(diagramPaster);
			possiblyNotitfyUserIfDataWasLost(diagramPaster);
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

	private String getUsersChoice(TransferableMiradiList list) throws ParseException
	{
		if (! list.atleastOneFactorExists())
			return AS_COPY_BUTTON;

		if (isPastingInSameDiagramAsCopiedFrom(list))
			return AS_COPY_BUTTON;
		
		if (!isPasteInSameProject(list))
			return AS_COPY_BUTTON;
		
		String[] buttons = {AS_COPY_BUTTON, AS_ALIAS_BUTTON, CANCEL_BUTTON};
		String title = EAM.text("Paste As...");
		String[] body = {EAM.text("Do you want to paste full new copies of the factors, or share the existing factors? " +
								"If you paste new copies, any changes will not affect the originals. " +
								"If you share, any changes will automatically affect all the diagrams.")};
	
		return EAM.choiceDialog(title, body, buttons);
	}

	private boolean isPastingInSameDiagramAsCopiedFrom(TransferableMiradiList list)
	{
		ORef diagramObjecRefCopiedFrom = list.getDiagramObjectRefCopiedFrom();
		ORef diagramObjectRefBeingPastedInto = getDiagramView().getDiagramPanel().getDiagramObject().getRef();
		
		boolean pasteInSameDiagram = diagramObjecRefCopiedFrom.equals(diagramObjectRefBeingPastedInto);
		return pasteInSameDiagram && isPasteInSameProject(list);
	}

	private boolean isPasteInSameProject(TransferableMiradiList list)
	{
		return list.getProjectFileName().equals(getProject().getFilename());
	}
	
	private DiagramPaster createDiagramPasterBaseOnUserChoice(TransferableMiradiList list, String usersChoice) throws Exception
	{		
		if (usersChoice.equals(AS_ALIAS_BUTTON))
			return new DiagramAliasPaster(getDiagramView().getDiagramPanel(), getDiagramView().getDiagramModel(), list);
		
		return new DiagramCopyPaster(getDiagramView().getDiagramPanel(), getDiagramView().getDiagramModel(), list);
	}

	private void possiblyNotitfyUserIfDataWasLost(DiagramPaster diagramPaster) throws Exception
	{
		if (!diagramPaster.wasAnyDataLost())
			return;
		
		EAM.notifyDialog(EAM.text("Some of the data could not be moved to this project because " +
								  "it refers to other data that only exists in the old project"));
	}

	protected void paste(DiagramPaster diagramPaster) throws Exception
	{
		diagramPaster.pasteFactorsAndLinks(getLocation());
		diagramPaster.wrapExistingLinksForDiagramFactorsInAllDiagramObjects();
	}
	
	private final String AS_COPY_BUTTON = EAM.text("Button|As Copy");
	private final String AS_ALIAS_BUTTON = EAM.text("Button|Shared");
	private final String CANCEL_BUTTON = EAM.text("Button|Cancel");
}
