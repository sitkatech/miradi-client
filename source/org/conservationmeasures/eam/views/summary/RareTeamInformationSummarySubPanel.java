/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.RareProjectData;
import org.conservationmeasures.eam.project.Project;

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
		return EAM.text("Label|Team Information");
	}
}
