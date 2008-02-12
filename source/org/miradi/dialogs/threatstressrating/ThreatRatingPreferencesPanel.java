/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;

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
