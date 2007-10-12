package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectPoolTablePanel;
import org.conservationmeasures.eam.dialogs.diagram.ShareSelectionDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;


abstract public class AbstractShareDoer extends AbstractTreeNodeCreateTaskDoer
{
	protected void appendSelectedObjectAsShared(ORef parentOfSharedRef, String tag) throws CommandFailedException
	{
		if (parentOfSharedRef.isInvalid())
			return; 
		
		BaseObject objectToShare = getUserChoiceOfSharedObject(parentOfSharedRef);
		if (objectToShare == null)
			return;
		
		try
		{
			BaseObject parentOfShared = getProject().findObject(parentOfSharedRef);
			CommandSetObjectData appendSharedObjectCommand = CommandSetObjectData.createAppendIdCommand(parentOfShared, tag, objectToShare.getId());
			getProject().executeCommand(appendSharedObjectCommand);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	protected BaseObject getUserChoiceOfSharedObject(ORef parentOfSharedObjectRefs)
	{
		ShareSelectionDialog listDialog = new ShareSelectionDialog(getMainWindow(), getShareDialogTitle(), getShareableObjectPoolTablePanel(parentOfSharedObjectRefs));
		listDialog.setVisible(true);
		
		return listDialog.getSelectedObject();
	}
	
	abstract protected String getShareDialogTitle();
	
	abstract protected ObjectPoolTablePanel getShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs);
	
	abstract protected ORef getParentOfShareableObjects();
}
