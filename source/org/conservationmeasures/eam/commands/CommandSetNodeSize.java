/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetNodeSize extends Command
{
	public CommandSetNodeSize(BaseId idToUpdate, Dimension updatedSize, Dimension previousSizeToUse)
	{
		id = idToUpdate;
		currentSize = updatedSize;
		previousSize = previousSizeToUse;
	}


	public CommandSetNodeSize(DataInputStream dataIn) throws IOException
	{
		id = new BaseId(dataIn.readInt());
		currentSize = new Dimension(dataIn.readInt(), dataIn.readInt());
		previousSize = new Dimension(dataIn.readInt(), dataIn.readInt());
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId().asInt());
		dataOut.writeInt(currentSize.width);
		dataOut.writeInt(currentSize.height);
		if(previousSize == null)
		{
			dataOut.writeInt(currentSize.width);
			dataOut.writeInt(currentSize.height);
		}
		else
		{
			dataOut.writeInt(previousSize.width);
			dataOut.writeInt(previousSize.height);
		}
	}

	public void execute(Project target) throws CommandFailedException
	{
		 previousSize = doSetSize(target, getCurrentSize(), getPreviousSize()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetSize(target, getPreviousSize(), getCurrentSize());
	}
	
	private Dimension doSetSize(Project target, Dimension desiredSize, Dimension expectedSize) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			Dimension currentNodeSize = node.getSize();
			if(expectedSize != null && !currentNodeSize.equals(expectedSize))
				throw new Exception("CommandSetNodeSize expected " + expectedSize + " but was " + currentNodeSize);
			node.setSize(desiredSize);
			Logging.logVerbose("Updating Cell Size from:"+ expectedSize +" to:"+ desiredSize);
			model.updateCell(node);
			return currentNodeSize;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + currentSize + ", " + previousSize;
	}

	public Dimension getCurrentSize()
	{
		return currentSize;
	}
	
	public Dimension getPreviousSize()
	{
		return previousSize;
	}

	BaseId getId()
	{
		return id;
	}
	
	public String getCommandName() 
	{
		return COMMAND_NAME;
	}
	
	public static final String COMMAND_NAME = "ReSize";

	BaseId id;
	Dimension currentSize;
	Dimension previousSize;
}