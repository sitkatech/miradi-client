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

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.Project;

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
	
	public String toString()
	{
		String stringOfIds = "(";
		for(int i=0; i < ids.length; ++i)
			stringOfIds += ids[i] + ",";
		stringOfIds += ")";
		return getCommandName() + ":" + stringOfIds + "," + deltaX + "," + deltaY;
	}
	
	public static String getCommandName()
	{
		return "DiagramMove";
	}
	
	public void execute(Project target) throws CommandFailedException
	{
		doMove(target, getDeltaX(), getDeltaY());
	}

	public void undo(Project target) throws CommandFailedException
	{
		doMove(target, -getDeltaX(), -getDeltaY());
	}
	
	private void doMove(Project target, int xDelta, int yDelta) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();

			for(int i = 0; i < ids.length; ++i)
			{
				EAMGraphCell cellToMove = model.getCellById(ids[i]);
				Point oldLocation = cellToMove.getLocation();
				Point newLocation = new Point(oldLocation.x + xDelta, oldLocation.y + yDelta);
				cellToMove.setLocation(newLocation);
				model.updateCell(cellToMove);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException();
		}
	}
	
	public void writeTo(DataOutputStream dataOut) throws IOException
	{
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
