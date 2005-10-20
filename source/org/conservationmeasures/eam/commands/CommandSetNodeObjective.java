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
import org.conservationmeasures.eam.diagram.nodes.Objective;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetNodeObjective extends Command 
{
	public CommandSetNodeObjective(int idToUpdate, Objective objectiveToUse)
	{
		id = idToUpdate;
		objective = objectiveToUse;
		previousObjective = null;
	}

	public CommandSetNodeObjective(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		objective = new Objective(dataIn.readUTF());
		previousObjective = new Objective(dataIn.readUTF());
	
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeUTF(objective.getLabel());
		dataOut.writeUTF(previousObjective.getLabel());
	}

	public String getCommandName() 
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousObjective = doSetObjective(target, getCurrentObjective(), getPreviousObjective()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetObjective(target, getPreviousObjective(), getCurrentObjective());
	}
	
	private Objective doSetObjective(Project target, Objective desiredObjective, Objective expectedObjective) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			Objective currentObjective = node.getObjective();
			if(expectedObjective != null && !currentObjective.equals(expectedObjective))
				throw new Exception("CommandSetObjective expected " + expectedObjective + " but was " + currentObjective);
			node.setObjective(desiredObjective);
			Logging.logDebug("Updating Objective:" + desiredObjective);
			model.updateCell(node);
			return currentObjective;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + objective + ", " + previousObjective;
	}

	public Objective getCurrentObjective()
	{
		return objective;
	}
	
	public Objective getPreviousObjective()
	{
		return previousObjective;
	}

	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetObjective";

	int id;
	Objective objective;
	Objective previousObjective;
}
