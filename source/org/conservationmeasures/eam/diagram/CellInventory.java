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
import org.conservationmeasures.eam.ids.DiagramLinkageId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;

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
		DiagramNodeId realId = node.getDiagramNodeId();
		
		if(getNodeById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		nodes.add(node);
	}
	
	public Vector getAllNodes()
	{
		return nodes;
	}
	
	public DiagramNode getNodeById(DiagramNodeId id)
	{
		for (Iterator iter = nodes.iterator(); iter.hasNext();) 
		{
			DiagramNode node = (DiagramNode)iter.next();
			if(node.getDiagramNodeId().equals(id))
				return node;
		}
		return null;
	}
	
	public DiagramNode getNodeById(ModelNodeId id)
	{
		for (Iterator iter = nodes.iterator(); iter.hasNext();) 
		{
			DiagramNode node = (DiagramNode)iter.next();
			if(node.getWrappedId().equals(id))
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
		DiagramLinkageId realId = linkage.getDiagramLinkageId();
		
		if(getLinkageById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		linkages.add(linkage);
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
