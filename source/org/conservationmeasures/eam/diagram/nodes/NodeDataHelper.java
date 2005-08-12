/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class NodeDataHelper 
{
	public NodeDataHelper(Vector existingNodesInProject)
	{
		setInitialMappingOfIdsToOriginalIds(existingNodesInProject);
	}

	public void setNewId(int originalNodeId, int newNodeId)
	{
		mapNodeIds.put(getKey(originalNodeId), getValue(newNodeId));
	}
	
	public int getNewId(int originalNodeId)
	{
		Integer newNodeId = (Integer)mapNodeIds.get(getKey(originalNodeId));
		if(newNodeId == null)
			return Node.INVALID_ID;
		return newNodeId.intValue();
	}
	
	public void setOriginalLocation(int originalNodeId, Point originalLocation)
	{
		mapNodeLocations.put(getKey(originalNodeId), originalLocation);
	}
	
	public Point getNewLocation(int originalNodeId, Point insertionPoint)
	{
		Point delta = computeDeltas(insertionPoint);
		Point originalNodeLocation = (Point)mapNodeLocations.get(getKey(originalNodeId));
		int originalX = originalNodeLocation.x;
		int originalY = originalNodeLocation.y;
		return new Point(originalX + delta.x, originalY + delta.y);
	}

	private void setInitialMappingOfIdsToOriginalIds(Vector existingNodesInProject) 
	{
		for (Iterator iter = existingNodesInProject.iterator(); iter.hasNext();) 
		{
			int id = ((Node) iter.next()).getId();
			setNewId(id, id);
		}
	}

	private Integer getKey(int key) 
	{
		return new Integer(key);
	}
	
	private Integer getValue(int value) 
	{
		return new Integer(value);
	}
	
	private Point computeDeltas(Point insertionPoint)
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
		Point delta = new Point();
		if(insertionPoint.x < rect.x)
			delta.x = -(rect.x - insertionPoint.x);
		else
			delta.x = insertionPoint.x - rect.x;
		
		if(insertionPoint.y < rect.y)
			delta.y = -(rect.y - insertionPoint.y);
		else
			delta.y = insertionPoint.y - rect.y;
		return delta;
	}
	
	HashMap mapNodeLocations = new HashMap();
	HashMap mapNodeIds = new HashMap();
}
