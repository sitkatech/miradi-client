/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;

//TODO InsertMethodDoer, InsertTaskDoer, InsertActivitDoer have code in common that needs to be refactored
public class CreateMethodDoer extends AbstractTaskTreeDoer
{
	public boolean isAvailable()
	{
		BaseObject[] selected = getObjects();
		if(selected == null || selected.length != 1)
			return false;
	
		if(selected[0].getType() != ObjectType.INDICATOR)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		BaseObject parent = getObjects()[0];
		String tag = AbstractTaskTreeDoer.getTaskIdsTag(parent.getRef());
		try
		{
			createTask(getProject(), parent, tag);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

}
