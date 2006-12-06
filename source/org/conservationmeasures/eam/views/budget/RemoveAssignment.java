/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class RemoveAssignment extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
	}
}
