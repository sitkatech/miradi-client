/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objects.DiagramFactorLink;

class CellInventory
{
	public CellInventory()
	{
		factorLinks = new HashMap();
		factorCellIds = new HashMap();
	}
	
	public void clear()
	{
		factorLinks.clear();
		factorCellIds.clear();
	}

	public void addFactor(FactorCell node)
	{
		DiagramFactorId realId = node.getDiagramFactorId();
		
		if(getFactorById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		factorCellIds.put(realId, node);
	}
	
	public Vector getAllFactors()
	{
		return new Vector(factorCellIds.values());
	}
	
	public FactorCell getFactorById(DiagramFactorId id)
	{
		return (FactorCell) factorCellIds.get(id);
	}
	
	public FactorCell getFactorById(FactorId id)
	{
		Collection list = factorCellIds.values();
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			FactorCell cell = (FactorCell) iter.next();
			if(cell.getWrappedId().equals(id))
				return cell;
		}
		
		return null;
	}
	
	public void removeFactor(DiagramFactorId diagramFactorId)
	{
		factorCellIds.remove(diagramFactorId);
	}
	
	public void addFactorLink(DiagramFactorLink link, LinkCell cell)
	{
		DiagramFactorLinkId realId = link.getDiagramLinkageId();
		
		if(getFactorLinkById(realId) != null)
			throw new RuntimeException("Can't add over existing id " + realId);
		
		factorLinks.put(link, cell);
	}

	public Vector getAllFactorLinkCells()
	{
		return new Vector(factorLinks.values());
	}
	
	public Vector getAllFactorLinks()
	{
		return new Vector(factorLinks.keySet());
	}
	
	public DiagramFactorLink getFactorLinkById(DiagramFactorLinkId id)
	{
		Iterator iter = factorLinks.keySet().iterator();
		while(iter.hasNext()) 
		{
			DiagramFactorLink link = (DiagramFactorLink) iter.next();
			if(link.getDiagramLinkageId().equals(id))
				return link;
		}
		return null;
	}
	
	public DiagramFactorLink getFactorLinkById(FactorLinkId id)
	{
		Iterator iter = factorLinks.keySet().iterator();
		while(iter.hasNext())
		{
			DiagramFactorLink link = (DiagramFactorLink) iter.next();
			if(link.getWrappedId().equals(id))
				return link;
		}
		return null;
	}
	
	public LinkCell getLinkCell(DiagramFactorLink link)
	{
		return (LinkCell)factorLinks.get(link);
	}
	
	public void removeFactorLink(DiagramFactorLink linkage)
	{
		factorLinks.remove(linkage);
	}
	
	private HashMap factorLinks;
	private HashMap factorCellIds;
}
