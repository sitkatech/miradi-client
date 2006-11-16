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

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;

public class NodeDataHelper 
{
	public NodeDataHelper(Vector existingNodesInProject)
	{
		setInitialMappingOfIdsToOriginalIds(existingNodesInProject);
	}

	public void setNewId(DiagramNodeId originalNodeId, DiagramNodeId newNodeId)
	{
		mapNodeIds.put(getKey(originalNodeId), getValue(newNodeId));
	}
	
	public DiagramNodeId getNewId(DiagramNodeId originalNodeId)
	{
		Integer newNodeId = (Integer)mapNodeIds.get(getKey(originalNodeId));
		if(newNodeId == null)
			return new DiagramNodeId(BaseId.INVALID.asInt());
		return new DiagramNodeId(newNodeId.intValue());
	}
	
	public void setOriginalLocation(DiagramNodeId originalNodeId, Point originalLocation)
	{
		mapNodeLocations.put(getKey(originalNodeId), originalLocation);
	}
	
	public Point getNewLocation(DiagramNodeId originalNodeId, Point insertionPoint)
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
			DiagramNodeId id = ((DiagramNode) iter.next()).getDiagramNodeId();
			setNewId(id, id);
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
		Rectangle rect = null;
		for (Iterator iter = mapNodeLocations.values().iterator(); iter.hasNext();) 
		{
			Point nodeLocation = (Point) iter.next();
			if(rect==null)
				rect = new Rectangle(nodeLocation);
			else
				rect.add(nodeLocation);
		}
		
		int deltaX = insertionPoint.x - rect.x;
		int deltaY = insertionPoint.y - rect.y;
		return new Point(deltaX, deltaY);	
	}
	HashMap mapNodeLocations = new HashMap();
	HashMap mapNodeIds = new HashMap();
}
