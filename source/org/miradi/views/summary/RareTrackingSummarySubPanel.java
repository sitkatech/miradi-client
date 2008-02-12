/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objects.RareProjectData;
import org.miradi.project.Project;

public class RareTrackingSummarySubPanel extends ObjectDataInputPanel
{
	public RareTrackingSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		add(new PanelTitleLabel(EAM.text("Project Number")));
		add(new PanelTitleLabel(EAM.text("(See Project tab)")));

		addField(createStringField(RareProjectData.TAG_COHORT));
		
		add(new PanelTitleLabel(EAM.text("Country")));
		add(new PanelTitleLabel(EAM.text("(See Location tab)")));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|Rare Tracking");
	}
}
