/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeDirectThreat;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeContributingFactor;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeTarget;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeDataMap extends EAMTestCase
{
	public TestNodeDataMap(String name)
	{
		super(name);
	}

	public void testConvertNodeTypeToInt()
	{
		assertEquals(NodeDataMap.INT_TYPE_TARGET, NodeDataMap.convertNodeTypeToInt(new FactorTypeTarget()));
		assertEquals(NodeDataMap.INT_TYPE_INDIRECT_FACTOR, NodeDataMap.convertNodeTypeToInt(new FactorTypeDirectThreat()));
		assertEquals(NodeDataMap.INT_TYPE_INDIRECT_FACTOR, NodeDataMap.convertNodeTypeToInt(new FactorTypeContributingFactor()));
		assertEquals(NodeDataMap.INT_TYPE_INTERVENTION, NodeDataMap.convertNodeTypeToInt(new FactorTypeStrategy()));
		assertEquals(NodeDataMap.INT_TYPE_CLUSTER, NodeDataMap.convertNodeTypeToInt(new FactorTypeCluster()));
	}
	
	public void testConvertIntToNodeType()
	{
		assertEquals(new FactorTypeTarget(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_TARGET));
		assertEquals(new FactorTypeCause(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_DIRECT_THREAT));
		assertEquals(new FactorTypeCause(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_INDIRECT_FACTOR));
		assertEquals(new FactorTypeStrategy(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_INTERVENTION));
		assertEquals(new FactorTypeCluster(), NodeDataMap.convertIntToNodeType(NodeDataMap.INT_TYPE_CLUSTER));
	}
}
