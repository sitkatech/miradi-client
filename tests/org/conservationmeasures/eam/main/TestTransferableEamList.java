/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorLinkDataMap;
import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectpools.FactorPool;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.ProjectForTesting;

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
		FactorPool nodePool = model.getFactorPool();

		FactorId node1Id = new FactorId(1);
		Point node1Location = new Point(1,2);
		
		Strategy cmIntervention = new Strategy(node1Id);
		nodePool.put(cmIntervention);
		DiagramFactor node1 = model.createDiagramFactor(cmIntervention.getModelNodeId());
		node1.setLocation(node1Location);
		
		FactorId node2Id = new FactorId(2);
		Point node2Location = new Point(2,3);
		
		Target cmTarget = new Target(node2Id);
		nodePool.put(cmTarget);
		DiagramFactor node2 = model.createDiagramFactor(cmTarget.getModelNodeId());
		node2.setLocation(node2Location);
		
		FactorLinkId linkage1Id = new FactorLinkId(3);
		FactorLink cmLinkage = new FactorLink(linkage1Id, node1Id, node2Id);
		DiagramFactorLink linkage1 = new DiagramFactorLink(model, cmLinkage);
		
		EAMGraphCell dataCells[] = {node1, node2, linkage1};
		TransferableEamList eamList = new TransferableEamList(project.getFilename(), dataCells);
		TransferableEamList eamTransferData = (TransferableEamList)eamList.getTransferData(TransferableEamList.eamListDataFlavor);
		assertNotNull(eamTransferData);
		
		FactorDataMap[] nodesData = eamTransferData.getArrayOfFactorDataMaps();
		FactorLinkDataMap[] linkagesData = eamTransferData.getArrayOfFactorLinkDataMaps();
		
		assertEquals(2, nodesData.length);
		assertEquals(node1.getDiagramFactorId(), nodesData[0].getId(DiagramFactor.TAG_ID));
		assertEquals(node1Location, nodesData[0].getPoint(DiagramFactor.TAG_LOCATION));
		assertEquals(node2.getDiagramFactorId(), nodesData[1].getId(DiagramFactor.TAG_ID));
		assertEquals(node2Location, nodesData[1].getPoint(DiagramFactor.TAG_LOCATION));

		assertEquals(1, linkagesData.length);
		assertEquals(linkage1Id, linkagesData[0].getId());
		assertEquals(node1.getDiagramFactorId(), linkagesData[0].getFromId());
		assertEquals(node2.getDiagramFactorId(), linkagesData[0].getToId());
		
		project.close();
	}
}
