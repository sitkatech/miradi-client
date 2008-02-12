/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.exceptions;

public class NothingToUndoException extends CommandFailedException
{
	public NothingToUndoException()
	{
		super("Nothing to undo");
	}

}
