/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.diagram;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramFactorLinkId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.project.ProjectForTesting;

public class TestDiagramAddFactorLink extends EAMTestCase
{
	public TestDiagramAddFactorLink(String name)
	{
		super(name);
	}

	public void testLinkNodes() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		FactorId interventionId = project.createNodeAndAddToDiagram(ObjectType.STRATEGY);
		FactorCell intervention = model.getFactorCellByWrappedId(interventionId);
		FactorId factorId = project.createNodeAndAddToDiagram(ObjectType.CAUSE);
		FactorCell factor = model.getFactorCellByWrappedId(factorId);

		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(intervention.getWrappedORef(), factor.getWrappedORef());
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createModelLinkage);
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();

		DiagramFactorId fromDiagramFactorId = project.getDiagramModel().getFactorCellByWrappedId(interventionId).getDiagramFactorId();
		DiagramFactorId toDiagramFactorId = project.getDiagramModel().getFactorCellByWrappedId(factorId).getDiagramFactorId();
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
		
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
    	project.executeCommand(createDiagramLinkCommand);
		
    	BaseId createdId = createDiagramLinkCommand.getCreatedId();
		DiagramFactorLinkId diagramFactorLinkId = new DiagramFactorLinkId(createdId.asInt());
		DiagramObject diagramObject = project.getDiagramObject();
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramFactorLinkId);
		project.executeCommand(addLink);

		DiagramLink linkage = model.getDiagramFactorLinkById(diagramFactorLinkId);
		LinkCell cell = project.getDiagramModel().findLinkCell(linkage);

		assertEquals("not from intervention?", intervention, cell.getFrom());
		assertEquals("not to target?", factor, cell.getTo());
		
		project.close();
	}
}
