/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

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
