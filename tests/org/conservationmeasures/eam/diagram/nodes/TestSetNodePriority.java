/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodePriority;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestSetNodePriority extends EAMTestCase 
{
	public TestSetNodePriority(String name)
	{
		super(name);
	}

	public void testSetNodePriority() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		CommandInsertNode insertCommand = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		insertCommand.execute(project);
		int id = insertCommand.getId();

		ThreatPriority createPriorityMedium = ThreatPriority.createPriorityMedium();
		int newPriority = createPriorityMedium.getValue();
		Command setPriorityCommand = new CommandSetNodePriority(id, createPriorityMedium);
		setPriorityCommand.execute(project);

		DiagramNode found = model.getNodeById(id);
		ThreatPriority foundPriority = found.getThreatPriority();
		assertEquals("wrong priority?", newPriority, foundPriority.getValue());
		
		project.close();
	}

}
