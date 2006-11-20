/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.DiagramCluster;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramLinkageId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.Logging;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.json.JSONArray;

public class DiagramModel extends DefaultGraphModel
{
	public DiagramModel(Project projectToUse)
	{
		project = projectToUse;
		clear();
	}
	
	public void clear()
	{
		while(getRootCount() > 0)
			remove(new Object[] {getRootAt(0)});

		cellInventory = new CellInventory();
		projectScopeBox = new ProjectScopeBox(this);
		insertCellIntoGraph(projectScopeBox);
	}
	
	public ProjectScopeBox getProjectScopeBox()
	{
		return projectScopeBox;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public ThreatRatingFramework getThreatRatingFramework()
	{
		return project.getThreatRatingFramework();
	}
	
	public void setScopeText(String text)
	{
		getProjectScopeBox().setText(EAM.text("Project Scope: " + text));
	}
	
	public DiagramNode createNode(ModelNodeId idToWrap) throws Exception
	{
		return createNode(idToWrap, new DiagramNodeId(BaseId.INVALID.asInt()));
	}

	public DiagramNode createNode(ModelNodeId idToWrap, DiagramNodeId requestedId) throws Exception
	{
		ConceptualModelNode cmObject = getNodePool().find(idToWrap);
		DiagramNodeId nodeId = requestedId;
		if(requestedId.isInvalid())
			nodeId = takeNextDiagramNodeId();
		DiagramNode node = DiagramNode.wrapConceptualModelObject(nodeId, cmObject);
		addNodeToModel(node);
		return node;
	}
	
	private DiagramNodeId takeNextDiagramNodeId()
	{
		return new DiagramNodeId(getProject().getAnnotationIdAssigner().takeNextId().asInt());
	}

	private void addNodeToModel(DiagramNode node) throws Exception
	{
		insertCellIntoGraph(node);
		cellInventory.addNode(node);
		notifyListeners(createDiagramModelEvent(node), new ModelEventNotifierNodeAdded());
	}

	private void insertCellIntoGraph(DefaultGraphCell cell)
	{
		Object[] cells = new Object[] {cell};
		Hashtable nestedAttributeMap = getNestedAttributeMap(cell);
		insert(cells, nestedAttributeMap, null, null, null);
	}

	public Hashtable getNestedAttributeMap(DefaultGraphCell cell)
	{
		Hashtable nest = new Hashtable();
		nest.put(cell, cell.getAttributes());
		return nest;
	}

	
	private DiagramModelEvent createDiagramModelEvent(EAMGraphCell node) throws Exception 
	{
		return new DiagramModelEvent(this, node);
	}
	
	public void addDiagramModelListener(DiagramModelListener listener)
	{
		diagramModelListenerList.add(listener);
	}
	
    public void removeMyEventListener(DiagramModelListener listener) 
    {
    	diagramModelListenerList.remove(listener);
    }	
    
    void notifyListeners(DiagramModelEvent event, ModelEventNotifier eventNotifier) 
    {
        for (int i=0; i<diagramModelListenerList.size(); ++i) 
        {
        	eventNotifier.doNotify((DiagramModelListener)diagramModelListenerList.get(i), event);
        }                
    }
	
    public void deleteNode(DiagramNode nodeToDelete) throws Exception
	{
		Object[] nodes = new Object[]{nodeToDelete};
		remove(nodes);
		cellInventory.removeNode(nodeToDelete);

		notifyListeners(createDiagramModelEvent(nodeToDelete), new ModelEventNotifierNodeDeleted());
	}
	
	public DiagramLinkage createLinkage(ConceptualModelLinkage cmLinkage) throws Exception
	{
		DiagramLinkage linkage = new DiagramLinkage(this, cmLinkage);
		Object[] linkages = new Object[]{linkage};
		Map nestedMap = getNestedAttributeMap(linkage);
		DiagramNode from = getNodeById(linkage.getFromModelNodeId());
		DiagramNode to = getNodeById(linkage.getToModelNodeId());
		ConnectionSet cs = new ConnectionSet(linkage, from.getPort(), to.getPort());
		insert(linkages, nestedMap, cs, null, null);
		cellInventory.addLinkage(linkage);
		notifyListeners(createDiagramModelEvent(linkage), new ModelEventNotifierLinkageAdded());
		
		return linkage;
	}
	
	public void deleteLinkage(DiagramLinkage linkageToDelete) throws Exception
	{
		Object[] linkages = new Object[]{linkageToDelete};
		remove(linkages);
		cellInventory.removeLinkage(linkageToDelete);
		notifyListeners(createDiagramModelEvent(linkageToDelete), new ModelEventNotifierLinkageDeleted());
	}
	
	public boolean hasLinkage(DiagramNode fromNode, DiagramNode toNode) throws Exception
	{
		ModelNodeId nodeId1 = fromNode.getWrappedId();
		ModelNodeId nodeId2 = toNode.getWrappedId();
		
		Vector linkages = cellInventory.getAllLinkages();
		for(int i = 0; i < linkages.size(); ++i)
		{
			DiagramLinkage linkage = (DiagramLinkage)linkages.get(i);
			ModelNodeId foundId1 = linkage.getFromModelNodeId();
			ModelNodeId foundId2 = linkage.getToModelNodeId();
			if(foundId1.equals(nodeId1) && foundId2.equals(nodeId2))
				return true;
			if(foundId1.equals(nodeId2) && foundId2.equals(nodeId1))
				return true;
		}
		
		return false;
	}
	
	public ConceptualModelNodeSet getDirectThreatChainNodes(ConceptualModelNode directThreat)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildDirectThreatChain(this, directThreat);
		return chainObject.getNodes();
	}
	
	public ConceptualModelNodeSet getNodesInChain(ConceptualModelNode node)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildNormalChain(this, node);
		return chainObject.getNodes();
	}
		
	public ConceptualModelNodeSet getAllUpstreamDownstreamNodes(ConceptualModelNode node)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildUpstreamDownstreamChain(this, node);
		return chainObject.getNodes();
	}

	public ConceptualModelNodeSet getAllUpstreamNodes(ConceptualModelNode startingNode)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildUpstreamChain(this, startingNode);
		return chainObject.getNodes();
	}

	public ConceptualModelNodeSet getDirectlyLinkedUpstreamNodes(ConceptualModelNode startingNode)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildDirectlyLinkedUpstreamChain(this, startingNode);
		return chainObject.getNodes();
	}
	
	public void moveNodes(int deltaX, int deltaY, DiagramNodeId[] ids) throws Exception
	{
		moveNodesWithoutNotification(deltaX, deltaY, ids);
		nodesWereMoved(ids);
	}

	public void moveNodesWithoutNotification(int deltaX, int deltaY, DiagramNodeId[] ids) throws Exception
	{
		for(int i = 0; i < ids.length; ++i)
		{
			DiagramNodeId id = ids[i];
			DiagramNode nodeToMove = getNodeById(id);
			Point oldLocation = nodeToMove.getLocation();
			Point newLocation = new Point(oldLocation.x + deltaX, oldLocation.y + deltaY);
			Logging.logVerbose("moved Node from:"+ oldLocation +" to:"+ newLocation);
			nodeToMove.setLocation(newLocation);
			updateCell(nodeToMove);
		}
	}
	
	public void nodesWereMoved(DiagramNodeId[] ids)
	{
		for(int i=0; i < ids.length; ++i)
		{
			try
			{
				DiagramNode node = getNodeById(ids[0]);
				notifyListeners(createDiagramModelEvent(node), new ModelEventNotifierNodeMoved());
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
	}
	
	public int getNodeCount()
	{
		return getAllNodes().size();
	}
	
	public int getLinkageCount()
	{
		return getAllLinkages().size();
	}

	public Set getLinkages(DiagramNode node)
	{
		return getEdges(this, new Object[] {node});
	}
	
	public void updateCell(EAMGraphCell nodeToUpdate) throws Exception
	{
		edit(getNestedAttributeMap(nodeToUpdate), null, null, null);
		notifyListeners(createDiagramModelEvent(nodeToUpdate), new ModelEventNotifierNodeChanged());
	}
	
	public boolean hasNode(DiagramNodeId id)
	{
		return (rawGetNodeById(id) != null);
	}
	
	public boolean hasNode(ModelNodeId id)
	{
		return (rawGetNodeById(id) != null);
	}
	
	
	
	public DiagramNode getNodeById(DiagramNodeId id) throws Exception
	{
		DiagramNode node = rawGetNodeById(id);
		if(node == null)
			throw new Exception("Node doesn't exist, id: " + id);
		return node;
	}

	public DiagramNode getNodeById(ModelNodeId id) throws Exception
	{
		DiagramNode node = rawGetNodeById(id);
		if(node == null)
			throw new Exception("Node doesn't exist, id: " + id);
		return node;
	}

	private DiagramNode rawGetNodeById(DiagramNodeId id)
	{
		return cellInventory.getNodeById(id);
	}

	private DiagramNode rawGetNodeById(ModelNodeId id)
	{
		return cellInventory.getNodeById(id);
	}

	public DiagramLinkage getLinkageById(DiagramLinkageId id) throws Exception
	{
		DiagramLinkage linkage = cellInventory.getLinkageById(id);
		if(linkage == null)
			throw new Exception("Linkage doesn't exist, id: " + id);
		return linkage;
	}
	
	public DiagramLinkage getLinkageById(ModelLinkageId id) throws Exception
	{
		DiagramLinkage linkage = cellInventory.getLinkageById(id);
		if(linkage == null)
			throw new Exception("Linkage doesn't exist, id: " + id);
		return linkage;
	}
	
	public boolean isNodeInProject(DiagramNode node)
	{
		return (cellInventory.getNodeById(node.getDiagramNodeId()) != null);
	}

	public boolean isLinkageInProject(DiagramLinkage linkage)
	{
		return (cellInventory.getLinkageById(linkage.getDiagramLinkageId()) != null);
	}

	public Vector getAllNodes()
	{
		return cellInventory.getAllNodes();
	}
	
	public Vector getAllLinkages()
	{
		return cellInventory.getAllLinkages();
	}
	
	public Goal getGoalById(BaseId id)
	{
		return getGoalPool().find(id);
	}
	
	public Objective getObjectiveById(BaseId id)
	{
		return getObjectivePool().find(id);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject nodeMap = new EnhancedJsonObject();
		Vector nodes = getAllNodes();
		for(int i=0; i < nodes.size(); ++i)
		{
			DiagramNode node = (DiagramNode)nodes.get(i);
			nodeMap.put(Integer.toString(node.getDiagramNodeId().asInt()), node.toJson());
		}
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_TYPE, JSON_TYPE_DIAGRAM);
		json.put(TAG_NODES, nodeMap);
		return json;
	}
	
	public void fillFrom(EnhancedJsonObject json) throws Exception
	{
		addNodesToModel(json);
		addLinkagesToModel();
	}

	private void addNodesToModel(EnhancedJsonObject json) throws Exception
	{
		EnhancedJsonObject nodeMap = json.getJson(TAG_NODES);
		JSONArray keys = nodeMap.names();
		
		// TODO: Really we should extend JSONObject to have a sane names() method
		// that returns an empty array if there are no names
		if(keys == null)
			return;
		for(int i=0; i < keys.length(); ++i)
		{
			String key = keys.getString(i);
			EnhancedJsonObject nodeJson = nodeMap.getJson(key);
			DiagramNode node = DiagramNode.createFromJson(getProject(), nodeJson);
			addNodeToModel(node);
		}
		
		ModelNodeId[] nodeIds = getNodePool().getModelNodeIds();
		for(int i = 0;i < nodeIds.length; ++i)
		{
			ModelNodeId modelNodeId = nodeIds[i];
			if(!hasNode(modelNodeId))
				addNodeToDiagram(modelNodeId);
			try
			{
				DiagramNode node = getNodeById(modelNodeId);
				if(node.isCluster())
					addNodesToCluster((DiagramCluster)node);
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog("Errors detected in the project. " +
						"Continuing to load, but you may experience problems. " +
						"Please contact technical support.");
			}
		}
	}
	
	void addNodeToDiagram(ModelNodeId modelNodeId)
	{
		try
		{
			createNode(modelNodeId);
			String[] bodyLines = {"A factor was missing from the diagram. It has been added."};
			EAM.okDialog("Repairing Project", bodyLines);
		}
		catch(Exception e)
		{
			EAM.errorDialog("This project has some internal errors that could not be automatically repaired. " +
					"Please contact technical support.");
		}
	}
	
	void addNodesToCluster(DiagramCluster cluster) throws Exception
	{
		ConceptualModelCluster cmCluster = (ConceptualModelCluster)cluster.getUnderlyingObject();
		IdList members = cmCluster.getMemberIds();
		for(int i = 0; i < members.size(); ++i)
		{
			DiagramNode memberNode = getNodeById((ModelNodeId)members.get(i));
			project.addNodeToCluster(cluster, memberNode);
		}
	}
	
	public void addLinkagesToModel() throws Exception
	{
		ModelLinkageId[] linkageIds = getLinkagePool().getModelLinkageIds();
		for(int i = 0; i < linkageIds.length; ++i)
		{
			ConceptualModelLinkage cmLinkage = getLinkagePool().find(linkageIds[i]);
			if(hasNode(cmLinkage.getFromNodeId()) && hasNode(cmLinkage.getToNodeId()))
			{
				createLinkage(cmLinkage);
			}
		}
	}

	public void updateProjectScope()
	{
		String newText = getProject().getMetadata().getShortProjectScope();
		setScopeText(newText);
	}

	
	public NodePool getNodePool()
	{
		return project.getNodePool();
	}
	
	LinkagePool getLinkagePool()
	{
		return project.getLinkagePool();
	}
	
	ObjectivePool getObjectivePool()
	{
		return project.getObjectivePool();
	}
	
	GoalPool getGoalPool()
	{
		return project.getGoalPool();
	}
	
	public DiagramNode[] getAllTargetNodes()
	{
		Vector allTargets = new Vector();
		Vector allNodes = getAllNodes();
		for (int i = 0; i < allNodes.size(); i++)
		{
			DiagramNode diagramNode = (DiagramNode)allNodes.get(i);
			if (diagramNode.isTarget())
				allTargets.add(diagramNode);
		}
		
		return (DiagramNode[])allTargets.toArray(new DiagramNode[0]);
	}
	
	
	private static final String TAG_TYPE = "Type";
	private static final String TAG_NODES = "Nodes";
	
	private static final String JSON_TYPE_DIAGRAM = "Diagram";
	
	Project project;
	CellInventory cellInventory;
	ProjectScopeBox projectScopeBox;
	protected List diagramModelListenerList = new ArrayList();
}

