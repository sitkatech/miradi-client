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
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.ViewDoer;

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
			ShowFullModelModeDoer.showFullModelModeWithoutSelecting(getProject());
			ViewData viewData = project.getViewData(getDiagramView().cardName());
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
		
		return EAM.substitute(EAM.text("[Page %s]"), Integer.toString(conceptualModelCount - 1));
		
	}
}
