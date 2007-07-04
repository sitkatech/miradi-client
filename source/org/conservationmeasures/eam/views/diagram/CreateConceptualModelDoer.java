/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.dialogs.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateConceptualModelDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (!isDiagramView())
			return false;

		if (getDiagramView().isResultsChainTab())
			return false;
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		Project project = getProject();
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			createConceptualModelDiagram(project);
		}
		catch (Exception e)
		{
			EAM.errorDialog("Could not create Conceptual Model");
			EAM.logException(e);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private void createConceptualModelDiagram(Project project) throws Exception
	{
		CommandCreateObject createConcetualModel = new CommandCreateObject(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		project.executeCommand(createConcetualModel);

		ORef createdDiagramObjectRef = new ORef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, createConcetualModel.getCreatedId());
		DiagramObject diagramObject = (DiagramObject) project.findObject(createdDiagramObjectRef);
		
		DiagramSplitPane.createDiagram(getMainWindow(), diagramObject);
		DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
		DiagramSplitPane diagramSplitPane = diagramPanel.getDiagramSplitPane();
		DiagramPageList diagramPageList = diagramSplitPane.getDiagramPageList();
		diagramPageList.fillListWithSelectedDiagramObject(diagramObject);		
	}
}
