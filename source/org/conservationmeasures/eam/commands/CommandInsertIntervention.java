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

public class CommandInsertIntervention extends Command
{
	public CommandInsertIntervention()
	{
		insertedId = Node.INVALID_ID;
	}

	public CommandInsertIntervention(DataInputStream dataIn) throws IOException
	{
		insertedId = dataIn.readInt();
	}

	public int getId()
	{
		return insertedId;
	}
	
	public String toString()
	{
		return getCommandName() + ":" + getId();
	}
	
	public static String getCommandName()
	{
		return "DiagramInsertIntervention";
	}
	
	public Object execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		Node node = model.createInterventionNode();
		insertedId = model.getNodeId(node);
		return node;
	}
	
	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getId());
	}

	int insertedId;
}
