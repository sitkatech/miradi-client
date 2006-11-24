/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramRemoveFactor extends Command
{
	public CommandDiagramRemoveFactor(DiagramNodeId idToDelete)
	{
		diagramNodeId = idToDelete;
		modelNodeId = new ModelNodeId(BaseId.INVALID.asInt());
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

	public DiagramNodeId getDiagramNodeId()
	{
		return diagramNodeId;
	}
	
	public ModelNodeId getModelNodeId()
	{
		return modelNodeId;
	}
	
	
	public static final String COMMAND_NAME = "DiagramRemoveNode";

	DiagramNodeId diagramNodeId;
	ModelNodeId modelNodeId;
}
