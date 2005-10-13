/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramNodeData extends EAMTestCase 
{

	public TestDiagramNodeData(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		int nodeAType = DiagramNode.TYPE_INDIRECT_FACTOR;
		DiagramNode nodeA = new DiagramNode(nodeAType);
		String nodeAText = "Node A";
		Point location = new Point(5,22);
		int id = 2;
		nodeA.setText(nodeAText);
		nodeA.setLocation(location);
		nodeA.setId(id);
		DataMap nodeAData = nodeA.createNodeDataMap();
		
		assertEquals("Text incorrect", nodeAText, nodeAData.getString(EAMGraphCell.TEXT));
		assertEquals("location incorrect", location, nodeAData.getPoint(EAMGraphCell.LOCATION));
		assertEquals("id incorrect", id, nodeAData.getInt(EAMGraphCell.ID));
		assertEquals("type incorrect", nodeAType, nodeAData.getInt(DiagramNode.TYPE));
		assertEquals("Priority default not None?", DiagramNode.PRIORITY_NONE, nodeAData.getInt(DiagramNode.PRIORITY));
	}

}
