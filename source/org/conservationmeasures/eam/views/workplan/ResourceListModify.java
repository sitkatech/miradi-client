/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ResourceListModify extends ObjectsDoer
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
