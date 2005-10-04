/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;

class CellInventory
{
	public CellInventory()
	{
		cells = new Vector();
	}
	
	public void clear()
	{
		nextId = 0;
		cells.clear();
	}

	public void addNode(Node nodeWithoutId, int forceId)
	{
		addCell(nodeWithoutId, forceId);
	}
	
	public Vector getAllNodes()
	{
		Vector nodes = new Vector();
		for(int i=0; i < size(); ++i)
		{
			EAMGraphCell cell = getCellByIndex(i);
			if(cell.isNode())
				nodes.add(cell);
		}	
		return nodes;
	}
	
	public Node getNodeById(int id)
	{
		return (Node)getById(id);
	}
	
	public void removeNode(Node node)
	{
		cells.remove(node);
	}
	
	public void addLinkage(Linkage linkageWithoutId, int forceId)
	{
		addCell(linkageWithoutId, forceId);
	}
	
	public Vector getAllLinkages()
	{
		Vector linkages = new Vector();
		for(int i=0; i < size(); ++i)
		{
			EAMGraphCell cell = getCellByIndex(i);
			if(cell.isLinkage())
				linkages.add(cell);
		}	
		return linkages;
	}
	
	public Linkage getLinkageById(int id)
	{
		return (Linkage)getById(id);
	}
	
	public void removeLinkage(Linkage linkage)
	{
		cells.remove(linkage);
	}
	
	private EAMGraphCell getById(int id)
	{
		for (Iterator iter = cells.iterator(); iter.hasNext();) 
		{
			EAMGraphCell cell = (EAMGraphCell) iter.next();
			if(cell.getId() == id)
				return cell;
		}
		return null;
	}
	
	private void addCell(EAMGraphCell cell, int id)
	{
		if(id == Node.INVALID_ID)
		{
			id = nextId++;
		}
		else
		{
			if(nextId < id+1)
				nextId = id + 1;
		}
		
		if(getById(id) != null)
			throw new RuntimeException("Can't add over existing id " + id);
		
		cell.setId(id);
		cells.add(cell);
	}
	
	private int size()
	{
		return cells.size();
	}
	
	private EAMGraphCell getCellByIndex(int index)
	{
		return (EAMGraphCell)cells.get(index);
	}
	
	private int nextId;
	Vector cells;
}
