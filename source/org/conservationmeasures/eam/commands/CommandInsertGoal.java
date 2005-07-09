/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.main.Project;

public class CommandInsertGoal extends Command
{
	public CommandInsertGoal(Point location, String text)
	{
		this.location = location;
		this.text = text;
	}
	
	public CommandInsertGoal(DataInputStream dataIn) throws IOException
	{
		int x = dataIn.readInt();
		int y = dataIn.readInt();
		location = new Point(x, y);
		text = dataIn.readUTF();
	}

	public static String getCommandName()
	{
		return "DiagramInsertGoal";
	}
	
	public String toString()
	{
		return getCommandName() + ":" + location;
	}
	
	public void execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		model.createGoalNode(getLocation(), getText());
	}
	
	public void writeTo(OutputStream out) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(location.x);
		dataOut.writeInt(location.y);
		dataOut.writeUTF(text);
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public String getText()
	{
		return text;
	}
	
	Point location;
	String text;
}
