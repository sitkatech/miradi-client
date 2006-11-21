/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.utils.DataMap;
import org.json.JSONObject;


public class NodeDataMap extends DataMap 
{
	public NodeDataMap()
	{
	}
	
	public NodeDataMap(JSONObject copyFrom) throws ParseException
	{
		super(copyFrom);
	}
	
	public static NodeType convertIntToNodeType(int rawType)
	{
		switch(rawType)
		{
			case DiagramNode.INT_TYPE_TARGET:
				return new NodeTypeTarget();
			case DiagramNode.INT_TYPE_INDIRECT_FACTOR:
				return new NodeTypeFactor();
			case DiagramNode.INT_TYPE_DIRECT_THREAT:
				return new NodeTypeFactor();
			case DiagramNode.INT_TYPE_INTERVENTION:
				return new NodeTypeIntervention();
			case DiagramNode.INT_TYPE_CLUSTER:
				return new NodeTypeCluster();
			default:
				throw new RuntimeException("Unknown factor type: " + rawType);
		}
		
	}
	
	public static int convertNodeTypeToInt(NodeType type)
	{
		if(type.isTarget())
			return DiagramNode.INT_TYPE_TARGET;
		if(type.isIndirectFactor())
			return DiagramNode.INT_TYPE_INDIRECT_FACTOR;
		if(type.isIntervention())
			return DiagramNode.INT_TYPE_INTERVENTION;
		if(type.isDirectThreat())
			return DiagramNode.INT_TYPE_INDIRECT_FACTOR;
		if(type.isCluster())
			return DiagramNode.INT_TYPE_CLUSTER;
		
		if(type.isFactor())
			return DiagramNode.INT_TYPE_INDIRECT_FACTOR;

		throw new RuntimeException("Unknown factor type: " + type);
	}
}
