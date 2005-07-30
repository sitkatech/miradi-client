/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.exceptions;

public class NothingToUndoException extends CommandFailedException
{
	public NothingToUndoException()
	{
		super("Nothing to undo");
	}

}
