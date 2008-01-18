/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.exceptions;

public class UnknownCommandException extends Exception
{
	public UnknownCommandException()
	{
		super();
	}

	public UnknownCommandException(String arg0)
	{
		super(arg0);
	}

	public UnknownCommandException(Throwable arg0)
	{
		super(arg0);
	}

	public UnknownCommandException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

}
