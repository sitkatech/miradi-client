/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.views.umbrella.ObjectPoolTablePanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiButton;

public class ResourcePoolTablePanel extends ObjectPoolTablePanel 
{
	public ResourcePoolTablePanel(UmbrellaView viewToUse, ModelessDialogPanel owningPanel)
	{
		super(viewToUse, columnTags, viewToUse.getProject().getResourcePool(), buttonActionClasses);
		owner = owningPanel;
		
		UiButton[] extraButtons = {
			createObjectsActionButton(ActionDeleteResource.class), 
		};
	
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
