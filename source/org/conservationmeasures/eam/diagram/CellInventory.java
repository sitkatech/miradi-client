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
		nodes = new Vector();
		linkages = new Vector();
	}
	
	public void clear()
	{
		nextId = 0;
		nodes.clear();
		linkages.clear();
	}

	public void addNode(Node nodeWithoutId, int forceId)
	{
		forceId = getRealId(forceId);
		
		if(getById(forceId) != null)
			throw new RuntimeException("Can't add over existing id " + forceId);
		
		nodeWithoutId.setId(forceId);
		nodes.add(nodeWithoutId);
	}
	
	public Vector getAllNodes()
	{
		return nodes;
	}
	
	public Node getNodeById(int id)
	{
		for (Iterator iter = nodes.iterator(); iter.hasNext();) 
		{
			Node node = (Node)iter.next();
			if(node.getId() == id)
				return node;
		}
		return null;
	}
	
	public void removeNode(Node node)
	{
		nodes.remove(node);
	}
	
	public void addLinkage(Linkage linkageWithoutId, int forceId)
	{
		forceId = getRealId(forceId);
		
		if(getById(forceId) != null)
			throw new RuntimeException("Can't add over existing id " + forceId);
		
		linkageWithoutId.setId(forceId);
		linkages.add(linkageWithoutId);
	}
	
	public Vector getAllLinkages()
	{
		return linkages;
	}
	
	public Linkage getLinkageById(int id)
	{
		for (Iterator iter = linkages.iterator(); iter.hasNext();) 
		{
			Linkage linkage = (Linkage) iter.next();
			if(linkage.getId() == id)
				return linkage;
		}
		return null;
	}
	
	public void removeLinkage(Linkage linkage)
	{
		linkages.remove(linkage);
	}
	
	private EAMGraphCell getById(int id)
	{
		for (Iterator iter = nodes.iterator(); iter.hasNext();) 
		{
			EAMGraphCell cell = (EAMGraphCell) iter.next();
			if(cell.getId() == id)
				return cell;
		}
		return null;
	}
	
	private int getRealId(int id)
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
		return id;
	}
	
	private int nextId;
	Vector nodes;
	Vector linkages;
}
