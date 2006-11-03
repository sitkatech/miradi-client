/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.views.umbrella.ObjectManagementPanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiButton;

public class ResourceListPanel extends ObjectManagementPanel 
{
	public ResourceListPanel(UmbrellaView viewToUse, ModelessDialogPanel owningPanel)
	{
		super(viewToUse, columnTags, viewToUse.getProject().getResourcePool(), buttonActionClasses);
		owner = owningPanel;
		
		UiButton[] extraButtons = {
			createObjectsActionButton(ActionDeleteResource.class), 
		};
	
		addDoubleClickAction(ActionModifyResource.class);
		
		addButtons(extraButtons);
		setMaxColumnWidthToHeaderWidth(0);
	}

	public void objectWasSelected(BaseId newId)
	{
		super.objectWasSelected(newId);
		if(owner != null)
			owner.objectWasSelected(newId);
	}

	static final String[] columnTags = {"Initials", "Name", "Position", };
	static final Class[] buttonActionClasses = {
		ActionCreateResource.class, 
		};

	ModelessDialogPanel owner;
}
