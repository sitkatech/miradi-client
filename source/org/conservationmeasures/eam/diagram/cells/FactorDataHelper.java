/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;

public class FactorDataHelper 
{
	public FactorDataHelper(DiagramFactorId[] diagramFactorIds)
	{
		setInitialMappingOfIdsToOriginalIds(diagramFactorIds);
	}

	public void setNewId(DiagramFactorId originalNodeId, DiagramFactorId newNodeId)
	{
		mapNodeIds.put(getKey(originalNodeId), getValue(newNodeId));
	}
	
	public DiagramFactorId getNewId(DiagramFactorId originalNodeId)
	{
		Integer newNodeId = (Integer)mapNodeIds.get(getKey(originalNodeId));
		if(newNodeId == null)
			return new DiagramFactorId(BaseId.INVALID.asInt());
		return new DiagramFactorId(newNodeId.intValue());
	}
	
	public void setOriginalLocation(DiagramFactorId originalNodeId, Point originalLocation)
	{
		mapNodeLocations.put(getKey(originalNodeId), originalLocation);
	}
	
	public Point getNewLocation(DiagramFactorId originalNodeId, Point insertionPoint)
	{
		if (insertionPoint == null)
			insertionPoint = getLeftmostUppermostCorner();
		
		Point delta = computeDeltas(insertionPoint);
		Point originalNodeLocation = (Point)mapNodeLocations.get(getKey(originalNodeId));
		int originalX = originalNodeLocation.x;
		int originalY = originalNodeLocation.y;
		return new Point(originalX + delta.x, originalY + delta.y);
	}

	private void setInitialMappingOfIdsToOriginalIds(DiagramFactorId[] diagramFactorIds) 
	{
		for (int i = 0; i < diagramFactorIds.length; i++)
		{
			setNewId(diagramFactorIds[i], diagramFactorIds[i]);
		}
	}
	
	private Integer getKey(BaseId key)
	{
		return getKey(key.asInt());
	}

	private Integer getKey(int key) 
	{
		return new Integer(key);
	}
	
	private Integer getValue(BaseId value)
	{
		return getValue(value.asInt());
	}
	
	private Integer getValue(int value) 
	{
		return new Integer(value);
	}
	
	private Point computeDeltas(Point insertionPoint)
	{
		Point upperLeft = getLeftmostUppermostCorner();
		
		int deltaX = insertionPoint.x - upperLeft.x;
		int deltaY = insertionPoint.y - upperLeft.y;
		return new Point(deltaX, deltaY);	
	}

	public Point getLeftmostUppermostCorner()
	{
		Rectangle rect = null;
		for (Iterator iter = mapNodeLocations.values().iterator(); iter.hasNext();) 
		{
			Point nodeLocation = (Point) iter.next();
			if(rect==null)
				rect = new Rectangle(nodeLocation);
			else
				rect.add(nodeLocation);
		}
		
		Point upperLeft = rect.getLocation();
		return upperLeft;
	}
	
	HashMap mapNodeLocations = new HashMap();
	HashMap mapNodeIds = new HashMap();
}
