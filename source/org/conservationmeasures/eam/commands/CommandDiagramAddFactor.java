/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;


public class CommandDiagramAddFactor extends Command
{
	public CommandDiagramAddFactor(DiagramNodeId idToUse, ModelNodeId idToWrap)
	{
		modelNodeId = idToWrap;
		insertedId = idToUse;
	}
	
	public DiagramNodeId getInsertedId()
	{
		return insertedId;
	}
	
	public ModelNodeId getModelNodeId()
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

	private ModelNodeId modelNodeId;
	private DiagramNodeId insertedId;
}
