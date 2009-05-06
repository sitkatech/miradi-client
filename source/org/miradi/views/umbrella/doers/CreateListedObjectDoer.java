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
package org.miradi.views.umbrella.doers;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.diagram.CreateAnnotationDoer;

abstract public class CreateListedObjectDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		return getSelectionHierarchy().size() > 0;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;

		try 
		{
			ORefList selectedRefs = getSelectionHierarchy();
			BaseObject selectedBaseObject = BaseObject.find(getProject(), selectedRefs.get(0));			
			createBaseObject(selectedBaseObject);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void createBaseObject(BaseObject selectedBaseObject) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject createCommand = new CommandCreateObject(getTypeToCreate());
			getProject().executeCommand(createCommand);

			Command appendCommand = CreateAnnotationDoer.createAppendCommand(selectedBaseObject, createCommand.getObjectRef(), getListTag());
			getProject().executeCommand(appendCommand);
		}
		finally 
		{
			getProject().executeCommand(new CommandEndTransaction());	
		}
	}

	abstract protected String getListTag();

	abstract protected int getTypeToCreate();
}
