/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;

public class CrossOrganizationSummaryPanel extends ObjectDataInputPanel
{
	public CrossOrganizationSummaryPanel(MainWindow mainWindowToUse, ProjectMetadata metadata)
	{
		super(mainWindowToUse.getProject(), metadata.getType(), metadata.getId());

		addField(createReadonlyTextField(metadata.PSEUDO_TAG_PROJECT_FILENAME));
		addField(createStringField(metadata.TAG_PROJECT_NAME));
	
		addField(createDateChooserField(metadata.TAG_START_DATE));
		addField(createDateChooserField(metadata.TAG_EXPECTED_END_DATE));
		
		addField(createDateChooserField(metadata.TAG_DATA_EFFECTIVE_DATE));
		
		addFieldTeam(mainWindowToUse);
		
		addField(createStringField(metadata.TAG_SHORT_PROJECT_SCOPE));
		addField(createMultilineField(metadata.TAG_PROJECT_SCOPE));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_VISION));
		addField(createMultilineField(metadata.TAG_PROJECT_VISION));
		addField(createNumericField(metadata.TAG_CURRENCY_DECIMAL_PLACES, 2));
		addField(createNumericField(metadata.TAG_TOTAL_BUDGET_FOR_FUNDING));
		addField(createNumericField(metadata.TAG_BUDGET_SECURED_PERCENT));


		addFieldWithCustomLabelAndHint(createNumericField(metadata.TAG_PROJECT_LATITUDE, 10), "(Latitude must be -90.0000 to +90.0000)");
		addFieldWithCustomLabelAndHint(createNumericField(metadata.TAG_PROJECT_LONGITUDE, 10), "(Longitude must be -180.0000 to +180.0000)");
				
		updateFieldsFromProject();
	}

	public void dispose()
	{
		super.dispose();
		teamEditorComponent.dispose();
	}

	private void addFieldTeam(MainWindow mainWindowToUse)
	{
		addLabel(EAM.text("Label|Team Members"));
		teamEditorComponent = new TeamEditorComponent(getProject(), mainWindowToUse.getActions());
		add(teamEditorComponent);
		addLabel("");
		addLabel(EAM.text("Label|<html><em>NOTE: Resources will only be " +
				"shown above if they have the Team Member role checked</em></html>"));
	}

	public String getPanelDescription()
	{
		return EAM.text("General");
	}

	TeamEditorComponent teamEditorComponent;
}
