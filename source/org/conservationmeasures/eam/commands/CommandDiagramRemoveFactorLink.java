/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.util.HashMap;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramRemoveFactorLink extends Command
{
	public CommandDiagramRemoveFactorLink(DiagramFactorLinkId idToDelete)
	{
		diagramFactorLinkIdToDelete = idToDelete;
		removedDiagramFactorLinkId = new DiagramFactorLinkId(BaseId.INVALID.asInt());
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
			removedDiagramFactorLinkId = target.removeLinkFromDiagram(diagramFactorLinkIdToDelete);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		CommandDiagramAddFactorLink commandDiagramAddFactorLink = new CommandDiagramAddFactorLink(removedDiagramFactorLinkId);
		commandDiagramAddFactorLink.setDiagramFactorLinkId(diagramFactorLinkIdToDelete);
		return commandDiagramAddFactorLink;
	}

	public DiagramFactorLinkId getDiagramFactorLinkId()
	{
		return diagramFactorLinkIdToDelete;
	}

	public DiagramFactorLinkId getFactorLinkId()
	{
		return removedDiagramFactorLinkId;
	}

	void setFactorLinkId(DiagramFactorLinkId diagramFactorLinkIdToUse)
	{
		removedDiagramFactorLinkId = diagramFactorLinkIdToUse;
	}
	
	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put(DiagramFactorLinkId.class.getSimpleName(), diagramFactorLinkIdToDelete);
		return dataPairs;
	}

	public static final String COMMAND_NAME = "CommandDiagramRemoveFactorLink";

	DiagramFactorLinkId diagramFactorLinkIdToDelete;
	DiagramFactorLinkId removedDiagramFactorLinkId;
}
