/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import java.awt.Dimension;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetFactorSize;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.martus.util.TestCaseEnhanced;

public class TestSetFactorSize extends TestCaseEnhanced 
{
	public TestSetFactorSize(String name)
	{
		super(name);
	}

	public void testSetNodeSize() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		FactorId modelNodeId = project.createNodeAndAddToDiagram(Factor.TYPE_TARGET, BaseId.INVALID);
		DiagramFactor found = model.getNodeById(modelNodeId);
		Dimension newSize = new Dimension(200,300);
		Command setNodeSize = new CommandSetFactorSize(found.getDiagramNodeId(), newSize, found.getPreviousSize());
		setNodeSize.execute(project);

		Dimension foundSize = found.getSize();
		assertEquals("wrong size?", newSize, foundSize);
		
		project.close();
	}

}
