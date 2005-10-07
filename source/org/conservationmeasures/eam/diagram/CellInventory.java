/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;

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

	public void addNode(DiagramNode node)
	{
		int realId = getRealId(node.getId());
		
		if(getById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		node.setId(realId);
		nodes.add(node);
	}
	
	public Vector getAllNodes()
	{
		return nodes;
	}
	
	public DiagramNode getNodeById(int id)
	{
		for (Iterator iter = nodes.iterator(); iter.hasNext();) 
		{
			DiagramNode node = (DiagramNode)iter.next();
			if(node.getId() == id)
				return node;
		}
		return null;
	}
	
	public void removeNode(DiagramNode node)
	{
		nodes.remove(node);
	}
	
	public void addLinkage(DiagramLinkage linkage)
	{
		int realId = getRealId(linkage.getId());
		
		if(getById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		linkage.setId(realId);
		linkages.add(linkage);
	}
	
	public Vector getAllLinkages()
	{
		return linkages;
	}
	
	public DiagramLinkage getLinkageById(int id)
	{
		for (Iterator iter = linkages.iterator(); iter.hasNext();) 
		{
			DiagramLinkage linkage = (DiagramLinkage) iter.next();
			if(linkage.getId() == id)
				return linkage;
		}
		return null;
	}
	
	public void removeLinkage(DiagramLinkage linkage)
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
		if(id == DiagramNode.INVALID_ID)
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
