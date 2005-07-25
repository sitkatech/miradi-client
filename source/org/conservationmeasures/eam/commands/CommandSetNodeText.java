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
import org.conservationmeasures.eam.main.Project;

public class CommandSetNodeText extends Command
{
	public CommandSetNodeText(int idToUpdate, String newTextToUse)
	{
		id = idToUpdate;
		newText = newTextToUse;
		previousText = "";
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
	
	public static String getCommandName()
	{
		return "SetNodeText";
	}
	
	public Object execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		EAMGraphCell node = model.getNodeById(getId());
		previousText = node.getText();
		node.setText(getNewText());
		model.updateCell(node);
		return null;
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
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

	int id;
	String newText;
	String previousText;
}
