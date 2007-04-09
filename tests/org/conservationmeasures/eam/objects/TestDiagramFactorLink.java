/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramCauseCell;
import org.conservationmeasures.eam.diagram.cells.DiagramTargetCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;

public class TestDiagramFactorLink extends ObjectTestCase
{
	public TestDiagramFactorLink(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		model = project.getDiagramModel();

		BaseId rawInterventionId = project.createObject(ObjectType.STRATEGY);
		FactorId interventionId = new FactorId(rawInterventionId.asInt());
		cmIntervention = project.findNode(interventionId);
		
		BaseId rawTargetId = project.createObject(ObjectType.TARGET);
		FactorId targetId = new FactorId(rawTargetId.asInt());
		cmTarget = project.findNode(targetId);
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}

//FIXME fails because extraInfo is specific to a project and verify fields creates its own project.
//	public void testAsObject() throws Exception
//	{
//		DiagramFactor diagramFactor1 = project.createNodeAndAddToDiagram2(Factor.TYPE_CAUSE);
//		DiagramFactor diagramFactor2 = project.createNodeAndAddToDiagram2(Factor.TYPE_CAUSE);
//		FactorLinkId factorLinkId = new FactorLinkId(44);
//		createDiagramFactorLink(project, diagramFactor1.getWrappedId(), diagramFactor2.getWrappedId(), factorLinkId);
//		extraInfo = new CreateDiagramFactorLinkParameter(factorLinkId, diagramFactor1.getDiagramFactorId(), diagramFactor2.getDiagramFactorId());
//
//		verifyFields(ObjectType.DIAGRAM_LINK, extraInfo);
//	}

	public void testBasics() throws Exception
	{
		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(project);
		CommandCreateObject createObject1 = factorCommandHelper.createFactorAndDiagramFactor(ObjectType.CAUSE);
		DiagramFactorId diagramFactorId1 = (DiagramFactorId) createObject1.getCreatedId();
		DiagramFactor diagramFactor1 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId1);
		FactorCell factorCell1 = model.getFactorCellById(diagramFactorId1);
		
		CommandCreateObject createObject2 = factorCommandHelper.createFactorAndDiagramFactor(ObjectType.CAUSE);
		DiagramFactorId diagramFactorId2 = (DiagramFactorId) createObject2.getCreatedId();
		DiagramFactor diagramFactor2 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId2);
		FactorCell factorCell2 = model.getFactorCellById(diagramFactorId2);
		
		CommandDiagramAddFactorLink commandDiagramAddFactorLink = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project, diagramFactor1.getWrappedId(), diagramFactor2.getWrappedId());
		DiagramFactorLinkId diagramFactorLinkId = commandDiagramAddFactorLink.getDiagramFactorLinkId();
		DiagramFactorLink diagramFactorLink = model.getDiagramFactorLinkById(diagramFactorLinkId);
		
		LinkCell cell = model.findLinkCell(diagramFactorLink);
		assertEquals("didn't remember from?", diagramFactor1, cell.getFrom().getDiagramFactor());
		assertEquals("didn't remember to?", diagramFactor2, cell.getTo().getDiagramFactor());

		assertEquals("source not the port of from?", factorCell1.getPort(), cell.getSource());
		assertEquals("target not the port of to?", factorCell2.getPort(), cell.getTarget());
	}
	
	public void testIds() throws Exception
	{
		DiagramCauseCell factor = (DiagramCauseCell) project.createFactorCell(ObjectType.CAUSE);
		DiagramTargetCell diagramTarget = (DiagramTargetCell) project.createFactorCell(ObjectType.TARGET);
		
		FactorLinkId linkId = new FactorLinkId(5);
		DiagramFactorLinkId id = new DiagramFactorLinkId(17);
		CreateDiagramFactorLinkParameter extraInfoForTestIds = new CreateDiagramFactorLinkParameter(
				linkId, factor.getDiagramFactorId(), diagramTarget.getDiagramFactorId());
		DiagramFactorLink linkage = new DiagramFactorLink(id, extraInfoForTestIds);
		assertEquals(id, linkage.getDiagramLinkageId());
		assertEquals(linkId, linkage.getWrappedId());
		
		CreateDiagramFactorLinkParameter gotExtraInfo = (CreateDiagramFactorLinkParameter)linkage.getCreationExtraInfo();
		assertEquals(extraInfoForTestIds.getFactorLinkId(), gotExtraInfo.getFactorLinkId());
		assertEquals(extraInfoForTestIds.getFromFactorId(), gotExtraInfo.getFromFactorId());
		assertEquals(extraInfoForTestIds.getToFactorId(), gotExtraInfo.getToFactorId());
	}
	
	public void testLinkNodes() throws Exception
	{
		FactorId interventionId = project.createNodeAndAddToDiagram(ObjectType.STRATEGY);
		FactorId factorId = 	project.createNodeAndAddToDiagram(ObjectType.CAUSE);
		CreateFactorLinkParameter extraInfoForLinkParameters = new CreateFactorLinkParameter(interventionId, factorId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfoForLinkParameters);
		project.executeCommand(createModelLinkage);
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		
		DiagramFactorLinkId createdDiagramFactorLinkId = createDiagramFactorLink(project, interventionId, factorId, modelLinkageId);		
		CommandDiagramAddFactorLink command = new CommandDiagramAddFactorLink(createdDiagramFactorLinkId);
		project.executeCommand(command);
		
		assertNotNull("link not in model?", model.getDiagramFactorLinkById(command.getDiagramFactorLinkId()));
		
		ProjectServer server = project.getTestDatabase();
		DiagramFactorLink dfl = project.getDiagramModel().getDiagramFactorLinkById(command.getDiagramFactorLinkId());
		FactorLink linkage = (FactorLink)server.readObject(ObjectType.FACTOR_LINK, dfl.getWrappedId());
		assertEquals("Didn't load from id?", interventionId, linkage.getFromFactorId());
		assertEquals("Didn't load to id?", factorId, linkage.getToFactorId());
	}

	private static DiagramFactorLinkId createDiagramFactorLink(ProjectForTesting projectForTesting, FactorId interventionId, FactorId factorId, FactorLinkId modelLinkageId) throws CommandFailedException
	{
		DiagramModel diagramModel = projectForTesting.getDiagramModel();
		FactorCell factorCell = diagramModel.getFactorCellByWrappedId(interventionId);
		DiagramFactorId fromDiagramFactorId = factorCell.getDiagramFactorId();
		DiagramFactorId toDiagramFactorId = diagramModel.getFactorCellByWrappedId(factorId).getDiagramFactorId();
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
		
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
		projectForTesting.executeCommand(createDiagramLinkCommand);
    	
    	DiagramFactorLinkId diagramFactorLinkId = new DiagramFactorLinkId(createDiagramLinkCommand.getCreatedId().asInt());
    	return diagramFactorLinkId;
	}
	
	ProjectForTesting project;
	DiagramModel model;
	Factor cmIntervention;
	Factor cmTarget;
}
