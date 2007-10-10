package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.dialogs.ActivityPoolTablePanel;
import org.conservationmeasures.eam.dialogs.diagram.ShareSelectionDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.diagram.LocationDoer;

public class ShareActivityDoer extends LocationDoer
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
		ShareSelectionDialog listDialog = new ShareSelectionDialog(getMainWindow(), EAM.text("Share Activity"), new ActivityPoolTablePanel(getProject(), diagramFactor.getWrappedORef()));
		listDialog.setVisible(true);
		
		appendSelectedActivity(diagramFactor, (Task) listDialog.getSelectedObject());
	}

	private void appendSelectedActivity(DiagramFactor diagramFactor, Task taskToShare) throws CommandFailedException
	{
		try
		{
			Strategy strategy = (Strategy) getProject().findObject(diagramFactor.getWrappedORef());
			CommandSetObjectData appendActivityCommand = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_ACTIVITY_IDS, taskToShare.getId());
			getProject().executeCommand(appendActivityCommand);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
