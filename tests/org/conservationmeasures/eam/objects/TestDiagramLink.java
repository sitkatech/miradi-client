/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Point;
import java.util.Set;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
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
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.views.diagram.LinkCreator;
import org.conservationmeasures.eam.views.diagram.TestLinkBendPointsMoveHandler;

public class TestDiagramLink extends ObjectTestCase
{
	public TestDiagramLink(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		model = project.getDiagramModel();

		BaseId rawInterventionId = project.createObjectAndReturnId(ObjectType.STRATEGY);
		FactorId interventionId = new FactorId(rawInterventionId.asInt());
		cmIntervention = project.findNode(interventionId);
		
		BaseId rawTargetId = project.createObjectAndReturnId(ObjectType.TARGET);
		FactorId targetId = new FactorId(rawTargetId.asInt());
		cmTarget = project.findNode(targetId);
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}

	public void testAsObject() throws Exception
	{
		DiagramFactor diagramFactor1 = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		DiagramFactor diagramFactor2 = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		FactorLinkId factorLinkId = new FactorLinkId(44);
		createDiagramFactorLink(project, diagramFactor1.getWrappedId(), diagramFactor2.getWrappedId(), factorLinkId);
		CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(factorLinkId, diagramFactor1.getDiagramFactorId(), diagramFactor2.getDiagramFactorId());

		verifyFields(ObjectType.DIAGRAM_LINK, extraInfo);
	}

	public void testBasics() throws Exception
	{
		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(project, project.getDiagramModel());
		CommandCreateObject createObject1 = factorCommandHelper.createFactorAndDiagramFactor(ObjectType.CAUSE);
		DiagramFactorId diagramFactorId1 = (DiagramFactorId) createObject1.getCreatedId();
		DiagramFactor diagramFactor1 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId1);
		
		CommandCreateObject createObject2 = factorCommandHelper.createFactorAndDiagramFactor(ObjectType.CAUSE);
		DiagramFactorId diagramFactorId2 = (DiagramFactorId) createObject2.getCreatedId();
		DiagramFactor diagramFactor2 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId2);
		
		LinkCreator linkCreator = new LinkCreator(project);
		FactorLinkId factorLinkId = linkCreator.createFactorLinkAndAddToDiagramUsingCommands(project.getDiagramModel(), diagramFactor1, diagramFactor2);
		DiagramLink diagramLink = project.getDiagramModel().getDiagramFactorLinkbyWrappedId(factorLinkId);
		
		assertEquals("didn't remember from?", diagramFactor1.getId(), diagramLink.getFromDiagramFactorId());
		assertEquals("didn't remember to?", diagramFactor2.getId(), diagramLink.getToDiagramFactorId());
	}
	
	public void testIds() throws Exception
	{
		DiagramCauseCell factor = (DiagramCauseCell) project.createFactorCell(ObjectType.CAUSE);
		DiagramTargetCell diagramTarget = (DiagramTargetCell) project.createFactorCell(ObjectType.TARGET);
		
		FactorLinkId linkId = new FactorLinkId(5);
		DiagramFactorLinkId id = new DiagramFactorLinkId(17);
		CreateDiagramFactorLinkParameter extraInfoForTestIds = new CreateDiagramFactorLinkParameter(
				linkId, factor.getDiagramFactorId(), diagramTarget.getDiagramFactorId());
		DiagramLink linkage = new DiagramLink(getObjectManager(), id, extraInfoForTestIds);
		assertEquals(id, linkage.getDiagramLinkageId());
		assertEquals(linkId, linkage.getWrappedId());
		
		CreateDiagramFactorLinkParameter gotExtraInfo = (CreateDiagramFactorLinkParameter)linkage.getCreationExtraInfo();
		assertEquals(extraInfoForTestIds.getFactorLinkId(), gotExtraInfo.getFactorLinkId());
		assertEquals(extraInfoForTestIds.getFromFactorId(), gotExtraInfo.getFromFactorId());
		assertEquals(extraInfoForTestIds.getToFactorId(), gotExtraInfo.getToFactorId());
	}
	
	public void testLinkNodes() throws Exception
	{
		DiagramFactor intervention = project.createDiagramFactorAndAddToDiagram(ObjectType.STRATEGY);
		DiagramFactor cause = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		CreateFactorLinkParameter extraInfoForLinkParameters = new CreateFactorLinkParameter(intervention.getWrappedORef(), cause.getWrappedORef());
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfoForLinkParameters);
		project.executeCommand(createModelLinkage);
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		
		DiagramFactorLinkId createdDiagramFactorLinkId = createDiagramFactorLink(project, intervention.getWrappedId(), cause.getWrappedId(), modelLinkageId);		
		DiagramObject diagramObject = project.getDiagramObject();
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, createdDiagramFactorLinkId);
		project.executeCommand(addLink);
		
		assertNotNull("link not in model?", model.getDiagramFactorLinkById(createdDiagramFactorLinkId));
		
		ProjectServer server = project.getTestDatabase();
		DiagramLink dfl = project.getDiagramModel().getDiagramFactorLinkById(createdDiagramFactorLinkId);
		FactorLink linkage = (FactorLink)server.readObject(project.getObjectManager(), ObjectType.FACTOR_LINK, dfl.getWrappedId());
		assertEquals("Didn't load from ref?", intervention.getWrappedORef(), linkage.getFromFactorRef());
		assertEquals("Didn't load to ref?", cause.getWrappedORef(), linkage.getToFactorRef());
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
	
	public void testBendPointAlreadyExists() throws Exception
	{
		PointList bendPointList = TestLinkBendPointsMoveHandler.createBendPointList();
		LinkCell linkCell = project.createLinkCellWithBendPoints(bendPointList);	
		DiagramLink diagramLink = linkCell.getDiagramLink();
		
		assertEquals("bend points not added?", 3, diagramLink.getBendPoints().size());
		assertEquals("bend point doestn exist?", true, diagramLink.bendPointAlreadyExists(new Point(1, 1)));
		assertEquals("bend point doestn exist?", false, diagramLink.bendPointAlreadyExists(new Point(4, 4)));
	}
	
	public void testGetReferencedObjectTags() throws Exception
	{
		ORef diagramLinkRef = project.createDiagramLink();
		DiagramLink diagramLink = DiagramLink.find(project, diagramLinkRef);
		Set<String> refererTags = diagramLink.getReferencedObjectTags();
		assertTrue("does not contain tag?", refererTags.contains(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID));
		assertTrue("does not contain tag?", refererTags.contains(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID));
		assertTrue("does not contain tag?", refererTags.contains(DiagramLink.TAG_WRAPPED_ID));
		assertTrue("does not contain tag?", refererTags.contains(DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS));
	}
	
	public void testDirection() throws Exception
	{
		ORef diagramLinkRef = project.createDiagramLink();
		DiagramLink diagramLink = DiagramLink.find(project, diagramLinkRef);
		assertEquals(diagramLink.getFromDiagramFactorRef(), diagramLink.getDiagramFactorRef(FactorLink.FROM));
		assertEquals(diagramLink.getToDiagramFactorRef(), diagramLink.getDiagramFactorRef(FactorLink.TO));
		
		assertEquals(diagramLink.getToDiagramFactorRef(), diagramLink.getOppositeDiagramFactorRef(FactorLink.FROM));
		assertEquals(diagramLink.getFromDiagramFactorRef(), diagramLink.getOppositeDiagramFactorRef(FactorLink.TO));
		
	}

	ProjectForTesting project;
	DiagramModel model;
	Factor cmIntervention;
	Factor cmTarget;
}
