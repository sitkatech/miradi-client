/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiButton;

public class ResourceManagementPanel extends ObjectManagementPanel 
{
	public ResourceManagementPanel(UmbrellaView viewToUse)
	{
		super(viewToUse, columnTags, viewToUse.getProject().getResourcePool(), buttonActionClasses);
		
		UiButton[] extraButtons = {
			createObjectsActionButton(ActionModifyResource.class), 
			createObjectsActionButton(ActionDeleteResource.class), 
		};
	
		addDoubleClickAction(ActionModifyResource.class);
		
		addButtons(extraButtons);
	}

	static final String[] columnTags = {"Initials", "Name", "Position", };
	static final Class[] buttonActionClasses = {
		ActionCreateResource.class, 
		};

}
