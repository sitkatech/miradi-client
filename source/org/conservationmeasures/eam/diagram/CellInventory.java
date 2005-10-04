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
	
	void add(EAMGraphCell cell, int id)
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
	
	public int size()
	{
		return cells.size();
	}
	
	public EAMGraphCell getCellByIndex(int index)
	{
		return (EAMGraphCell)cells.get(index);
	}
	
	public Node getNodeByIndex(int index)
	{
		int currentIndex = -1;
		for(int i = 0; i < cells.size(); ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)cells.get(i);
			if(cell.isNode())
				++currentIndex;
			if(currentIndex == index)
				return (Node)cell;
		}
		return null;
	}
	
	public Linkage getLinkageByIndex(int index)
	{
		int currentIndex = -1;
		for(int i = 0; i < cells.size(); ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)cells.get(i);
			if(cell.isLinkage())
				++currentIndex;
			if(currentIndex == index)
				return (Linkage)cell;
		}
		return null;
	}
	
	public EAMGraphCell getById(int id)
	{
		for (Iterator iter = cells.iterator(); iter.hasNext();) 
		{
			EAMGraphCell cell = (EAMGraphCell) iter.next();
			if(cell.getId() == id)
				return cell;
		}
		return null;
	}
	
	public void remove(EAMGraphCell cell)
	{
		cells.remove(cell);
	}
	
	public void clear()
	{
		nextId = 0;
		cells.clear();
	}

	private int nextId;
	Vector cells;
}