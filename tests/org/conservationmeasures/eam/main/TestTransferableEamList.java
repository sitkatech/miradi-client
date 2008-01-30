/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.views.diagram.LinkCreator;

public class TestTransferableEamList extends EAMTestCase 
{

	public TestTransferableEamList(String name) 
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}

	public void testGetTransferDataFlavors() throws Exception
	{
		EAMGraphCell emptyCells[] = {};
		ORef diagramObjectRef = project.getDiagramObject().getRef();
		TransferableMiradiList miradiList = new TransferableMiradiList(project, diagramObjectRef);
		miradiList.storeData(emptyCells);
		DataFlavor flavors[] = miradiList.getTransferDataFlavors();
		assertEquals("Should only support 1 flavor?", 1, flavors.length);
		assertEquals("MiradiListDataFlavor not found?", TransferableMiradiList.miradiListDataFlavor, flavors[0]);
	}
	
	public void testIsDataFlavorSupported() throws Exception
	{
		EAMGraphCell emptyCells[] = {};
		ORef diagramObjectRef = project.getDiagramObject().getRef();
		TransferableMiradiList miradiList = new TransferableMiradiList(project, diagramObjectRef);
		miradiList.storeData(emptyCells);
		assertTrue("MiradiListDataFlavor not supported?", miradiList.isDataFlavorSupported(TransferableMiradiList.miradiListDataFlavor));
	}
	
	public void testProjectFileName() throws Exception
	{
		String projectFileName = project.getFilename();
		ORef diagramObjectRef = project.getDiagramObject().getRef();
		TransferableMiradiList miradiList = new TransferableMiradiList(project, diagramObjectRef);
		miradiList.storeData(new EAMGraphCell[0]);
		assertEquals("wrong project filename?", projectFileName, miradiList.getProjectFileName());
	}

	public void testGetTransfeData() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		
		Point node1Location = new Point(1,2);
		FactorCommandHelper commandHelper = new FactorCommandHelper(project, model);
		CommandCreateObject createCommand1 = commandHelper.createFactorAndDiagramFactor(ObjectType.CAUSE);
		DiagramFactorId diagramFactorId1 = (DiagramFactorId) createCommand1.getCreatedId();
		DiagramFactor diagramFactor1 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId1);
		diagramFactor1.setLocation(node1Location);
		
		Point node2Location = new Point(2,3);
		CommandCreateObject createCommand2 = commandHelper.createFactorAndDiagramFactor(ObjectType.STRATEGY);
		DiagramFactorId diagramFactorId2 = (DiagramFactorId) createCommand2.getCreatedId();
		DiagramFactor diagramFactor2 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId2);
		diagramFactor2.setLocation(node2Location);
		
		LinkCreator linkCreator = new LinkCreator(project);
		FactorLinkId factorLinkId = linkCreator.createFactorLinkAndAddToDiagramUsingCommands(project.getDiagramModel(), diagramFactor1, diagramFactor2);
		DiagramLink diagramLink = project.getDiagramModel().getDiagramFactorLinkbyWrappedId(factorLinkId);
		
		FactorCell factorCell1 = model.getFactorCellById(diagramFactorId1);
		FactorCell factorCell2 = model.getFactorCellById(diagramFactorId2);
		EAMGraphCell dataCells[] = {factorCell1, factorCell2, model.findLinkCell(diagramLink)};
		
		ORef diagramObjectRef = project.getDiagramObject().getRef();
		TransferableMiradiList miradiList = new TransferableMiradiList(project, diagramObjectRef);
		miradiList.storeData(dataCells);
		TransferableMiradiList miradiTransferData = (TransferableMiradiList)miradiList.getTransferData(TransferableMiradiList.miradiListDataFlavor);
		assertNotNull(miradiTransferData);
		
		Vector<String> factorDeepCopies = miradiTransferData.getFactorDeepCopies();
		assertEquals(2, factorDeepCopies.size());
		
		EnhancedJsonObject factor1Json = new EnhancedJsonObject(factorDeepCopies.get(0));
		int factor1Type = factor1Json.getInt("Type");
		assertEquals("wrong type for factor 1?", ObjectType.CAUSE, factor1Type);
		assertEquals("wrong factor id?", diagramFactor1.getWrappedId(), factor1Json.getId(Factor.TAG_ID));
		
		EnhancedJsonObject factor2Json = new EnhancedJsonObject(factorDeepCopies.get(1));
		int factor2Type = factor2Json.getInt("Type");
		assertEquals("wrong type for factor 1?", ObjectType.STRATEGY, factor2Type);
		assertEquals("wrong factor id?", diagramFactor2.getWrappedId(), factor2Json.getId(Factor.TAG_ID));
		
		Vector<String> diagramFactorDeepCopies = miradiTransferData.getDiagramFactorDeepCopies();
		assertEquals(2, diagramFactorDeepCopies.size());
		EnhancedJsonObject diagramFactor1Json = new EnhancedJsonObject(diagramFactorDeepCopies.get(0));
		int diagramFactor1Type = diagramFactor1Json.getInt("Type");
		assertEquals("wrong type for diagram factor 1?", ObjectType.DIAGRAM_FACTOR, diagramFactor1Type);
		Point diagramFactor1Location = diagramFactor1Json.getPoint(DiagramFactor.TAG_LOCATION);
		assertEquals("wrong diagram factor location?", node1Location, diagramFactor1Location);
		ORef wrappedRef1 = diagramFactor1Json.getRef(DiagramFactor.TAG_WRAPPED_REF);
		assertEquals("wrong diagram factor wrapped ref?", diagramFactor1.getWrappedORef(), wrappedRef1);
		
		EnhancedJsonObject diagramFactor2Json = new EnhancedJsonObject(diagramFactorDeepCopies.get(1));
		int diagramFactor2Type = diagramFactor2Json.getInt("Type");
		assertEquals("wrong type for diagram factor 2?", ObjectType.DIAGRAM_FACTOR, diagramFactor2Type);
		Point diagramFactor2Location = diagramFactor2Json.getPoint(DiagramFactor.TAG_LOCATION);
		assertEquals("wrong diagram factor location?", node2Location, diagramFactor2Location);
		ORef wrappedRef2 = diagramFactor2Json.getRef(DiagramFactor.TAG_WRAPPED_REF);
		assertEquals("wrong diagram factor wrapped ref?", diagramFactor2.getWrappedORef(), wrappedRef2);
		
		Vector<String> factorLinkDeepCopies = miradiTransferData.getFactorLinkDeepCopies();
		assertEquals(1, factorLinkDeepCopies.size());
		EnhancedJsonObject factorLinkJson = new EnhancedJsonObject(factorLinkDeepCopies.get(0));
		BaseId factorLinkIdFromJson = factorLinkJson.getId(FactorLink.TAG_ID);
		assertEquals("wrong factor link id?", factorLinkId, factorLinkIdFromJson);
		ORef fromRef = factorLinkJson.getRef(FactorLink.TAG_FROM_REF);
		assertEquals("wrong factor link from ref?", diagramFactor1.getWrappedORef(), fromRef);
		ORef toRef = factorLinkJson.getRef(FactorLink.TAG_TO_REF);
		assertEquals("wrong factor link to ref?", diagramFactor2.getWrappedORef(), toRef);
				
		Vector<String> diagramLinkDeepCopies = miradiTransferData.getDiagramLinkDeepCopies();
		assertEquals(1, diagramLinkDeepCopies.size()); 
		EnhancedJsonObject diagramLinkJson = new EnhancedJsonObject(diagramLinkDeepCopies.get(0));
		BaseId diagramLinkIdFromJson = diagramLinkJson.getId(DiagramLink.TAG_ID);
		assertEquals("wrong diagram link id?", diagramLink.getId(), diagramLinkIdFromJson);
		BaseId fromDiagramFactorId = diagramLinkJson.getId(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
		assertEquals("wrong diagram link from id?", diagramFactor1.getDiagramFactorId(), fromDiagramFactorId);
		BaseId toDiagramFactorId = diagramLinkJson.getId(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
		assertEquals("wrong diagram link to id?", diagramFactor2.getDiagramFactorId(), toDiagramFactorId);	
	}
	
	ProjectForTesting project;
}
