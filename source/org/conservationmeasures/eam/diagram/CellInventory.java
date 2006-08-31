/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;

class CellInventory
{
	public CellInventory()
	{
		nodes = new Vector();
		linkages = new Vector();
	}
	
	public void clear()
	{
		nodes.clear();
		linkages.clear();
	}

	public void addNode(DiagramNode node)
	{
		BaseId realId = node.getDiagramNodeId();
		
		if(doesIdExist(realId))
			throw new RuntimeException("Can't add over existing id " + realId);
		
		nodes.add(node);
	}
	
	public Vector getAllNodes()
	{
		return nodes;
	}
	
	public DiagramNode getNodeById(BaseId id)
	{
		for (Iterator iter = nodes.iterator(); iter.hasNext();) 
		{
			DiagramNode node = (DiagramNode)iter.next();
			if(node.getDiagramNodeId().equals(id))
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
		BaseId realId = linkage.getDiagramLinkageId();
		
		if(doesIdExist(realId))
			throw new RuntimeException("Can't add over existing id " + realId);
		
		linkages.add(linkage);
	}

	private boolean doesIdExist(BaseId realId)
	{
		if(getNodeById(realId) != null)
			return true;
		if(getLinkageById(realId) != null)
			return true;
		
		return false;
	}
	
	public Vector getAllLinkages()
	{
		return linkages;
	}
	
	public DiagramLinkage getLinkageById(BaseId id)
	{
		for (Iterator iter = linkages.iterator(); iter.hasNext();) 
		{
			DiagramLinkage linkage = (DiagramLinkage) iter.next();
			if(linkage.getDiagramLinkageId().equals(id))
				return linkage;
		}
		return null;
	}
	
	public void removeLinkage(DiagramLinkage linkage)
	{
		linkages.remove(linkage);
	}
	
	Vector nodes;
	Vector linkages;
}
