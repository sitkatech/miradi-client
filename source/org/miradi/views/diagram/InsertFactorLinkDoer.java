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
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.dialogs.diagram.LinkCreateDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramLinkId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.views.ViewDoer;

public class InsertFactorLinkDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! getProject().isOpen())
			return false;
		
		if (!isInDiagram())
			return false;
		
		return (getDiagramView().getDiagramModel().getFactorCount() >= 2);
	}

	public void doIt() throws CommandFailedException
	{
		DiagramView diagramView = getDiagramView();
		PersistentDiagramModel model = diagramView.getDiagramModel();

		FromToDiagramFactorsHolder fromToFactorsHolder = getFromToDiagramFactors(diagramView);
		if (fromToFactorsHolder == null)
			return;
		
		DiagramFactor from = fromToFactorsHolder.getFrom();
		DiagramFactor to = fromToFactorsHolder.getTo();
		
		LinkCreator linkCreator = new LinkCreator(getProject());
		try
		{
			addOrphandDiagramLinksToDiagramObject(linkCreator, from, to);
			if (linkCreator.linkToBeCreatedWasRejected(model, from, to))
				return;
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			linkCreator.createFactorLinkAndDiagramLink(model, from, to);
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

	private void addOrphandDiagramLinksToDiagramObject(LinkCreator linkCreator, DiagramFactor from, DiagramFactor to) throws Exception
	{
		ORef factorLinkRef = getProject().getFactorLinkPool().getLinkedRef(from.getWrappedFactor(), to.getWrappedFactor());
		if (factorLinkRef.isInvalid())
			return;
		
		ORefList diagramObjectRefs = DiagramObject.getDiagramRefsContainingLink(getProject(), factorLinkRef);
		if (diagramObjectRefs.size() > 0)
			return;

		FactorLink factorLink = FactorLink.find(getProject(), factorLinkRef);
		ORefList diagramLinkReferrerRefs = factorLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
		ORef diagramLinkReferrerRef = diagramLinkReferrerRefs.getRefForType(DiagramLink.getObjectType());
	
		diagramLinkReferrerRef.ensureType(DiagramLink.getObjectType());
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkReferrerRef);
		diagramLink.invalidateCachedOwner();
		
		CommandSetObjectData addDiagramLink = CommandSetObjectData.createAppendIdCommand(getDiagramView().getCurrentDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, new DiagramLinkId(diagramLinkReferrerRef.getObjectId().asInt()));
		getProject().executeCommand(addDiagramLink);
		
		EAM.logWarning("An Orphand Diagram Link was found for where the user wanted to create a link.  The Orphand Link was added to the Diagram");
	}

	private FromToDiagramFactorsHolder getFromToDiagramFactors(DiagramView diagramView)
	{
		DiagramPanel diagramPanel = diagramView.getDiagramPanel();
		DiagramComponent diagram = diagramPanel.getCurrentDiagramComponent();
		FromToDiagramFactorsHolder fromToHolder = getFromToDiagramsForNonDiaglogCreation(diagram);
		if (fromToHolder != null)
			return fromToHolder;
		
		LinkCreateDialog dialog = new LinkCreateDialog(getMainWindow(), diagramPanel);
		dialog.setVisible(true);
		if(!dialog.getResult())
			return null;
		
		return new FromToDiagramFactorsHolder(dialog.getFrom(), dialog.getTo());
	}

	private FromToDiagramFactorsHolder getFromToDiagramsForNonDiaglogCreation(DiagramComponent diagram)
	{
		if (diagram.getOnlySelectedFactorCells().length != 2)
			return null;

		FactorCell fromCell = diagram.getSelectedFactor(0);
		FactorCell toCell = diagram.getSelectedFactor(1);
		if (isInvalidType(fromCell))
			return null;
		
		if (isInvalidType(toCell))
			return null;
		
		return new FromToDiagramFactorsHolder(fromCell.getDiagramFactor(), toCell.getDiagramFactor());
	}

	private boolean isInvalidType(FactorCell cell)
	{
		if (cell == null)
			return true;
		
		return !LinkCreator.isValidLinkableType(cell.getWrappedType());
	}

	private class FromToDiagramFactorsHolder
	{
		public FromToDiagramFactorsHolder(DiagramFactor fromToUse, DiagramFactor toToUse)
		{
			from = fromToUse;
			to = toToUse;
		}

		public DiagramFactor getFrom()
		{
			return from;
		}
		public DiagramFactor getTo()
		{
			return to;
		}		
		
		private DiagramFactor from;
		private DiagramFactor to;
	}
}
