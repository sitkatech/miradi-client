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

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class CommandDiagramSelectCells extends Command
{
	public CommandDiagramSelectCells(int[] selectedCellIds)
	{
		ids = selectedCellIds;
	}
	
	public CommandDiagramSelectCells(DataInputStream dataIn) throws IOException
	{
		int idCount = dataIn.readInt();
		ids = new int[idCount];
		for(int i=0; i < idCount; ++i)
			ids[i] = dataIn.readInt();
	}
	
	public String toString()
	{
		String result = getCommandName() + ":";
		for(int i=0; i < ids.length; ++i)
			result += ids[i] + ",";
		return result;
	}

	public static String getCommandName()
	{
		return "DiagramSelectCells";
	}
	
	public void execute(MainWindow target)
	{
		EAM.logWarning("CommandDiagramMove.execute not implemented");
		// FIXME: Doesn't work because after inserting a node,
		// it doesn't show up in the model for some reason
		
//		DiagramComponent diagram = target.getDiagramComponent();
//		DiagramModel model = diagram.getDiagramModel();
//
//		Object[] cells = new Object[ids.length];
//		for(int i=0; i < cells.length; ++i)
//		{
//			EAM.logDebug("Selecting " + ids[i]);
//			cells[i] = model.getNodeById(ids[i]);
//		}
//		diagram.addSelectionCells(cells);
	}
	
	public void writeTo(OutputStream out) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(ids.length);
		for(int i=0; i < ids.length; ++i)
			dataOut.writeInt(ids[i]);
	}
	
	public int[] getIds()
	{
		return ids;
	}
	
	int[] ids;

}
