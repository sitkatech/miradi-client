/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramNodeData extends EAMTestCase 
{

	public TestDiagramNodeData(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		BaseId id = new BaseId(2);
		ConceptualModelFactor cmFactor = new ConceptualModelFactor(id);
		DiagramNode nodeA = DiagramNode.wrapConceptualModelObject(cmFactor);
		String nodeAText = "Node A";
		nodeA.setLabel(nodeAText);
		Point location = new Point(5,22);
		nodeA.setLocation(location);
		NodeDataMap nodeAData = nodeA.createNodeDataMap();
		
		assertEquals("Text incorrect", nodeAText, nodeAData.getString(DiagramNode.TAG_VISIBLE_LABEL));
		assertEquals("location incorrect", location, nodeAData.getPoint(DiagramNode.TAG_LOCATION));
		assertEquals("id incorrect", id, nodeAData.getId(DiagramNode.TAG_ID));
	}
	
}
