/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCause;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.utils.DataMap;
import org.json.JSONObject;


public class NodeDataMap extends DataMap 
{
	public static final int INT_TYPE_INVALID = -1;
	public static final int INT_TYPE_TARGET = 1;
	public static final int INT_TYPE_INDIRECT_FACTOR = 2;
	public static final int INT_TYPE_INTERVENTION = 3;
	public static final int INT_TYPE_DIRECT_THREAT = 4;
	public static final int INT_TYPE_CLUSTER = 5;

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
			case NodeDataMap.INT_TYPE_TARGET:
				return new NodeTypeTarget();
			case NodeDataMap.INT_TYPE_INDIRECT_FACTOR:
				return new NodeTypeCause();
			case NodeDataMap.INT_TYPE_DIRECT_THREAT:
				return new NodeTypeCause();
			case NodeDataMap.INT_TYPE_INTERVENTION:
				return new NodeTypeIntervention();
			case NodeDataMap.INT_TYPE_CLUSTER:
				return new NodeTypeCluster();
			default:
				throw new RuntimeException("Unknown factor type: " + rawType);
		}
		
	}
	
	public static int convertNodeTypeToInt(NodeType type)
	{
		if(type.isTarget())
			return NodeDataMap.INT_TYPE_TARGET;
		if(type.isIndirectFactor())
			return NodeDataMap.INT_TYPE_INDIRECT_FACTOR;
		if(type.isIntervention())
			return NodeDataMap.INT_TYPE_INTERVENTION;
		if(type.isDirectThreat())
			return NodeDataMap.INT_TYPE_INDIRECT_FACTOR;
		if(type.isCluster())
			return NodeDataMap.INT_TYPE_CLUSTER;
		
		if(type.isCause())
			return NodeDataMap.INT_TYPE_INDIRECT_FACTOR;

		throw new RuntimeException("Unknown factor type: " + type);
	}
}
