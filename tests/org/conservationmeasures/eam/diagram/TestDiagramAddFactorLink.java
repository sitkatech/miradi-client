/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.ProjectForTesting;

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

		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(interventionId, factorId);
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
