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
		diagramLinkageId = idToDelete;
		modelLinkageId = new FactorLinkId(BaseId.INVALID.asInt());
	}
	
	public String toString()
	{
		return getCommandName() + ":" + getDiagramLinkageId() + "," + getModelLinkageId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			modelLinkageId = target.removeLinkageFromDiagram(diagramLinkageId);
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
			target.addLinkageToDiagram(modelLinkageId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public DiagramFactorLinkId getDiagramLinkageId()
	{
		return diagramLinkageId;
	}

	public FactorLinkId getModelLinkageId()
	{
		return modelLinkageId;
	}

	

	public static final String COMMAND_NAME = "DiagramRemoveLinkage";

	DiagramFactorLinkId diagramLinkageId;
	FactorLinkId modelLinkageId;
}
