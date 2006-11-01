/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramLinkageId;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramAddLinkage extends Command
{
	public CommandDiagramAddLinkage(ModelLinkageId idToWrap)
	{
		modelLinkageId = idToWrap;
		diagramLinkageId = new DiagramLinkageId(BaseId.INVALID.asInt());
	}
	
	public DiagramLinkageId getDiagramLinkageId()
	{
		return diagramLinkageId;
	}

	public ModelLinkageId getModelLinkageId()
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


	// FIXME: Delete this as soon as possible
	public static BaseId createLinkage(Project target, BaseId linkageId, ModelNodeId fromId, ModelNodeId toId) throws Exception, IOException, ParseException
	{
		CreateModelLinkageParameter parameter = new CreateModelLinkageParameter(fromId, toId);
		BaseId createdId = target.createObject(ObjectType.MODEL_LINKAGE, linkageId, parameter);
		target.addLinkageToDiagram(createdId);
		return createdId;
	}


	public static final String COMMAND_NAME = "DiagramAddLinkage";

	ModelLinkageId modelLinkageId;
	DiagramLinkageId diagramLinkageId;
}
