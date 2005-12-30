/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetIndicator extends Command 
{
	public CommandSetIndicator(int idToUpdate, IndicatorId indicatorToUse)
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
		indicator = new IndicatorId(dataIn.readInt());
		previousIndicator = new IndicatorId(dataIn.readInt());
	
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
	
	private IndicatorId doSetIndicator(Project target, IndicatorId desiredIndicator, IndicatorId expectedIndicator) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			IndicatorId currentIndicator = node.getIndicatorId();
			if(expectedIndicator != null && !currentIndicator.equals(expectedIndicator))
				throw new Exception("CommandSetIndicator expected " + expectedIndicator + " but was " + currentIndicator);
			node.setIndicator(desiredIndicator);
			Logging.logVerbose("Updating Indicator:" + desiredIndicator);
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

	public IndicatorId getCurrentIndicator()
	{
		return indicator;
	}
	
	public IndicatorId getPreviousIndicator()
	{
		return previousIndicator;
	}

	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetIndicator";

	int id;
	IndicatorId indicator;
	IndicatorId previousIndicator;
}
