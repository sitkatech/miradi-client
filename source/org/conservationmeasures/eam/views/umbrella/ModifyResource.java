/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;



import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ModifyResource extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			ProjectResource resource = (ProjectResource)getObjects()[0];
			getView().showPropertiesDialog(resource);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

}
