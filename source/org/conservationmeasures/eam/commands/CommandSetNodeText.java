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
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetNodeText extends Command
{
	public CommandSetNodeText(int idToUpdate, String newTextToUse)
	{
		id = idToUpdate;
		newText = newTextToUse;
		previousText = null;
	}

	public CommandSetNodeText(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		newText = dataIn.readUTF();
		previousText = dataIn.readUTF();
	}
	
	public String getPreviousText()
	{
		return previousText;
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + newText + "," + previousText;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousText = doSetText(target, getNewText(), getPreviousText()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetText(target, getPreviousText(), getNewText());
	}

	private String doSetText(Project target, String desiredText, String expectedText) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			EAMGraphCell node = model.getNodeById(getId());
			String currentText = node.getText();
			if(expectedText != null && !currentText.equals(expectedText))
				throw new Exception("CommandSetNodeText expected " + expectedText + " but was " + currentText);
			node.setText(desiredText);
			Logging.logVerbose("Updating text:"+desiredText);
			model.updateCell(node);
			return currentText;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeUTF(getNewText());
		dataOut.writeUTF(getPreviousText());
	}

	int getId()
	{
		return id;
	}
	
	String getNewText()
	{
		return newText;
	}


	public static final String COMMAND_NAME = "SetNodeText";

	int id;
	String newText;
	String previousText;
}
