/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;

public class SummaryTeamPanel extends ObjectDataInputPanel
{
	public SummaryTeamPanel(MainWindow mainWindowToUse, ProjectMetadata metadata)
	{
		super(mainWindowToUse.getProject(), metadata.getType(), metadata.getId());
	
		addFieldTeam(mainWindowToUse);
	
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
		return EAM.text("Team");
	}

	private TeamEditorComponent teamEditorComponent;
}
