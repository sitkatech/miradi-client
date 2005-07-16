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
import org.conservationmeasures.eam.main.Project;

public class CommandInsertGoal extends Command
{
	public CommandInsertGoal()
	{
	}
	
	public CommandInsertGoal(DataInputStream dataIn) throws IOException
	{
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
		return model.createGoalNode();
	}
	
	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
	}
}
