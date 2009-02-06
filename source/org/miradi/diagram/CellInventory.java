/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;

class CellInventory
{
	public CellInventory()
	{
		diagramLinkToCellMap = new HashMap<DiagramLink, LinkCell>();
		diagramFactorRefToCellMap = new HashMap<ORef, FactorCell>();
	}
	
	public void clear()
	{
		diagramLinkToCellMap.clear();
		diagramFactorRefToCellMap.clear();
	}

	public void addFactor(FactorCell factorCell)
	{
		ORef diagramFactorRef = factorCell.getDiagramFactorRef();
		if(getFactorCellByDiagramFactorRef(diagramFactorRef) != null)
			throw new RuntimeException("Can't add over existing ref " + diagramFactorRef);
		
		diagramFactorRefToCellMap.put(factorCell.getDiagramFactorRef(), factorCell);
	}
	
	public Vector getAllFactors()
	{
		return new Vector(diagramFactorRefToCellMap.values());
	}
	
	public FactorCell getFactorCellByDiagramFactorRef(ORef diagramFactorRef)
	{
		diagramFactorRef.ensureType(DiagramFactor.getObjectType());
		return diagramFactorRefToCellMap.get(diagramFactorRef);
	}
	
	public FactorCell getFactorByRef(ORef factorRef)
	{
		Collection list = diagramFactorRefToCellMap.values();
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			FactorCell cell = (FactorCell) iter.next();
			if(cell.getWrappedFactorRef().equals(factorRef))
				return cell;
		}
		
		return null;
	}
	
	public void removeFactor(ORef diagramFactorRef)
	{
		diagramFactorRefToCellMap.remove(diagramFactorRef);
	}
	
	public void addFactorLink(DiagramLink link, LinkCell cell)
	{
		ORef diagramLinkRef = link.getRef();	
		if(getDiagramLink(diagramLinkRef) != null)
			throw new RuntimeException("Can't add over existing ref " + diagramLinkRef);
		
		diagramLinkToCellMap.put(link, cell);
	}

	public Vector getAllFactorLinkCells()
	{
		return new Vector(diagramLinkToCellMap.values());
	}
	
	public Vector getAllFactorLinks()
	{
		return new Vector(diagramLinkToCellMap.keySet());
	}
	
	public DiagramLink getDiagramLink(ORef diagramLinkRef)
	{
		Iterator iter = diagramLinkToCellMap.keySet().iterator();
		while(iter.hasNext()) 
		{
			DiagramLink diagramLink = (DiagramLink) iter.next();
			if(diagramLink.getRef().equals(diagramLinkRef))
				return diagramLink;
		}
		
		return null;
	}
	
	public DiagramLink getFactorLinkByRef(ORef factorLinkRef)
	{
		Iterator iter = diagramLinkToCellMap.keySet().iterator();
		while(iter.hasNext())
		{
			DiagramLink diagramLink = (DiagramLink) iter.next();
			if(diagramLink.getWrappedRef().equals(factorLinkRef))
				return diagramLink;
		}
		return null;
	}
	
	public LinkCell getLinkCell(DiagramLink diagramLink)
	{
		return diagramLinkToCellMap.get(diagramLink);
	}
	
	public void removeFactorLink(DiagramLink diagramLink)
	{
		diagramLinkToCellMap.remove(diagramLink);
	}
	
	private HashMap<DiagramLink, LinkCell> diagramLinkToCellMap;
	private HashMap<ORef, FactorCell> diagramFactorRefToCellMap;
}
