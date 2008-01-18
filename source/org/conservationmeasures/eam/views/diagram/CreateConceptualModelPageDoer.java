/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateConceptualModelPageDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (!isInDiagram())
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
			ViewData viewData = project.getViewData(getDiagramView().cardName());
			setToDefaultMode(viewData);
			createConceptualModelPage(viewData);
		}
		catch (Exception e)
		{
			EAM.errorDialog("Could not create Conceptual Model Page");
			EAM.logException(e);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	private void setToDefaultMode(ViewData viewData) throws Exception
	{
		if (viewData.getData(ViewData.TAG_CURRENT_MODE).equals(ViewData.MODE_DEFAULT))
			return;
	
		CommandSetObjectData setMode = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_MODE, ViewData.MODE_DEFAULT);
		getProject().executeCommand(setMode);
	}
	
	private void createConceptualModelPage(ViewData viewData) throws Exception
	{
		CommandSetObjectData setCurrentDiagramToInvalid = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF, ORef.INVALID);
		getProject().executeCommand(setCurrentDiagramToInvalid);
		
		CommandCreateObject createConceptualModel = new CommandCreateObject(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		getProject().executeCommand(createConceptualModel);
		
		DiagramObject diagramObject = (DiagramObject) getProject().findObject(createConceptualModel.getObjectRef());
		CommandSetObjectData setLabel = new CommandSetObjectData(diagramObject.getRef(), DiagramObject.TAG_LABEL, getConceptualModelPageName(getProject(), diagramObject));
		getProject().executeCommand(setLabel);
		
		CommandSetObjectData setCurrentDiagram = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF, createConceptualModel.getObjectRef());
		getProject().executeCommand(setCurrentDiagram);
	}

	private String getConceptualModelPageName(Project project, DiagramObject diagramObject)
	{
		int conceptualModelCount = project.getConceptualModelDiagramPool().size();
		if (conceptualModelCount == 1)
			return diagramObject.toString();
		
		return EAM.text("[Page " + Integer.toString(conceptualModelCount - 1) + "]");
		
	}
}
