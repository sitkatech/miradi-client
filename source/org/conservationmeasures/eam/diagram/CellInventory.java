/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;

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

	public void addNode(DiagramFactor node)
	{
		DiagramFactorId realId = node.getDiagramNodeId();
		
		if(getNodeById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		nodes.add(node);
	}
	
	public Vector getAllNodes()
	{
		return nodes;
	}
	
	public DiagramFactor getNodeById(DiagramFactorId id)
	{
		for (Iterator iter = nodes.iterator(); iter.hasNext();) 
		{
			DiagramFactor node = (DiagramFactor)iter.next();
			if(node.getDiagramNodeId().equals(id))
				return node;
		}
		return null;
	}
	
	public DiagramFactor getNodeById(FactorId id)
	{
		for (Iterator iter = nodes.iterator(); iter.hasNext();) 
		{
			DiagramFactor node = (DiagramFactor)iter.next();
			if(node.getWrappedId().equals(id))
				return node;
		}
		return null;
	}
	
	public void removeNode(DiagramFactor node)
	{
		nodes.remove(node);
	}
	
	public void addLinkage(DiagramFactorLink linkage)
	{
		DiagramFactorLinkId realId = linkage.getDiagramLinkageId();
		
		if(getLinkageById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		linkages.add(linkage);
	}

	public Vector getAllLinkages()
	{
		return linkages;
	}
	
	public DiagramFactorLink getLinkageById(DiagramFactorLinkId id)
	{
		for (Iterator iter = linkages.iterator(); iter.hasNext();) 
		{
			DiagramFactorLink linkage = (DiagramFactorLink) iter.next();
			if(linkage.getDiagramLinkageId().equals(id))
				return linkage;
		}
		return null;
	}
	
	public DiagramFactorLink getLinkageById(FactorLinkId id)
	{
		for (Iterator iter = linkages.iterator(); iter.hasNext();) 
		{
			DiagramFactorLink linkage = (DiagramFactorLink) iter.next();
			if(linkage.getWrappedId().equals(id))
				return linkage;
		}
		return null;
	}
	
	public void removeLinkage(DiagramFactorLink linkage)
	{
		linkages.remove(linkage);
	}
	
	Vector nodes;
	Vector linkages;
}
