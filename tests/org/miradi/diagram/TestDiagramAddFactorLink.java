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
import org.miradi.ids.BaseId;
import org.miradi.main.MiradiTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.project.ProjectForTesting;

public class TestDiagramAddFactorLink extends MiradiTestCase
{
	public TestDiagramAddFactorLink(String name)
	{
		super(name);
	}

	public void testLinkNodes() throws Exception
	{
		ProjectForTesting project = ProjectForTesting.createProjectWithDefaultObjects(getName());
		DiagramModel model = project.getTestingDiagramModel();

		FactorCell intervention = project.createFactorCell(ObjectType.STRATEGY);
		FactorCell factor = project.createFactorCell(ObjectType.CAUSE);

		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK);
		project.executeCommand(createModelLinkage);
		BaseId modelLinkageId = createModelLinkage.getCreatedId();
		ORef factorLinkRef = createModelLinkage.getObjectRef();
		project.setObjectData(factorLinkRef, FactorLink.TAG_FROM_REF, intervention.getWrappedFactorRef().toString());
		project.setObjectData(factorLinkRef, FactorLink.TAG_TO_REF, factor.getWrappedFactorRef().toString());

		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK);
    	project.executeCommand(createDiagramLinkCommand);
		
		ORef diagramLinkRef = createDiagramLinkCommand.getObjectRef();
		project.setObjectData(diagramLinkRef, DiagramLink.TAG_WRAPPED_ID, modelLinkageId.toString());
		project.setObjectData(diagramLinkRef, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID, intervention.getDiagramFactorId().toString());
		project.setObjectData(diagramLinkRef, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID, factor.getDiagramFactorId().toString());

		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkRef.getObjectId());
		project.executeCommand(addLink);

		DiagramLink linkage = model.getDiagramLinkByRef(diagramLinkRef);
		LinkCell cell = project.getTestingDiagramModel().findLinkCell(linkage);

		assertEquals("not from intervention?", intervention, cell.getFrom());
		assertEquals("not to target?", factor, cell.getTo());
		
		project.close();
	}
}
