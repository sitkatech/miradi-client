/*
 * Copyright 2006, The Benetech Initiative
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

public class CommandSetNodeName extends Command
{
	public CommandSetNodeName(BaseId idToUpdate, String newNameToUse)
	{
		id = idToUpdate;
		newName = newNameToUse;
		previousName = null;
	}
	
	public CommandSetNodeName(DataInputStream dataIn) throws IOException
	{
		id = new BaseId(dataIn.readInt());
		newName = dataIn.readUTF();
		previousName = dataIn.readUTF();
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousName = doSetName(target, getNewName(), getPreviousName()); 
	}

	public void undo(Project target) throws CommandFailedException
	{
		doSetName(target, getPreviousName(), getNewName());
	}

	private String doSetName(Project target, String desiredName, String expectedName) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(id);
			String currentName = node.getLabel();
			if(expectedName != null && !currentName.equals(expectedName))
				throw new Exception("CommandSetNodeName expected " + expectedName + " but was " + currentName);
			target.setNodeName(getId(), desiredName, expectedName);
			return currentName;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId().asInt());
		dataOut.writeUTF(getNewName());
		dataOut.writeUTF(getPreviousName());
	}

	BaseId getId()
	{
		return id;
	}
	
	String getNewName()
	{
		return newName;
	}

	public String getPreviousName()
	{
		return previousName;
	}
	
	public static final String COMMAND_NAME = "SetNodeName";
	
	BaseId id;
	String newName;
	String previousName;
}
