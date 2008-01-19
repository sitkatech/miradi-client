/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;

public class UndoRedoState
{
	public UndoRedoState()
	{
		undoableCommands = new Vector();
		redoableCommands = new Vector();
	}
	
	public int size()
	{
		return undoableCommands.size();
	}

	public boolean canUndo()
	{
		return (undoableCommands.size() > 0);
	}
	
	public boolean canRedo()
	{
		return (redoableCommands.size() > 0);
	}
	
	public Command popCommandToUndo() throws NothingToUndoException
	{
		if(!canUndo())
			throw new NothingToUndoException();
		Command cmd = (Command)undoableCommands.remove(0);
		redoableCommands.insertElementAt(cmd, 0);
		return cmd;
	}
	
	public Command popCommandToRedo() throws NothingToRedoException
	{
		if(!canRedo())
			throw new NothingToRedoException();
		Command cmd = (Command)redoableCommands.remove(0);
		undoableCommands.insertElementAt(cmd, 0);
		return cmd;
	}

	public void pushUndoableCommand(Command command) throws IOException
	{
		redoableCommands.clear();
		undoableCommands.insertElementAt(command, 0);
	}

	Vector undoableCommands;
	Vector redoableCommands;
}
