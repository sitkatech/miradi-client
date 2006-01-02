/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.annotations.GoalPool;
import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.annotations.ObjectiveIds;
import org.conservationmeasures.eam.annotations.ObjectivePool;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.LinkageDataMap;
import org.conservationmeasures.eam.diagram.nodes.NodeDataHelper;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ThreatPriority;
import org.conservationmeasures.eam.utils.Logging;
import org.conservationmeasures.eam.views.NoProjectView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.LayerManager;
import org.conservationmeasures.eam.views.interview.InterviewModel;
import org.conservationmeasures.eam.views.interview.InterviewStepModel;
import org.jgraph.graph.GraphSelectionModel;
import org.json.JSONObject;


public class Project
{
	public Project() throws IOException
	{
		this(new ProjectServer());
	}
	
	public Project(ProjectServer databaseToUse) throws IOException
	{
		database = databaseToUse;
		
		idAssigner = new IdAssigner(); 
		annotationIdAssigner = new IdAssigner();
		nodePool = new NodePool();
		linkagePool = new LinkagePool();
		goalPool = GoalPool.createSampleGoals(annotationIdAssigner);
		objectivePool = ObjectivePool.createSampleObjectives(annotationIdAssigner);
		diagramModel = new DiagramModel(nodePool, goalPool, objectivePool);
		interviewModel = new InterviewModel();
		interviewModel.loadSteps();
		currentView = NoProjectView.getViewName();
		commandExecutedListeners = new Vector();
		viewChangeListeners = new Vector();
		dataMap = new JSONObject();
		layerManager = new LayerManager();
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// simple getters
	
	public IdAssigner getIdAssigner()
	{
		return idAssigner;
	}
	
	protected ProjectServer getDatabase()
	{
		return database;
	}
	
	public NodePool getNodePool()
	{
		return nodePool;
	}
	
	public LinkagePool getLinkagePool()
	{
		return linkagePool;
	}
	
	public GoalPool getGoalPool()
	{
		return goalPool;
	}
	
	public ObjectivePool getObjectivePool()
	{
		return objectivePool;
	}

	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}
	
	public InterviewModel getInterviewModel()
	{
		return interviewModel;
	}
	
	public LayerManager getLayerManager()
	{
		return layerManager;
	}
	
	public String getCurrentView()
	{
		return currentView;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// database
	
	public void open(File projectDirectory) throws IOException, CommandFailedException, UnknownCommandException
	{
		getDatabase().open(projectDirectory);
		loadCommandsFromDatabase();
	}

	void loadCommandsFromDatabase() throws IOException, UnknownCommandException, CommandFailedException
	{
		Vector commands = getDatabase().load();
		applySnapToOldUnsnappedCommands(commands);
		replayCommands(commands);
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
		if(!isOpen())
			return;
		
		try
		{
			getDatabase().close();
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
		dataMap = new JSONObject();
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

	protected void replayCommands(Vector commands) throws CommandFailedException, IOException
	{
		idAssigner.clear();
		getDiagramModel().clear();
		for(int i=0; i < commands.size(); ++i)
		{
			Command command = (Command)commands.get(i);
			EAM.logVerbose("Executing " + command);
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

	public void applySnapToOldUnsnappedCommands(Vector commands)
	{
		for(int i=0; i < commands.size(); ++i)
		{
			Command command = (Command)commands.get(i);
			if(command instanceof CommandDiagramMove)
			{
				CommandDiagramMove unsnapped = (CommandDiagramMove)command;
				Point unsnappedPoint = new Point(unsnapped.getDeltaX(), unsnapped.getDeltaY());
				Point snappedPoint = getSnapped(unsnappedPoint);
				if(!snappedPoint.equals(unsnappedPoint))
				{
					EAM.logDebug("Adjusting " + unsnappedPoint.toString() + " to " + snappedPoint.toString());
					CommandDiagramMove snapped = new CommandDiagramMove(snappedPoint.x, snappedPoint.y, unsnapped.getIds());
					commands.set(i, snapped);
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// data values

	public String getDataValue(String fieldName)
	{
		return dataMap.optString(fieldName, "");
	}
	
	public void setDataValue(String fieldName, String fieldData)
	{
		EAM.logVerbose("BaseProject.setDataValue to: " + fieldData);
		dataMap.put(fieldName, fieldData);
	}

	/////////////////////////////////////////////////////////////////////////////////
	// command execution

	public void executeCommand(Command command) throws CommandFailedException
	{
		try 
		{
			command.execute(this);
		} 
		catch (CommandFailedException e) 
		{
			fireCommandFailed(command, e);
			throw(e);
		}
		recordCommand(command);
	}
	
	public void replayCommand(Command command) throws CommandFailedException
	{
		command.execute(this);
		fireCommandExecuted(command);
	}
	
	public void recordCommand(Command command)
	{
		try
		{
			database.appendCommand(command);
			database.writeDiagram(getDiagramModel());
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
		fireCommandExecuted(command);
	}

	public void addCommandExecutedListener(CommandExecutedListener listener)
	{
		commandExecutedListeners.add(listener);
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
	
	void fireCommandFailed(Command command, CommandFailedException e)
	{
		for(int i=0; i < commandExecutedListeners.size(); ++i)
		{
			CommandExecutedListener listener = (CommandExecutedListener)commandExecutedListeners.get(i);
			listener.commandFailed(command, e);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// views
	
	public void switchToView(String viewName) throws CommandFailedException
	{
		currentView = viewName;
		fireSwitchToView(viewName);
	}
	
	public void addViewChangeListener(ViewChangeListener listener)
	{
		viewChangeListeners.add(listener);
	}
	
	void fireSwitchToView(String viewName)
	{
		for(int i=0; i < viewChangeListeners.size(); ++i)
		{
			ViewChangeListener listener = (ViewChangeListener)viewChangeListeners.get(i);
			listener.switchToView(viewName);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// interview view
	
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
	
	/////////////////////////////////////////////////////////////////////////////////
	// diagram view
	
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
			int originalNodeId = nodeData.getInt(DiagramNode.TAG_ID);
			
			NodeType type = NodeDataMap.convertIntToNodeType(nodeData.getInt(DiagramNode.TAG_NODE_TYPE)); 
			CommandInsertNode newNode = new CommandInsertNode(type);
			executeCommand(newNode);

			int newNodeId = newNode.getId();
			dataHelper.setNewId(originalNodeId, newNodeId);
			dataHelper.setOriginalLocation(originalNodeId, nodeData.getPoint(DiagramNode.TAG_LOCATION));
			
			CommandSetNodeText newNodeText = new CommandSetNodeText(newNodeId, nodeData.getString(DiagramNode.TAG_VISIBLE_LABEL));
			executeCommand(newNodeText);
			Logging.logDebug("Paste Node: " + newNodeId +":" + nodeData.getString(DiagramNode.TAG_VISIBLE_LABEL));
		}
		
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeDataMap nodeData = nodes[i];
			Point newNodeLocation = dataHelper.getNewLocation(nodeData.getInt(DiagramNode.TAG_ID), startPoint);
			newNodeLocation = getSnapped(newNodeLocation);
			int newNodeId = dataHelper.getNewId(nodeData.getInt(DiagramNode.TAG_ID));
			CommandDiagramMove move = new CommandDiagramMove(newNodeLocation.x, newNodeLocation.y, new int[]{newNodeId});
			executeCommand(move);
		}
	}
	
	private void pasteLinksIntoProject(TransferableEamList list, NodeDataHelper dataHelper) throws CommandFailedException 
	{
		LinkageDataMap[] links = list.getLinkageDataCells();
		for (int i = 0; i < links.length; i++) 
		{
			LinkageDataMap linkageData = links[i];
			
			int newFromId = dataHelper.getNewId(linkageData.getFromId());
			int newToId = dataHelper.getNewId(linkageData.getToId());
			if(newFromId == IdAssigner.INVALID_ID || newToId == IdAssigner.INVALID_ID)
			{
				Logging.logWarning("Unable to Paste Link : from OriginalId:" + linkageData.getFromId() + " to OriginalId:" + linkageData.getToId()+" node deleted?");	
				continue;
			}
			CommandLinkNodes link = new CommandLinkNodes(newFromId, newToId);
			executeCommand(link);
			Logging.logDebug("Paste Link : " + link.getLinkageId() + " from:" + link.getFromId() + " to:" + link.getToId());
		}
	}

	public NodeType deleteNode(int idToDelete) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode nodeToDelete = model.getNodeById(idToDelete);
		NodeType nodeType = nodeToDelete.getType();
		model.deleteNode(nodeToDelete);
		nodePool.remove(idToDelete);
		
		return nodeType; 
	}

	public int insertNodeAtId(NodeType typeToInsert, int requestedId) throws Exception
	{
		int realId = idAssigner.obtainRealId(requestedId);
		ConceptualModelNode cmObject = ConceptualModelNode.createConceptualModelObject(typeToInsert);
		cmObject.setId(realId);
		nodePool.put(cmObject);
		database.writeNode(cmObject);
		
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.createNode(realId);
		
		int idThatWasInserted = node.getId();
		return idThatWasInserted;
	}
	
	public void deleteLinkage(int idToDelete) throws Exception
	{
		database.deleteLinkage(idToDelete);
		DiagramModel model = getDiagramModel();
		DiagramLinkage linkageToDelete = model.getLinkageById(idToDelete);
		model.deleteLinkage(linkageToDelete);
	}

	public int insertLinkageAtId(int requestedLinkageId, int linkFromId, int linkToId) throws Exception
	{
		int realId = idAssigner.obtainRealId(requestedLinkageId);
		DiagramModel model = getDiagramModel();
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(realId, linkFromId, linkToId);
		DiagramLinkage linkage = model.createLinkage(cmLinkage);
		int insertedLinkageId = linkage.getId();
		database.writeLinkage(cmLinkage);
		return insertedLinkageId;
	}
	
	public void setFactorType(int nodeId, NodeType desiredType) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		
		node.setType(desiredType);
		Logging.logVerbose("SetFactorType:" + desiredType);
		model.updateCell(node);

		ConceptualModelNode cmNode = getNodePool().find(nodeId);
		database.writeNode(cmNode);
	}
	
	public void setIndicator(int nodeId, IndicatorId desiredIndicatorId) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		
		node.setIndicator(desiredIndicatorId);
		Logging.logVerbose("SetIndicator:" + desiredIndicatorId);
		model.updateCell(node);
		
		ConceptualModelNode cmNode = getNodePool().find(nodeId);
		database.writeNode(cmNode);
	}
	
	public void setObjectives(int nodeId, ObjectiveIds desiredObjectives) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);

		node.setObjectives(desiredObjectives);
		Logging.logVerbose("SetObjectives:" + desiredObjectives);
		model.updateCell(node);
	
		ConceptualModelNode cmNode = getNodePool().find(nodeId);
		database.writeNode(cmNode);
	}
	
	public void setPriority(int nodeId, ThreatPriority desiredPriority) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);

		node.setNodePriority(desiredPriority);
		Logging.logVerbose("Updating Priority:"+desiredPriority.getStringValue());
		model.updateCell(node);
		
		ConceptualModelNode cmNode = getNodePool().find(nodeId);
		database.writeNode(cmNode);
	}
	
	public void setGoals(int nodeId, GoalIds desiredGoals) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);

		node.setGoals(desiredGoals);
		Logging.logVerbose("Updating Goals:" + desiredGoals);
		model.updateCell(node);
		
		ConceptualModelNode cmNode = getNodePool().find(nodeId);
		database.writeNode(cmNode);
	}

	public void moveNodes(int deltaX, int deltaY, int[] ids) throws Exception 
	{
		DiagramModel model = getDiagramModel();
		model.moveNodes(deltaX, deltaY, ids);
	}
	
	public void nodesWereMovedOrResized(int deltaX, int deltaY, int[] ids)
	{
		DiagramModel model = getDiagramModel();
		model.nodesWereMoved(ids);

		Vector commands = new Vector();
		Vector movedNodes = new Vector();
		for(int i = 0 ; i < ids.length; ++i)
		{
			try 
			{
				DiagramNode node = model.getNodeById(ids[i]);
				if(node.hasMoved())
					movedNodes.add(node);
				
				if(node.sizeHasChanged())
					commands.add(buildResizeCommand(node));
			} 
			catch (Exception e) 
			{
				EAM.logException(e);
			}
		}
		
		if(movedNodes.size() > 0)
		{
			int[] idsActuallyMoved = new int[movedNodes.size()];
			for(int i = 0; i < movedNodes.size(); ++i)
			{
				DiagramNode node = (DiagramNode)movedNodes.get(i);
				idsActuallyMoved[i] = node.getId();
			}
			
			commands.add(new CommandDiagramMove(deltaX, deltaY, idsActuallyMoved));			
		}
		
		if(commands.size() > 0)
		{
			recordCommand(new CommandBeginTransaction());
			for(int i=0; i < commands.size(); ++i)
				recordCommand((Command)commands.get(i));
			recordCommand(new CommandEndTransaction());
		}
	}
	
	private Command buildResizeCommand(DiagramNode node)
	{
		return new CommandSetNodeSize(node.getId(), node.getSize(), node.getPreviousSize());
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

	public EAMGraphCell[] getOnlySelectedCells()
	{
		Object[] rawCells = selectionModel.getSelectionCells();
		EAMGraphCell[] cells = new EAMGraphCell[rawCells.length];
		for(int i=0; i < cells.length; ++i)
			cells[i] = (EAMGraphCell)rawCells[i];
		return cells;
	}
	
	public DiagramNode[] getOnlySelectedNodes()
	{
		Object[] rawCells = selectionModel.getSelectionCells();
		return getOnlySelectedNodes(rawCells);
	}

	public DiagramNode[] getOnlySelectedNodes(Object[] allSelectedCells)
	{
		Vector nodes = new Vector();
		for(int i=0; i < allSelectedCells.length; ++i)
		{
			if(((EAMGraphCell)allSelectedCells[i]).isNode())
				nodes.add(allSelectedCells[i]);
		}
		return (DiagramNode[])nodes.toArray(new DiagramNode[0]);
	}

	
	public int getGridSize()
	{
		return DEFAULT_GRID_SIZE;
	}
	
	public Point getSnapped(Point point)
	{
		int gridSize = getGridSize();
		return new Point(roundTo(point.x, gridSize), roundTo(point.y, gridSize));
	}
	
	int roundTo(int valueToRound, int incrementToRoundTo)
	{
		int sign = 1;
		if(valueToRound < 0)
			sign = -1;
		valueToRound = Math.abs(valueToRound);
		
		int half = incrementToRoundTo / 2;
		valueToRound += half;
		valueToRound -= (valueToRound % incrementToRoundTo);
		return valueToRound * sign;
	}

	/////////////////////////////////////////////////////////////////////////////////
	// undo/redo
	
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
	


	public static final int DEFAULT_GRID_SIZE = 15;

	IdAssigner annotationIdAssigner;
	GoalPool goalPool;
	ObjectivePool objectivePool;
	NodePool nodePool;
	LinkagePool linkagePool;
	ProjectServer database;
	InterviewModel interviewModel;
	DiagramModel diagramModel;
	GraphSelectionModel selectionModel;
	Vector commandExecutedListeners;
	Vector viewChangeListeners;
	String currentView;
	JSONObject dataMap;
	LayerManager layerManager;
	IdAssigner idAssigner;

}

