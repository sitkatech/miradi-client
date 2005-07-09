/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertGoal;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphConstants;

public class TestSetNodeText extends EAMTestCase
{
	public TestSetNodeText(String name)
	{
		super(name);
	}

	public void testSetNodeText()
	{
		Project project = new Project();
		DiagramModel model = project.getDiagramModel();

		Command insertCommand = new CommandInsertGoal();
		Node node = (Node)insertCommand.execute(project);
		int id = model.getNodeId(node);

		String sampleText = "A blue bird";
		Command setTextCommand = new CommandSetNodeText(id, sampleText);
		setTextCommand.execute(project);

		EAMGraphCell found = model.getNodeById(id);
		String foundText = (String)GraphConstants.getValue(found.getMap());
		assertEquals("wrong text?", sampleText, foundText);
	}
}
