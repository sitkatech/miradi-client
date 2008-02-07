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

public class RareCampaignSummarySubPanel extends ObjectDataInputPanel
{
	public RareCampaignSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		addField(createStringField(RareProjectData.TAG_THREATS_ADDRESSED_NOTES));
		addLabelsOnSingleRow(EAM.text("Threats at Site"), EAM.text("(see Diagram)"));
		
		addField(createNumericField(RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA));
		addField(createStringField(RareProjectData.TAG_AUDIENCE));
		
		addLabelsOnSingleRow(EAM.text("Human Stakeholder Pop Size"), EAM.text("(see Scope tab)"));
		addLabelsOnSingleRow(EAM.text("Biodiversity Area (ha)"), EAM.text("(see Scope tab)"));
		addLabelsOnSingleRow(EAM.text("Habitat"), EAM.text("(see TNC tab for Ecoregions)"));
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
		return EAM.text("Label|Campaign");
	}

}
