/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.exceptions;


public class NothingToRedoException extends CommandFailedException
{
	public NothingToRedoException()
	{
		super("Nothing to redo");
	}

}
