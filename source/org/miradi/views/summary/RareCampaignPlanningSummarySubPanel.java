/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.main.EAM;
import org.miradi.objects.RareProjectData;
import org.miradi.project.Project;

public class RareCampaignPlanningSummarySubPanel extends ObjectDataInputPanel
{
	public RareCampaignPlanningSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		addField(createMultilineField(RareProjectData.getObjectType(), RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE));
		addField(createMultilineField(RareProjectData.TAG_CAMPAIGN_SLOGAN));
		addField(createMultilineField(RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES));
		addField(createMultilineField(RareProjectData.TAG_MAIN_ACTIVITIES_NOTES));
		addLabelsOnSingleRow(EAM.text("Related Projects"), EAM.text("(see Project tab)"));
		addField(createMultilineField(RareProjectData.TAG_THREAT_REDUCTION_OBJECTIVE_NOTES));
		addField(createMultilineField(RareProjectData.TAG_MONITORING_OBJECTIVE_NOTES));
		
		updateFieldsFromProject();
	}

	private void addLabelsOnSingleRow(String string, String string2)
	{
		add(new PanelTitleLabel(string));
		add(new PanelTitleLabel(string2));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Label|Campaign Planning");
	}

}
