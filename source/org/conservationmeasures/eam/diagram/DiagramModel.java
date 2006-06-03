/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.GoalPool;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ObjectivePool;
import org.conservationmeasures.eam.project.LinkagePool;
import org.conservationmeasures.eam.project.NodePool;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.Logging;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.json.JSONArray;
import org.json.JSONObject;

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
	
	public ThreatRatingFramework getThreatRatingFramework()
	{
		return project.getThreatRatingFramework();
	}

	public DiagramNode createNode(int id) throws Exception
	{
		ConceptualModelNode cmObject = getNodePool().find(id);
		DiagramNode node = DiagramNode.wrapConceptualModelObject(cmObject);
		addNodeToModel(node);
		return node;
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
		ConnectionSet cs = linkage.getConnectionSet();
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
		return getLinkagePool().hasLinkage(fromNode.getId(), toNode.getId());
	}
	
	public int[] getDirectThreatChainIds(ConceptualModelNode directThreat)
	{
		HashSet results = new HashSet();
		if(!directThreat.isDirectThreat())
			return new int[0];
		results.addAll(getDirectlyLinkedDownstreamNodeIds(directThreat));
		results.addAll(getAllUpstreamNodeIds(directThreat));
		
		return intArrayFromSet(results);
	}

	public HashSet getAllUpstreamNodeIds(ConceptualModelNode startingNode)
	{
		return getAllLinkedNodeIds(ConceptualModelLinkage.TO, startingNode);
	}

	public HashSet getAllDownstreamNodeIds(ConceptualModelNode startingNode)
	{
		return getAllLinkedNodeIds(ConceptualModelLinkage.FROM, startingNode);
	}

	public HashSet getDirectlyLinkedUpstreamNodeIds(ConceptualModelNode startingNode)
	{
		return getDirectlyLinkedNodeIds(ConceptualModelLinkage.TO, startingNode);
	}

	public HashSet getDirectlyLinkedDownstreamNodeIds(ConceptualModelNode startingNode)
	{
		return getDirectlyLinkedNodeIds(ConceptualModelLinkage.FROM, startingNode);
	}

	private HashSet getDirectlyLinkedNodeIds(int direction, ConceptualModelNode startingNode)
	{

		HashSet results = new HashSet();
		results.add(new Integer(startingNode.getId()));
		
		LinkagePool linkagePool = getLinkagePool();
		for(int i = 0; i < linkagePool.getIds().length; ++i)
		{
			ConceptualModelLinkage thisLinkage = linkagePool.find(linkagePool.getIds()[i]);
			if(thisLinkage.getNodeId(direction) == startingNode.getId())
			{
				int downstreamNodeId = thisLinkage.getToNodeId();
				results.add(new Integer(downstreamNodeId));
			}
		}
		return results;
	}

	private HashSet getAllLinkedNodeIds(int direction, ConceptualModelNode startingNode)
	{
		HashSet linkedNodes = new HashSet();
		HashSet unprocessedNodes = new HashSet();
		linkedNodes.add(new Integer(startingNode.getId()));

		LinkagePool linkagePool = getLinkagePool();
		for(int i = 0; i < linkagePool.getIds().length; ++i)
		{
			ConceptualModelLinkage thisLinkage = linkagePool.find(linkagePool.getIds()[i]);
			if(thisLinkage.getNodeId(direction) == startingNode.getId())
			{
				ConceptualModelNode linkedNode = getNodePool().find(thisLinkage.getFromNodeId());
				unprocessedNodes.add(linkedNode);
			}
		}		
		
		while(unprocessedNodes.size() > 0)
		{
			ConceptualModelNode thisNode = (ConceptualModelNode)unprocessedNodes.toArray()[0];
			linkedNodes.add(new Integer(thisNode.getId()));
			for(int i = 0; i < linkagePool.getIds().length; ++i)
			{
				ConceptualModelLinkage thisLinkage = linkagePool.find(linkagePool.getIds()[i]);
				if(thisLinkage.getToNodeId() == thisNode.getId())
				{
					ConceptualModelNode linkedNode = getNodePool().find(thisLinkage.getFromNodeId());
					unprocessedNodes.add(linkedNode);
				}
					
			}
			unprocessedNodes.remove(thisNode);
		}
		return linkedNodes;
	}

	private int[] intArrayFromSet(HashSet results)
	{
		int[] chainIds = new int[results.size()];
		int next = 0;
		Iterator iter = results.iterator();
		while(iter.hasNext())
		{
			Integer id = (Integer)iter.next();
			chainIds[next++] = id.intValue();
		}
		return chainIds;
	}

	public void moveNodes(int deltaX, int deltaY, int[] ids) throws Exception
	{
		for(int i = 0; i < ids.length; ++i)
		{
			int id = ids[i];
			DiagramNode nodeToMove = getNodeById(id);
			Point oldLocation = nodeToMove.getLocation();
			Point newLocation = new Point(oldLocation.x + deltaX, oldLocation.y + deltaY);
			Logging.logVerbose("moved Node from:"+ oldLocation +" to:"+ newLocation);
			nodeToMove.setLocation(newLocation);
			updateCell(nodeToMove);
		}
		
		nodesWereMoved(ids);
	}
	
	public void nodesWereMoved(int[] ids)
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
	
	public boolean hasNode(int id)
	{
		return (rawGetNodeById(id) != null);
	}
	
	public DiagramNode getNodeById(int id) throws Exception
	{
		DiagramNode node = rawGetNodeById(id);
		if(node == null)
			throw new Exception("Node doesn't exist, id: " + id);
		return node;
	}

	private DiagramNode rawGetNodeById(int id)
	{
		return cellInventory.getNodeById(id);
	}

	public DiagramLinkage getLinkageById(int id) throws Exception
	{
		DiagramLinkage linkage = cellInventory.getLinkageById(id);
		if(linkage == null)
			throw new Exception("Linkage doesn't exist, id: " + id);
		return linkage;
	}
	
	public boolean isNodeInProject(DiagramNode node)
	{
		return (cellInventory.getNodeById(node.getId()) != null);
	}

	public boolean isLinkageInProject(DiagramLinkage linkage)
	{
		return (cellInventory.getLinkageById(linkage.getId()) != null);
	}

	public Vector getAllNodes()
	{
		return cellInventory.getAllNodes();
	}
	
	public Vector getAllLinkages()
	{
		return cellInventory.getAllLinkages();
	}
	
	public Goal getGoalById(int id)
	{
		return getGoalPool().find(id);
	}
	
	public Objective getObjectiveById(int id)
	{
		return getObjectivePool().find(id);
	}
	
	public JSONObject toJson()
	{
		JSONObject nodeMap = new JSONObject();
		Vector nodes = getAllNodes();
		for(int i=0; i < nodes.size(); ++i)
		{
			DiagramNode node = (DiagramNode)nodes.get(i);
			nodeMap.put(Integer.toString(node.getId()), node.toJson());
		}
		JSONObject json = new JSONObject();
		json.put(TAG_TYPE, JSON_TYPE_DIAGRAM);
		json.put(TAG_NODES, nodeMap);
		return json;
	}
	
	public void fillFrom(JSONObject json) throws Exception
	{
		addNodesToModel(json);
		addLinkagesToModel();
	}

	private void addNodesToModel(JSONObject json) throws ParseException, Exception
	{
		JSONObject nodeMap = json.getJSONObject(TAG_NODES);
		JSONArray keys = nodeMap.names();
		
		// TODO: Really we should extend JSONObject to have a sane names() method
		// that returns an empty array if there are no names
		if(keys == null)
			return;
		for(int i=0; i < keys.length(); ++i)
		{
			String key = keys.getString(i);
			int id = Integer.parseInt(key);
			JSONObject nodeJson = nodeMap.getJSONObject(key);

			ConceptualModelNode cmObject = getNodePool().find(id);
			if(cmObject == null)
				EAM.logError("Attempted to wrap missing node: " + id);
			DiagramNode node = DiagramNode.wrapConceptualModelObject(cmObject);
			node.fillFrom(nodeJson);
			
			addNodeToModel(node);
		}
	}
	
	public void addLinkagesToModel() throws Exception
	{
		int[] linkageIds = getLinkagePool().getIds();
		for(int i = 0; i < linkageIds.length; ++i)
		{
			ConceptualModelLinkage cmLinkage = getLinkagePool().find(linkageIds[i]);
			if(hasNode(cmLinkage.getFromNodeId()) && hasNode(cmLinkage.getToNodeId()))
			{
				createLinkage(cmLinkage);
			}
		}
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
	
	
	private static final String TAG_TYPE = "Type";
	private static final String TAG_NODES = "Nodes";
	
	private static final String JSON_TYPE_DIAGRAM = "Diagram";
	
	Project project;
	CellInventory cellInventory;
	ProjectScopeBox projectScopeBox;
	protected List diagramModelListenerList = new ArrayList();
}

