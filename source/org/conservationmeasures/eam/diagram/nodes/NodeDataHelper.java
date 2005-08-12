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
	
	public Point getNewLocation(int originalNodeId, int insertionPointX, int insertionPointY)
	{
		computeDeltas(insertionPointX, insertionPointY);
		Point originalNodeLocation = (Point)mapNodeLocations.get(getKey(originalNodeId));
		int originalX = originalNodeLocation.x;
		int originalY = originalNodeLocation.y;
		return new Point(originalX + deltaX, originalY + deltaY);
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
	
	private void computeDeltas(int insertionPointX, int insertionPointY)
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
		
		if(insertionPointX < rect.x)
			deltaX = -(rect.x - insertionPointX);
		else
			deltaX = insertionPointX - rect.x;
		
		if(insertionPointY < rect.y)
			deltaY = -(rect.y - insertionPointY);
		else
			deltaY = insertionPointY - rect.y;
		
	}
	
	HashMap mapNodeLocations = new HashMap();
	HashMap mapNodeIds = new HashMap();
	int deltaX = 0;
	int deltaY = 0;
}
