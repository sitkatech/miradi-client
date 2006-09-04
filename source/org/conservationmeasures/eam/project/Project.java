/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.database.DataUpgrader;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.PartialGraphLayoutCache;
import org.conservationmeasures.eam.diagram.nodes.DiagramCluster;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.FutureVersionException;
import org.conservationmeasures.eam.exceptions.OldVersionException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.GoalIds;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.ids.ObjectiveIds;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objectpools.TaskPool;
import org.conservationmeasures.eam.objectpools.ViewPool;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.Logging;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.LayerManager;
import org.conservationmeasures.eam.views.interview.InterviewModel;
import org.conservationmeasures.eam.views.interview.InterviewStepModel;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.ParentMap;


public class Project
{
	public Project() throws IOException
	{
		this(new ProjectServer());
	}
	
	public Project(ProjectServer databaseToUse) throws IOException
	{
		database = databaseToUse;
		commandExecutedListeners = new Vector();
		viewChangeListeners = new Vector();
		
		clear();
	}

	private void clear() throws IOException
	{
		projectInfo = new ProjectInfo();
		objectManager = new ObjectManager(this);
		undoRedoState = new UndoRedoState();
		
		diagramModel = new DiagramModel(this);
		diagramModel.addDiagramModelListener(new LinkageMonitor());
		interviewModel = new InterviewModel();
		interviewModel.loadSteps();
		layerManager = new LayerManager();
		threatRatingFramework = new ThreatRatingFramework(this);
		graphLayoutCache = new PartialGraphLayoutCache(diagramModel);
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// simple getters
	
	public IdAssigner getNodeIdAssigner()
	{
		return projectInfo.getNodeIdAssigner();
	}
	
	public IdAssigner getAnnotationIdAssigner()
	{
		return projectInfo.getAnnotationIdAssigner();
	}
	
	protected ProjectServer getDatabase()
	{
		return database;
	}
	
	public EAMObjectPool getPool(int objectType)
	{
		return objectManager.getPool(objectType);
	}
	
	public NodePool getNodePool()
	{
		return objectManager.getNodePool();
	}
	
	public LinkagePool getLinkagePool()
	{
		return objectManager.getLinkagePool();
	}
	
	public TaskPool getTaskPool()
	{
		return objectManager.getTaskPool();
	}
	
	public ViewPool getViewPool()
	{
		return objectManager.getViewPool();
	}
	
	public ResourcePool getResourcePool()
	{
		return objectManager.getResourcePool();
	}
	
	public IndicatorPool getIndicatorPool()
	{
		return objectManager.getIndicatorPool();
	}

	public ObjectivePool getObjectivePool()
	{
		return objectManager.getObjectivePool();
	}
	
	public GoalPool getGoalPool()
	{
		return objectManager.getGoalPool();
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
		return projectInfo.getCurrentView();
	}
	
	public void setCurrentView(String newCurrentView)
	{
		projectInfo.setCurrentView(newCurrentView);
	}
	
	public ViewData getCurrentViewData() throws Exception
	{
		return getViewData(getCurrentView());
	}
	
	public ViewData getViewData(String viewName) throws Exception
	{
		ViewData found = getViewPool().findByLabel(viewName);
		if(found != null)
			return found;
		
		BaseId createdId = createObject(ObjectType.VIEW_DATA);
		setObjectData(ObjectType.VIEW_DATA, createdId, ViewData.TAG_LABEL, viewName);
		return getViewPool().find(createdId);
	}
	
	public ThreatRatingFramework getThreatRatingFramework()
	{
		return threatRatingFramework;
	}
	
	public GraphLayoutCache getGraphLayoutCache()
	{
		return graphLayoutCache;
	}
	
	public Task getRootTask() throws Exception
	{
		BaseId rootTaskId = projectInfo.getRootTaskId();
		if(rootTaskId.isInvalid())
		{
			rootTaskId = createObject(ObjectType.TASK);
			projectInfo.setRootTaskId(rootTaskId);
			getDatabase().writeProjectInfo(projectInfo);
		}
		return getTaskPool().find(rootTaskId);
	}
	
	public EAMObject findObject(int objectType, BaseId objectId)
	{
		EAMObjectPool pool = getPool(objectType);
		return pool.findObject(objectId);
	}
	
	public ConceptualModelNode findNode(BaseId nodeId)
	{
		return (ConceptualModelNode)findObject(ObjectType.MODEL_NODE, nodeId);
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// objects
	
	public BaseId obtainRealLinkageId(BaseId proposedId)
	{
		return projectInfo.obtainRealLinkageId(proposedId);
	}
	
	public BaseId createObject(int objectType) throws Exception
	{
		return createObject(objectType, BaseId.INVALID);
	}
	
	public BaseId createObject(int objectType, BaseId objectId) throws Exception
	{
		return createObject(objectType, objectId, null);
	}
	
	public BaseId createObject(int objectType, BaseId objectId, Object extraInfo) throws Exception
	{
		return objectManager.createObject(objectType, objectId, extraInfo);
	}
	
	public void deleteObject(int objectType, BaseId objectId) throws IOException, ParseException
	{
		objectManager.deleteObject(objectType, objectId);
	}
	
	public void setObjectData(int objectType, BaseId objectId, String fieldTag, String dataValue) throws Exception
	{
		objectManager.setObjectData(objectType, objectId, fieldTag, dataValue);
		if(objectType == ObjectType.MODEL_NODE)
		{
			DiagramModel model = getDiagramModel();
			if(model.hasNode(objectId))
			{
				DiagramNode diagramNode = getDiagramModel().getNodeById(objectId);
				getDiagramModel().updateCell(diagramNode);
			}
		}
	}
	
	public String getObjectData(int objectType, BaseId objectId, String fieldTag)
	{
		return objectManager.getObjectData(objectType, objectId, fieldTag);
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// database
	
	public void createOrOpen(File projectDirectory) throws Exception
	{
		clear();

		if(ProjectServer.isExistingProject(projectDirectory))
			openProject(projectDirectory);
		else
			createProject(projectDirectory);
		
		finishOpening();
	}

	private void createDefaultObjectsIfNeeded() throws Exception
	{
		if(threatRatingFramework.getCriteria().length == 0)
			threatRatingFramework.createDefaultObjects();
	}

	private void openProject(File projectDirectory) throws Exception
	{
		if(ProjectServer.readDataVersion(projectDirectory) > ProjectServer.DATA_VERSION)
			throw new FutureVersionException();

		if(ProjectServer.readDataVersion(projectDirectory) < ProjectServer.DATA_VERSION)
			DataUpgrader.attemptUpgrade(projectDirectory);
		
		if(ProjectServer.readDataVersion(projectDirectory) < ProjectServer.DATA_VERSION)
			throw new OldVersionException();

		ProjectServer db = getDatabase();
		db.open(projectDirectory);
		loadProjectInfo();
		objectManager.loadFromDatabase();
		loadThreatRatingFramework();
		loadDiagram();
	}
	
	private void createProject(File projectDirectory) throws Exception
	{
		getDatabase().create(projectDirectory);
	}
	
	private void loadProjectInfo() throws IOException, ParseException
	{
		getDatabase().readProjectInfo(projectInfo);
	}
	
	private void loadThreatRatingFramework() throws Exception
	{
		getThreatRatingFramework().load();
	}
	
	private void loadDiagram() throws Exception
	{
		getDatabase().readDiagram(getDiagramModel());
	}

	protected void finishOpening() throws Exception
	{
		createDefaultObjectsIfNeeded();

		database.writeVersion();
		String currentView = getCurrentView();
		if(currentView.length() == 0)
			setCurrentView(DiagramView.getViewName());
		fireSwitchToView(getCurrentView());
		
		fireCommandExecuted(new CommandDoNothing());
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
	
	public void close() throws Exception
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
		setCurrentView(NoProjectView.getViewName());
		fireSwitchToView(getCurrentView());
	}
	
	static public boolean isValidProjectName(String candidate)
	{
		if(candidate.length() > 32)
			return false;
		
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

	/////////////////////////////////////////////////////////////////////////////////
	// data values

	public String getDataValue(String fieldName)
	{
		return projectInfo.getProjectData().optString(fieldName, "");
	}
	
	public void setDataValue(String fieldName, String fieldData)
	{
		EAM.logVerbose("BaseProject.setDataValue to: " + fieldData);
		projectInfo.getProjectData().put(fieldName, fieldData);
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
	
	public void recordCommand(Command command)
	{
		try
		{
			undoRedoState.pushUndoableCommand(command);
			database.writeProjectInfo(projectInfo);
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
	
	public void removeCommandExecutedListener(CommandExecutedListener listener)
	{
		commandExecutedListeners.remove(listener);
	}

	void fireCommandExecuted(Command command)
	{
		EAM.logVerbose("Command executed: " + command.toString());
		CommandExecutedEvent event = new CommandExecutedEvent(command);
		for(int i=0; i < commandExecutedListeners.size(); ++i)
		{
			CommandExecutedListener listener = (CommandExecutedListener)commandExecutedListeners.get(i);
			listener.commandExecuted(event);
		}
	}
	
	void fireCommandUndone(Command command)
	{
		EAM.logVerbose("Command undone: " + command.toString());
		CommandExecutedEvent event = new CommandExecutedEvent(command);
		for(int i=0; i < commandExecutedListeners.size(); ++i)
		{
			CommandExecutedListener listener = (CommandExecutedListener)commandExecutedListeners.get(i);
			listener.commandUndone(event);
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
	
	public boolean canUndo()
	{
		return undoRedoState.canUndo();
	}
	
	public boolean canRedo()
	{
		return undoRedoState.canRedo();
	}
	
	public Command undo() throws CommandFailedException
	{
		Command cmd = undoRedoState.popCommandToUndo();
		cmd.undo(this);
		fireCommandUndone(cmd);
		return cmd;
	}
	
	public Command redo() throws CommandFailedException
	{
		Command cmd = undoRedoState.popCommandToRedo();
		cmd.execute(this);
		fireCommandExecuted(cmd);
		return cmd;
	}


	
	/////////////////////////////////////////////////////////////////////////////////
	// views
	
	public void switchToView(String viewName) throws CommandFailedException
	{
		setCurrentView(viewName);
		try
		{
			fireSwitchToView(viewName);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	public void addViewChangeListener(ViewChangeListener listener)
	{
		viewChangeListeners.add(listener);
	}
	
	void fireSwitchToView(String viewName) throws Exception
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
	
	public void addNodeToCluster(DiagramCluster cluster, DiagramNode node)
	{
		ParentMap parentMap = new ParentMap();
		parentMap.addEntry(node, cluster);
		getGraphLayoutCache().edit(null, null, parentMap, null);
	}

	public void removeNodeFromCluster(DiagramCluster cluster, DiagramNode node)
	{
		DiagramNode[] nodes = {node};
		ParentMap parentMap = ParentMap.create(getDiagramModel(), nodes, true, false);
		getGraphLayoutCache().edit(null, null, parentMap, null);
	}

	public NodeType deleteNode(BaseId idToDelete) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode nodeToDelete = model.getNodeById(idToDelete);
		ModelNodeId nodeId = nodeToDelete.getWrappedId();
		NodeType nodeType = nodeToDelete.getNodeType();
		model.deleteNode(nodeToDelete);

		database.deleteObject(ObjectType.MODEL_NODE, nodeId);
		getNodePool().remove(nodeId);
		
		return nodeType; 
	}

	public ModelNodeId insertNodeAtId(NodeType typeToInsert, BaseId requestedId) throws Exception
	{
		ModelNodeId realId = projectInfo.obtainRealNodeId(requestedId);
		createObject(ObjectType.MODEL_NODE, realId, typeToInsert);
		
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.createNode(realId);
		updateVisibilityOfSingleNode(node);
		
		ModelNodeId idThatWasInserted = node.getWrappedId();
		return idThatWasInserted;
	}
	
	public void deleteLinkage(BaseId idToDelete) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramLinkage linkageToDelete = model.getLinkageById(idToDelete);
		model.deleteLinkage(linkageToDelete);

		database.deleteObject(ObjectType.MODEL_LINKAGE, idToDelete);
		getLinkagePool().remove(idToDelete);
	}

	public BaseId insertLinkageAtId(BaseId requestedLinkageId, ModelNodeId linkFromId, ModelNodeId linkToId) throws Exception
	{
		BaseId createdId = createObject(ObjectType.MODEL_LINKAGE, requestedLinkageId);
		ConceptualModelLinkage cmLinkage = getLinkagePool().find(createdId);
		cmLinkage.setFromId(linkFromId);
		cmLinkage.setToId(linkToId);
		getDatabase().writeObject(cmLinkage);
		DiagramModel model = getDiagramModel();
		DiagramLinkage linkage = model.createLinkage(cmLinkage);
		return linkage.getDiagramLinkageId();
	}
	
	public void setNodeName(ModelNodeId nodeId, String desiredName, String expectedName) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		node.setLabel(desiredName);
		Logging.logVerbose("Updating name: "+desiredName);
		model.updateCell(node);
		
		writeNode(nodeId);
	}

	public void setNodeComment(ModelNodeId nodeId, String desiredComment, String expectedComment) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		node.setComment(desiredComment);
		Logging.logVerbose("Updating comment: "+desiredComment);
		model.updateCell(node);
		
		writeNode(nodeId);
	}

	public void setFactorType(ModelNodeId nodeId, NodeType desiredType) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		
		node.setNodeType(desiredType);
		Logging.logVerbose("SetFactorType:" + desiredType);
		model.updateCell(node);

		writeNode(nodeId);
	}
	
	public void setObjectives(ModelNodeId nodeId, ObjectiveIds desiredObjectives) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);

		node.setObjectives(desiredObjectives);
		Logging.logVerbose("SetObjectives:" + desiredObjectives);
		model.updateCell(node);
	
		writeNode(nodeId);
	}
	
	public void setGoals(ModelNodeId nodeId, GoalIds desiredGoals) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);

		node.setGoals(desiredGoals);
		Logging.logVerbose("Updating Goals:" + desiredGoals);
		model.updateCell(node);
		
		writeNode(nodeId);
	}

	protected void writeNode(ModelNodeId nodeId) throws IOException, ParseException
	{
		ConceptualModelNode cmNode = getNodePool().find(nodeId);
		database.writeObject(cmNode);
	}

	public void moveNodes(int deltaX, int deltaY, BaseId[] ids) throws Exception 
	{
		getDiagramModel().moveNodes(deltaX, deltaY, ids);
	}
	
	public void moveNodesWithoutNotification(int deltaX, int deltaY, BaseId[] ids) throws Exception 
	{
		getDiagramModel().moveNodesWithoutNotification(deltaX, deltaY, ids);
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
	
	public boolean isLinked(BaseId nodeId1, BaseId nodeId2)
	{
		return getLinkagePool().hasLinkage(nodeId1, nodeId2);
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
		if(selectionModel == null)
			return new DiagramNode[0];
		
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

	public void updateVisibilityOfNodes()
	{
		DiagramModel model = getDiagramModel();
		
		Vector nodes = model.getAllNodes();
		for(int i = 0; i < nodes.size(); ++i)
		{
			DiagramNode node = (DiagramNode)nodes.get(i);
			updateVisibilityOfSingleNode(node);
		}
		
		getGraphLayoutCache().setVisible(getDiagramModel().getProjectScopeBox(), true);
	}

	public void updateVisibilityOfSingleNode(DiagramNode node)
	{
		LayerManager manager = getLayerManager();
		boolean isVisible = manager.isVisible(node);
		getGraphLayoutCache().setVisible(node, isVisible);
	}


	public static final int DEFAULT_GRID_SIZE = 15;

	ProjectInfo projectInfo;
	ObjectManager objectManager;
	UndoRedoState undoRedoState;

	ThreatRatingFramework threatRatingFramework;
	
	ProjectServer database;
	InterviewModel interviewModel;
	DiagramModel diagramModel;

	Vector commandExecutedListeners;
	Vector viewChangeListeners;
	
	LayerManager layerManager;
	GraphSelectionModel selectionModel;
	GraphLayoutCache graphLayoutCache;
}

