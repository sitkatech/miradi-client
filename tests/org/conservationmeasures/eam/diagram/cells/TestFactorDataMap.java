/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCluster;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeContributingFactor;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeDirectThreat;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objects.Cause;
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

	public void testFactorDataMap() throws Exception
	{
		FactorId wrappedId = new FactorId(2);
		Cause cmFactor = new Cause(wrappedId);
		DiagramFactorId nodeId = new DiagramFactorId(44);
		DiagramFactor nodeA = DiagramFactor.wrapConceptualModelObject(nodeId, cmFactor);
		String nodeAText = "Node A";
		nodeA.setLabel(nodeAText);
		Point location = new Point(5,22);
		nodeA.setLocation(location);
		FactorDataMap nodeAData = nodeA.createNodeDataMap();
		
		assertEquals("Text incorrect", nodeAText, nodeAData.getString(DiagramFactor.TAG_VISIBLE_LABEL));
		assertEquals("location incorrect", location, nodeAData.getPoint(DiagramFactor.TAG_LOCATION));
		assertEquals("id incorrect", nodeId, nodeAData.getId(DiagramFactor.TAG_ID));
		assertEquals("wrapped id incorrect", wrappedId, nodeAData.getId(DiagramFactor.TAG_WRAPPED_ID));
	}
}
