/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import java.awt.Component;

import javax.swing.BorderFactory;

import org.conservationmeasures.eam.dialogs.base.AbstractObjectDataInputPanel;
import org.conservationmeasures.eam.layout.OneColumnGridLayout;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class SummaryPlanningPanel extends AbstractObjectDataInputPanel
{
	public SummaryPlanningPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		setLayout(new OneColumnGridLayout());

		SummaryPlanningWorkPlanSubPanel workPlanSubPanel = new SummaryPlanningWorkPlanSubPanel(projectToUse, orefToUse);
		workPlanSubPanel.setBorder(BorderFactory.createTitledBorder(EAM.text("Workplan")));
		addSubPanel(workPlanSubPanel);
		add(workPlanSubPanel);
		
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Planning");
	}

	public void addFieldComponent(Component component)
	{
	}
}
