/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.views.umbrella.LegacyObjectPoolTablePanel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ActivityPoolTablePanel extends LegacyObjectPoolTablePanel implements ObjectPicker
{
	public ActivityPoolTablePanel(UmbrellaView viewToUse, ModelessDialogPanel owningPanel)
	{
		super(viewToUse, new ActivityPoolTableModel(viewToUse.getProject()), buttonActionClasses);
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
