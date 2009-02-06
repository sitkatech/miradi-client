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
import org.miradi.main.EAM;
import org.miradi.objects.RareProjectData;
import org.miradi.project.Project;

public class RareTeamInformationSummarySubPanel extends ObjectDataInputPanel
{
	public RareTeamInformationSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		addField(createMediumStringField(RareProjectData.TAG_COURSE_MANAGER_NOTES));
		addField(createMediumStringField(RareProjectData.TAG_REGIONAL_DIRECTOR_NOTES));
		addField(createMediumStringField(RareProjectData.TAG_CAMPAIGN_MANAGER_NOTES));
		addField(createMediumStringField(RareProjectData.TAG_LOCAL_PARTNER_CONTACT_NOTES));
		addField(createMediumStringField(RareProjectData.TAG_BINGO_PARTNER_CONTACT_NOTES));
		addField(createMediumStringField(RareProjectData.TAG_THREAT_REDUCTION_PARTNER_CONTACT_NOTES));
		addField(createMediumStringField(RareProjectData.TAG_MONITORING_PARTNER_CONTACT_NOTES));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Label|Team Information");
}
