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
	public CommandSetNodeText(int idToUpdate, String newText)
	{
		id = idToUpdate;
		text = newText;
	}

	public CommandSetNodeText(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		text = dataIn.readUTF();
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + text;
	}
	
	public static String getCommandName()
	{
		return "SetNodeText";
	}
	
	public Object execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		EAMGraphCell node = model.getNodeById(getId());
		node.setText(getText());
		model.updateCell(node);
		return null;
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getId());
		dataOut.writeUTF(getText());
	}

	int getId()
	{
		return id;
	}
	
	String getText()
	{
		return text;
	}

	int id;
	String text;
}
