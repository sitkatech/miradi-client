/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.ViewData;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractTreeExpansionDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			ViewData viewData = getProject().getViewData(getProject().getCurrentView());		
			CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId() ,ViewData.TAG_CURRENT_EXPANSION_LIST, getExpandedNodeRefsAsString());
			getProject().executeCommand(cmd);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	abstract protected String getExpandedNodeRefsAsString() throws Exception;
}
