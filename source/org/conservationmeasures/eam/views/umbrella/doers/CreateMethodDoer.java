/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;

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
