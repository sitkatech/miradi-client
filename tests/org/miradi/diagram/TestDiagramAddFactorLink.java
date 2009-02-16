/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.diagram;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
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
		PersistentDiagramModel model = project.getDiagramModel();

		FactorCell intervention = project.createFactorCell(ObjectType.STRATEGY);
		FactorCell factor = project.createFactorCell(ObjectType.CAUSE);

		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(intervention.getWrappedFactorRef(), factor.getWrappedFactorRef());
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createModelLinkage);
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();

		DiagramFactorId fromDiagramFactorId = intervention.getDiagramFactorId();
		DiagramFactorId toDiagramFactorId = factor.getDiagramFactorId();
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
		
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
    	project.executeCommand(createDiagramLinkCommand);
		
		ORef diagramLinkRef = createDiagramLinkCommand.getObjectRef();
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkRef.getObjectId());
		project.executeCommand(addLink);

		DiagramLink linkage = model.getDiagramLinkByRef(diagramLinkRef);
		LinkCell cell = project.getDiagramModel().findLinkCell(linkage);

		assertEquals("not from intervention?", intervention, cell.getFrom());
		assertEquals("not to target?", factor, cell.getTo());
		
		project.close();
	}
}
