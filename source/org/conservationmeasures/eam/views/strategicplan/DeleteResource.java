/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteResource extends ViewDoer
{
	public DeleteResource(StrategicPlanView viewToUse)
	{
		view = viewToUse;
	}
	
	public ResourcePanel getResourcePanel()
	{
		return view.getResourcePanel();
	}
	
	public boolean isAvailable()
	{
		if(getResourcePanel() == null)
			return false;
		
		return getResourcePanel().getSelectedResource() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		ProjectResource resource = getResourcePanel().getSelectedResource();
		getProject().executeCommand(new CommandBeginTransaction());
		int type = resource.getType();
		int id = resource.getId();
		getProject().executeCommand(new CommandSetObjectData(type, id, EAMObject.TAG_LABEL, EAMObject.DEFAULT_LABEL));
		getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_INITIALS, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_NAME, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_POSITION, ""));
		getProject().executeCommand(new CommandDeleteObject(type, id));
		getProject().executeCommand(new CommandEndTransaction());
	}

	StrategicPlanView view;
}
