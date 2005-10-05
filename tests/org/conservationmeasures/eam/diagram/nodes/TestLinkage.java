/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.main.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestLinkage extends EAMTestCase
{
	public TestLinkage(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		Node factor = new Node(Node.TYPE_FACTOR);
		Node target = new Node(Node.TYPE_TARGET);
		Linkage linkage = new Linkage(factor, target);
		assertEquals("didn't remember from?", factor, linkage.getFromNode());
		assertEquals("didn't remember to?", target, linkage.getToNode());

		assertEquals("source not the port of from?", factor.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", target.getPort(), linkage.getTarget());
	}
	
	public void testIds()
	{
		Node factor = new Node(Node.TYPE_FACTOR);
		Node target = new Node(Node.TYPE_TARGET);
		Linkage linkage = new Linkage(factor, target);
		assertEquals(Node.INVALID_ID,linkage.getId());
		int id = 243;
		linkage.setId(id);
		assertEquals(id, linkage.getId());
	}
	
	public void testLinkNodes() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();
		
		CommandInsertNode insertIntervention = new CommandInsertNode(Node.TYPE_INTERVENTION);
		CommandInsertNode insertFactor = new CommandInsertNode(Node.TYPE_FACTOR);
		insertIntervention.execute(project);
		Node intervention = model.getNodeById(insertIntervention.getId());
		insertFactor.execute(project);
		Node factor = model.getNodeById(insertFactor.getId());
		int interventionId = intervention.getId();
		int factorId = factor.getId();
		CommandLinkNodes link = new CommandLinkNodes(interventionId, factorId);
		link.execute(project);
		assertNotNull("linkage not in model?", model.getLinkageById(link.getLinkageId()));
	}
}
