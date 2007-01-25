/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

	public Command getReverseCommand() throws CommandFailedException
	{
		CommandDiagramRemoveFactor commandDiagramRemoveFactor = new CommandDiagramRemoveFactor(insertedDiagramFactorId);
		commandDiagramRemoveFactor.setFactorId(wrappedFactorId);
		return commandDiagramRemoveFactor;
	}

	public static final String COMMAND_NAME = "CommandDiagramAddFactor";

	private FactorId wrappedFactorId;
	private DiagramFactorId insertedDiagramFactorId;
}
