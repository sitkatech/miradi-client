package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ShareMethodDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
	}
}
