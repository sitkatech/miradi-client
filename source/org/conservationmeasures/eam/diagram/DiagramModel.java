/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

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

	public Node createNode(int nodeType)
	{
		Node node = new Node(nodeType);
		insertNode(node);
		return node;
	}
	
	public void deleteNode(Node nodeToDelete)
	{
		Object[] nodes = new Object[]{nodeToDelete};
		remove(nodes);
		cellInventory.remove(nodeToDelete);
	}
	
	public Linkage createLinkage(Node fromNode, Node toNode)
	{
		Linkage linkage = new Linkage(fromNode, toNode);
		insertLinkage(linkage);
		return linkage;
	}
	
	public void deleteLinkage(Linkage linkageToDelete)
	{
		Object[] linkages = new Object[]{linkageToDelete};
		remove(linkages);
		cellInventory.remove(linkageToDelete);
	}
	
	private void insertLinkage(Linkage linkageToInsert)
	{
		Object[] linkages = new Object[]{linkageToInsert};
		Map nestedMap = linkageToInsert.getNestedAttributeMap();
		ConnectionSet cs = linkageToInsert.getConnectionSet();
		insert(linkages, nestedMap, cs, null, null);
		cellInventory.add(linkageToInsert);
	}
	
	public boolean hasLinkage(Node fromNode, Node toNode)
	{
		for(int i=0; i < cellInventory.size(); ++i)
		{
			EAMGraphCell cell = cellInventory.getByIndex(i);
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
	
	public Set getLinkages(Node node)
	{
		return getEdges(this, new Object[] {node});
	}
	
	private void insertNode(Node nodeToInsert)
	{
		Object[] nodes = new Object[] {nodeToInsert};
		Hashtable nestedAttributeMap = nodeToInsert.getNestedAttributeMap();
		insert(nodes, nestedAttributeMap, null, null, null);
		cellInventory.add(nodeToInsert);
	}
	
	public void updateCell(EAMGraphCell nodeToUpdate)
	{
		edit(nodeToUpdate.getNestedAttributeMap(), null, null, null);
	}
	
	public boolean isNode(EAMGraphCell cell)
	{
		return cell.isNode();
	}
	
	public int getCellId(EAMGraphCell cell)
	{
		return cellInventory.find(cell);
	}

	public int getNodeId(Node node)
	{
		return getCellId(node);
	}
	
	public int getLinkageId(Linkage linkage)
	{
		return getCellId(linkage);
	}
	
	public EAMGraphCell getCellById(int id) throws Exception
	{
		EAMGraphCell cell = cellInventory.getById(id);
		if(cell == null)
			throw new Exception("Cell doesn't exist, id: " + id);
		return cell;
	}

	public Node getNodeById(int id) throws Exception
	{
		return (Node)getCellById(id);
	}

	public Linkage getLinkageById(int id) throws Exception
	{
		return (Linkage)getCellById(id);
	}

	CellInventory cellInventory;
}

class CellInventory
{
	public CellInventory()
	{
		idToCellMap = new HashMap();
		cellToIdMap = new HashMap();
	}
	
	public void add(EAMGraphCell cell)
	{
		Integer id = new Integer(nextId++);
		idToCellMap.put(id, cell);
		cellToIdMap.put(cell, id);
	}
	
	public int size()
	{
		return idToCellMap.size();
	}
	
	public EAMGraphCell getByIndex(int index)
	{
		Integer[] keys = (Integer[])idToCellMap.keySet().toArray(new Integer[0]);
		EAMGraphCell foundCell = (EAMGraphCell)idToCellMap.get(keys[index]);
		if(foundCell == null)
			throw new RuntimeException("Cell not found id: " + index);
		return foundCell;
	}
	
	public EAMGraphCell getById(int id)
	{
		return (EAMGraphCell)idToCellMap.get(new Integer(id));
	}
	
	public int find(EAMGraphCell cell)
	{
		Integer found = ((Integer)cellToIdMap.get(cell));
		if(found == null)
			return -1;
		return found.intValue();
	}
	
	public void remove(EAMGraphCell cell)
	{
		idToCellMap.remove(new Integer(find(cell)));
		cellToIdMap.remove(cell);
	}
	
	public void clear()
	{
		nextId = 0;
		idToCellMap.clear();
		cellToIdMap.clear();
	}

	int nextId;
	Map idToCellMap;
	Map cellToIdMap;
}
