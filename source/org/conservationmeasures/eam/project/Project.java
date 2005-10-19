/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.LinkageData;
import org.conservationmeasures.eam.diagram.nodes.NodeDataHelper;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.utils.Logging;
import org.conservationmeasures.eam.views.NoProjectView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.LayerManager;
import org.conservationmeasures.eam.views.interview.InterviewModel;
import org.conservationmeasures.eam.views.interview.InterviewStepModel;
import org.jgraph.graph.GraphSelectionModel;


public class Project
{
	public Project() throws IOException
	{
		database = new ProjectServer();

		diagramModel = new DiagramModel();
		interviewModel = new InterviewModel();
		interviewModel.loadSteps();
		currentView = NoProjectView.getViewName();
		commandExecutedListeners = new Vector();
		viewChangeListeners = new Vector();
		dataMap = new HashMap();
		layerManager = new LayerManager();
	}
	
	public void open(File projectDirectory) throws IOException, CommandFailedException, UnknownCommandException
	{
		getDatabase().open(projectDirectory);
		loadCommandsFromDatabase();
	}

	void loadCommandsFromDatabase() throws IOException, UnknownCommandException, CommandFailedException
	{
		Vector commands = getDatabase().load();
		loadCommands(commands);
	}
	
	public String getName()
	{
		if(isOpen())
			return getDatabase().getName();
		return EAM.text("[No Project]");
	}

	
	public boolean isOpen()
	{
		return getDatabase().isOpen();
	}
	
	public void close()
	{
		try
		{
			getDatabase().close();
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
		dataMap.clear();
		currentView = NoProjectView.getViewName();
		fireSwitchToView(currentView);
	}
	
	static public boolean isValidProjectName(String candidate)
	{
		char[] asArray = candidate.toCharArray();
		for(int i = 0; i < candidate.length(); ++i)
		{
			char c = asArray[i];
			if(c >= 128)
				continue;
			if(Character.isLetterOrDigit(c))
				continue;
			if(c == ' ' || c == '.' || c == '-')
				continue;
			
			return false;
		}
		return true;
	}

	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}
	
	public InterviewModel getInterviewModel()
	{
		return interviewModel;
	}
	
	public String getDataValue(String fieldName)
	{
		String value = (String)dataMap.get(fieldName);
		if(value == null)
			value = "";
		return value;
	}
	
	public void setDataValue(String fieldName, String fieldData)
	{
		EAM.logVerbose("BaseProject.setDataValue to: " + fieldData);
		dataMap.put(fieldName, fieldData);
	}
	
	public LayerManager getLayerManager()
	{
		return layerManager;
	}
	
	public void addCommandExecutedListener(CommandExecutedListener listener)
	{
		commandExecutedListeners.add(listener);
	}

	public void addViewChangeListener(ViewChangeListener listener)
	{
		viewChangeListeners.add(listener);
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
	
	public String getCurrentView()
	{
		return currentView;
	}
	
	public void switchToView(String viewName) throws CommandFailedException
	{
		currentView = viewName;
		fireSwitchToView(viewName);
	}
	
	public InterviewStepModel getCurrentInterviewStep()
	{
		return getInterviewModel().getCurrentStep();
	}
	
	public String getCurrentInterviewStepName()
	{
		return getInterviewModel().getCurrentStepName();
	}
	
	public void setCurrentInterviewStepName(String newStepName)
	{
		getInterviewModel().setCurrentStepName(newStepName);
	}
	
	public void pasteNodesAndLinksIntoProject(TransferableEamList list, Point startPoint) throws CommandFailedException
	{
		executeCommand(new CommandBeginTransaction());
		NodeDataHelper dataHelper = new NodeDataHelper(getDiagramModel().getAllNodes());
		pasteNodesIntoProject(list, startPoint, dataHelper);
		pasteLinksIntoProject(list, dataHelper);
		executeCommand(new CommandEndTransaction());
	}
	
	public void pasteNodesOnlyIntoProject(TransferableEamList list, Point startPoint) throws CommandFailedException
	{
		executeCommand(new CommandBeginTransaction());
		NodeDataHelper dataHelper = new NodeDataHelper(getDiagramModel().getAllNodes());
		pasteNodesIntoProject(list, startPoint, dataHelper);
		executeCommand(new CommandEndTransaction());
	}

	private void pasteNodesIntoProject(TransferableEamList list, Point startPoint, NodeDataHelper dataHelper) throws CommandFailedException 
	{
		NodeDataMap[] nodes = list.getNodeDataCells();
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeDataMap nodeData = nodes[i];
			CommandInsertNode newNode = new CommandInsertNode(nodeData.getInt(DiagramNode.TYPE));
			executeCommand(newNode);

			int originalNodeId = nodeData.getInt(EAMGraphCell.ID);
			int newNodeId = newNode.getId();
			dataHelper.setNewId(originalNodeId, newNodeId);
			dataHelper.setOriginalLocation(originalNodeId, nodeData.getPoint(EAMGraphCell.LOCATION));
			
			CommandSetNodeText newNodeText = new CommandSetNodeText(newNodeId, nodeData.getString(EAMGraphCell.TEXT));
			executeCommand(newNodeText);
			Logging.logDebug("Paste Node: " + newNodeId +":" + nodeData.getString(EAMGraphCell.TEXT));
		}
		
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeDataMap nodeData = nodes[i];
			Point newNodeLocation = dataHelper.getNewLocation(nodeData.getInt(EAMGraphCell.ID), startPoint);
			int newNodeId = dataHelper.getNewId(nodeData.getInt(EAMGraphCell.ID));
			CommandDiagramMove move = new CommandDiagramMove(newNodeLocation.x, newNodeLocation.y, new int[]{newNodeId});
			executeCommand(move);
		}
	}
	
	private void pasteLinksIntoProject(TransferableEamList list, NodeDataHelper dataHelper) throws CommandFailedException 
	{
		LinkageData[] links = list.getLinkageDataCells();
		for (int i = 0; i < links.length; i++) 
		{
			LinkageData linkageData = links[i];
			
			int newFromId = dataHelper.getNewId(linkageData.getFromNodeId());
			int newToId = dataHelper.getNewId(linkageData.getToNodeId());
			if(newFromId == DiagramNode.INVALID_ID || newToId == DiagramNode.INVALID_ID)
			{
				Logging.logWarning("Unable to Paste Link : from OriginalId:" + linkageData.getFromNodeId() + " to OriginalId:" + linkageData.getToNodeId()+" node deleted?");	
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
	
	void fireSwitchToView(String viewName)
	{
		for(int i=0; i < viewChangeListeners.size(); ++i)
		{
			ViewChangeListener listener = (ViewChangeListener)viewChangeListeners.get(i);
			listener.switchToView(viewName);
		}
	}
	
	public void recordCommand(Command command)
	{
		try
		{
			database.appendCommand(command);
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
		DiagramNode nodeToDelete = model.getNodeById(idToDelete);
		int nodeType = nodeToDelete.getNodeType();
		model.deleteNode(nodeToDelete);
		return nodeType; 
	}

	public int insertNodeAtId(int typeToInsert, int requestedId) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.createNodeAtId(typeToInsert, requestedId);
		int idThatWasInserted = node.getId();
		return idThatWasInserted;
	}
	
	public void deleteLinkage(int idToDelete) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramLinkage linkageToDelete = model.getLinkageById(idToDelete);
		model.deleteLinkage(linkageToDelete);
	}

	public int insertLinkageAtId(int requestedLinkageId, int linkFromId, int linkToId) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramLinkage linkage = model.createLinkage(requestedLinkageId, linkFromId, linkToId);
		int insertedLinkageId = linkage.getId();
		return insertedLinkageId;
	}
	
	public void nodesWereMoved(int deltaX, int deltaY, int[] ids)
	{
		getDiagramModel().nodesWereMoved(deltaX, deltaY, ids);
		recordCommand(new CommandDiagramMove(deltaX, deltaY, ids));
	}
	
	public void undo() throws CommandFailedException
	{
		getCommandToUndo().undo(this);
		// TODO: should we fire a command-undone here?
	}
	
	public void redo() throws CommandFailedException
	{
		replayCommand(getCommandToRedo());
	}

	public Command getCommandToUndo() throws NothingToUndoException

	{
		int indexToUndo = getIndexToUndo();
		if(indexToUndo < 0)
			throw new NothingToUndoException();
		return database.getCommandAt(indexToUndo);
	}
	
	public Command getCommandToRedo() throws NothingToRedoException
	{
		int indexToRedo = getIndexToRedo();
		if(indexToRedo < 0)
			throw new NothingToRedoException();
		return database.getCommandAt(indexToRedo);
	}
	
	public int getIndexToUndo()
	{
		UndoRedoState state = new UndoRedoState(database);
		return state.getIndexToUndo();
	}

	public int getIndexToRedo()
	{
		UndoRedoState state = new UndoRedoState(database);
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
				Set linkages = model.getLinkages((DiagramNode)cell);
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

	protected void loadCommands(Vector commands) throws CommandFailedException, IOException
	{
		getDiagramModel().clear();
		for(int i=0; i < commands.size(); ++i)
		{
			Command command = (Command)commands.get(i);
			EAM.logDebug("Executing " + command);
			replayCommand(command);
			database.addCommandWithoutSaving(command);
		}
		
		if(currentView.length() == 0)
		{
			currentView = DiagramView.getViewName();
			fireSwitchToView(currentView);
		}
		
		fireCommandExecuted(new CommandDoNothing());
	}

	protected ProjectServer getDatabase()
	{
		return database;
	}

	ProjectServer database;
	InterviewModel interviewModel;
	DiagramModel diagramModel;
	GraphSelectionModel selectionModel;
	Vector commandExecutedListeners;
	Vector viewChangeListeners;
	String currentView;
	Map dataMap;
	LayerManager layerManager;
}

