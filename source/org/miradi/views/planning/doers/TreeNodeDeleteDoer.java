/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
