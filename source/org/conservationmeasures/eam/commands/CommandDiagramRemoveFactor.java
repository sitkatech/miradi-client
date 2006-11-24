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
		diagramFactorId = idToDelete;
		wrappedFactorId = new FactorId(BaseId.INVALID.asInt());
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
			wrappedFactorId = target.removeDiagramFactorFromDiagram(diagramFactorId);
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
			target.addFactorToDiagram(wrappedFactorId, diagramFactorId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public DiagramFactorId getDiagramNodeId()
	{
		return diagramFactorId;
	}
	
	public FactorId getFactorId()
	{
		return wrappedFactorId;
	}
	
	
	public static final String COMMAND_NAME = "CommandDiagramRemoveFactor";

	DiagramFactorId diagramFactorId;
	FactorId wrappedFactorId;
}
