/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.jgraph.graph.ConnectionSet;
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
	}

	public Node createNode(int nodeType) throws Exception
	{
		return createNodeAtId(nodeType, Node.INVALID_ID);
	}
	
	public Node createNodeAtId(int nodeType, int id) throws Exception
	{
		Node node = new Node(nodeType);
		Object[] nodes = new Object[] {node};
		Hashtable nestedAttributeMap = node.getNestedAttributeMap();
		insert(nodes, nestedAttributeMap, null, null, null);
		cellInventory.add(node, id);
		notifyListeners(createDiagramModelEvent(node), new ModelEventNotifierNodeAdded());
		return node;
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
	
    public void deleteNode(Node nodeToDelete) throws Exception
	{
		notifyListeners(createDiagramModelEvent(nodeToDelete), new ModelEventNotifierNodeDeleted());

		Object[] nodes = new Object[]{nodeToDelete};
		remove(nodes);
		cellInventory.remove(nodeToDelete);
	}
	
	public Linkage createLinkage(int linkageId, int linkFromId, int linkToId) throws Exception
	{
		Node fromNode = getNodeById(linkFromId);
		Node toNode = getNodeById(linkToId);

		Linkage linkage = new Linkage(fromNode, toNode);
		Object[] linkages = new Object[]{linkage};
		Map nestedMap = linkage.getNestedAttributeMap();
		ConnectionSet cs = linkage.getConnectionSet();
		insert(linkages, nestedMap, cs, null, null);
		cellInventory.add(linkage, linkageId);
		notifyListeners(createDiagramModelEvent(linkage), new ModelEventNotifierLinkageAdded());
		
		return linkage;
	}
	
	public void deleteLinkage(Linkage linkageToDelete) throws Exception
	{
		notifyListeners(createDiagramModelEvent(linkageToDelete), new ModelEventNotifierLinkageDeleted());
		Object[] linkages = new Object[]{linkageToDelete};
		remove(linkages);
		cellInventory.remove(linkageToDelete);
	}
	
	public boolean hasLinkage(Node fromNode, Node toNode)
	{
		for(int i=0; i < cellInventory.size(); ++i)
		{
			EAMGraphCell cell = cellInventory.getCellByIndex(i);
			if(!cell.isLinkage())
				continue;
			
			Linkage linkage = (Linkage)cell;
			Node thisFromNode = linkage.getFromNode();
			Node thisToNode = linkage.getToNode();
			if(thisFromNode.equals(fromNode) && thisToNode.equals(toNode))
				return true;
			
			if(thisFromNode.equals(toNode) && thisToNode.equals(fromNode))
				return true;
		}
		
		return false;
	}
	
	public int getCellCount()
	{
		return cellInventory.size();
	}
	
	public int getNodeCount()
	{
		return getAllNodes().size();
	}
	
	public int getLinkageCount()
	{
		return getAllLinkages().size();
	}

	public Set getLinkages(Node node)
	{
		return getEdges(this, new Object[] {node});
	}
	
	public void updateCell(EAMGraphCell nodeToUpdate) throws Exception
	{
		edit(nodeToUpdate.getNestedAttributeMap(), null, null, null);
		notifyListeners(createDiagramModelEvent(nodeToUpdate), new ModelEventNotifierNodeChanged());
	}
	
	public Node getNodeById(int id) throws Exception
	{
		return (Node)getCellById(id);
	}

	public Linkage getLinkageById(int id) throws Exception
	{
		return (Linkage)getCellById(id);
	}
	
	public boolean isCellInProject(EAMGraphCell cell)
	{
		try
		{
			getCellById(cell.getId());
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public EAMGraphCell getCellById(int id) throws Exception
	{
		EAMGraphCell cell = cellInventory.getById(id);
		if(cell == null)
			throw new Exception("Cell doesn't exist, id: " + id);
		return cell;
	}
	
	public Node getNodeByIndex(int index) throws Exception
	{
		Node cell = cellInventory.getNodeByIndex(index);
		if(cell == null)
			throw new Exception("Cell doesn't exist, index: " + index);
		return cell;
	}
	
	public Linkage getLinkageByIndex(int index) throws Exception
	{
		Linkage linkage = cellInventory.getLinkageByIndex(index);
		if(linkage == null)
			throw new Exception("Linkage doesn't exist, index: " + index);
		return linkage;
		
	}

	public Vector getAllNodes()
	{
		Vector nodes = new Vector();
		for(int i=0; i < cellInventory.size(); ++i)
		{
			EAMGraphCell cell = cellInventory.getCellByIndex(i);
			if(cell.isNode())
				nodes.add(cell);
		}	
		return nodes;
	}
	
	public Vector getAllLinkages()
	{
		Vector linkages = new Vector();
		for(int i=0; i < cellInventory.size(); ++i)
		{
			EAMGraphCell cell = cellInventory.getCellByIndex(i);
			if(cell.isLinkage())
				linkages.add(cell);
		}	
		return linkages;
	}
	

	CellInventory cellInventory;
	protected List diagramModelListenerList = new ArrayList();
}

