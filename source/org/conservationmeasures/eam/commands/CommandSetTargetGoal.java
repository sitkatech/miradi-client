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
import org.conservationmeasures.eam.diagram.nodes.Goals;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetTargetGoal extends Command 
{
	public CommandSetTargetGoal(int idToUpdate, Goals goalsToUse)
	{
		id = idToUpdate;
		goals = goalsToUse;
		previousGoals = null;
	}

	public CommandSetTargetGoal(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		goals = new Goals(dataIn);
		previousGoals = new Goals(dataIn);
	
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		goals.writeDataTo(dataOut);
		previousGoals.writeDataTo(dataOut);
	}

	public String getCommandName() 
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousGoals = doSetGoals(target, getCurrentGoals(), getPreviousGoals()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetGoals(target, getPreviousGoals(), getCurrentGoals());
	}
	
	private Goals doSetGoals(Project target, Goals desiredGoals, Goals expectedGoals) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			Goals currentGoals = node.getGoals();
			if(expectedGoals != null && !currentGoals.equals(expectedGoals))
				throw new Exception("CommandSetObjective expected " + expectedGoals + " but was " + currentGoals);
			node.setGoals(desiredGoals);
			Logging.logVerbose("Updating Goals:" + desiredGoals);
			model.updateCell(node);
			return currentGoals;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + goals + ", " + previousGoals;
	}

	public Goals getCurrentGoals()
	{
		return goals;
	}
	
	public Goals getPreviousGoals()
	{
		return previousGoals;
	}

	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetGoals";

	int id;
	Goals goals;
	Goals previousGoals;
}
