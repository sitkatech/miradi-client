/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;

import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.LinkageData;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
import org.conservationmeasures.eam.diagram.nodes.types.NodeType;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestTransferableEamList extends EAMTestCase 
{

	public TestTransferableEamList(String name) 
	{
		super(name);
	}
	
	public void testGetTransferDataFlavors() throws Exception
	{
		EAMGraphCell emptyCells[] = {};
		TransferableEamList eamList = new TransferableEamList(emptyCells);
		DataFlavor flavors[] = eamList.getTransferDataFlavors();
		assertEquals("Should only support 1 flavor", 1, flavors.length);
		assertEquals("EamListDataFlavor not found?", TransferableEamList.eamListDataFlavor, flavors[0]);
	}
	
	public void testIsDataFlavorSupported() throws Exception
	{
		EAMGraphCell emptyCells[] = {};
		TransferableEamList eamList = new TransferableEamList(emptyCells);
		assertTrue("EamListDataFlavor not supported?", eamList.isDataFlavorSupported(TransferableEamList.eamListDataFlavor));
	}

	public void testGetTransferData() throws Exception
	{
		int node1Id = 1;
		String node1Text = "Target 1";
		Point node1Location = new Point(1,2);
		NodeType node1Type = DiagramNode.TYPE_TARGET;
		DiagramNode node1 = DiagramNode.createDiagramNode(node1Type);
		node1.setId(node1Id);
		node1.setText(node1Text);
		node1.setLocation(node1Location);
		
		int node2Id = 2;
		String node2Text = "Factor 1";
		Point node2Location = new Point(2,3);
		NodeType node2Type = DiagramNode.TYPE_INDIRECT_FACTOR;
		
		DiagramNode node2 = DiagramNode.createDiagramNode(node2Type);
		node2.setId(node2Id);
		node2.setText(node2Text);
		node2.setLocation(node2Location);
		
		int linkage1Id = 3;
		DiagramLinkage linkage1 = new DiagramLinkage(node2, node1);
		linkage1.setId(linkage1Id);
		
		EAMGraphCell dataCells[] = {node1, node2, linkage1};
		TransferableEamList eamList = new TransferableEamList(dataCells);
		TransferableEamList eamTransferData = (TransferableEamList)eamList.getTransferData(TransferableEamList.eamListDataFlavor);
		assertNotNull(eamTransferData);
		
		NodeDataMap[] nodesData = eamTransferData.getNodeDataCells();
		LinkageData[] linkagesData = eamTransferData.getLinkageDataCells();
		
		assertEquals(2, nodesData.length);
		assertEquals(node1Id, nodesData[0].getInt(EAMGraphCell.ID));
		assertEquals(node1Text, nodesData[0].getString(EAMGraphCell.TEXT));
		assertEquals(node1Location, nodesData[0].getPoint(EAMGraphCell.LOCATION));
		assertEquals(node1Type, nodesData[0].getNodeType());
		assertEquals(node2Id, nodesData[1].getInt(EAMGraphCell.ID));
		assertEquals(node2Text, nodesData[1].getString(EAMGraphCell.TEXT));
		assertEquals(node2Location, nodesData[1].getPoint(EAMGraphCell.LOCATION));
		assertEquals(node2Type, nodesData[1].getNodeType());

		assertEquals(1, linkagesData.length);
		assertEquals(linkage1Id, linkagesData[0].getId());
		assertEquals(node2Id, linkagesData[0].getFromNodeId());
		assertEquals(node1Id, linkagesData[0].getToNodeId());
	}
}
