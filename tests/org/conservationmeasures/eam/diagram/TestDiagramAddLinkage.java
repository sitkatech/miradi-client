/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramAddLinkage extends EAMTestCase
{
	public TestDiagramAddLinkage(String name)
	{
		super(name);
	}

	public void testLinkNodes() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		ModelNodeId interventionId = project.createNodeAndAddToDiagram(Factor.TYPE_INTERVENTION, BaseId.INVALID);
		DiagramNode intervention = model.getNodeById(interventionId);
		ModelNodeId factorId = project.createNodeAndAddToDiagram(Factor.TYPE_CAUSE, BaseId.INVALID);
		DiagramNode factor = model.getNodeById(factorId);

		CreateModelLinkageParameter extraInfo = new CreateModelLinkageParameter(interventionId, factorId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.MODEL_LINKAGE, extraInfo);
		project.executeCommand(createModelLinkage);
		ModelLinkageId modelLinkageId = (ModelLinkageId)createModelLinkage.getCreatedId();
		CommandDiagramAddFactorLink command = new CommandDiagramAddFactorLink(modelLinkageId);
		project.executeCommand(command);

		DiagramFactorLink linkage = model.getLinkageById(command.getDiagramLinkageId());

		assertEquals("not from intervention?", intervention, linkage.getFromNode());
		assertEquals("not to target?", factor, linkage.getToNode());
		
		project.close();
	}
}
