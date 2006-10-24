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
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class CommandLinkNodes extends Command
{
	public CommandLinkNodes(ModelNodeId fromId, ModelNodeId toId)
	{
		this.fromId = fromId;
		this.toId = toId;
		linkageId = BaseId.INVALID;
	}
	
	public BaseId getLinkageId()
	{
		return linkageId;
	}

	public String toString()
	{
		return getCommandName() + ": " + linkageId + "," + fromId + ", " + toId;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			linkageId = createLinkage(target, getLinkageId(), getFromId(), getToId());
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
			CommandDiagramRemoveLinkage.deleteLinkage(target, getLinkageId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		
	}

	public ModelNodeId getFromId()
	{
		return fromId;
	}
	
	public ModelNodeId getToId()
	{
		return toId;
	}
	
	public static BaseId createLinkage(Project target, BaseId linkageId, ModelNodeId fromId, ModelNodeId toId) throws Exception, IOException, ParseException
	{
		CreateModelLinkageParameter parameter = new CreateModelLinkageParameter(fromId, toId);
		BaseId createdId = target.createObject(ObjectType.MODEL_LINKAGE, linkageId, parameter);
		target.addLinkageToDiagram(createdId);
		return createdId;
	}


	public static final String COMMAND_NAME = "LinkNodes";

	ModelNodeId fromId;
	ModelNodeId toId;
	BaseId linkageId;
}
