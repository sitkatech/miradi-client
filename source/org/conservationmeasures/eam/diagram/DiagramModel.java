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

import org.conservationmeasures.eam.diagram.nodes.ConceptualModelObject;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.types.NodeType;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;

public class DiagramModel extends DefaultGraphModel
{
	public DiagramModel()
	{
		cellInventory = new CellInventory();
	}
	
	public void clear()
	{
		while(getRootCount() > 0)
			remove(new Object[] {getRootAt(0)});
		cellInventory.clear();
		projectScopeBox = new ProjectScopeBox(this);
		insertCell(projectScopeBox);
	}
	
	public ProjectScopeBox getProjectScopeBox()
	{
		return projectScopeBox;
	}

	// FIXME: ONLY USED FOR TESTING--MOVE OR DELETE
	public DiagramNode createNode(NodeType nodeType) throws Exception
	{
		ConceptualModelObject cmObject = Project.createConceptualModelObject(nodeType);
		return createNodeAtId(cmObject, DiagramNode.INVALID_ID);
	}

	// FIXME: ONLY USED FOR TESTING--MOVE OR DELETE
	public DiagramNode createNodeAtId(NodeType nodeType, int id) throws Exception
	{
		DiagramNode node = createNode(nodeType);
		node.setId(id);
		return node;
	}
	
	public DiagramNode createNodeAtId(ConceptualModelObject cmObject, int id) throws Exception
	{
		DiagramNode node = DiagramNode.wrapConceptualModelObject(cmObject);
		node.setId(id);
		insertCell(node);
		cellInventory.addNode(node);
		notifyListeners(createDiagramModelEvent(node), new ModelEventNotifierNodeAdded());
		return node;
	}

	private void insertCell(DefaultGraphCell cell)
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
	
	public DiagramLinkage createLinkage(int linkageId, int linkFromId, int linkToId) throws Exception
	{
		DiagramNode fromNode = getNodeById(linkFromId);
		DiagramNode toNode = getNodeById(linkToId);

		DiagramLinkage linkage = new DiagramLinkage(fromNode, toNode);
		linkage.setId(linkageId);
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
	
	public boolean hasLinkage(DiagramNode fromNode, DiagramNode toNode)
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
	
	CellInventory cellInventory;
	ProjectScopeBox projectScopeBox;
	protected List diagramModelListenerList = new ArrayList();
}

