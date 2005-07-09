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
import org.conservationmeasures.eam.main.Project;

public class CommandInsertGoal extends Command
{
	public CommandInsertGoal(String text)
	{
		this.text = text;
	}
	
	public CommandInsertGoal(DataInputStream dataIn) throws IOException
	{
		text = dataIn.readUTF();
	}

	public static String getCommandName()
	{
		return "DiagramInsertGoal";
	}
	
	public String toString()
	{
		return getCommandName();
	}
	
	public Object execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		return model.createGoalNode(getText());
	}
	
	public void writeTo(OutputStream out) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		dataOut.writeUTF(getCommandName());
		dataOut.writeUTF(text);
	}
	
	public String getText()
	{
		return text;
	}
	
	String text;
}
