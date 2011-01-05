/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.forms.summary.RareTabTrackingSubPanelForm;
import org.miradi.main.EAM;
import org.miradi.objects.RareProjectData;
import org.miradi.project.Project;

public class RareTrackingSummarySubPanel extends ObjectDataInputPanel
{
	public RareTrackingSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		add(new PanelTitleLabel(RareTabTrackingSubPanelForm.PROJECT_NUMBER_CONSTANT));
		add(new PanelTitleLabel(RareTabTrackingSubPanelForm.SEE_PROJECT_TAB_CONSTANT));

		addField(createStringField(RareProjectData.TAG_COHORT));
		
		add(new PanelTitleLabel(RareTabTrackingSubPanelForm.COUNTRY_CONSTANT));
		add(new PanelTitleLabel(RareTabTrackingSubPanelForm.SEE_LOCATION_TAB_CONSTANT));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Label|Rare Tracking");
}
