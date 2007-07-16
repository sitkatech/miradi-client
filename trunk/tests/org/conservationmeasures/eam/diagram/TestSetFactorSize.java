/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import java.awt.Dimension;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
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

		FactorId modelNodeId = project.createNodeAndAddToDiagram(ObjectType.TARGET);
		FactorCell found = model.getFactorCellByWrappedId(modelNodeId);
		String newSize = EnhancedJsonObject.convertFromDimension(new Dimension(200,300));
		
		DiagramFactorId diagramFactorId = found.getDiagramFactorId();
		CommandSetObjectData cmd = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, diagramFactorId, DiagramFactor.TAG_SIZE, newSize);
		project.executeCommand(cmd);
		
		ORef oRef = new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(oRef);
		String foundSize = EnhancedJsonObject.convertFromDimension(diagramFactor.getSize());
		assertEquals("wrong size?", newSize, foundSize);
		
		project.close();
	}

}
