/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ThreatRatingModeChoiceQuestion;

public class ThreatRatingPreferencesPanel extends ObjectDataInputPanel
{
	public ThreatRatingPreferencesPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getMetadata().getRef());

		addField(createChoiceField(ProjectMetadata.getObjectType(), new ThreatRatingModeChoiceQuestion(ProjectMetadata.TAG_THREAT_RATING_MODE)));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Threat Rating Preferences Panel");
	}
}
