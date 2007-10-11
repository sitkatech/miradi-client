package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ShareableMethodPoolTablePanel;
import org.conservationmeasures.eam.dialogs.diagram.ShareSelectionDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;

public class ShareMethodDoer extends AbstractTreeNodeCreateTaskDoer
{	
	protected boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Indicator.getObjectType())
			return true;
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		ORef selectedRef = getSelectedRef();
		ShareSelectionDialog listDialog = new ShareSelectionDialog(getMainWindow(), EAM.text("Share Method"), new ShareableMethodPoolTablePanel(getProject(), selectedRef));
		listDialog.setVisible(true);
		
		appendSelectedActivity(selectedRef, listDialog.getSelectedObject());
	}

	private void appendSelectedActivity(ORef indicatorRef, BaseObject objectToShare) throws CommandFailedException
	{
		if (objectToShare == null)
			return;
		
		try
		{
			Indicator indicator = (Indicator) getProject().findObject(indicatorRef);
			CommandSetObjectData appendMethodCommand = CommandSetObjectData.createAppendIdCommand(indicator, Indicator.TAG_TASK_IDS, objectToShare.getId());
			getProject().executeCommand(appendMethodCommand);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
