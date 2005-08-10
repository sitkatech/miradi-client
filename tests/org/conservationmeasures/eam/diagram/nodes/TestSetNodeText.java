/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.DiagramModel;
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

	public void testSetNodeText() throws Exception
	{
		Project project = new Project();
		DiagramModel model = project.getDiagramModel();

		CommandInsertNode insertCommand = new CommandInsertNode(Node.TYPE_GOAL);
		insertCommand.execute(project);
		int id = insertCommand.getId();

		String sampleText = "A blue bird";
		Command setTextCommand = new CommandSetNodeText(id, sampleText);
		setTextCommand.execute(project);

		EAMGraphCell found = model.getNodebyId(id);
		String foundText = (String)GraphConstants.getValue(found.getAttributes());
		assertEquals("wrong text?", sampleText, foundText);
	}
}
