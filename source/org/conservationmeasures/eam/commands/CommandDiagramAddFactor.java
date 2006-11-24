/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;


public class CommandDiagramAddFactor extends Command
{
	public CommandDiagramAddFactor(DiagramFactorId idToUse, FactorId idToWrap)
	{
		wrappedFactorId = idToWrap;
		insertedDiagramFactorId = idToUse;
	}
	
	public DiagramFactorId getInsertedId()
	{
		return insertedDiagramFactorId;
	}
	
	public FactorId getFactorId()
	{
		return wrappedFactorId;
	}

	public String toString()
	{
		return getCommandName() + ":" + insertedDiagramFactorId + ","+ wrappedFactorId;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			insertedDiagramFactorId = target.addFactorToDiagram(wrappedFactorId, insertedDiagramFactorId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void undo(Project target) throws CommandFailedException
	{
		try
		{
			target.removeDiagramFactorFromDiagram(insertedDiagramFactorId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public static final String COMMAND_NAME = "CommandDiagramAddFactor";

	private FactorId wrappedFactorId;
	private DiagramFactorId insertedDiagramFactorId;
}
