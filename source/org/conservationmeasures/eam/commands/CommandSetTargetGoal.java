/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetTargetGoal extends Command 
{
	public CommandSetTargetGoal(int idToUpdate, GoalIds goalsToUse)
	{
		id = idToUpdate;
		goals = goalsToUse;
		previousGoals = null;
	}

	public CommandSetTargetGoal(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		goals = new GoalIds(dataIn);
		previousGoals = new GoalIds(dataIn);
	
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
	
	private GoalIds doSetGoals(Project target, GoalIds desiredGoals, GoalIds expectedGoals) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			GoalIds currentGoalIds = node.getGoals();
			if(expectedGoals != null && !currentGoalIds.equals(expectedGoals))
				throw new Exception("CommandSetObjective expected " + expectedGoals + " but was " + currentGoalIds);
			node.setGoals(desiredGoals);
			Logging.logVerbose("Updating Goals:" + desiredGoals);
			model.updateCell(node);
			return currentGoalIds;
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

	public GoalIds getCurrentGoals()
	{
		return goals;
	}
	
	public GoalIds getPreviousGoals()
	{
		return previousGoals;
	}

	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetGoals";

	int id;
	GoalIds goals;
	GoalIds previousGoals;
}
