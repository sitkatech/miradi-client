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
import org.miradi.objects.RareProjectData;
import org.miradi.views.summary.RareTeamInformationSummarySubPanel;

public class RareTabTeamInformationSubPanelForm extends FieldPanelSpec
{
	public RareTabTeamInformationSubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(RareTeamInformationSummarySubPanel.PANEL_DESCRIPTION);
		
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_COURSE_MANAGER_NOTES);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_REGIONAL_DIRECTOR_NOTES);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_CAMPAIGN_MANAGER_NOTES);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_LOCAL_PARTNER_CONTACT_NOTES);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_BINGO_PARTNER_CONTACT_NOTES);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_THREAT_REDUCTION_PARTNER_CONTACT_NOTES);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_MONITORING_PARTNER_CONTACT_NOTES);
	}
}
