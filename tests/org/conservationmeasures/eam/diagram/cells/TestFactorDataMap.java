/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCluster;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeContributingFactor;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeDirectThreat;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestFactorDataMap extends EAMTestCase
{
	public TestFactorDataMap(String name)
	{
		super(name);
	}

	public void testConvertNodeTypeToInt()
	{
		assertEquals(FactorDataMap.INT_TYPE_TARGET, FactorDataMap.convertNodeTypeToInt(new FactorTypeTarget()));
		assertEquals(FactorDataMap.INT_TYPE_INDIRECT_FACTOR, FactorDataMap.convertNodeTypeToInt(new FactorTypeDirectThreat()));
		assertEquals(FactorDataMap.INT_TYPE_INDIRECT_FACTOR, FactorDataMap.convertNodeTypeToInt(new FactorTypeContributingFactor()));
		assertEquals(FactorDataMap.INT_TYPE_INTERVENTION, FactorDataMap.convertNodeTypeToInt(new FactorTypeStrategy()));
		assertEquals(FactorDataMap.INT_TYPE_CLUSTER, FactorDataMap.convertNodeTypeToInt(new FactorTypeCluster()));
	}
	
	public void testConvertIntToNodeType()
	{
		assertEquals(new FactorTypeTarget(), FactorDataMap.convertIntToNodeType(FactorDataMap.INT_TYPE_TARGET));
		assertEquals(new FactorTypeCause(), FactorDataMap.convertIntToNodeType(FactorDataMap.INT_TYPE_DIRECT_THREAT));
		assertEquals(new FactorTypeCause(), FactorDataMap.convertIntToNodeType(FactorDataMap.INT_TYPE_INDIRECT_FACTOR));
		assertEquals(new FactorTypeStrategy(), FactorDataMap.convertIntToNodeType(FactorDataMap.INT_TYPE_INTERVENTION));
		assertEquals(new FactorTypeCluster(), FactorDataMap.convertIntToNodeType(FactorDataMap.INT_TYPE_CLUSTER));
	}
}
