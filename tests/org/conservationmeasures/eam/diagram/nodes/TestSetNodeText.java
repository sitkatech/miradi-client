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
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.project.ProjectForTesting;
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
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		CommandInsertNode insertCommand = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		insertCommand.execute(project);
		int id = insertCommand.getId();

		String sampleText = "A blue bird";
		Command setTextCommand = new CommandSetNodeText(id, sampleText);
		setTextCommand.execute(project);

		EAMGraphCell found = model.getNodeById(id);
		String foundText = (String)GraphConstants.getValue(found.getAttributes());
		assertEquals("wrong text?", sampleText, foundText);
		
		project.close();
	}
}
