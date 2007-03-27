/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.diagram.cells.FactorLinkDataMap;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;

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

	public void testGetTransfeData() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();
		
		Point node1Location = new Point(1,2);
		FactorCommandHelper commandHelper = new FactorCommandHelper(project);
		CommandCreateObject createCommand1 = commandHelper.createFactorAndDiagramFactor(Factor.TYPE_CAUSE);
		DiagramFactorId diagramFactorId1 = (DiagramFactorId) createCommand1.getCreatedId();
		DiagramFactor diagramFactor1 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId1);
		FactorId factorId1 = diagramFactor1.getWrappedId();
		diagramFactor1.setLocation(node1Location);
		
		Point node2Location = new Point(2,3);
		CommandCreateObject createCommand2 = commandHelper.createFactorAndDiagramFactor(Factor.TYPE_CAUSE);
		DiagramFactorId diagramFactorId2 = (DiagramFactorId) createCommand2.getCreatedId();
		DiagramFactor diagramFactor2 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId2);
		FactorId factorId2 = diagramFactor2.getWrappedId();
		diagramFactor2.setLocation(node2Location);
		
		CommandDiagramAddFactorLink commandDiagramAddFactorLink = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project, factorId1, factorId2);
		DiagramFactorLinkId diagramFactorLinkId = commandDiagramAddFactorLink.getDiagramFactorLinkId();
		DiagramFactorLink diagramFactorLink = model.getDiagramFactorLinkById(diagramFactorLinkId);

		FactorCell factorCell1 = model.getFactorCellById(diagramFactorId1);
		FactorCell factorCell2 = model.getFactorCellById(diagramFactorId2);
		EAMGraphCell dataCells[] = {factorCell1, factorCell2, model.findLinkCell(diagramFactorLink)};
		TransferableEamList eamList = new TransferableEamList(project.getFilename(), dataCells);
		TransferableEamList eamTransferData = (TransferableEamList)eamList.getTransferData(TransferableEamList.eamListDataFlavor);
		assertNotNull(eamTransferData);
		
		FactorDataMap[] nodesData = eamTransferData.getArrayOfFactorDataMaps();
		FactorLinkDataMap[] linkagesData = eamTransferData.getArrayOfFactorLinkDataMaps();
		
		assertEquals(2, nodesData.length);
		
		assertEquals(diagramFactorId1, nodesData[0].getId(DiagramFactor.TAG_ID));
		Point point1 = nodesData[0].getPoint(DiagramFactor.TAG_LOCATION);
		assertEquals(node1Location, point1);
		
		assertEquals(diagramFactorId2, nodesData[1].getId(DiagramFactor.TAG_ID));
		Point point2 = nodesData[1].getPoint(DiagramFactor.TAG_LOCATION);
		assertEquals(node2Location, point2);

		assertEquals(1, linkagesData.length);
		
		assertEquals(diagramFactorLink.getId(), linkagesData[0].getId());
		assertEquals(diagramFactorId1, linkagesData[0].getFromId());
		assertEquals(diagramFactorId2, linkagesData[0].getToId());
		
		project.close();
	}
}
