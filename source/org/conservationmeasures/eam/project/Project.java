/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.annotations.GoalPool;
import org.conservationmeasures.eam.annotations.IndicatorPool;
import org.conservationmeasures.eam.annotations.ResourcePool;
import org.conservationmeasures.eam.annotations.TaskPool;
import org.conservationmeasures.eam.annotations.ViewPool;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandSetNodeName;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.database.DataUpgrader;
import org.conservationmeasures.eam.database.LinkageManifest;
import org.conservationmeasures.eam.database.NodeManifest;
import org.conservationmeasures.eam.database.ObjectManifest;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.PartialGraphLayoutCache;
import org.conservationmeasures.eam.diagram.nodes.DiagramCluster;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.LinkageDataMap;
import org.conservationmeasures.eam.diagram.nodes.NodeDataHelper;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ObjectiveIds;
import org.conservationmeasures.eam.objects.ObjectivePool;
import org.conservationmeasures.eam.objects.ProjectResource;
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
		
		pools = new HashMap();
		pools.put(new Integer(ObjectType.MODEL_NODE), new NodePool());
		pools.put(new Integer(ObjectType.MODEL_LINKAGE), new LinkagePool());
		pools.put(new Integer(ObjectType.TASK), new TaskPool());
		pools.put(new Integer(ObjectType.VIEW_DATA), new ViewPool());
		pools.put(new Integer(ObjectType.PROJECT_RESOURCE), new ResourcePool());
		pools.put(new Integer(ObjectType.INDICATOR), new IndicatorPool());
		pools.put(new Integer(ObjectType.OBJECTIVE), new ObjectivePool());

		goalPool = GoalPool.createSampleGoals(getAnnotationIdAssigner());
		diagramModel = new DiagramModel(this);
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
		return (EAMObjectPool)pools.get(new Integer(objectType));
	}
	
	public NodePool getNodePool()
	{
		return (NodePool)getPool(ObjectType.MODEL_NODE);
	}
	
	public LinkagePool getLinkagePool()
	{
		return (LinkagePool)getPool(ObjectType.MODEL_LINKAGE);
	}
	
	public TaskPool getTaskPool()
	{
		return (TaskPool)getPool(ObjectType.TASK);
	}
	
	public ViewPool getViewPool()
	{
		return (ViewPool)getPool(ObjectType.VIEW_DATA);
	}
	
	public ResourcePool getResourcePool()
	{
		return (ResourcePool)getPool(ObjectType.PROJECT_RESOURCE);
	}
	
	public IndicatorPool getIndicatorPool()
	{
		return (IndicatorPool)getPool(ObjectType.INDICATOR);
	}

	public ObjectivePool getObjectivePool()
	{
		return (ObjectivePool)getPool(ObjectType.OBJECTIVE);
	}
	
	public GoalPool getGoalPool()
	{
		return goalPool;
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
		
		int createdId = createObject(ObjectType.VIEW_DATA);
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
		int rootTaskId = projectInfo.getRootTaskId();
		if(rootTaskId == IdAssigner.INVALID_ID)
		{
			rootTaskId = createObject(ObjectType.TASK);
			projectInfo.setRootTaskId(rootTaskId);
			getDatabase().writeProjectInfo(projectInfo);
		}
		return getTaskPool().find(rootTaskId);
	}
	
	public EAMObject findObject(int objectType, int objectId)
	{
		EAMObjectPool pool = getPool(objectType);
		return pool.findObject(objectId);
	}
	
	public ConceptualModelNode findNode(int nodeId)
	{
		return (ConceptualModelNode)findObject(ObjectType.MODEL_NODE, nodeId);
	}
	
	public ConceptualModelNodeSet findNodesThatUseThisObjective(int objectiveId)
	{
		ConceptualModelNodeSet foundNodes = new ConceptualModelNodeSet();
		NodePool pool = getNodePool();
		int[] allNodeIds = pool.getIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			ConceptualModelNode node = pool.find(allNodeIds[i]);
			if(node.getObjectives().contains(objectiveId))
				foundNodes.attemptToAdd(node);
		}
		
		return foundNodes;
	}

	public ConceptualModelNodeSet findNodesThatUseThisIndicator(int indicatorId)
	{
		ConceptualModelNodeSet foundNodes = new ConceptualModelNodeSet();
		NodePool pool = getNodePool();
		int[] allNodeIds = pool.getIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			ConceptualModelNode node = pool.find(allNodeIds[i]);
			if(node.getIndicatorId() == indicatorId)
				foundNodes.attemptToAdd(node);
		}
		
		return foundNodes;
	}
	
	public ConceptualModelNodeSet findAllNodesRelatedToThisIndicator(int indicatorId)
	{
		ConceptualModelNode[] nodesThatUseThisIndicator = findNodesThatUseThisIndicator(indicatorId).toNodeArray();
		ConceptualModelNodeSet relatedNodes = new ConceptualModelNodeSet();
		
		for(int i = 0; i < nodesThatUseThisIndicator.length; ++i)
		{
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllNodesInChain(nodesThatUseThisIndicator[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	public ConceptualModelNodeSet findAllNodesRelatedToThisObjective(int objectiveId)
	{
		ConceptualModelNode[] nodesThatUseThisObjective = findNodesThatUseThisObjective(objectiveId).toNodeArray();
		ConceptualModelNodeSet relatedNodes = new ConceptualModelNodeSet();
		
		for(int i = 0; i < nodesThatUseThisObjective.length; ++i)
		{
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllNodesInChain(nodesThatUseThisObjective[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}

	/////////////////////////////////////////////////////////////////////////////////
	// objects
	
	public int createObject(int objectType) throws Exception
	{
		return createObject(objectType, IdAssigner.INVALID_ID);
	}
	
	public int createObject(int objectType, int objectId) throws Exception
	{
		int createdId = IdAssigner.INVALID_ID;
		switch(objectType)
		{
			case ObjectType.THREAT_RATING_CRITERION:
			{
				createdId = getThreatRatingFramework().createCriterion(objectId);
				EAMObject newObject = getThreatRatingFramework().getCriterion(createdId);
				getDatabase().writeObject(newObject);
				getDatabase().writeThreatRatingFramework(getThreatRatingFramework());
				break;
			}
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
			{
				createdId = getThreatRatingFramework().createValueOption(objectId);
				EAMObject newObject = getThreatRatingFramework().getValueOption(createdId);
				getDatabase().writeObject(newObject);
				getDatabase().writeThreatRatingFramework(getThreatRatingFramework());
				break;
			}
			case ObjectType.TASK:
			{
				if(objectId == IdAssigner.INVALID_ID)
					objectId = getAnnotationIdAssigner().takeNextId();
				Task task = new Task(objectId);
				getTaskPool().put(task);
				getDatabase().writeObject(task);
				createdId = task.getId();
				break;
			}
			
			case ObjectType.MODEL_NODE:
			{
				createdId = insertNodeAtId(new NodeTypeTarget(), objectId);
				break;
			}
			
			case ObjectType.VIEW_DATA:
			{
				if(objectId == IdAssigner.INVALID_ID)
					objectId = getAnnotationIdAssigner().takeNextId();
				ViewData viewData = new ViewData(objectId);
				getViewPool().put(viewData);
				getDatabase().writeObject(viewData);
				createdId = viewData.getId();
				break;
			}
			
			case ObjectType.MODEL_LINKAGE:
			{
				objectId = projectInfo.obtainRealLinkageId(objectId);
				ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(objectId, -2, -2);
				getLinkagePool().put(cmLinkage);
				database.writeObject(cmLinkage);
				createdId = cmLinkage.getId();
				break;
			}
			
			case ObjectType.PROJECT_RESOURCE:
			{
				if(objectId == IdAssigner.INVALID_ID)
					objectId = getAnnotationIdAssigner().takeNextId();
				ProjectResource resource = new ProjectResource(objectId);
				getResourcePool().put(resource);
				getDatabase().writeObject(resource);
				createdId = resource.getId();
				break;
			}
				
			case ObjectType.INDICATOR:
			{
				if(objectId == IdAssigner.INVALID_ID)
					objectId = getAnnotationIdAssigner().takeNextId();
				Indicator indicator = new Indicator(objectId);
				getIndicatorPool().put(indicator);
				getDatabase().writeObject(indicator);
				createdId = indicator.getId();
				break;
			}
				
			case ObjectType.OBJECTIVE:
			{
				if(objectId == IdAssigner.INVALID_ID)
					objectId = getAnnotationIdAssigner().takeNextId();
				Objective objective = new Objective(objectId);
				getObjectivePool().put(objective);
				getDatabase().writeObject(objective);
				createdId = objective.getId();
				break;
			}
				
			default:
				throw new RuntimeException("Attempted to create unknown object type: " + objectType);
		}
		
		return createdId;
	}
	
	public void deleteObject(int objectType, int objectId) throws IOException, ParseException
	{
		switch(objectType)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				getThreatRatingFramework().deleteCriterion(objectId);
				getDatabase().deleteObject(objectType, objectId);
				getDatabase().writeThreatRatingFramework(getThreatRatingFramework());
				break;
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				getThreatRatingFramework().deleteValueOption(objectId);
				getDatabase().deleteObject(objectType, objectId);
				getDatabase().writeThreatRatingFramework(getThreatRatingFramework());
				break;
				
			case ObjectType.TASK:
				getTaskPool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.MODEL_NODE:
				getNodePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.VIEW_DATA:
				getViewPool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.MODEL_LINKAGE:
				getLinkagePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.PROJECT_RESOURCE:
				getResourcePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.INDICATOR:
				getIndicatorPool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.OBJECTIVE:
				getObjectivePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			default:
				throw new RuntimeException("Attempted to delete unknown object type: " + objectType);
		}
	}
	
	public void setObjectData(int objectType, int objectId, String fieldTag, String dataValue) throws Exception
	{
		switch(objectType)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				getThreatRatingFramework().setCriterionData(objectId, fieldTag, dataValue);
				getDatabase().writeObject(getThreatRatingFramework().getCriterion(objectId));
				break;
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				getThreatRatingFramework().setValueOptionData(objectId, fieldTag, dataValue);
				getDatabase().writeObject(getThreatRatingFramework().getValueOption(objectId));
				break;
			
			case ObjectType.TASK:
				Task task = getTaskPool().find(objectId);
				task.setData(fieldTag, dataValue);
				getDatabase().writeObject(task);
				break;
				
			case ObjectType.MODEL_NODE:
				ConceptualModelNode node = getNodePool().find(objectId);
				node.setData(fieldTag, dataValue);
				getDatabase().writeNode(node);
				break;
				
			case ObjectType.VIEW_DATA:
				ViewData viewData = getViewPool().find(objectId);
				viewData.setData(fieldTag, dataValue);
				getDatabase().writeObject(viewData);
				break;
				
			case ObjectType.MODEL_LINKAGE:
				ConceptualModelLinkage linkage = getLinkagePool().find(objectId);
				linkage.setData(fieldTag, dataValue);
				getDatabase().writeLinkage(linkage);
				break;
				
			case ObjectType.PROJECT_RESOURCE:
				ProjectResource resource = getResourcePool().find(objectId);
				resource.setData(fieldTag, dataValue);
				getDatabase().writeObject(resource);
				break;
				
			case ObjectType.INDICATOR:
				Indicator indicator = getIndicatorPool().find(objectId);
				indicator.setData(fieldTag, dataValue);
				getDatabase().writeObject(indicator);
				break;
				
			case ObjectType.OBJECTIVE:
				Objective objective = getObjectivePool().find(objectId);
				objective.setData(fieldTag, dataValue);
				getDatabase().writeObject(objective);
				break;
				
				
			default:
				throw new RuntimeException("Attempted to set data for unknown object type: " + objectType);
		}
	}
	
	public String getObjectData(int objectType, int objectId, String fieldTag)
	{
		switch(objectType)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				return getThreatRatingFramework().getCriterionData(objectId, fieldTag);
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				return getThreatRatingFramework().getValueOptionData(objectId, fieldTag);
				
			case ObjectType.TASK:
				return getTaskPool().find(objectId).getData(fieldTag);
				
			case ObjectType.MODEL_NODE:
				return getNodePool().find(objectId).getData(fieldTag);
				
			case ObjectType.VIEW_DATA:
				return getViewPool().find(objectId).getData(fieldTag);
				
			case ObjectType.MODEL_LINKAGE:
				return getLinkagePool().find(objectId).getData(fieldTag);
				
			case ObjectType.PROJECT_RESOURCE:
				return getResourcePool().find(objectId).getData(fieldTag);
				
			case ObjectType.INDICATOR:
				return getIndicatorPool().find(objectId).getData(fieldTag);
				
			case ObjectType.OBJECTIVE:
				return getObjectivePool().find(objectId).getData(fieldTag);
				
			default:
				throw new RuntimeException("Attempted to get data for unknown object type: " + objectType);
		}
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
		loadThreatRatingFramework();
		loadNodePool();
		loadLinkagePool();
		loadTaskPool();
		loadViewPool();
		loadResourcePool();
		loadIndicatorPool();
		loadObjectivePool();
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
	
	private void loadNodePool() throws IOException, ParseException
	{
		NodeManifest nodes = getDatabase().readNodeManifest();
		int[] nodeIds = nodes.getAllKeys();
		for(int i = 0; i < nodeIds.length; ++i)
		{
			ConceptualModelNode node = getDatabase().readNode(nodeIds[i]);
			getNodePool().put(node);
		}
	}
	
	private void loadLinkagePool() throws IOException, ParseException
	{
		LinkageManifest linkages = getDatabase().readLinkageManifest();
		int[] linkageIds = linkages.getAllKeys();
		for(int i = 0; i < linkageIds.length; ++i)
		{
			ConceptualModelLinkage linkage = getDatabase().readLinkage(linkageIds[i]);
			getLinkagePool().put(linkage);
		}
	}
	
	private void loadTaskPool() throws Exception
	{
		ObjectManifest manifest = getDatabase().readObjectManifest(ObjectType.TASK);
		int[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			Task task = (Task)getDatabase().readObject(ObjectType.TASK, ids[i]);
			getTaskPool().put(task);
		}
	}
	
	private void loadViewPool() throws Exception
	{
		ObjectManifest manifest = getDatabase().readObjectManifest(ObjectType.VIEW_DATA);
		int[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			ViewData viewData = (ViewData)getDatabase().readObject(ObjectType.VIEW_DATA, ids[i]);
			getViewPool().put(viewData);
		}
	}
	
	private void loadResourcePool() throws Exception
	{
		ObjectManifest manifest = getDatabase().readObjectManifest(ObjectType.PROJECT_RESOURCE);
		int[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			ProjectResource resource = (ProjectResource)getDatabase().readObject(ObjectType.PROJECT_RESOURCE, ids[i]);
			getResourcePool().put(resource);
		}
	}
	
	private void loadIndicatorPool() throws Exception
	{
		ObjectManifest manifest = getDatabase().readObjectManifest(ObjectType.INDICATOR);
		int[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			Indicator indicator = (Indicator)getDatabase().readObject(ObjectType.INDICATOR, ids[i]);
			getIndicatorPool().put(indicator);
		}
	}
	
	private void loadObjectivePool() throws Exception
	{
		ObjectManifest manifest = getDatabase().readObjectManifest(ObjectType.OBJECTIVE);
		int[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			Objective objective = (Objective)getDatabase().readObject(ObjectType.OBJECTIVE, ids[i]);
			getObjectivePool().put(objective);
		}
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

	public void pasteNodesAndLinksIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		NodeDataHelper dataHelper = new NodeDataHelper(getDiagramModel().getAllNodes());
		pasteNodesIntoProject(list, startPoint, dataHelper);
		pasteLinksIntoProject(list, dataHelper);
		executeCommand(new CommandEndTransaction());
	}
	
	public void pasteNodesOnlyIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		NodeDataHelper dataHelper = new NodeDataHelper(getDiagramModel().getAllNodes());
		pasteNodesIntoProject(list, startPoint, dataHelper);
		executeCommand(new CommandEndTransaction());
	}

	private void pasteNodesIntoProject(TransferableEamList list, Point startPoint, NodeDataHelper dataHelper) throws Exception 
	{
		NodeDataMap[] nodes = list.getNodeDataCells();
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeDataMap nodeData = nodes[i];
			int originalNodeId = nodeData.getInt(DiagramNode.TAG_ID);
			
			NodeType type = NodeDataMap.convertIntToNodeType(nodeData.getInt(DiagramNode.TAG_NODE_TYPE)); 
			int newNodeId = createNode(type);
			dataHelper.setNewId(originalNodeId, newNodeId);
			dataHelper.setOriginalLocation(originalNodeId, nodeData.getPoint(DiagramNode.TAG_LOCATION));
			
			CommandSetNodeName newNodeLabel = new CommandSetNodeName(newNodeId, nodeData.getString(DiagramNode.TAG_VISIBLE_LABEL));
			executeCommand(newNodeLabel);
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

	public int createNode(NodeType nodeType) throws Exception
	{
		CommandInsertNode commandInsertNode = new CommandInsertNode(nodeType);
		executeCommand(commandInsertNode);
		int id = commandInsertNode.getId();
		Command[] commandsToAddToView = getCurrentViewData().buildCommandsToAddNode(id);
		for(int i = 0; i < commandsToAddToView.length; ++i)
			executeCommand(commandsToAddToView[i]);
		return id;
	}

	public NodeType deleteNode(int idToDelete) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode nodeToDelete = model.getNodeById(idToDelete);
		NodeType nodeType = nodeToDelete.getNodeType();
		model.deleteNode(nodeToDelete);

		database.deleteNode(idToDelete);
		getNodePool().remove(idToDelete);
		
		return nodeType; 
	}

	public int insertNodeAtId(NodeType typeToInsert, int requestedId) throws Exception
	{
		int realId = projectInfo.obtainRealNodeId(requestedId);
		ConceptualModelNode cmObject = ConceptualModelNode.createConceptualModelObject(realId, typeToInsert);
		getNodePool().put(cmObject);
		writeNode(realId);
		
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.createNode(realId);
		updateVisibilityOfSingleNode(node);
		
		int idThatWasInserted = node.getId();
		return idThatWasInserted;
	}
	
	public void deleteLinkage(int idToDelete) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramLinkage linkageToDelete = model.getLinkageById(idToDelete);
		model.deleteLinkage(linkageToDelete);

		database.deleteLinkage(idToDelete);
		getLinkagePool().remove(idToDelete);
	}

	public int insertLinkageAtId(int requestedLinkageId, int linkFromId, int linkToId) throws Exception
	{
		int createdId = createObject(ObjectType.MODEL_LINKAGE, requestedLinkageId);
		ConceptualModelLinkage cmLinkage = getLinkagePool().find(createdId);
		cmLinkage.setFromId(linkFromId);
		cmLinkage.setToId(linkToId);
		getDatabase().writeLinkage(cmLinkage);
		DiagramModel model = getDiagramModel();
		DiagramLinkage linkage = model.createLinkage(cmLinkage);
		return linkage.getId();
	}
	
	public void setNodeName(int nodeId, String desiredName, String expectedName) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		node.setLabel(desiredName);
		Logging.logVerbose("Updating name: "+desiredName);
		model.updateCell(node);
		
		writeNode(nodeId);
	}

	public void setNodeComment(int nodeId, String desiredComment, String expectedComment) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		node.setComment(desiredComment);
		Logging.logVerbose("Updating comment: "+desiredComment);
		model.updateCell(node);
		
		writeNode(nodeId);
	}

	public void setFactorType(int nodeId, NodeType desiredType) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		
		node.setNodeType(desiredType);
		Logging.logVerbose("SetFactorType:" + desiredType);
		model.updateCell(node);

		writeNode(nodeId);
	}
	
	public void setIndicator(int nodeId, int desiredIndicatorId) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);
		
		node.setIndicator(desiredIndicatorId);
		Logging.logVerbose("SetIndicator:" + desiredIndicatorId);
		model.updateCell(node);
		
		writeNode(nodeId);
	}
	
	public void setObjectives(int nodeId, ObjectiveIds desiredObjectives) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);

		node.setObjectives(desiredObjectives);
		Logging.logVerbose("SetObjectives:" + desiredObjectives);
		model.updateCell(node);
	
		writeNode(nodeId);
	}
	
	public void setGoals(int nodeId, GoalIds desiredGoals) throws Exception
	{
		DiagramModel model = getDiagramModel();
		DiagramNode node = model.getNodeById(nodeId);

		node.setGoals(desiredGoals);
		Logging.logVerbose("Updating Goals:" + desiredGoals);
		model.updateCell(node);
		
		writeNode(nodeId);
	}

	protected void writeNode(int nodeId) throws IOException, ParseException
	{
		ConceptualModelNode cmNode = getNodePool().find(nodeId);
		database.writeNode(cmNode);
	}

	public void moveNodes(int deltaX, int deltaY, int[] ids) throws Exception 
	{
		getDiagramModel().moveNodes(deltaX, deltaY, ids);
	}
	
	public void moveNodesWithoutNotification(int deltaX, int deltaY, int[] ids) throws Exception 
	{
		getDiagramModel().moveNodesWithoutNotification(deltaX, deltaY, ids);
	}
	
	public void nodesWereMovedOrResized(int deltaX, int deltaY, int[] ids) throws CommandFailedException
	{
		DiagramModel model = getDiagramModel();
		model.nodesWereMoved(ids);

		Vector commandsToRecord = new Vector();
		Vector commandsToExecute = new Vector();
		Vector movedNodes = new Vector();
		for(int i = 0 ; i < ids.length; ++i)
		{
			try 
			{
				DiagramNode node = model.getNodeById(ids[i]);
				if(node.getParent() != null)
					commandsToExecute.addAll(buildDetachFromClusterCommand(node));
				if(node.getParent() == null)
					commandsToExecute.addAll(buildAttachToClusterCommand(node));
				
				if(node.hasMoved())
					movedNodes.add(node);
				
				if(node.sizeHasChanged())
					commandsToRecord.add(buildResizeCommand(node));
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
			
			commandsToRecord.add(new CommandDiagramMove(deltaX, deltaY, idsActuallyMoved));
		}
		
		if(commandsToRecord.size() > 0 || commandsToExecute.size() > 0)
		{
			recordCommand(new CommandBeginTransaction());
			for(int i=0; i < commandsToRecord.size(); ++i)
				recordCommand((Command)commandsToRecord.get(i));
			for(int i=0; i < commandsToExecute.size(); ++i)
				executeCommand((Command)commandsToExecute.get(i));
			recordCommand(new CommandEndTransaction());
		}
		
		/*
		 * NOTE: The following chunk of code works around a weird bug deep in jgraph
		 * If you click on a cluster, then click on a member, then drag the member out,
		 * part of jgraph still thinks the cluster has a member that is selected.
		 * So when you drag the node back in, it doesn't become a member because jgraph 
		 * won't return the cluster, because it thinks the cluster has something selected.
		 * The workaround is to re-select what is selected, so the cached values inside 
		 * jgraph get reset to their proper values.
		 */
		MainWindow mainWindow = EAM.mainWindow;
		if(mainWindow != null)
		{
			DiagramComponent diagram = mainWindow.getDiagramComponent();
			diagram.setSelectionCells(diagram.getSelectionCells());
		}

	}
	
	private List buildAttachToClusterCommand(DiagramNode node) throws Exception
	{
		Vector result = new Vector();
		if(node.isCluster())
			return result;
		
		DiagramCluster cluster = getFirstClusterThatContains(node.getRectangle());
		if(cluster == null)
			return result;
		
		addNodeToCluster(cluster, node);
		CommandSetObjectData cmd = CommandSetObjectData.createAppendIdCommand(cluster.getUnderlyingObject(), 
				ConceptualModelCluster.TAG_MEMBER_IDS, node.getId());
		result.add(cmd);
		return result;
	}
	
	private List buildDetachFromClusterCommand(DiagramNode node) throws ParseException
	{
		Vector result = new Vector();
		DiagramCluster cluster = (DiagramCluster)node.getParent();
		if(cluster.getRectangle().contains(node.getRectangle()))
			return result;
		
		removeNodeFromCluster(cluster, node);
		CommandSetObjectData cmd = CommandSetObjectData.createRemoveIdCommand(cluster.getUnderlyingObject(), 
				ConceptualModelCluster.TAG_MEMBER_IDS, node.getId());
		result.add(cmd);
		return result;
	}
	
	private DiagramCluster getFirstClusterThatContains(Rectangle candidateRect) throws Exception
	{
		DiagramModel model = getDiagramModel();
		int[] allNodeIds = getNodePool().getIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			DiagramNode possibleCluster = model.getNodeById(allNodeIds[i]);
			if(!possibleCluster.isCluster())
				continue;
			
			if(possibleCluster.getRectangle().contains(candidateRect))
				return (DiagramCluster)possibleCluster;
		}
		
		return null;
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
	
	public boolean isLinked(int nodeId1, int nodeId2)
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


	/////////////////////////////////////////////////////////////////////////////////
	// undo/redo
	
	public void undo() throws CommandFailedException
	{
		Command cmd = getCommandToUndo();
		cmd.undo(this);
		fireCommandUndone(cmd);
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

	ProjectInfo projectInfo;

	HashMap pools;
	
	GoalPool goalPool;
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

