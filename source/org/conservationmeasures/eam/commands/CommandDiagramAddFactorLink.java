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
		modelLinkageId = idToWrap;
		diagramLinkageId = new DiagramFactorLinkId(BaseId.INVALID.asInt());
	}
	
	public DiagramFactorLinkId getDiagramLinkageId()
	{
		return diagramLinkageId;
	}

	public FactorLinkId getModelLinkageId()
	{
		return modelLinkageId;
	}

	public String toString()
	{
		return getCommandName() + ": " + diagramLinkageId + "," + modelLinkageId ;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			diagramLinkageId = target.addLinkageToDiagram(modelLinkageId);
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
			target.removeLinkageFromDiagram(diagramLinkageId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		
	}


	public static final String COMMAND_NAME = "DiagramAddLinkage";

	FactorLinkId modelLinkageId;
	DiagramFactorLinkId diagramLinkageId;
}
