/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.exceptions;

public class AlreadyInThatViewException extends CommandFailedException
{
	public AlreadyInThatViewException(String message)
	{
		super(message);
	}

	public AlreadyInThatViewException(Exception causedBy)
	{
		super(causedBy);
	}

}
