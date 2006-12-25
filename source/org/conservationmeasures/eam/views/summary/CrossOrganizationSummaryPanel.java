/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
		
		addField(createNumericField(metadata.TAG_CURRENCY_DECIMAL_PLACES, 2));
		addField(createDateChooserField(metadata.TAG_DATA_EFFECTIVE_DATE));
		
		addFieldTeam(mainWindowToUse);
		
		addField(createMultilineField(metadata.TAG_PROJECT_SCOPE));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_SCOPE));
		addField(createMultilineField(metadata.TAG_PROJECT_VISION));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_VISION));

		updateFieldsFromProject();
	}

	public void dispose()
	{
		super.dispose();
		teamEditorComponent.dispose();
	}

	private void addFieldTeam(MainWindow mainWindowToUse)
	{
		addLabel(EAM.text("Label|Team Members:"));
		teamEditorComponent = new TeamEditorComponent(getProject(), mainWindowToUse.getActions());
		add(teamEditorComponent);
	}

	public String getPanelDescription()
	{
		return EAM.text("General");
	}

	TeamEditorComponent teamEditorComponent;
}
