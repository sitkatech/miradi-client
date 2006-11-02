/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionModifyActivity;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiButton;

public class ActivitiesPanel extends ObjectManagementPanel
{
	public ActivitiesPanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new ActivitiesTableModel(viewToUse.getProject()), buttonActionClasses);
		addDoubleClickAction(ActionModifyActivity.class);
		UiButton[] extraButtons =
		{
				createObjectsActionButton(ActionModifyActivity.class), 
		};
		
		addButtons(extraButtons);
	}

	static final String[] columnTags = {"Label", "Strategy", "ResourceIds", };
	static final Class[] buttonActionClasses = {};
}
