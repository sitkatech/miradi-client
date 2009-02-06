/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;

public class CreateActivityDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		BaseObject selectedParent = getSelectedParentFactor();
		if (selectedParent == null)
			return false;
			
		return Strategy.is(selectedParent);
	}

	public void doIt() throws CommandFailedException
	{
		doInsertActivity();
	}

	private void doInsertActivity() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		Strategy strategy = (Strategy) getSelectedParentFactor();

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

	private void insertActivity(Project project, Strategy strategy, int childIndex) throws CommandFailedException, ParseException, Exception
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
