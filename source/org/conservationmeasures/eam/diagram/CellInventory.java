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
		factors = new Vector();
		factorLinks = new Vector();
	}
	
	public void clear()
	{
		factors.clear();
		factorLinks.clear();
	}

	public void addFactor(DiagramFactor node)
	{
		DiagramFactorId realId = node.getDiagramFactorId();
		
		if(getFactorById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		factors.add(node);
	}
	
	public Vector getAllFactors()
	{
		return factors;
	}
	
	public DiagramFactor getFactorById(DiagramFactorId id)
	{
		for (Iterator iter = factors.iterator(); iter.hasNext();) 
		{
			DiagramFactor factor = (DiagramFactor)iter.next();
			if(factor.getDiagramFactorId().equals(id))
				return factor;
		}
		return null;
	}
	
	public DiagramFactor getFactorById(FactorId id)
	{
		for (Iterator iter = factors.iterator(); iter.hasNext();) 
		{
			DiagramFactor factor = (DiagramFactor)iter.next();
			if(factor.getWrappedId().equals(id))
				return factor;
		}
		return null;
	}
	
	public void removeFactor(DiagramFactor node)
	{
		factors.remove(node);
	}
	
	public void addFactorLink(DiagramFactorLink linkage)
	{
		DiagramFactorLinkId realId = linkage.getDiagramLinkageId();
		
		if(getFactorLinkById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		factorLinks.add(linkage);
	}

	public Vector getAllFactorLinks()
	{
		return factorLinks;
	}
	
	public DiagramFactorLink getFactorLinkById(DiagramFactorLinkId id)
	{
		for (Iterator iter = factorLinks.iterator(); iter.hasNext();) 
		{
			DiagramFactorLink link = (DiagramFactorLink) iter.next();
			if(link.getDiagramLinkageId().equals(id))
				return link;
		}
		return null;
	}
	
	public DiagramFactorLink getFactorLinkById(FactorLinkId id)
	{
		for (Iterator iter = factorLinks.iterator(); iter.hasNext();) 
		{
			DiagramFactorLink link = (DiagramFactorLink) iter.next();
			if(link.getWrappedId().equals(id))
				return link;
		}
		return null;
	}
	
	public void removeFactorLink(DiagramFactorLink linkage)
	{
		factorLinks.remove(linkage);
	}
	
	Vector factors;
	Vector factorLinks;
}
