/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodePriority;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.objects.ThreatRatingValue;
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

		ThreatRatingValue createPriorityMedium = ThreatRatingValue.createMedium();
		int newPriority = createPriorityMedium.getRatingOptionId();
		Command setPriorityCommand = new CommandSetNodePriority(id, createPriorityMedium);
		setPriorityCommand.execute(project);

		DiagramNode found = model.getNodeById(id);
		ThreatRatingValue foundPriority = found.getThreatRating();
		assertEquals("wrong priority?", newPriority, foundPriority.getRatingOptionId());
		
		project.close();
	}

}
