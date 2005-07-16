/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteLinkage;
import org.conservationmeasures.eam.commands.CommandInsertIntervention;
import org.conservationmeasures.eam.commands.CommandInsertThreat;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDeleteLinkage extends EAMTestCase
{
	public TestDeleteLinkage(String name)
	{
		super(name);
	}
	
	public void testBasics()
	{
		Project project = new Project();
		DiagramModel model = project.getDiagramModel();
		
		Command insertIntervention = new CommandInsertIntervention();
		Command insertThreat = new CommandInsertThreat();
		Node intervention = (Node)insertIntervention.execute(project);
		Node threat = (Node)insertThreat.execute(project);
		int interventionId = model.getNodeId(intervention);
		int threatId = model.getNodeId(threat);
		CommandLinkNodes link = new CommandLinkNodes(interventionId, threatId);
		Linkage linkage = (Linkage)link.execute(project);
		int linkageId = model.getLinkageId(linkage);
		
		assertTrue("linkage not found?", model.hasLinkage(intervention, threat));
		
		CommandDeleteLinkage delete = new CommandDeleteLinkage(linkageId);
		delete.execute(project);
		assertFalse("linkage not deleted?", model.hasLinkage(intervention, threat));
		
	}
}
