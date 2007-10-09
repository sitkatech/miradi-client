package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;

public class ShareActivityDoer extends ViewDoer
{	
	public boolean isAvailable()
	{
		if (! isDiagramView())
			return false;
	
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		
	}
}
