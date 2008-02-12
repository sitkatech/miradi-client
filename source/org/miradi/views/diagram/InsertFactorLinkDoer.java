/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.dialogs.diagram.LinkCreateDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.TextBox;
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
		DiagramModel model = diagramView.getDiagramModel();

		FromToDiagramFactorsHolder fromToFactorsHolder = getFromToDiagramFactors(diagramView);
		if (fromToFactorsHolder == null)
			return;
		
		DiagramFactor from = fromToFactorsHolder.getFrom();
		DiagramFactor to = fromToFactorsHolder.getTo();
		
		LinkCreator linkCreator = new LinkCreator(getProject());
		try
		{
			if (linkCreator.linkWasRejected(model, from, to))
				return;
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			if (!from.isGroupBoxFactor() && !to.isGroupBoxFactor())
				linkCreator.createFactorLinkAndAddToDiagramUsingCommands(model, from, to);
			else
				linkCreator.createGroupBoxChildrenDiagramLinks(model, from, to);
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

	private FromToDiagramFactorsHolder getFromToDiagramFactors(DiagramView diagramView)
	{
		DiagramPanel diagramPanel = diagramView.getDiagramPanel();
		DiagramComponent diagram = diagramPanel.getdiagramComponent();
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
		
		return cell.getWrappedType() == TextBox.getObjectType();
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
