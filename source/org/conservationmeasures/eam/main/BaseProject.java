/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.LinkageData;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.diagram.nodes.NodeData;
import org.conservationmeasures.eam.diagram.nodes.NodeDataHelper;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.utils.Logging;
import org.jgraph.graph.GraphSelectionModel;


public class BaseProject
{
	public BaseProject()
	{
		diagramModel = new DiagramModel();
		storage = new Storage();
		commandExecutedListeners = new Vector();
	}
	
	public boolean isOpen()
	{
		return true;
	}

	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}
	
	public void addCommandExecutedListener(CommandExecutedListener listener)
	{
		commandExecutedListeners.add(listener);
	}

	public void executeCommand(Command command) throws CommandFailedException
	{
		command.execute(this);
		recordCommand(command);
	}
	
	public void replayCommand(Command command) throws CommandFailedException
	{
		command.execute(this);
		fireCommandExecuted(command);
	}
	
	public void pasteCellsIntoProject(TransferableEamList list, Point startPoint) throws CommandFailedException 
	{
		NodeData[] nodes = list.getNodeDataCells();
		LinkageData[] links = list.getLinkageDataCells();
		NodeDataHelper dataHelper = new NodeDataHelper(getDiagramModel().getAllNodes());
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeData nodeData = nodes[i];
			CommandInsertNode newNode = new CommandInsertNode(nodeData.getNodeType());
			executeCommand(newNode);

			int originalNodeId = nodeData.getId();
			int newNodeId = newNode.getId();
			dataHelper.setNewId(originalNodeId, newNodeId);
			dataHelper.setOriginalLocation(originalNodeId, nodeData.getLocation());
			
			CommandSetNodeText newNodeText = new CommandSetNodeText(newNodeId, nodeData.getText());
			executeCommand(newNodeText);
			Logging.logDebug("Paste Node: " + newNodeId +":" + nodeData.getText());
		}
		
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeData nodeData = nodes[i];
			Point newNodeLocation = dataHelper.getNewLocation(nodeData.getId(), startPoint);
			int newNodeId = dataHelper.getNewId(nodeData.getId());
			CommandDiagramMove move = new CommandDiagramMove(newNodeLocation.x, newNodeLocation.y, new int[]{newNodeId});
			executeCommand(move);
		}
		
		for (int i = 0; i < links.length; i++) 
		{
			LinkageData linkageData = links[i];
			
			int newFromId = dataHelper.getNewId(linkageData.getFromNodeId());
			int newToId = dataHelper.getNewId(linkageData.getToNodeId());
			if(newFromId == Node.INVALID_ID || newToId == Node.INVALID_ID)
			{
				Logging.logDebug("Unable to Paste Link : from OriginalId:" + linkageData.getFromNodeId() + " to OriginalId:" + linkageData.getToNodeId()+" node deleted?");	
				continue;
			}
			CommandLinkNodes link = new CommandLinkNodes(newFromId, newToId);
			executeCommand(link);
			Logging.logDebug("Paste Link : " + link.getLinkageId() + " from:" + link.getFromId() + " to:" + link.getToId());
		}
	}

	void fireCommandExecuted(Command command)
	{
		CommandExecutedEvent event = new CommandExecutedEvent(command);
		for(int i=0; i < commandExecutedListeners.size(); ++i)
		{
			CommandExecutedListener listener = (CommandExecutedListener)commandExecutedListeners.get(i);
			listener.commandExecuted(event);
		}
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
		fireCommandExecuted(command);
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
		int idThatWasInserted = node.getId();
		return idThatWasInserted;
	}

	public int insertLinkageAtId(int linkageId, int linkFromId, int linkToId) throws Exception
	{
		DiagramModel model = getDiagramModel();
		Linkage linkage = model.createLinkage(linkageId, linkFromId, linkToId);
		int insertedLinkageId = linkage.getId();
		return insertedLinkageId;
	}

	public void undo() throws CommandFailedException
	{
		int indexToUndo = getIndexToUndo();
		if(indexToUndo < 0)
			throw new NothingToUndoException();
		Command commandToUndo = storage.getCommand(indexToUndo);
		commandToUndo.undo(this);
		// TODO: should we fire a command-undone here?
	}
	
	public void redo() throws CommandFailedException
	{
		int indexToRedo = getIndexToRedo();
		if(indexToRedo < 0)
			throw new NothingToRedoException();
		
		Command commandToRedo = storage.getCommand(indexToRedo);
		replayCommand(commandToRedo);
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
	
	public void setSelectionModel(GraphSelectionModel selectionModelToUse)
	{
		selectionModel = selectionModelToUse;
	}
	
	public EAMGraphCell[] getSelectedAndRelatedCells()
	{
		Object[] selectedCells = selectionModel.getSelectionCells();
		Vector cellVector = getAllSelectedCellsWithLinkages(selectedCells);
		return (EAMGraphCell[])cellVector.toArray(new EAMGraphCell[0]);
	}

	public EAMGraphCell[] getOnlySelectedCells()
	{
		Object[] rawCells = selectionModel.getSelectionCells();
		EAMGraphCell[] cells = new EAMGraphCell[rawCells.length];
		for(int i=0; i < cells.length; ++i)
			cells[i] = (EAMGraphCell)rawCells[i];
		return cells;
	}

	public Vector getAllSelectedCellsWithLinkages(Object[] selectedCells) 
	{
		DiagramModel model = getDiagramModel();
		Vector selectedCellsWithLinkages = new Vector();
		for(int i=0; i < selectedCells.length; ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)selectedCells[i];
			if(cell.isLinkage())
			{
				if(!selectedCellsWithLinkages.contains(cell))
					selectedCellsWithLinkages.add(cell);
			}
			else if(cell.isNode())
			{
				Set linkages = model.getLinkages((Node)cell);
				for (Iterator iter = linkages.iterator(); iter.hasNext();) 
				{
					EAMGraphCell link = (EAMGraphCell) iter.next();
					if(!selectedCellsWithLinkages.contains(link))
						selectedCellsWithLinkages.add(link);
				}
				selectedCellsWithLinkages.add(cell);
			}
		}
		return selectedCellsWithLinkages;
	}

	Storage storage;
	DiagramModel diagramModel;
	GraphSelectionModel selectionModel;
	Vector commandExecutedListeners;
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

