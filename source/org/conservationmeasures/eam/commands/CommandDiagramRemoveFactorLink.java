/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramRemoveFactorLink extends Command
{
	public CommandDiagramRemoveFactorLink(DiagramFactorLinkId idToDelete)
	{
		diagramFactorLinkId = idToDelete;
		wrappedFactorLinkId = new FactorLinkId(BaseId.INVALID.asInt());
	}
	
	public String toString()
	{
		return getCommandName() + ":" + getDiagramFactorLinkId() + "," + getFactorLinkId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			wrappedFactorLinkId = target.removeLinkageFromDiagram(diagramFactorLinkId);
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
			target.addLinkageToDiagram(wrappedFactorLinkId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public DiagramFactorLinkId getDiagramFactorLinkId()
	{
		return diagramFactorLinkId;
	}

	public FactorLinkId getFactorLinkId()
	{
		return wrappedFactorLinkId;
	}

	

	public static final String COMMAND_NAME = "CommandDiagramRemoveFactorLink";

	DiagramFactorLinkId diagramFactorLinkId;
	FactorLinkId wrappedFactorLinkId;
}
