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
import org.miradi.forms.summary.RareTabCampaignSubPanelForm;
import org.miradi.main.EAM;
import org.miradi.objects.RareProjectData;
import org.miradi.project.Project;

public class RareCampaignSummarySubPanel extends ObjectDataInputPanel
{
	public RareCampaignSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		addField(createStringField(RareProjectData.TAG_THREATS_ADDRESSED_NOTES));
		addLabelsOnSingleRow(RareTabCampaignSubPanelForm.THREATS_AT_SITE_CONSTANT, RareTabCampaignSubPanelForm.SEE_DIAGRAM_CONSTANT);
		
		addField(createNumericField(RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA));
		addField(createStringField(RareProjectData.TAG_AUDIENCE));
		
		addLabelsOnSingleRow(RareTabCampaignSubPanelForm.HUMAN_STAKEHOLDER_POP_SIZE_CONSTANT, RareTabCampaignSubPanelForm.SEE_SCOPE_TAB_CONSTANT);
		addLabelsOnSingleRow(RareTabCampaignSubPanelForm.BIODIVERSITY_AREA_HA_CONSTANT, RareTabCampaignSubPanelForm.SEE_SCOPE_TAB_CONSTANT);
		addLabelsOnSingleRow(RareTabCampaignSubPanelForm.HABITAT_CONSTANT, RareTabCampaignSubPanelForm.SEE_TNC_TAB_FOR_ECOREGIONS_CONSTANT);
		addField(createStringField(RareProjectData.TAG_BIODIVERSITY_HOTSPOTS));
		addField(createStringField(RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME));
		addField(createStringField(RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME));
		addField(createMultilineField(RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL));
		
		updateFieldsFromProject();
	}

	private void addLabelsOnSingleRow(String string, String string2)
	{
		add(new PanelTitleLabel(string));
		add(new PanelTitleLabel(string2));
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Label|Campaign"); 
}
