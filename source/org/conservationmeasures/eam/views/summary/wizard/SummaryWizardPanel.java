/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class SummaryWizardPanel extends WizardPanel
{
	public SummaryWizardPanel(Actions actionsToUse) throws Exception
	{
		actions = actionsToUse;
		
		OVERVIEW = addStep(new SummaryWizardOverviewStep(this));
		TEAM_MEMBERS = addStep(new SummaryWizardDefineTeamMembers(this));
		PROJECT_LEADER = addStep(new SummaryWizardDefineProjectLeader(this));

		setStep(OVERVIEW);
	}

	public void jump(Class stepMarker) throws Exception
	{
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}

	public void previous() throws Exception
	{
		super.previous();
	}
	
	public void next() throws Exception
	{	
		super.next();
	}

	Actions actions;
	int OVERVIEW;
	int TEAM_MEMBERS;
	int PROJECT_LEADER;
}
