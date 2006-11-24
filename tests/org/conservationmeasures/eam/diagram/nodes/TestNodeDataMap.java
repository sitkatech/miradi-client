/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeDirectThreat;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCause;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIndirectFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeDataMap extends EAMTestCase
{
	public TestNodeDataMap(String name)
	{
		super(name);
	}

	public void testConvertNodeTypeToInt()
	{
		assertEquals(NodeDataMap.INT_TYPE_TARGET, NodeDataMap.convertNodeTypeToInt(new NodeTypeTarget()));
		assertEquals(NodeDataMap.INT_TYPE_INDIRECT_FACTOR, NodeDataMap.convertNodeTypeToInt(new NodeTypeDirectThreat()));
		assertEquals(NodeDataMap.INT_TYPE_INDIRECT_FACTOR, NodeDataMap.convertNodeTypeToInt(new NodeTypeIndirectFactor()));
		assertEquals(NodeDataMap.INT_TYPE_INTERVENTION, NodeDataMap.convertNodeTypeToInt(new NodeTypeIntervention()));
		assertEquals(NodeDataMap.INT_TYPE_CLUSTER, NodeDataMap.convertNodeTypeToInt(new NodeTypeCluster()));
	}
	
	public void testConvertIntToNodeType()
	{
		assertEquals(new NodeTypeTarget(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_TARGET));
		assertEquals(new NodeTypeCause(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_DIRECT_THREAT));
		assertEquals(new NodeTypeCause(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_INDIRECT_FACTOR));
		assertEquals(new NodeTypeIntervention(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_INTERVENTION));
		assertEquals(new NodeTypeCluster(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_CLUSTER));
	}
}
