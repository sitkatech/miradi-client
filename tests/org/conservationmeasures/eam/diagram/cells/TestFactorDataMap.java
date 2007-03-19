/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;

public class TestFactorDataMap extends EAMTestCase
{
	public TestFactorDataMap(String name)
	{
		super(name);
	}

	public void testFactorDataMap() throws Exception
	{
		FactorId wrappedId = new FactorId(2);
		DiagramFactorId nodeId = new DiagramFactorId(44);
		DiagramFactor nodeA = FactorCell.wrapConceptualModelObject(nodeId, wrappedId);
		String nodeAText = "Node A";
		nodeA.setLabel(nodeAText);
		Point location = new Point(5,22);
		nodeA.setLocation(location);
		String factorType = Factor.TYPE_TARGET.toString();
		String label = "Different from Node A";
		FactorDataMap nodeAData = nodeA.createFactorDataMap(factorType, label);
		
		assertEquals("location incorrect", location, nodeAData.getPoint(DiagramFactor.TAG_LOCATION));
		assertEquals("id incorrect", nodeId, nodeAData.getId(DiagramFactor.TAG_ID));
		assertEquals("wrapped id incorrect", wrappedId, nodeAData.getId(DiagramFactor.TAG_WRAPPED_ID));
		assertEquals("node type incorrect", factorType, nodeAData.getFactorType());
		assertEquals("label incorrect", label, nodeAData.getLabel());
	}
}
