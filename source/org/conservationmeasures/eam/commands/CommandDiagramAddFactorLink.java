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

public class CommandDiagramAddFactorLink extends Command
{
	public CommandDiagramAddFactorLink(FactorLinkId idToWrap)
	{
		wrappedFactorLinkId = idToWrap;
		insertedDiagramFactorLinkId = new DiagramFactorLinkId(BaseId.INVALID.asInt());
	}
	
	public DiagramFactorLinkId getDiagramFactorLinkId()
	{
		return insertedDiagramFactorLinkId;
	}

	public FactorLinkId getFactorLinkId()
	{
		return wrappedFactorLinkId;
	}

	public String toString()
	{
		return getCommandName() + ": " + insertedDiagramFactorLinkId + "," + wrappedFactorLinkId ;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			insertedDiagramFactorLinkId = target.addLinkageToDiagram(wrappedFactorLinkId);
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
			target.removeLinkageFromDiagram(insertedDiagramFactorLinkId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		
	}


	public static final String COMMAND_NAME = "CommandDiagramAddFactorLink";

	FactorLinkId wrappedFactorLinkId;
	DiagramFactorLinkId insertedDiagramFactorLinkId;
}
