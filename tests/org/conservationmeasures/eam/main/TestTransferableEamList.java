/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.LinkageData;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.diagram.nodes.NodeData;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestTransferableEamList extends EAMTestCase 
{

	public TestTransferableEamList(String name) 
	{
		super(name);
	}
	
	public void testGetTransferDataFlavors() throws Exception
	{
		Vector emptyCells = new Vector();
		TransferableEamList eamList = new TransferableEamList(emptyCells);
		DataFlavor flavors[] = eamList.getTransferDataFlavors();
		assertEquals("Should only support 1 flavor", 1, flavors.length);
		assertEquals("EamListDataFlavor not found?", TransferableEamList.eamListDataFlavor, flavors[0]);
	}
	
	public void testIsDataFlavorSupported() throws Exception
	{
		Vector emptyCells = new Vector();
		TransferableEamList eamList = new TransferableEamList(emptyCells);
		assertTrue("EamListDataFlavor not supported?", eamList.isDataFlavorSupported(TransferableEamList.eamListDataFlavor));
	}

	public void testGetTransferData() throws Exception
	{
		int node1Id = 1;
		String node1Text = "Goal 1";
		Point node1Location = new Point(1,2);
		int node1Type = Node.TYPE_GOAL;
		Node node1 = new Node(node1Type);
		node1.setId(node1Id);
		node1.setText(node1Text);
		node1.setLocation(node1Location);
		
		int node2Id = 2;
		String node2Text = "Threat 1";
		Point node2Location = new Point(2,3);
		int node2Type = Node.TYPE_THREAT;
		
		Node node2 = new Node(node2Type);
		node2.setId(node2Id);
		node2.setText(node2Text);
		node2.setLocation(node2Location);
		
		int linkage1Id = 3;
		Linkage linkage1 = new Linkage(node2, node1);
		linkage1.setId(linkage1Id);
		
		Vector dataCells = new Vector();
		dataCells.add(node1);
		dataCells.add(node2);
		dataCells.add(linkage1);
		TransferableEamList eamList = new TransferableEamList(dataCells);
		TransferableEamList eamTransferData = (TransferableEamList)eamList.getTransferData(TransferableEamList.eamListDataFlavor);
		assertNotNull(eamTransferData);
		
		NodeData[] nodesData = eamTransferData.getNodeDataCells();
		LinkageData[] linkagesData = eamTransferData.getLinkageDataCells();
		
		assertEquals(2, nodesData.length);
		assertEquals(node1Id, nodesData[0].getId());
		assertEquals(node1Text, nodesData[0].getText());
		assertEquals(node1Location, nodesData[0].getLocation());
		assertEquals(node1Type, nodesData[0].getNodeType());
		assertEquals(node2Id, nodesData[1].getId());
		assertEquals(node2Text, nodesData[1].getText());
		assertEquals(node2Location, nodesData[1].getLocation());
		assertEquals(node2Type, nodesData[1].getNodeType());

		assertEquals(1, linkagesData.length);
		assertEquals(linkage1Id, linkagesData[0].getId());
		assertEquals(node2Id, linkagesData[0].getFromNodeId());
		assertEquals(node1Id, linkagesData[0].getToNodeId());
	}
}
