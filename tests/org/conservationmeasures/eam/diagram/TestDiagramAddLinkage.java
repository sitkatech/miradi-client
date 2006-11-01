/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandDiagramAddLinkage;
import org.conservationmeasures.eam.commands.CommandDiagramRemoveNode;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramAddLinkage extends EAMTestCase
{
	public TestDiagramAddLinkage(String name)
	{
		super(name);
	}

	public void testLinkNodes() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		ModelNodeId interventionId = CommandDiagramRemoveNode.createNode(project, DiagramNode.TYPE_INTERVENTION, BaseId.INVALID);
		DiagramNode intervention = model.getNodeById(interventionId);
		ModelNodeId factorId = CommandDiagramRemoveNode.createNode(project, DiagramNode.TYPE_FACTOR, BaseId.INVALID);
		DiagramNode factor = model.getNodeById(factorId);

		CommandDiagramAddLinkage command = new CommandDiagramAddLinkage(interventionId, factorId);
		command.execute(project);
		DiagramLinkage linkage = model.getLinkageById(command.getLinkageId());

		assertEquals("not from intervention?", intervention, linkage.getFromNode());
		assertEquals("not to target?", factor, linkage.getToNode());
		
		project.close();
	}
}
