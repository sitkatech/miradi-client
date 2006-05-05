/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.LinkageDataMap;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.project.NodePool;
import org.conservationmeasures.eam.project.ProjectForTesting;
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
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();
		NodePool nodePool = model.getNodePool();

		int node1Id = 1;
		String node1Text = "Intervention 1";
		Point node1Location = new Point(1,2);
		
		ConceptualModelIntervention cmIntervention = new ConceptualModelIntervention(node1Id);
		nodePool.put(cmIntervention);
		DiagramNode node1 = model.createNode(cmIntervention.getId());
		node1.setText(node1Text);
		node1.setLocation(node1Location);
		
		int node2Id = 2;
		String node2Text = "Target 1";
		Point node2Location = new Point(2,3);
		
		ConceptualModelTarget cmTarget = new ConceptualModelTarget(node2Id);
		nodePool.put(cmTarget);
		DiagramNode node2 = model.createNode(cmTarget.getId());
		node2.setText(node2Text);
		node2.setLocation(node2Location);
		
		int linkage1Id = 3;
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(linkage1Id, node1Id, node2Id);
		DiagramLinkage linkage1 = new DiagramLinkage(model, cmLinkage);
		
		EAMGraphCell dataCells[] = {node1, node2, linkage1};
		TransferableEamList eamList = new TransferableEamList(dataCells);
		TransferableEamList eamTransferData = (TransferableEamList)eamList.getTransferData(TransferableEamList.eamListDataFlavor);
		assertNotNull(eamTransferData);
		
		NodeDataMap[] nodesData = eamTransferData.getNodeDataCells();
		LinkageDataMap[] linkagesData = eamTransferData.getLinkageDataCells();
		
		assertEquals(2, nodesData.length);
		assertEquals(node1Id, nodesData[0].getInt(DiagramNode.TAG_ID));
		assertEquals(node1Text, nodesData[0].getString(DiagramNode.TAG_VISIBLE_LABEL));
		assertEquals(node1Location, nodesData[0].getPoint(DiagramNode.TAG_LOCATION));
		assertEquals(node2Id, nodesData[1].getInt(DiagramNode.TAG_ID));
		assertEquals(node2Text, nodesData[1].getString(DiagramNode.TAG_VISIBLE_LABEL));
		assertEquals(node2Location, nodesData[1].getPoint(DiagramNode.TAG_LOCATION));

		assertEquals(1, linkagesData.length);
		assertEquals(linkage1Id, linkagesData[0].getId());
		assertEquals(node1Id, linkagesData[0].getFromId());
		assertEquals(node2Id, linkagesData[0].getToId());
		
		project.close();
	}
}
