package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.MethodPoolTablePanel;
import org.conservationmeasures.eam.dialogs.diagram.ActivityShareSelectionDiaglog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;

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
		ActivityShareSelectionDiaglog listDialog = new ActivityShareSelectionDiaglog(getMainWindow(), EAM.text("Share Method"), new MethodPoolTablePanel(getProject(), selectedRef));
		listDialog.setVisible(true);
		
		appendSelectedActivity(selectedRef, (Task) listDialog.getSelectedObject());
	}

	private void appendSelectedActivity(ORef indicatorRef, Task taskToShare) throws CommandFailedException
	{
		try
		{
			Indicator indicator = (Indicator) getProject().findObject(indicatorRef);
			CommandSetObjectData appendMethodCommand = CommandSetObjectData.createAppendIdCommand(indicator, Indicator.TAG_TASK_IDS, taskToShare.getId());
			getProject().executeCommand(appendMethodCommand);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
