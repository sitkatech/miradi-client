package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.dialogs.ShareableActivityPoolTablePanel;
import org.conservationmeasures.eam.dialogs.diagram.ShareSelectionDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.views.planning.doers.AbstractShareDoer;

public class ShareActivityDoer extends AbstractShareDoer
{	
	public boolean isAvailable()
	{
		if (! isDiagramView())
			return false;
	
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		EAMGraphCell selected = getDiagramView().getDiagramPanel().getOnlySelectedCells()[0];
		if (! selected.isFactor())
			return;
		
		DiagramFactor diagramFactor = selected.getDiagramFactor();
		ShareSelectionDialog listDialog = new ShareSelectionDialog(getMainWindow(), EAM.text("Share Activity"), new ShareableActivityPoolTablePanel(getProject(), diagramFactor.getWrappedORef()));
		listDialog.setVisible(true);
		
		appendSelectedActivity(diagramFactor, listDialog.getSelectedObject());
	}

	private void appendSelectedActivity(DiagramFactor diagramFactor, BaseObject objectToShare) throws CommandFailedException
	{
		if (objectToShare == null)
			return;
		
		try
		{
			Strategy strategy = (Strategy) getProject().findObject(diagramFactor.getWrappedORef());
			CommandSetObjectData appendActivityCommand = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_ACTIVITY_IDS, objectToShare.getId());
			getProject().executeCommand(appendActivityCommand);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
