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

import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.GoalPool;
import org.conservationmeasures.eam.annotations.Objective;
import org.conservationmeasures.eam.annotations.ObjectivePool;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.NodePool;
import org.conservationmeasures.eam.utils.Logging;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class DiagramModel extends DefaultGraphModel
{
	public DiagramModel(NodePool nodePoolToUse, GoalPool goalPoolToUse, ObjectivePool objectivePoolToUse)
	{
		this(nodePoolToUse);
		goalPool = goalPoolToUse;
		objectivePool = objectivePoolToUse;
	}
	
	public DiagramModel(NodePool objectPoolToUse)
	{
		objectPool = objectPoolToUse;
		cellInventory = new CellInventory();
	}
	
	public void clear()
	{
		while(getRootCount() > 0)
			remove(new Object[] {getRootAt(0)});
		cellInventory.clear();
		projectScopeBox = new ProjectScopeBox(this);
		insertCellIntoGraph(projectScopeBox);
	}
	
	public ProjectScopeBox getProjectScopeBox()
	{
		return projectScopeBox;
	}

	public DiagramNode createNode(int id) throws Exception
	{
		ConceptualModelNode cmObject = objectPool.find(id);
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
		Vector allLinkages = cellInventory.getAllLinkages();
		for(int i=0; i < allLinkages.size(); ++i)
		{
			DiagramLinkage linkage = (DiagramLinkage)allLinkages.get(i);
			DiagramNode thisFromNode = linkage.getFromNode();
			DiagramNode thisToNode = linkage.getToNode();
			if(thisFromNode.equals(fromNode) && thisToNode.equals(toNode))
				return true;
			
			if(thisFromNode.equals(toNode) && thisToNode.equals(fromNode))
				return true;
		}
		
		return false;
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
	
	public DiagramNode getNodeById(int id) throws Exception
	{
		DiagramNode node = cellInventory.getNodeById(id);
		if(node == null)
			throw new Exception("Node doesn't exist, id: " + id);
		return node;
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
		return goalPool.find(id);
	}
	
	public Objective getObjectiveById(int id)
	{
		return objectivePool.find(id);
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
		JSONObject nodeMap = json.getJSONObject(TAG_NODES);
		JSONArray keys = nodeMap.names();
		for(int i=0; i < keys.length(); ++i)
		{
			String key = keys.getString(i);
			int id = Integer.parseInt(key);
			JSONObject nodeJson = nodeMap.getJSONObject(key);

			ConceptualModelNode cmObject = objectPool.find(id);
			DiagramNode node = DiagramNode.wrapConceptualModelObject(cmObject);
			node.fillFrom(nodeJson);
			
			addNodeToModel(node);
		}
	}
	
	private static final String TAG_TYPE = "Type";
	private static final String TAG_NODES = "Nodes";
	
	private static final String JSON_TYPE_DIAGRAM = "Diagram";
	
	NodePool objectPool;
	GoalPool goalPool;
	ObjectivePool objectivePool;
	CellInventory cellInventory;
	ProjectScopeBox projectScopeBox;
	protected List diagramModelListenerList = new ArrayList();
}

