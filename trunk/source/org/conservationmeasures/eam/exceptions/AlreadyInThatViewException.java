/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
