/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

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
		
		
		possiblyDeleteFromParentOnly((Task) selected);
		possiblyDeleteFromAllParents((Task) selected);
			
	}
	
	private void possiblyDeleteFromAllParents(Task selectedTaskToDelete) throws CommandFailedException
	{
		if (shouldDeleteFromParentOnly())
			return;
		
		DeleteActivity.deleteTask(getProject(), selectedTaskToDelete.findObjectsThatReferToUs(), selectedTaskToDelete);
	}

	private void possiblyDeleteFromParentOnly(Task selectedTaskToDelete) throws CommandFailedException
	{
		if (!shouldDeleteFromParentOnly())
			return;
		
		DeleteActivity.deleteTask(getProject(), getSelectionHierarchy(), selectedTaskToDelete);
	}

	private boolean shouldDeleteFromParentOnly()
	{
		Task selectedTask = (Task) getSingleSelectedObject();
		ORefList referrers = selectedTask.findObjectsThatReferToUs();
		ORefList selectionHierarchy = getSelectionHierarchy();
		for (int i = 0; i < selectionHierarchy.size(); ++i)
		{
			if (referrers.contains(selectionHierarchy.get(i)))
				return true;
		}
		
		return false;
	}

}
