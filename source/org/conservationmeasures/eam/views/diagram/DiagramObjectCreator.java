/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;

public class DiagramObjectCreator
{
 	public DiagramObjectCreator(Project projectToUse)
	{
		project = projectToUse;
	}

 	public DiagramObject createDiagramObject(int diagramObjectType) throws Exception
 	{
 		CommandCreateObject createCommand = new CommandCreateObject(diagramObjectType);
 		project.executeCommand(createCommand);
 		
 		BaseId diagramObjectId = createCommand.getCreatedId();
 		DiagramObject diagramObject = (DiagramObject) project.findObject(new ORef(diagramObjectType, diagramObjectId));
 		return diagramObject;
 	}
 	
	private Project project;	
}
