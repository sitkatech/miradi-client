/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.RareProjectData;
import org.conservationmeasures.eam.project.Project;

public class RareCampaignPlanningSummarySubPanel extends ObjectDataInputPanel
{
	public RareCampaignPlanningSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		addField(createMultilineField(RareProjectData.getObjectType(), RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE, 200));
		addField(createStringField(RareProjectData.TAG_CAMPAIGN_SLOGAN));
		addField(createStringField(RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES));
		addLabelsOnSingleRow(EAM.text("Main activities of the projects (no more than 200 words)"), EAM.text("(not implemented yet)"));
		addField(createStringField(RareProjectData.TAG_RELATED_PROJECTS));
		addLabelsOnSingleRow(EAM.text("Summary of Objectives for Threat Reduction Partner "), EAM.text("(not implemented yet)"));
		addLabelsOnSingleRow(EAM.text("Summary of Objectives for Impact Monitoring Partner"), EAM.text("(not implemented yet)"));
		
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
