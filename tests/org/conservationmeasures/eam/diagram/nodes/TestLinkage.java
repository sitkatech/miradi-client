/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestLinkage extends EAMTestCase
{
	public TestLinkage(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		Node threat = new Node(Node.TYPE_THREAT);
		Node goal = new Node(Node.TYPE_GOAL);
		Linkage linkage = new Linkage(threat, goal);
		assertEquals("didn't remember from?", threat, linkage.getFromNode());
		assertEquals("didn't remember to?", goal, linkage.getToNode());

		assertEquals("source not the port of from?", threat.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", goal.getPort(), linkage.getTarget());
	}
	
	public void testIds()
	{
		Node threat = new Node(Node.TYPE_THREAT);
		Node goal = new Node(Node.TYPE_GOAL);
		Linkage linkage = new Linkage(threat, goal);
		assertEquals(Node.INVALID_ID,linkage.getId());
		int id = 243;
		linkage.setId(id);
		assertEquals(id, linkage.getId());
	}
	
	public void testLinkNodes() throws Exception
	{
		Project project = new Project();
		DiagramModel model = project.getDiagramModel();
		
		CommandInsertNode insertIntervention = new CommandInsertNode(Node.TYPE_INTERVENTION);
		CommandInsertNode insertThreat = new CommandInsertNode(Node.TYPE_THREAT);
		insertIntervention.execute(project);
		Node intervention = model.getNodebyId(insertIntervention.getId());
		insertThreat.execute(project);
		Node threat = model.getNodebyId(insertThreat.getId());
		int interventionId = intervention.getId();
		int threatId = threat.getId();
		CommandLinkNodes link = new CommandLinkNodes(interventionId, threatId);
		link.execute(project);
		assertNotNull("linkage not in model?", model.getLinkagebyId(link.getLinkageId()));
		
		
	}
}
