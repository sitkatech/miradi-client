/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.annotations.ResourcePool;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectResource;

public class ResourcePanel extends ObjectManagementPanel 
{
	public ResourcePanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, columnTags, mainWindowToUse.getProject().getResourcePool(), buttonActionClasses);
	}
	
	public ProjectResource getSelectedResource()
	{
		int row = table.getSelectedRow();
		if(row < 0)
			return null;
		
		ResourcePool pool = getProject().getResourcePool();
		int resourceId = pool.getIds()[row];
		ProjectResource resource = pool.find(resourceId);
		return resource;
	}
	
	static final String[] columnTags = {"Initials", "Name", "Position", };
	static final Class[] buttonActionClasses = {
		ActionCreateResource.class, 
		ActionModifyResource.class, 
		ActionDeleteResource.class, 
		};

}
