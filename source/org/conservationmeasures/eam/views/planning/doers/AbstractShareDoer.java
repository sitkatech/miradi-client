package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;


abstract public class AbstractShareDoer extends AbstractTreeNodeCreateTaskDoer
{
	protected void appendSelectedObjectAsShared(ORef parentOfSharedRef, String tag, BaseObject objectToShare) throws CommandFailedException
	{
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
}
