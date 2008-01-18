/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.layout.OneColumnGridLayout;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.RareProjectData;
import org.conservationmeasures.eam.project.Project;

public class RARESummaryPanel extends ObjectDataInputPanel
{
	public RARESummaryPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		setLayout(new OneColumnGridLayout());
		
		addSubPanelWithTitledBorder(new RareTrackingSummarySubPanel(projectToUse));
		addSubPanelWithTitledBorder(new RareCampaignSummarySubPanel(projectToUse));
		addSubPanelWithTitledBorder(new RareCampaignPlanningSummarySubPanel(projectToUse));
		addSubPanelWithTitledBorder(new RareTeamInformationSummarySubPanel(projectToUse));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|RARE");
	}
}
