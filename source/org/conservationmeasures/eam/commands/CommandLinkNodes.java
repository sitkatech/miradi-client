/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;

public class CommandLinkNodes extends Command
{
	public CommandLinkNodes(int fromId, int toId)
	{
		this.fromId = fromId;
		this.toId = toId;
	}
	
	public CommandLinkNodes(DataInputStream dataIn) throws IOException
	{
		fromId = dataIn.readInt();
		toId = dataIn.readInt();
	}

	public static String getCommandName()
	{
		return "LinkNodes";
	}
	
	public Object execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		Node fromNode = (Node)model.getNodeById(fromId);
		Node toNode = (Node)model.getNodeById(toId);
		model.createLinkage(fromNode, toNode);
		return null;
	}

	public void writeTo(OutputStream out) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getFromId());
		dataOut.writeInt(getToId());
	}
	
	public int getFromId()
	{
		return fromId;
	}
	
	public int getToId()
	{
		return toId;
	}
	
	int fromId;
	int toId;
}
