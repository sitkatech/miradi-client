/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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

		addField(createChoiceField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_THREAT_RATING_MODE, new ThreatRatingModeChoiceQuestion()));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Threat Rating Preferences Panel");
	}
}
