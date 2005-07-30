/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.exceptions;

public class CommandFailedException extends Exception
{
	public CommandFailedException(String message)
	{
		super(message);
	}
	
	public CommandFailedException(Exception causedBy)
	{
		super(causedBy);
	}

}
