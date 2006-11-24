/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramRemoveFactor extends Command
{
	public CommandDiagramRemoveFactor(DiagramFactorId idToDelete)
	{
		diagramNodeId = idToDelete;
		modelNodeId = new FactorId(BaseId.INVALID.asInt());
	}

	public String toString()
	{
		return getCommandName() + ":" + getDiagramNodeId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			modelNodeId = target.removeNodeFromDiagram(diagramNodeId);
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
			target.addNodeToDiagram(modelNodeId, diagramNodeId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public DiagramFactorId getDiagramNodeId()
	{
		return diagramNodeId;
	}
	
	public FactorId getModelNodeId()
	{
		return modelNodeId;
	}
	
	
	public static final String COMMAND_NAME = "DiagramRemoveNode";

	DiagramFactorId diagramNodeId;
	FactorId modelNodeId;
}
