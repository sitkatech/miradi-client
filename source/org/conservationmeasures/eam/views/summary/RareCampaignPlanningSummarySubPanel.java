/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
		addField(createMultilineField(RareProjectData.TAG_CAMPAIGN_SLOGAN));
		addField(createMultilineField(RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES));
		addField(createMultilineField(RareProjectData.TAG_MAIN_ACTIVITIES_NOTES));
		addLabelsOnSingleRow(EAM.text("Related Projects"), EAM.text("(see Project tab)"));
		addField(createMultilineField(RareProjectData.TAG_THREAT_REDUCTION_OBJECTIVE_NOTES));
		addField(createMultilineField(RareProjectData.TAG_MONITORING_OBJECTIVE_NOTES));
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
