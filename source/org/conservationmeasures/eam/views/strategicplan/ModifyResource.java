/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;



import org.conservationmeasures.eam.dialogs.ObjectPropertiesDialog;
import org.conservationmeasures.eam.dialogs.ProjectResourcePropertiesDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class ModifyResource extends MainWindowDoer
{
	public ModifyResource(StrategicPlanView viewToUse)
	{
		view = viewToUse;
	}
	
	public ResourceManagementPanel getResourcePanel()
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
		modify(resource);
	}

	public void modify(ProjectResource resource)
	{
		String[] tagsToEdit = new String[] {"Initials", "Name", "Position"};
		ObjectPropertiesDialog dlg = new ProjectResourcePropertiesDialog(getMainWindow(), resource, tagsToEdit);
		dlg.setVisible(true);
	}

	StrategicPlanView view;
}
