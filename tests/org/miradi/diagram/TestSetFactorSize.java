/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.miradi.diagram;

import java.awt.Dimension;

import org.martus.util.TestCaseEnhanced;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.EnhancedJsonObject;

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
