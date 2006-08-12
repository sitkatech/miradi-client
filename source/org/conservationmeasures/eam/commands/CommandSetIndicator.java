/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandSetIndicator extends Command 
{
	public CommandSetIndicator(BaseId idToUpdate, BaseId indicatorToUse)
	{
		id = idToUpdate;
		indicatorId = indicatorToUse;
		previousIndicator = new BaseId();
	}

	public String getCommandName() 
	{
		return COMMAND_NAME;
	}

	public CommandSetIndicator(DataInputStream dataIn) throws IOException
	{
		id = new BaseId(dataIn.readInt());
		indicatorId = new BaseId(dataIn.readInt());
		previousIndicator = new BaseId(dataIn.readInt());
	
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId().asInt());
		dataOut.writeInt(indicatorId.asInt());
		dataOut.writeInt(previousIndicator.asInt());
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousIndicator = doSetIndicator(target, getCurrentIndicator(), getPreviousIndicator()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetIndicator(target, getPreviousIndicator(), getCurrentIndicator());
	}
	
	private BaseId doSetIndicator(Project target, BaseId desiredIndicator, BaseId expectedIndicator) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			BaseId oldIndicator = node.getIndicatorId();
			if(!expectedIndicator.isInvalid() && !oldIndicator.equals(expectedIndicator))
				throw new Exception("CommandSetIndicator expected " + expectedIndicator + " but was " + oldIndicator);
			target.setIndicator(getId(), desiredIndicator);
			return oldIndicator;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + indicatorId + ", " + previousIndicator;
	}

	public BaseId getCurrentIndicator()
	{
		return indicatorId;
	}
	
	public BaseId getPreviousIndicator()
	{
		return previousIndicator;
	}

	BaseId getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetIndicator";

	BaseId id;
	BaseId indicatorId;
	BaseId previousIndicator;
}
