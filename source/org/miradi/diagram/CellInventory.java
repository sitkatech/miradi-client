/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.diagram;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramFactorLinkId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.objects.DiagramLink;

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
	
	public void addFactorLink(DiagramLink link, LinkCell cell)
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
	
	public DiagramLink getFactorLinkById(DiagramFactorLinkId id)
	{
		Iterator iter = factorLinks.keySet().iterator();
		while(iter.hasNext()) 
		{
			DiagramLink link = (DiagramLink) iter.next();
			if(link.getDiagramLinkageId().equals(id))
				return link;
		}
		return null;
	}
	
	public DiagramLink getFactorLinkById(FactorLinkId id)
	{
		Iterator iter = factorLinks.keySet().iterator();
		while(iter.hasNext())
		{
			DiagramLink link = (DiagramLink) iter.next();
			if(link.getWrappedId().equals(id))
				return link;
		}
		return null;
	}
	
	public LinkCell getLinkCell(DiagramLink link)
	{
		return (LinkCell)factorLinks.get(link);
	}
	
	public void removeFactorLink(DiagramLink linkage)
	{
		factorLinks.remove(linkage);
	}
	
	private HashMap factorLinks;
	private HashMap factorCellIds;
}
