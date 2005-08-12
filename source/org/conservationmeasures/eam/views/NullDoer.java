/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.exceptions.CommandFailedException;

public class NullDoer extends Doer
{
	public boolean isAvailable()
	{
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		throw new CommandFailedException("Attempted to doIt in NullDoer!");
	}
}
