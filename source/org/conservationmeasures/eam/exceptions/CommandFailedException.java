/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
	
	public CommandFailedException(String message, Exception causedBy)
	{
		super(message, causedBy);
	}
}
