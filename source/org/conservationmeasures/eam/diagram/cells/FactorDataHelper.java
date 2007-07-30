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

//TODO come up with a better name
public class FactorDataHelper 
{
	public FactorDataHelper(DiagramFactorId[] diagramFactorIds, Point insertioPointToUse)
	{
		setInitialMappingOfIdsToOriginalIds(diagramFactorIds);
		insertionPoint = insertioPointToUse;
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

	public Point getNewLocation(Point originalLocation)
	{
		Point validatedInsertionPoint = getValidatedInsertionPoint(insertionPoint);
		return getLocation(originalLocation, validatedInsertionPoint);
	}

	public Point getNewLocation(DiagramFactorId originalNodeId)
	{
		Point validatednsertionPoint = getValidatedInsertionPoint(insertionPoint);
		Point originalNodeLocation = (Point)mapNodeLocations.get(getKey(originalNodeId));
		
		return getLocation(originalNodeLocation, validatednsertionPoint);
	}
	
	private Point getValidatedInsertionPoint(Point insertionPointToValidate)
	{
		if (insertionPointToValidate == null)
			return  getLeftmostUppermostCorner();
		
		return insertionPointToValidate;
	}

	private Point getLocation(Point originalLocation, Point point)
	{
		Point delta = computeDeltas(point);
		int originalX = originalLocation.x;
		int originalY = originalLocation.y;
		
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
	
	public Point computeDeltas(Point point)
	{
		Point upperLeft = getLeftmostUppermostCorner();
		if (upperLeft == null)
			return new Point(0, 0);
		
		int deltaX = point.x - upperLeft.x;
		int deltaY = point.y - upperLeft.y;
		return new Point(deltaX, deltaY);	
	}

	public Point getLeftmostUppermostCorner()
	{
		if (mapNodeLocations.size() == 0)
			return null;
		
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
	Point insertionPoint;
}
