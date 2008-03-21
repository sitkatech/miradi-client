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
package org.miradi.views.planning.doers;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.DeleteActivity;

public class TreeNodeDeleteDoer extends AbstractTreeNodeDoer
{
	public boolean isAvailable()
	{
		BaseObject selected = getSingleSelectedObject();
		if(selected == null)
			return false;
		if(selected.getType() != Task.getObjectType())
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		BaseObject selected = getSingleSelectedObject();
		if(selected.getType() != Task.getObjectType())
			return;
		
		Task selectedTaskToDelete = (Task) getSingleSelectedObject();
		if (shouldDeleteFromParentOnly(selectedTaskToDelete))
			DeleteActivity.deleteTaskWithUserConfirmation(getProject(), getSelectionHierarchy(), selectedTaskToDelete);
		else
			DeleteActivity.deleteTaskWithUserConfirmation(getProject(), selectedTaskToDelete.findObjectsThatReferToUs(), selectedTaskToDelete);
	}
	
	private boolean shouldDeleteFromParentOnly(Task selectedTaskToDelete)
	{
		ORefList referrers = selectedTaskToDelete.findObjectsThatReferToUs();
		ORefList selectionHierarchy = getSelectionHierarchy();
		
		return selectionHierarchy.containsAnyOf(referrers);
	}
}
