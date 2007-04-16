/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandDiagramRemoveFactorLink;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.DiagramObject;
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
		
		DiagramFactorId interventionId = project.createAndAddFactorToDiagram(ObjectType.STRATEGY);
		DiagramFactor intervention = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, interventionId));
		
		DiagramFactorId causeId = project.createAndAddFactorToDiagram(ObjectType.CAUSE);
		DiagramFactor cause = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, causeId));
		DiagramFactorLink diagramFactorLink = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project.getDiagramModel(), intervention, cause);
		
		assertTrue("link not found?", model.areLinked(interventionId, causeId));
		
		CommandDiagramRemoveFactorLink delete = new CommandDiagramRemoveFactorLink(diagramFactorLink.getDiagramLinkageId());
		delete.execute(project);
		assertFalse("link not deleted?", model.areLinked(interventionId, causeId));
		
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
		
		DiagramObject diagramObject = project.getDiagramObject();
		CommandSetObjectData removeFactor = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, causeId);
		project.executeCommand(removeFactor);
		assertFalse("node not deleted?", model.doesDiagramFactorExist(causeId));
		
		project.close();
	}
}
