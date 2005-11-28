/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Dimension;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.martus.util.TestCaseEnhanced;

public class TestSetNodeSize extends TestCaseEnhanced 
{
	public TestSetNodeSize(String name)
	{
		super(name);
	}

	public void testSetNodeSize() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		CommandInsertNode insertCommand = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		insertCommand.execute(project);
		int id = insertCommand.getId();
		DiagramNode found = model.getNodeById(id);
		Dimension newSize = new Dimension(200,300);
		Command setNodeSize = new CommandSetNodeSize(id, newSize, found.getPreviousSize());
		setNodeSize.execute(project);

		Dimension foundSize = found.getSize();
		assertEquals("wrong size?", newSize, foundSize);
		
		project.close();
	}

}
