/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;

public class CreateMethodDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getSelectedIndicator() == null)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Indicator indicator = getSelectedIndicator();
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORef createdRef = createAndAppendId(indicator, Indicator.TAG_TASK_IDS, Task.getObjectType());
			getPicker().ensureObjectVisible(createdRef);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
		
	}

	private ORef createAndAppendId(BaseObject parent, String idsTag, int objectType) throws Exception
	{
		Project project = parent.getProject();
		CommandCreateObject create = new CommandCreateObject(objectType);
		project.executeCommand(create);
		BaseId createdId = create.getCreatedId();

		CommandSetObjectData addChild = CommandSetObjectData.createAppendIdCommand(parent, idsTag, createdId);
		project.executeCommand(addChild);
		return create.getObjectRef();
	}

	public Indicator getSelectedIndicator()
	{
		if(getSelectedHierarchies().length != 1)
			return null;
		
		ORef indicatorRef = getSelectedHierarchies()[0].getRefForType(Indicator.getObjectType());
		if(indicatorRef == null || indicatorRef.isInvalid())
			return null;
		
		return Indicator.find(getProject(), indicatorRef);
	}
}
