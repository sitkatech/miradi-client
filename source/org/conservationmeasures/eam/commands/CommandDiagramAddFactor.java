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
		modelNodeId = idToWrap;
		insertedId = idToUse;
	}
	
	public DiagramFactorId getInsertedId()
	{
		return insertedId;
	}
	
	public FactorId getModelNodeId()
	{
		return modelNodeId;
	}

	public String toString()
	{
		return getCommandName() + ":" + insertedId + ","+ modelNodeId;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			insertedId = target.addNodeToDiagram(modelNodeId, insertedId);
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
			target.removeNodeFromDiagram(insertedId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public static final String COMMAND_NAME = "CommandDiagramAddNode";

	private FactorId modelNodeId;
	private DiagramFactorId insertedId;
}
