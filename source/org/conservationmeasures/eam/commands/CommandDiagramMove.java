/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.jgraph.graph.GraphConstants;

public class CommandDiagramMove extends Command
{
	public CommandDiagramMove(int deltaX, int deltaY, int[] idsToMove)
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.ids = idsToMove;
	}
	
	public CommandDiagramMove(DataInputStream dataIn) throws IOException
	{
		deltaX = dataIn.readInt();
		deltaY = dataIn.readInt();

		int idCount = dataIn.readInt();
		ids = new int[idCount];
		for(int i=0; i < idCount; ++i)
			ids[i] = dataIn.readInt();
	}
	
	public static String getCommandName()
	{
		return "DiagramMove";
	}
	
	public String toString()
	{
		return getCommandName() + ":" + deltaX + "," + deltaY;
	}
	
	public void execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();

		for(int i = 0; i < ids.length; ++i)
		{
			Node nodeToMove = model.getNodeById(ids[i]);
			Map map = nodeToMove.getMap();
			Rectangle2D oldBounds = GraphConstants.getBounds(map);
			int newX = (int)oldBounds.getX() + getDeltaX();
			int newY = (int)oldBounds.getY() + getDeltaY();
			Rectangle newBounds = new Rectangle(newX, newY, (int)oldBounds.getWidth(), (int)oldBounds.getHeight());
			GraphConstants.setBounds(map, newBounds);
			model.updateNode(nodeToMove);
		}
	}
	
	public void writeTo(OutputStream out) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getDeltaX());
		dataOut.writeInt(getDeltaY());
		dataOut.writeInt(ids.length);
		for(int i=0; i < ids.length; ++i)
			dataOut.writeInt(ids[i]);
	}
	
	public int getDeltaX()
	{
		return deltaX;
	}
	
	public int getDeltaY()
	{
		return deltaY;
	}
	
	public int[] getIds()
	{
		return ids;
	}

	int deltaX;
	int deltaY;
	int[] ids;
}
