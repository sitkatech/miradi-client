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
package org.miradi.forms.summary;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.main.EAM;
import org.miradi.objects.RareProjectData;
import org.miradi.views.summary.RareCampaignPlanningSummarySubPanel;

public class RareTabCampaignPlanningSubPanelForm extends FieldPanelSpec
{
	public RareTabCampaignPlanningSubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(RareCampaignPlanningSummarySubPanel.PANEL_DESCRIPTION);
	
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_CAMPAIGN_SLOGAN);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_MAIN_ACTIVITIES_NOTES);
		addLeftRightConstants(RELATED_PROJECTS_CONSTANT, SEE_PROJECT_TAB_CONSTANT);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_THREAT_REDUCTION_OBJECTIVE_NOTES);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_MONITORING_OBJECTIVE_NOTES);
	}
	
	public static final String RELATED_PROJECTS_CONSTANT = EAM.text("Related Projects");
	public static final String SEE_PROJECT_TAB_CONSTANT = EAM.text("(see Project tab)");
}
