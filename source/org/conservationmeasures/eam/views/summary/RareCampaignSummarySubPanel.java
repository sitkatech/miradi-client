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

public class RareCampaignSummarySubPanel extends ObjectDataInputPanel
{
	public RareCampaignSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		addLabelsOnSingleRow(EAM.text("Threats Addressed"), EAM.text("(not implemented yet)"));
		addLabelsOnSingleRow(EAM.text("Threats at Site"), EAM.text("(not implemented yet)"));
		
		addField(createNumericField(RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA));
		addField(createStringField(RareProjectData.TAG_AUDIENCE));
		
		addLabelsOnSingleRow(EAM.text("Population Size"), EAM.text("(see Location tab)"));
		addLabelsOnSingleRow(EAM.text("> Area Size (ha)"), EAM.text("(see Scope tab)"));
		addLabelsOnSingleRow(EAM.text("Habitat"), EAM.text("(not implemented yet)"));
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
