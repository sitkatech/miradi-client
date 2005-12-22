/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.annotations.Objectives;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetNodeObjectives extends Command 
{
	public CommandSetNodeObjectives(int idToUpdate, Objectives objectivesToUse)
	{
		id = idToUpdate;
		objectives = objectivesToUse;
		previousObjectives = null;
	}

	public CommandSetNodeObjectives(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		objectives = new Objectives(dataIn);
		previousObjectives = new Objectives(dataIn);
	
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		objectives.writeDataTo(dataOut);
		previousObjectives.writeDataTo(dataOut);
	}

	public String getCommandName() 
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousObjectives = doSetObjectives(target, getCurrentObjectives(), getPreviousObjectives()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetObjectives(target, getPreviousObjectives(), getCurrentObjectives());
	}
	
	private Objectives doSetObjectives(Project target, Objectives desiredObjectives, Objectives expectedObjectives) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			Objectives currentObjectives = node.getObjectives();
			if(expectedObjectives != null && !currentObjectives.equals(expectedObjectives))
				throw new Exception("CommandSetObjective expected " + expectedObjectives + " but was " + currentObjectives);
			node.setObjectives(desiredObjectives);
			Logging.logVerbose("Updating Objective:" + desiredObjectives);
			model.updateCell(node);
			return currentObjectives;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + objectives + ", " + previousObjectives;
	}

	public Objectives getCurrentObjectives()
	{
		return objectives;
	}
	
	public Objectives getPreviousObjectives()
	{
		return previousObjectives;
	}

	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetObjectives";

	int id;
	Objectives objectives;
	Objectives previousObjectives;
}
