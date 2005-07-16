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
import org.conservationmeasures.eam.main.Project;

public class CommandDeleteLinkage extends Command
{
	public CommandDeleteLinkage(int idToDelete)
	{
		id = idToDelete;
	}
	
	public CommandDeleteLinkage(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
	}

	public String toString()
	{
		return getCommandName() + ":" + getId();
	}
	
	public static String getCommandName()
	{
		return "DeleteLinkage";
	}
	
	public Object execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		Linkage linkageToDelete = model.getLinkageById(id);
		model.deleteLinkage(linkageToDelete);
		return null;
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getId());
	}

	public int getId()
	{
		return id;
	}

	int id;
}
