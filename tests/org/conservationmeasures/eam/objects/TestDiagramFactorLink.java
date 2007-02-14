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
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ProjectForTesting;

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

		CreateFactorParameter createIntervention = new CreateFactorParameter(new FactorTypeStrategy());
		BaseId rawInterventionId = project.createObject(ObjectType.FACTOR, BaseId.INVALID, createIntervention);
		FactorId interventionId = new FactorId(rawInterventionId.asInt());
		cmIntervention = project.findNode(interventionId);
		
		CreateFactorParameter createTarget = new CreateFactorParameter(new FactorTypeTarget());
		BaseId rawTargetId = project.createObject(ObjectType.FACTOR, BaseId.INVALID, createTarget);
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
		CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(
				new FactorLinkId(27), new DiagramFactorId(29), new DiagramFactorId(99));
		verifyFields(ObjectType.DIAGRAM_LINK, extraInfo);
	}

	public void testBasics() throws Exception
	{
		DiagramFactor factor = model.createDiagramFactor(cmIntervention.getFactorId());
		DiagramFactor target = model.createDiagramFactor(cmTarget.getFactorId());
		FactorLinkId id = new FactorLinkId(5);
		FactorLink cmLinkage = new FactorLink(id, factor.getWrappedId(), target.getWrappedId());
		DiagramFactorLink linkage = model.createDiagramFactorLink(cmLinkage);
		LinkCell cell = model.findLinkCell(linkage);
		assertEquals("didn't remember from?", factor, cell.getFrom());
		assertEquals("didn't remember to?", target, cell.getTo());

		assertEquals("source not the port of from?", factor.getPort(), cell.getSource());
		assertEquals("target not the port of to?", target.getPort(), cell.getTarget());
	}
	
	public void testIds() throws Exception
	{
		DiagramFactor factor = model.createDiagramFactor(cmIntervention.getFactorId());
		DiagramFactor target = model.createDiagramFactor(cmTarget.getFactorId());
		FactorLinkId linkId = new FactorLinkId(5);
		DiagramFactorLinkId id = new DiagramFactorLinkId(17);
		CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(
				linkId, factor.getDiagramFactorId(), target.getDiagramFactorId());
		DiagramFactorLink linkage = new DiagramFactorLink(id, extraInfo);
		assertEquals(id, linkage.getDiagramLinkageId());
		assertEquals(linkId, linkage.getWrappedId());
		
		CreateDiagramFactorLinkParameter gotExtraInfo = (CreateDiagramFactorLinkParameter)linkage.getCreationExtraInfo();
		assertEquals(extraInfo.getFactorLinkId(), gotExtraInfo.getFactorLinkId());
		assertEquals(extraInfo.getFromFactorId(), gotExtraInfo.getFromFactorId());
		assertEquals(extraInfo.getToFactorId(), gotExtraInfo.getToFactorId());
	}
	
	public void testLinkNodes() throws Exception
	{
		FactorId interventionId = project.createNodeAndAddToDiagram(Factor.TYPE_STRATEGY, BaseId.INVALID);
		FactorId factorId = 	project.createNodeAndAddToDiagram(Factor.TYPE_CAUSE, BaseId.INVALID);
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(interventionId, factorId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createModelLinkage);
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		CommandDiagramAddFactorLink command = new CommandDiagramAddFactorLink(modelLinkageId);
		project.executeCommand(command);
		assertNotNull("link not in model?", model.getDiagramFactorLinkById(command.getDiagramFactorLinkId()));
		
		ProjectServer server = project.getTestDatabase();
		DiagramFactorLink dfl = project.getDiagramModel().getDiagramFactorLinkById(command.getDiagramFactorLinkId());
		FactorLink linkage = (FactorLink)server.readObject(ObjectType.FACTOR_LINK, dfl.getWrappedId());
		assertEquals("Didn't load from id?", interventionId, linkage.getFromFactorId());
		assertEquals("Didn't load to id?", factorId, linkage.getToFactorId());
	}
	
	ProjectForTesting project;
	DiagramModel model;
	Factor cmIntervention;
	Factor cmTarget;
}
