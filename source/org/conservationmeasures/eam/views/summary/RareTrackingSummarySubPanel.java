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

public class RareTrackingSummarySubPanel extends ObjectDataInputPanel
{
	public RareTrackingSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		addField(createNumericField(RareProjectData.TAG_PROJECT_CODE, 6));
		addField(createStringField(RareProjectData.TAG_COHORT));
		add(new PanelTitleLabel(EAM.text("Country")));
		add(new PanelTitleLabel(EAM.text("(see Location tab)")));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|Rare Tracking");
	}
}
