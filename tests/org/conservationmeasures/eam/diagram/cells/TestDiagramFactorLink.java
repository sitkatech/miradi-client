/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestDiagramFactorLink extends EAMTestCase
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
		BaseId rawInterventionId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, createIntervention);
		FactorId interventionId = new FactorId(rawInterventionId.asInt());
		cmIntervention = project.findNode(interventionId);
		
		CreateFactorParameter createTarget = new CreateFactorParameter(new FactorTypeTarget());
		BaseId rawTargetId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, createTarget);
		FactorId targetId = new FactorId(rawTargetId.asInt());
		cmTarget = project.findNode(targetId);
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}

	public void testBasics() throws Exception
	{
		DiagramFactor factor = model.createDiagramFactor(cmIntervention.getModelNodeId());
		DiagramFactor target = model.createDiagramFactor(cmTarget.getModelNodeId());
		FactorLinkId id = new FactorLinkId(5);
		FactorLink cmLinkage = new FactorLink(id, factor.getWrappedId(), target.getWrappedId());
		DiagramFactorLink linkage = new DiagramFactorLink(model, cmLinkage);
		assertEquals("didn't remember from?", factor, linkage.getFromNode());
		assertEquals("didn't remember to?", target, linkage.getToNode());

		assertEquals("source not the port of from?", factor.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", target.getPort(), linkage.getTarget());
	}
	
	public void testIds() throws Exception
	{
		DiagramFactor factor = model.createDiagramFactor(cmIntervention.getModelNodeId());
		DiagramFactor target = model.createDiagramFactor(cmTarget.getModelNodeId());
		FactorLinkId id = new FactorLinkId(5);
		FactorLink cmLinkage = new FactorLink(id, factor.getWrappedId(), target.getWrappedId());
		DiagramFactorLink linkage = new DiagramFactorLink(model, cmLinkage);
		assertEquals(id, linkage.getDiagramLinkageId());
	}
	
	public void testLinkNodes() throws Exception
	{
		FactorId interventionId = project.createNodeAndAddToDiagram(Factor.TYPE_INTERVENTION, BaseId.INVALID);
		FactorId factorId = 	project.createNodeAndAddToDiagram(Factor.TYPE_CAUSE, BaseId.INVALID);
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(interventionId, factorId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.MODEL_LINKAGE, extraInfo);
		project.executeCommand(createModelLinkage);
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		CommandDiagramAddFactorLink command = new CommandDiagramAddFactorLink(modelLinkageId);
		project.executeCommand(command);
		assertNotNull("link not in model?", model.getDiagramFactorLinkById(command.getDiagramFactorLinkId()));
		
		ProjectServer server = project.getTestDatabase();
		FactorLink linkage = (FactorLink)server.readObject(ObjectType.MODEL_LINKAGE, command.getDiagramFactorLinkId());
		assertEquals("Didn't load from id?", interventionId, linkage.getFromFactorId());
		assertEquals("Didn't load to id?", factorId, linkage.getToFactorId());
	}
	
	ProjectForTesting project;
	DiagramModel model;
	Factor cmIntervention;
	Factor cmTarget;
}
