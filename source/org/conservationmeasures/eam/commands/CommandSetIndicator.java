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
import org.conservationmeasures.eam.diagram.nodes.Indicator;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetIndicator extends Command 
{
	public CommandSetIndicator(int idToUpdate, Indicator indicatorToUse)
	{
		id = idToUpdate;
		indicator = indicatorToUse;
		previousIndicator = null;
	}

	public String getCommandName() 
	{
		return COMMAND_NAME;
	}

	public CommandSetIndicator(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		indicator = new Indicator(dataIn.readInt());
		previousIndicator = new Indicator(dataIn.readInt());
	
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeInt(indicator.getValue());
		dataOut.writeInt(previousIndicator.getValue());
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousIndicator = doSetIndicator(target, getCurrentIndicator(), getPreviousIndicator()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetIndicator(target, getPreviousIndicator(), getCurrentIndicator());
	}
	
	private Indicator doSetIndicator(Project target, Indicator desiredIndicator, Indicator expectedIndicator) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			Indicator currentIndicator = node.getIndicator();
			if(expectedIndicator != null && !currentIndicator.equals(expectedIndicator))
				throw new Exception("CommandSetIndicator expected " + expectedIndicator + " but was " + currentIndicator);
			node.setIndicator(desiredIndicator);
			Logging.logDebug("Updating Indicator:" + desiredIndicator);
			model.updateCell(node);
			return currentIndicator;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + indicator + ", " + previousIndicator;
	}

	public Indicator getCurrentIndicator()
	{
		return indicator;
	}
	
	public Indicator getPreviousIndicator()
	{
		return previousIndicator;
	}

	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetIndicator";

	int id;
	Indicator indicator;
	Indicator previousIndicator;
}
