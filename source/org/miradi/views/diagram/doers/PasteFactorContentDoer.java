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
package org.miradi.views.diagram.doers;

import java.util.HashMap;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.views.diagram.DiagramCopyPaster;

public class PasteFactorContentDoer extends AbstractPasteDoer
{
	@Override
	public boolean isAvailable()
	{
		boolean isSuperAvailable = super.isAvailable();
		if (!isSuperAvailable)
			return false;

		if (hasIncorrectFactorSelection())
			return false;

		return hasOnlySingleFactorInClipboard();
	}

	private boolean hasOnlySingleFactorInClipboard()
	{
		try
		{
			return getClipboardDiagramFactors().size() == 1;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	private boolean hasIncorrectFactorSelection()
	{
		return getSelectedFactor() == null;
	}

	private FactorCell getSelectedFactor()
	{
		return getDiagramView().getDiagramComponent().getSelectedFactor();
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;

		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORef selectedFactorRef = getSelectedFactor().getWrappedORef();
			DiagramCopyPaster paster = new DiagramCopyPaster(getDiagramPanel(), getDiagramModel(), getTransferableMiradiList());
			paster.pasteFactors(getLocation());
			
			//FIXME  can this diagram factor be null,  needs if condition?			
			DiagramFactor newlyPastedDiagramFactor = getNewlyPastedFactor(paster);
			if (newlyPastedDiagramFactor.getWrappedType() != selectedFactorRef.getObjectType())
				return;
			
			Vector<Command> commands = buildCommandsToFill(selectedFactorRef, newlyPastedDiagramFactor.getWrappedFactor());
			getProject().executeCommandsWithoutTransaction(commands);
			
			deleteDiagramFactorAndUnderlyingFactor(newlyPastedDiagramFactor);
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

	private void deleteDiagramFactorAndUnderlyingFactor(DiagramFactor newlyPastedDiagramFactor) throws Exception
	{
		Factor factorToDelete = newlyPastedDiagramFactor.getWrappedFactor();
		CommandSetObjectData removeFromDiagram = CommandSetObjectData.createRemoveIdCommand(getDiagramModel().getDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_IDS, newlyPastedDiagramFactor.getId());
		getProject().executeCommand(removeFromDiagram);
		
		getProject().executeCommandsWithoutTransaction(newlyPastedDiagramFactor.createCommandsToClear());
		getProject().executeCommand(new CommandDeleteObject(newlyPastedDiagramFactor));
		
		getProject().executeCommandsWithoutTransaction(factorToDelete.createCommandsToClear());
		getProject().executeCommand(new CommandDeleteObject(factorToDelete));		
	}

	private DiagramFactor getNewlyPastedFactor(DiagramCopyPaster paster)
	{
		HashMap<ORef, ORef> oldToNewFactorRefMap = paster.getOldToNewObjectRefMap();
		ORef[] pastedRefsArray = oldToNewFactorRefMap.values().toArray(new ORef[0]);
		ORefList pastedRefs = new ORefList(pastedRefsArray);
		ORef diagramFactorRef = pastedRefs.getRefForType(DiagramFactor.getObjectType());
		
		return DiagramFactor.find(getProject(), diagramFactorRef);
	}

	private Vector<Command> buildCommandsToFill(ORef selectedFactorRef, Factor newlyPastedFactor)
	{
		Vector<Command> commands = new Vector();
		String[] allTags = newlyPastedFactor.getFieldTags();
		for (int tagIndex = 0; tagIndex < allTags.length; ++tagIndex)
		{
			String dataToTransfer = newlyPastedFactor.getData(allTags[tagIndex]);
			if (newlyPastedFactor.isPseudoField(allTags[tagIndex]))
				continue;
			
			commands.add(new CommandSetObjectData(selectedFactorRef, allTags[tagIndex], dataToTransfer));
		}
		
		return commands;
	}

	private Vector getClipboardDiagramFactors() throws Exception
	{
		TransferableMiradiList list = getTransferableMiradiList();
		if (list == null)
			return new Vector();
		
		return list.getDiagramFactorDeepCopies();
	}
}
