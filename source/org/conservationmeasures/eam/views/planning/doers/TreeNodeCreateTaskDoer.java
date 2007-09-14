/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class TreeNodeCreateTaskDoer extends AbstractTreeNodeDoer
{
	public boolean isAvailable()
	{
		BaseObject selected = getSingleSelectedObject();
		if(selected == null)
			return false;
		if(!canOwnTask(selected))
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			createTask(getProject(), getSingleSelectedObject(), getPicker());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: " + e.getMessage());
		}
	}
	
	boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Task.getObjectType())
			return true;
		
		return false;
	}

	public static void createTask(Project project, BaseObject parent, ObjectPicker picker) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			project.executeCommand(create);
			BaseId createdId = create.getCreatedId();

			String containerTag = AbstractTreeNodeMoveDoer.getTaskIdsTag(parent);
			CommandSetObjectData addChildCommand = CommandSetObjectData.createAppendIdCommand(parent, containerTag, createdId);
			project.executeCommand(addChildCommand);
			
			ORef createdRef = new ORef(ObjectType.TASK, createdId);
			picker.ensureObjectVisible(createdRef);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
}
