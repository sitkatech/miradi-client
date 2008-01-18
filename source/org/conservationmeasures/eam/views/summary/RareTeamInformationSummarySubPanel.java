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

public class RareTeamInformationSummarySubPanel extends ObjectDataInputPanel
{
	public RareTeamInformationSummarySubPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(RareProjectData.getObjectType()));
		
		addLabelsOnSingleRow(EAM.text("Cours Manager"), EAM.text("(not implemented yet)"));
		addLabelsOnSingleRow(EAM.text("Rare Regional Director"), EAM.text("(not implemented yet)"));
		addLabelsOnSingleRow(EAM.text("Campaign Manager"), EAM.text("(not implemented yet)"));
		addLabelsOnSingleRow(EAM.text("Contact at Local partner"), EAM.text("(not implemented yet)"));
		addLabelsOnSingleRow(EAM.text("Contact at BINGO Partner"), EAM.text("(not implemented yet)"));
		addLabelsOnSingleRow(EAM.text("Contact at Threat Reduction Partner"), EAM.text("(not implemented yet)"));
		addLabelsOnSingleRow(EAM.text("Contact at Impact Monitoring Partner"), EAM.text("(not implemented yet)"));
		
		updateFieldsFromProject();
	}

	private void addLabelsOnSingleRow(String string, String string2)
	{
		add(new PanelTitleLabel(string));
		add(new PanelTitleLabel(string2));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Label|Team Information");
	}
}
