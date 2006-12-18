/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.commands.CommandDiagramRemoveFactorLink;
import org.conservationmeasures.eam.commands.CommandDiagramRemoveFactor;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;

public class TestDelete extends EAMTestCase
{
	public TestDelete(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();
		
		FactorId interventionId = project.createNodeAndAddToDiagram(Factor.TYPE_STRATEGY, BaseId.INVALID);
		DiagramFactor intervention = model.getDiagramFactorByWrappedId(interventionId);
		FactorId factorId = project.createNodeAndAddToDiagram(Factor.TYPE_CAUSE, BaseId.INVALID);
		DiagramFactor factor = model.getDiagramFactorByWrappedId(factorId);
		CommandDiagramAddFactorLink addLinkageCommand = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project, interventionId, factorId);
		DiagramFactorLinkId linkageId = addLinkageCommand.getDiagramFactorLinkId();
		
		assertTrue("link not found?", model.areLinked(intervention, factor));
		
		CommandDiagramRemoveFactorLink delete = new CommandDiagramRemoveFactorLink(linkageId);
		delete.execute(project);
		assertFalse("link not deleted?", model.areLinked(intervention, factor));
		
		EAM.setLogToString();
		try
		{
			delete.execute(project);
			fail("should have thrown for deleting non-existant link");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
		
		CommandDiagramRemoveFactor deleteNode = new CommandDiagramRemoveFactor(factor.getDiagramFactorId());
		deleteNode.execute(project);
		assertFalse("node not deleted?", model.doesDiagramFactorExist(factor));

		EAM.setLogToString();
		try
		{
			deleteNode.execute(project);
			fail("should have thrown for deleting non-existant link");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
		
		project.close();
	}
}
