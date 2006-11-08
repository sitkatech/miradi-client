/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.views.strategicplan.ActivitiesTableModel;
import org.conservationmeasures.eam.views.umbrella.ObjectManagementPanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ActivitiesTablePanel extends ObjectManagementPanel
{
	public ActivitiesTablePanel(UmbrellaView viewToUse, ModelessDialogPanel owningPanel)
	{
		super(viewToUse, new ActivitiesTableModel(viewToUse.getProject()), buttonActionClasses);
		owner = owningPanel;
		setMaxColumnWidthToHeaderWidth(0);
	}
	
	public void objectWasSelected(BaseId newId)
	{
		super.objectWasSelected(newId);
		if(owner != null)
			owner.objectWasSelected(newId);
	}

	ModelessDialogPanel owner;
	static final String[] columnTags = {"Label", "Strategy", "ResourceIds", };
	static final Class[] buttonActionClasses = {};
}
