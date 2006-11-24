/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramNodeData extends EAMTestCase 
{

	public TestDiagramNodeData(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		ModelNodeId wrappedId = new ModelNodeId(2);
		Cause cmFactor = new Cause(wrappedId);
		DiagramNodeId nodeId = new DiagramNodeId(44);
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
