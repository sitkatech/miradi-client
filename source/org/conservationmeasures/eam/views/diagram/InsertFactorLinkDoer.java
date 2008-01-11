/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.dialogs.diagram.LinkCreateDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactor;
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
		LinkCreateDialog dialog = new LinkCreateDialog(getMainWindow(), diagramView.getDiagramPanel());
		dialog.setVisible(true);
		if(!dialog.getResult())
			return;
			
		DiagramModel model = diagramView.getDiagramModel();
		DiagramFactor fromDiagramFactor = dialog.getFrom();
		DiagramFactor toDiagramFactor = dialog.getTo();
		LinkCreator linkCreator = new LinkCreator(getProject());
		try
		{
			if (linkCreator.linkWasRejected(model, fromDiagramFactor, toDiagramFactor))
				return;
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			if (!fromDiagramFactor.isGroupBoxFactor() && !toDiagramFactor.isGroupBoxFactor())
				linkCreator.createFactorLinkAndAddToDiagramUsingCommands(model, fromDiagramFactor, toDiagramFactor);
			else
				linkCreator.createGroupBoxChildrenDiagramLinks(model, fromDiagramFactor, toDiagramFactor);
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
}
