/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.diagram;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.project.ProjectForTesting;
import org.miradi.views.diagram.LinkCreator;

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
		LinkCreator linkCreator = new LinkCreator(project);
		FactorLinkId factorLinkId = linkCreator.createFactorLinkAndAddToDiagramUsingCommands(project.getDiagramModel(), intervention, cause);
		DiagramLink diagramLink = model.getDiagramFactorLinkbyWrappedId(factorLinkId);
		
		assertTrue("link not found?", model.areLinked(interventionId, causeId));

		DiagramObject diagramObject1 = project.getDiagramObject();
		CommandSetObjectData removeLink = CommandSetObjectData.createRemoveIdCommand(diagramObject1, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLink.getDiagramLinkageId());
		project.executeCommand(removeLink);
		
	
		DiagramObject diagramObject2 = project.getDiagramObject();
		CommandSetObjectData removeFactor = CommandSetObjectData.createRemoveIdCommand(diagramObject2, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, causeId);
		project.executeCommand(removeFactor);
		assertFalse("node not deleted?", model.doesDiagramFactorExist(causeId));
		
		project.close();
	}
}
