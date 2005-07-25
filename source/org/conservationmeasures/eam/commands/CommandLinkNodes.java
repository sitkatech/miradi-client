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
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;

public class CommandLinkNodes extends Command
{
	public CommandLinkNodes(int fromId, int toId)
	{
		this.fromId = fromId;
		this.toId = toId;
		linkageId = Node.INVALID_ID;
	}
	
	public CommandLinkNodes(DataInputStream dataIn) throws IOException
	{
		fromId = dataIn.readInt();
		toId = dataIn.readInt();
		linkageId = dataIn.readInt();
	}
	
	public int getLinkageId()
	{
		return linkageId;
	}

	public String toString()
	{
		return getCommandName() + ": " + linkageId + "," + fromId + ", " + toId;
	}
	
	public static String getCommandName()
	{
		return "LinkNodes";
	}
	
	public Object execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		Node fromNode = model.getNodeById(fromId);
		Node toNode = model.getNodeById(toId);
		Linkage linkage = model.createLinkage(fromNode, toNode);
		linkageId = model.getLinkageId(linkage);
		return linkage;
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getFromId());
		dataOut.writeInt(getToId());
		dataOut.writeInt(getLinkageId());
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
	int linkageId;
}
