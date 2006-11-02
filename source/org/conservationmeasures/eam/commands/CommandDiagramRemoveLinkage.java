/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramLinkageId;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramRemoveLinkage extends Command
{
	public CommandDiagramRemoveLinkage(DiagramLinkageId idToDelete)
	{
		diagramLinkageId = idToDelete;
		modelLinkageId = new ModelLinkageId(BaseId.INVALID.asInt());
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

	public DiagramLinkageId getDiagramLinkageId()
	{
		return diagramLinkageId;
	}

	public ModelLinkageId getModelLinkageId()
	{
		return modelLinkageId;
	}

	

	public static final String COMMAND_NAME = "DiagramRemoveLinkage";

	DiagramLinkageId diagramLinkageId;
	ModelLinkageId modelLinkageId;
}
