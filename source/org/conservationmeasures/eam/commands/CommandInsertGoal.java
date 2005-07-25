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
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;

public class CommandInsertGoal extends AbstractCommandInsertNode
{
	public CommandInsertGoal()
	{
	}
	
	public CommandInsertGoal(DataInputStream dataIn) throws IOException
	{
		setId(dataIn.readInt());
	}

	public String toString()
	{
		return getCommandName() + ":" + getId();
	}
	
	public static String getCommandName()
	{
		return "DiagramInsertGoal";
	}
	
	public void execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		Node node = model.createNode(Node.TYPE_GOAL);
		setId(model.getNodeId(node));
	}
	
	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getId());
	}
}
