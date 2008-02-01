/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.dialogs.diagram.LinkCreateDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.TextBox;
import org.conservationmeasures.eam.views.ViewDoer;

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
