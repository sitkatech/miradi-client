/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ActivitiesPanel extends ObjectManagementPanel
{
	public ActivitiesPanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new ActivitiesTableModel(viewToUse.getProject()), buttonActionClasses);
	}

	static final String[] columnTags = {"Label", "Strategy", "ResourceIds", };
	static final Class[] buttonActionClasses = {
		};
}
