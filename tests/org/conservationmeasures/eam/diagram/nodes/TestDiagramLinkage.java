/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
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
		model = project.getDiagramModel();
		cmIntervention = new ConceptualModelIntervention();
		cmIntervention.setId(node1Id);
		cmTarget = new ConceptualModelTarget();
		cmTarget.setId(node2Id);
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}

	public void testBasics() throws Exception
	{
		DiagramNode factor = model.createNode(cmIntervention);
		DiagramNode target = model.createNode(cmTarget);
		int id = 5;
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(id, factor.getId(), target.getId());
		DiagramLinkage linkage = new DiagramLinkage(model, cmLinkage);
		assertEquals("didn't remember from?", factor, linkage.getFromNode());
		assertEquals("didn't remember to?", target, linkage.getToNode());

		assertEquals("source not the port of from?", factor.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", target.getPort(), linkage.getTarget());
	}
	
	public void testIds() throws Exception
	{
		DiagramNode factor = model.createNode(cmIntervention);
		DiagramNode target = model.createNode(cmTarget);
		int id = 5;
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(id, factor.getId(), target.getId());
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
		int interventionId = intervention.getId();
		int factorId = factor.getId();
		CommandLinkNodes link = new CommandLinkNodes(interventionId, factorId);
		link.execute(project);
		assertNotNull("linkage not in model?", model.getLinkageById(link.getLinkageId()));
	}
	
	static final int node1Id = 1;
	static final int node2Id = 2;
	
	ProjectForTesting project;
	DiagramModel model;
	ConceptualModelIntervention cmIntervention;
	ConceptualModelTarget cmTarget;
}
