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
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
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
		objectPool = project.getNodePool();
		model = project.getDiagramModel();
		
		cmIntervention = new ConceptualModelIntervention(node1Id);
		objectPool.put(cmIntervention);
		
		cmTarget = new ConceptualModelTarget(node2Id);
		objectPool.put(cmTarget);
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}

	public void testBasics() throws Exception
	{
		DiagramNode factor = model.createNode(cmIntervention.getId());
		DiagramNode target = model.createNode(cmTarget.getId());
		BaseId id = new BaseId(5);
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(id, factor.getDiagramNodeId(), target.getDiagramNodeId());
		DiagramLinkage linkage = new DiagramLinkage(model, cmLinkage);
		assertEquals("didn't remember from?", factor, linkage.getFromNode());
		assertEquals("didn't remember to?", target, linkage.getToNode());

		assertEquals("source not the port of from?", factor.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", target.getPort(), linkage.getTarget());
	}
	
	public void testIds() throws Exception
	{
		DiagramNode factor = model.createNode(cmIntervention.getId());
		DiagramNode target = model.createNode(cmTarget.getId());
		BaseId id = new BaseId(5);
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(id, factor.getDiagramNodeId(), target.getDiagramNodeId());
		DiagramLinkage linkage = new DiagramLinkage(model, cmLinkage);
		assertEquals(id, linkage.getId());
	}
	
	public void testLinkNodes() throws Exception
	{
		CommandInsertNode insertIntervention = new CommandInsertNode(DiagramNode.TYPE_INTERVENTION);
		CommandInsertNode insertFactor = new CommandInsertNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		insertIntervention.execute(project);
		DiagramNode intervention = model.getNodeById(insertIntervention.getId());
		insertFactor.execute(project);
		DiagramNode factor = model.getNodeById(insertFactor.getId());
		BaseId interventionId = intervention.getDiagramNodeId();
		BaseId factorId = factor.getDiagramNodeId();
		CommandLinkNodes link = new CommandLinkNodes(interventionId, factorId);
		link.execute(project);
		assertNotNull("linkage not in model?", model.getLinkageById(link.getLinkageId()));
		
		ProjectServer server = project.getTestDatabase();
		ConceptualModelLinkage linkage = server.readLinkage(link.getLinkageId());
		assertEquals("Didn't load from id?", interventionId, linkage.getFromNodeId());
		assertEquals("Didn't load to id?", factorId, linkage.getToNodeId());
	}
	
	static final BaseId node1Id = new BaseId(1);
	static final BaseId node2Id = new BaseId(2);
	
	ProjectForTesting project;
	NodePool objectPool;
	DiagramModel model;
	ConceptualModelIntervention cmIntervention;
	ConceptualModelTarget cmTarget;
}
