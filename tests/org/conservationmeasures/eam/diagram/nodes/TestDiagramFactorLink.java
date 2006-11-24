/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddLinkage;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

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

		CreateModelNodeParameter createIntervention = new CreateModelNodeParameter(new FactorTypeStrategy());
		BaseId rawInterventionId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, createIntervention);
		ModelNodeId interventionId = new ModelNodeId(rawInterventionId.asInt());
		cmIntervention = project.findNode(interventionId);
		
		CreateModelNodeParameter createTarget = new CreateModelNodeParameter(new FactorTypeTarget());
		BaseId rawTargetId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, createTarget);
		ModelNodeId targetId = new ModelNodeId(rawTargetId.asInt());
		cmTarget = project.findNode(targetId);
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}

	public void testBasics() throws Exception
	{
		DiagramNode factor = model.createNode(cmIntervention.getModelNodeId());
		DiagramNode target = model.createNode(cmTarget.getModelNodeId());
		ModelLinkageId id = new ModelLinkageId(5);
		FactorLink cmLinkage = new FactorLink(id, factor.getWrappedId(), target.getWrappedId());
		DiagramFactorLink linkage = new DiagramFactorLink(model, cmLinkage);
		assertEquals("didn't remember from?", factor, linkage.getFromNode());
		assertEquals("didn't remember to?", target, linkage.getToNode());

		assertEquals("source not the port of from?", factor.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", target.getPort(), linkage.getTarget());
	}
	
	public void testIds() throws Exception
	{
		DiagramNode factor = model.createNode(cmIntervention.getModelNodeId());
		DiagramNode target = model.createNode(cmTarget.getModelNodeId());
		ModelLinkageId id = new ModelLinkageId(5);
		FactorLink cmLinkage = new FactorLink(id, factor.getWrappedId(), target.getWrappedId());
		DiagramFactorLink linkage = new DiagramFactorLink(model, cmLinkage);
		assertEquals(id, linkage.getDiagramLinkageId());
	}
	
	public void testLinkNodes() throws Exception
	{
		ModelNodeId interventionId = project.createNodeAndAddToDiagram(Factor.TYPE_INTERVENTION, BaseId.INVALID);
		ModelNodeId factorId = 	project.createNodeAndAddToDiagram(Factor.TYPE_CAUSE, BaseId.INVALID);
		CreateModelLinkageParameter extraInfo = new CreateModelLinkageParameter(interventionId, factorId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.MODEL_LINKAGE, extraInfo);
		project.executeCommand(createModelLinkage);
		ModelLinkageId modelLinkageId = (ModelLinkageId)createModelLinkage.getCreatedId();
		CommandDiagramAddLinkage command = new CommandDiagramAddLinkage(modelLinkageId);
		project.executeCommand(command);
		assertNotNull("link not in model?", model.getLinkageById(command.getDiagramLinkageId()));
		
		ProjectServer server = project.getTestDatabase();
		FactorLink linkage = (FactorLink)server.readObject(ObjectType.MODEL_LINKAGE, command.getDiagramLinkageId());
		assertEquals("Didn't load from id?", interventionId, linkage.getFromNodeId());
		assertEquals("Didn't load to id?", factorId, linkage.getToNodeId());
	}
	
	ProjectForTesting project;
	DiagramModel model;
	Factor cmIntervention;
	Factor cmTarget;
}
