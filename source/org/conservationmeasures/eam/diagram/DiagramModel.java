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

import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.ProjectScopeBox;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objectpools.FactorPool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;
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
	
	public DiagramFactor createNode(FactorId idToWrap) throws Exception
	{
		return createNode(idToWrap, new DiagramFactorId(BaseId.INVALID.asInt()));
	}

	public DiagramFactor createNode(FactorId idToWrap, DiagramFactorId requestedId) throws Exception
	{
		Factor cmObject = getNodePool().find(idToWrap);
		DiagramFactorId nodeId = requestedId;
		if(requestedId.isInvalid())
			nodeId = takeNextDiagramNodeId();
		DiagramFactor node = DiagramFactor.wrapConceptualModelObject(nodeId, cmObject);
		addNodeToModel(node);
		return node;
	}
	
	private DiagramFactorId takeNextDiagramNodeId()
	{
		return new DiagramFactorId(getProject().getAnnotationIdAssigner().takeNextId().asInt());
	}

	private void addNodeToModel(DiagramFactor node) throws Exception
	{
		insertCellIntoGraph(node);
		cellInventory.addNode(node);
		notifyListeners(createDiagramModelEvent(node), new ModelEventNotifierFactorAdded());
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
	
    public void deleteNode(DiagramFactor nodeToDelete) throws Exception
	{
		Object[] nodes = new Object[]{nodeToDelete};
		remove(nodes);
		cellInventory.removeNode(nodeToDelete);

		notifyListeners(createDiagramModelEvent(nodeToDelete), new ModelEventNotifierFactorDeleted());
	}
	
	public DiagramFactorLink createLinkage(FactorLink cmLinkage) throws Exception
	{
		DiagramFactorLink linkage = new DiagramFactorLink(this, cmLinkage);
		Object[] linkages = new Object[]{linkage};
		Map nestedMap = getNestedAttributeMap(linkage);
		DiagramFactor from = getNodeById(linkage.getFromModelNodeId());
		DiagramFactor to = getNodeById(linkage.getToModelNodeId());
		ConnectionSet cs = new ConnectionSet(linkage, from.getPort(), to.getPort());
		insert(linkages, nestedMap, cs, null, null);
		cellInventory.addLinkage(linkage);
		notifyListeners(createDiagramModelEvent(linkage), new ModelEventNotifierFactorLinkAdded());
		
		return linkage;
	}
	
	public void deleteLinkage(DiagramFactorLink linkageToDelete) throws Exception
	{
		Object[] linkages = new Object[]{linkageToDelete};
		remove(linkages);
		cellInventory.removeLinkage(linkageToDelete);
		notifyListeners(createDiagramModelEvent(linkageToDelete), new ModelEventNotifierFactorLinkDeleted());
	}
	
	public boolean hasLinkage(DiagramFactor fromNode, DiagramFactor toNode) throws Exception
	{
		FactorId nodeId1 = fromNode.getWrappedId();
		FactorId nodeId2 = toNode.getWrappedId();
		
		Vector linkages = cellInventory.getAllLinkages();
		for(int i = 0; i < linkages.size(); ++i)
		{
			DiagramFactorLink linkage = (DiagramFactorLink)linkages.get(i);
			FactorId foundId1 = linkage.getFromModelNodeId();
			FactorId foundId2 = linkage.getToModelNodeId();
			if(foundId1.equals(nodeId1) && foundId2.equals(nodeId2))
				return true;
			if(foundId1.equals(nodeId2) && foundId2.equals(nodeId1))
				return true;
		}
		
		return false;
	}
	
	public FactorSet getDirectThreatChainNodes(Factor directThreat)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildDirectThreatChain(this, directThreat);
		return chainObject.getNodes();
	}
	
	public FactorSet getNodesInChain(Factor node)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildNormalChain(this, node);
		return chainObject.getNodes();
	}
		
	public FactorSet getAllUpstreamDownstreamNodes(Factor node)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildUpstreamDownstreamChain(this, node);
		return chainObject.getNodes();
	}

	public FactorSet getAllUpstreamNodes(Factor startingNode)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildUpstreamChain(this, startingNode);
		return chainObject.getNodes();
	}

	public FactorSet getDirectlyLinkedUpstreamNodes(Factor startingNode)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildDirectlyLinkedUpstreamChain(this, startingNode);
		return chainObject.getNodes();
	}
	
	public void moveNodes(int deltaX, int deltaY, DiagramFactorId[] ids) throws Exception
	{
		moveNodesWithoutNotification(deltaX, deltaY, ids);
		nodesWereMoved(ids);
	}

	public void moveNodesWithoutNotification(int deltaX, int deltaY, DiagramFactorId[] ids) throws Exception
	{
		for(int i = 0; i < ids.length; ++i)
		{
			DiagramFactorId id = ids[i];
			DiagramFactor nodeToMove = getNodeById(id);
			Point oldLocation = nodeToMove.getLocation();
			Point newLocation = new Point(oldLocation.x + deltaX, oldLocation.y + deltaY);
			Logging.logVerbose("moved Node from:"+ oldLocation +" to:"+ newLocation);
			nodeToMove.setLocation(newLocation);
			updateCell(nodeToMove);
		}
	}
	
	public void nodesWereMoved(DiagramFactorId[] ids)
	{
		for(int i=0; i < ids.length; ++i)
		{
			try
			{
				DiagramFactor node = getNodeById(ids[0]);
				notifyListeners(createDiagramModelEvent(node), new ModelEventNotifierFactorMoved());
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

	public Set getLinkages(DiagramFactor node)
	{
		return getEdges(this, new Object[] {node});
	}
	
	public void updateCell(EAMGraphCell nodeToUpdate) throws Exception
	{
		edit(getNestedAttributeMap(nodeToUpdate), null, null, null);
		notifyListeners(createDiagramModelEvent(nodeToUpdate), new ModelEventNotifierFactorChanged());
	}
	
	public boolean hasNode(DiagramFactorId id)
	{
		return (rawGetNodeById(id) != null);
	}
	
	public boolean hasNode(FactorId id)
	{
		return (rawGetNodeById(id) != null);
	}
	
	
	
	public DiagramFactor getNodeById(DiagramFactorId id) throws Exception
	{
		DiagramFactor node = rawGetNodeById(id);
		if(node == null)
			throw new Exception("Node doesn't exist, id: " + id);
		return node;
	}

	public DiagramFactor getNodeById(FactorId id) throws Exception
	{
		DiagramFactor node = rawGetNodeById(id);
		if(node == null)
			throw new Exception("Node doesn't exist, id: " + id);
		return node;
	}

	private DiagramFactor rawGetNodeById(DiagramFactorId id)
	{
		return cellInventory.getNodeById(id);
	}

	private DiagramFactor rawGetNodeById(FactorId id)
	{
		return cellInventory.getNodeById(id);
	}

	public DiagramFactorLink getLinkageById(DiagramFactorLinkId id) throws Exception
	{
		DiagramFactorLink linkage = cellInventory.getLinkageById(id);
		if(linkage == null)
			throw new Exception("Link doesn't exist, id: " + id);
		return linkage;
	}
	
	public DiagramFactorLink getLinkageById(FactorLinkId id) throws Exception
	{
		DiagramFactorLink linkage = cellInventory.getLinkageById(id);
		if(linkage == null)
			throw new Exception("Link doesn't exist, id: " + id);
		return linkage;
	}
	
	public boolean isNodeInProject(DiagramFactor node)
	{
		return (cellInventory.getNodeById(node.getDiagramNodeId()) != null);
	}

	public boolean isLinkageInProject(DiagramFactorLink linkage)
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
			DiagramFactor node = (DiagramFactor)nodes.get(i);
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
			DiagramFactor node = DiagramFactor.createFromJson(getProject(), nodeJson);
			addNodeToModel(node);
		}
		
		FactorId[] nodeIds = getNodePool().getModelNodeIds();
		for(int i = 0;i < nodeIds.length; ++i)
		{
			FactorId modelNodeId = nodeIds[i];
			if(!hasNode(modelNodeId))
				addNodeToDiagram(modelNodeId);
			try
			{
				DiagramFactor node = getNodeById(modelNodeId);
				if(node.isCluster())
					addNodesToCluster((DiagramFactorCluster)node);
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
	
	void addNodeToDiagram(FactorId modelNodeId)
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
	
	void addNodesToCluster(DiagramFactorCluster cluster) throws Exception
	{
		FactorCluster cmCluster = (FactorCluster)cluster.getUnderlyingObject();
		IdList members = cmCluster.getMemberIds();
		for(int i = 0; i < members.size(); ++i)
		{
			DiagramFactor memberNode = getNodeById((FactorId)members.get(i));
			project.addNodeToCluster(cluster, memberNode);
		}
	}
	
	public void addLinkagesToModel() throws Exception
	{
		FactorLinkId[] linkageIds = getLinkagePool().getModelLinkageIds();
		for(int i = 0; i < linkageIds.length; ++i)
		{
			FactorLink cmLinkage = getLinkagePool().find(linkageIds[i]);
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

	
	public FactorPool getNodePool()
	{
		return project.getNodePool();
	}
	
	FactorLinkPool getLinkagePool()
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
	
	public DiagramFactor[] getAllTargetNodes()
	{
		Vector allTargets = new Vector();
		Vector allNodes = getAllNodes();
		for (int i = 0; i < allNodes.size(); i++)
		{
			DiagramFactor diagramNode = (DiagramFactor)allNodes.get(i);
			if (diagramNode.isTarget())
				allTargets.add(diagramNode);
		}
		
		return (DiagramFactor[])allTargets.toArray(new DiagramFactor[0]);
	}
	
	
	private static final String TAG_TYPE = "Type";
	private static final String TAG_NODES = "Nodes";
	
	private static final String JSON_TYPE_DIAGRAM = "Diagram";
	
	Project project;
	CellInventory cellInventory;
	ProjectScopeBox projectScopeBox;
	protected List diagramModelListenerList = new ArrayList();
}

