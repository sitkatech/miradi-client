/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Task;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.umbrella.DeleteActivity;

public class DeleteMethodDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getSelectedMethod() == null)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		DeleteActivity.deleteTaskWithUserConfirmation(getProject(), getSelectionHierarchy(), getSelectedMethod());

	}

	public Task getSelectedMethod()
	{
		if(getSelectedHierarchies().length != 1)
			return null;
		
		ORef methodRef = getSelectedHierarchies()[0].getRefForType(Task.getObjectType());
		if(methodRef == null || methodRef.isInvalid())
			return null;
		
		return Task.find(getProject(), methodRef);
	}

}
