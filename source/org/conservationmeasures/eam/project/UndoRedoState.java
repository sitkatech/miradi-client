/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.database.EAMDatabase;

public class UndoRedoState
{
	public UndoRedoState(EAMDatabase storageToUse)
	{
		storage = storageToUse;
		nonUndoneCommandIndexes = new Vector();
		redoableCommandIndexes = new Vector();
	}
	
	public int getIndexToUndo()
	{
		loadSnapshotOfStorage();
		if(nonUndoneCommandIndexes.size() < 1)
			return -1;
		
		return ((Integer)nonUndoneCommandIndexes.get(0)).intValue();
	}
	
	public int getIndexToRedo()
	{
		loadSnapshotOfStorage();
		if(redoableCommandIndexes.size() < 1)
			return -1;
		return ((Integer)redoableCommandIndexes.get(0)).intValue();
	}
	
	private void loadSnapshotOfStorage()
	{
		for(int i=0; i < storage.getCommandCount(); ++i)
		{
			Command cmd = storage.getCommandAt(i);
			if(cmd.isUndo())
			{
				Object commandBeingUndone = nonUndoneCommandIndexes.get(0);
				redoableCommandIndexes.insertElementAt(commandBeingUndone, 0);
				nonUndoneCommandIndexes.remove(0);
			}
			else if(cmd.isRedo())
			{
				Object commandBeingRedone = redoableCommandIndexes.get(0);
				nonUndoneCommandIndexes.insertElementAt(commandBeingRedone, 0);
				redoableCommandIndexes.remove(0);
			}
			else
			{
				nonUndoneCommandIndexes.insertElementAt(new Integer(i), 0);
				redoableCommandIndexes.clear();
			}
		}
	}

	EAMDatabase storage;
	Vector nonUndoneCommandIndexes;
	Vector redoableCommandIndexes;
}