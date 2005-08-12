/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class NodeDataHelper 
{
	public NodeDataHelper(Vector existingNodesInProject)
	{
		setInitialMappingOfIdsToOriginalIds(existingNodesInProject);
	}

	private void setInitialMappingOfIdsToOriginalIds(Vector existingNodesInProject) 
	{
		for (Iterator iter = existingNodesInProject.iterator(); iter.hasNext();) 
		{
			Node node = (Node) iter.next();
			setNewId(node.getId(), node.getId());
		}
	}
	
	public void setNewId(int originalNodeId, int newNodeId)
	{
		mapNodeIds.put(new Integer(originalNodeId), new Integer(newNodeId));
	}
	
	public int getNewId(int originalNodeId)
	{
		Integer newNodeId = (Integer)mapNodeIds.get(new Integer(originalNodeId));
		if(newNodeId == null)
			return Node.INVALID_ID;
		return newNodeId.intValue();
	}

	HashMap mapNodeIds = new HashMap();
}
