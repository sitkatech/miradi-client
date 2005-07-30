/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;


public class BaseProject
{
	public BaseProject()
	{
		diagramModel = new DiagramModel();
		storage = new Storage();
	}

	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}

	public void executeCommand(Command command) throws CommandFailedException
	{
		command.execute(this);
		recordCommand(command);
	}
	
	public void recordCommand(Command command)
	{
		try
		{
			storage.appendCommand(command);
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
	}

	public int deleteNode(int idToDelete) throws Exception
	{
		DiagramModel model = getDiagramModel();
		Node nodeToDelete = model.getNodeById(idToDelete);
		int nodeType = nodeToDelete.getNodeType();
		model.deleteNode(nodeToDelete);
		return nodeType; 
	}

	public int insertNodeAtId(int typeToInsert, int id)
	{
		DiagramModel model = getDiagramModel();
		Node node = model.createNodeAtId(typeToInsert, id);
		int idThatWasInserted = model.getNodeId(node);
		return idThatWasInserted;
	}

	public int insertLinkageAtId(int linkageId, int linkFromId, int linkToId) throws Exception
	{
		DiagramModel model = getDiagramModel();
		Linkage linkage = model.createLinkage(linkageId, linkFromId, linkToId);
		int insertedLinkageId = model.getLinkageId(linkage);
		return insertedLinkageId;
	}

	public void undo() throws CommandFailedException
	{
		int indexToUndo = getIndexToUndo();
		if(indexToUndo < 0)
			throw new NothingToUndoException();
		Command commandToUndo = storage.getCommand(indexToUndo);
		commandToUndo.undo(this);
	}
	
	public void redo() throws CommandFailedException
	{
		int indexToRedo = getIndexToRedo();
		if(indexToRedo < 0)
			throw new NothingToRedoException();
		
		Command commandToRedo = storage.getCommand(indexToRedo);
		commandToRedo.execute(this);
	}
	
	public int getIndexToUndo()
	{
		UndoRedoState state = new UndoRedoState(storage);
		return state.getIndexToUndo();
	}

	public int getIndexToRedo()
	{
		UndoRedoState state = new UndoRedoState(storage);
		return state.getIndexToRedo();
	}
	
	

	Storage storage;
	DiagramModel diagramModel;
}

class UndoRedoState
{
	public UndoRedoState(Storage storageToUse)
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
			Command cmd = storage.getCommand(i);
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

	Storage storage;
	Vector nonUndoneCommandIndexes;
	Vector redoableCommandIndexes;
}

