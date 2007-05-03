/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTablePanel;

public abstract class AbstractTaskTreeDoer extends ObjectsDoer
{
	TaskTreeTablePanel getPanel()
	{
		return getView().getTaskTreeTablePanel();
	}

	protected IdList getTaskIds(ORef parentRef)
	{
		try
		{
			return new IdList(getProject().getObjectData(parentRef, getTaskIdsTag(parentRef)));
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			return new IdList();
		}
	}

	public static String getTaskIdsTag(ORef container)
	{
		int type = container.getObjectType();
		switch(type)
		{
			case ObjectType.TASK:
				return Task.TAG_SUBTASK_IDS;
				
			// TODO: If we migrate all Task parents to have the 
			// correct object type, we can remove the FACTOR case
			case ObjectType.FACTOR:
			case ObjectType.STRATEGY:
				return Strategy.TAG_ACTIVITY_IDS;
			case ObjectType.INDICATOR:
				return Indicator.TAG_TASK_IDS;
		}
		
		throw new RuntimeException("getTaskIdsTag called for non-task container type " + container.getObjectType());
	}

	public void createTask(Project project, BaseObject object, String containerTag) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			project.executeCommand(create);
			BaseId createdId = create.getCreatedId();

			CommandSetObjectData addChildCommand = CommandSetObjectData.createAppendIdCommand(object, containerTag, createdId);
			project.executeCommand(addChildCommand);
			
			ORef ref = new ORef(ObjectType.TASK, createdId);
			getPicker().ensureObjectVisible(ref);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

}
