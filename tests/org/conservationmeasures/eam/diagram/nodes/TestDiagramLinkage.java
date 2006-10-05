/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramLinkage extends EAMTestCase
{
	public TestDiagramLinkage(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		model = project.getDiagramModel();

		CreateModelNodeParameter createIntervention = new CreateModelNodeParameter(new NodeTypeIntervention());
		BaseId interventionId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, createIntervention);
		cmIntervention = project.findNode(interventionId);
		
		CreateModelNodeParameter createTarget = new CreateModelNodeParameter(new NodeTypeTarget());
		BaseId targetId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, createTarget);
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
		BaseId id = new BaseId(5);
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(id, factor.getWrappedId(), target.getWrappedId());
		DiagramLinkage linkage = new DiagramLinkage(model, cmLinkage);
		assertEquals("didn't remember from?", factor, linkage.getFromNode());
		assertEquals("didn't remember to?", target, linkage.getToNode());

		assertEquals("source not the port of from?", factor.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", target.getPort(), linkage.getTarget());
	}
	
	public void testIds() throws Exception
	{
		DiagramNode factor = model.createNode(cmIntervention.getModelNodeId());
		DiagramNode target = model.createNode(cmTarget.getModelNodeId());
		BaseId id = new BaseId(5);
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(id, factor.getWrappedId(), target.getWrappedId());
		DiagramLinkage linkage = new DiagramLinkage(model, cmLinkage);
		assertEquals(id, linkage.getDiagramLinkageId());
	}
	
	public void testLinkNodes() throws Exception
	{
		CommandInsertNode insertIntervention = new CommandInsertNode(DiagramNode.TYPE_INTERVENTION);
		insertIntervention.execute(project);

		CommandInsertNode insertFactor = new CommandInsertNode(DiagramNode.TYPE_FACTOR);
		insertFactor.execute(project);

		DiagramNode intervention = model.getNodeById(insertIntervention.getId());
		DiagramNode factor = model.getNodeById(insertFactor.getId());
		ModelNodeId interventionId = intervention.getWrappedId();
		ModelNodeId factorId = factor.getWrappedId();
		CommandLinkNodes link = new CommandLinkNodes(interventionId, factorId);
		link.execute(project);
		assertNotNull("linkage not in model?", model.getLinkageById(link.getLinkageId()));
		
		ProjectServer server = project.getTestDatabase();
		ConceptualModelLinkage linkage = (ConceptualModelLinkage)server.readObject(ObjectType.MODEL_LINKAGE, link.getLinkageId());
		assertEquals("Didn't load from id?", interventionId, linkage.getFromNodeId());
		assertEquals("Didn't load to id?", factorId, linkage.getToNodeId());
	}
	
	ProjectForTesting project;
	DiagramModel model;
	ConceptualModelNode cmIntervention;
	ConceptualModelNode cmTarget;
}
