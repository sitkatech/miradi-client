/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import java.text.ParseException;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.views.ViewDoer;

public class CreateActivityDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		doInsertActivity();
	}

	private void doInsertActivity() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		Strategy strategy = (Strategy)getView().getSelectedObject();

		try
		{
			insertActivity(getProject(), strategy, strategy.getActivityIds().size());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public static void insertActivity(Project project, Strategy strategy, int childIndex) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			project.executeCommand(create);
			BaseId createdId = create.getCreatedId();
	
			CommandSetObjectData addChild = CommandSetObjectData.createInsertIdCommand(strategy, 
					Strategy.TAG_ACTIVITY_IDS, createdId, childIndex);
			project.executeCommand(addChild);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
		
	}
}
