/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.util.HashMap;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;


public class CommandDiagramAddFactor extends Command
{
	public CommandDiagramAddFactor(DiagramFactorId idToUse)
	{
		diagramFactorId = idToUse;
	}
	
	public DiagramFactorId getInsertedId()
	{
		return diagramFactorId;
	}
	
	public String toString()
	{
		return getCommandName() + ":" + diagramFactorId;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			target.addFactorToDiagram(diagramFactorId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandDiagramRemoveFactor(diagramFactorId);
	}
	
	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put(DiagramFactorId.class.getSimpleName(), diagramFactorId);
		return dataPairs;
	}

	public static final String COMMAND_NAME = "CommandDiagramAddFactor";

	private DiagramFactorId diagramFactorId;
}
