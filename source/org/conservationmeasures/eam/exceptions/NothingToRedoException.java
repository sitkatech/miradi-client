/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.exceptions;


public class NothingToRedoException extends CommandFailedException
{
	public NothingToRedoException()
	{
		super("Nothing to redo");
	}

}
