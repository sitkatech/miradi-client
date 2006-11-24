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
import org.conservationmeasures.eam.diagram.nodes.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.LinkageDataMap;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
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
		TransferableEamList eamList = new TransferableEamList("SomeProjectName", emptyCells);
		DataFlavor flavors[] = eamList.getTransferDataFlavors();
		assertEquals("Should only support 1 flavor", 1, flavors.length);
		assertEquals("EamListDataFlavor not found?", TransferableEamList.eamListDataFlavor, flavors[0]);
	}
	
	public void testIsDataFlavorSupported() throws Exception
	{
		EAMGraphCell emptyCells[] = {};
		TransferableEamList eamList = new TransferableEamList("SomeProjectName", emptyCells);
		assertTrue("EamListDataFlavor not supported?", eamList.isDataFlavorSupported(TransferableEamList.eamListDataFlavor));
	}
	
	public void testProjectFileName() throws Exception
	{
		String projectFileName = "blah blah";
		TransferableEamList eamList = new TransferableEamList(projectFileName, new EAMGraphCell[0]);
		assertEquals("wrong project filename?", projectFileName, eamList.getProjectFileName());
	}

	public void testGetTransferData() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();
		NodePool nodePool = model.getNodePool();

		ModelNodeId node1Id = new ModelNodeId(1);
		Point node1Location = new Point(1,2);
		
		Strategy cmIntervention = new Strategy(node1Id);
		nodePool.put(cmIntervention);
		DiagramNode node1 = model.createNode(cmIntervention.getModelNodeId());
		node1.setLocation(node1Location);
		
		ModelNodeId node2Id = new ModelNodeId(2);
		Point node2Location = new Point(2,3);
		
		Target cmTarget = new Target(node2Id);
		nodePool.put(cmTarget);
		DiagramNode node2 = model.createNode(cmTarget.getModelNodeId());
		node2.setLocation(node2Location);
		
		ModelLinkageId linkage1Id = new ModelLinkageId(3);
		FactorLink cmLinkage = new FactorLink(linkage1Id, node1Id, node2Id);
		DiagramFactorLink linkage1 = new DiagramFactorLink(model, cmLinkage);
		
		EAMGraphCell dataCells[] = {node1, node2, linkage1};
		TransferableEamList eamList = new TransferableEamList(project.getFilename(), dataCells);
		TransferableEamList eamTransferData = (TransferableEamList)eamList.getTransferData(TransferableEamList.eamListDataFlavor);
		assertNotNull(eamTransferData);
		
		NodeDataMap[] nodesData = eamTransferData.getNodeDataCells();
		LinkageDataMap[] linkagesData = eamTransferData.getLinkageDataCells();
		
		assertEquals(2, nodesData.length);
		assertEquals(node1.getDiagramNodeId(), nodesData[0].getId(DiagramNode.TAG_ID));
		assertEquals(node1Location, nodesData[0].getPoint(DiagramNode.TAG_LOCATION));
		assertEquals(node2.getDiagramNodeId(), nodesData[1].getId(DiagramNode.TAG_ID));
		assertEquals(node2Location, nodesData[1].getPoint(DiagramNode.TAG_LOCATION));

		assertEquals(1, linkagesData.length);
		assertEquals(linkage1Id, linkagesData[0].getId());
		assertEquals(node1.getDiagramNodeId(), linkagesData[0].getFromId());
		assertEquals(node2.getDiagramNodeId(), linkagesData[0].getToId());
		
		project.close();
	}
}
