/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDesignateLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class SummaryWizardPanel extends WizardPanel
{
	public SummaryWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		actions = mainWindow.getActions();
		
		OVERVIEW = addStep(new SummaryWizardOverviewStep(this));
		TEAM_MEMBERS = addStep(new SummaryWizardDefineTeamMembers(this));
		PROJECT_LEADER = addStep(new SummaryWizardDefineProjectLeader(this));
		PROJECT_SCOPE = addStep(new SummaryWizardDefineProjecScope(this));
		PROJECT_VISION = addStep(new SummaryWizardDefineProjectVision(this));
		
		setStep(OVERVIEW);
	}

	public void jump(Class stepMarker) throws Exception
	{ 
		if(stepMarker.equals(ActionJumpSelectTeam.class))
			setStep(TEAM_MEMBERS);
		else if(stepMarker.equals(ActionJumpDesignateLeader.class))
			setStep(PROJECT_LEADER);
		else if(stepMarker.equals(ActionJumpDefineScope.class))
			setStep(PROJECT_SCOPE);
		else if(stepMarker.equals(ActionJumpEstablishVision.class))
			setStep(PROJECT_VISION);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}

	public void previous() throws Exception
	{
		super.previous();
	}
	
	public void next() throws Exception
	{	
		if (currentStep == PROJECT_VISION)
			actions.get(ActionJumpDiagramOverview.class).doAction();
		else
			super.next();
	}

	Actions actions;
	int OVERVIEW;
	int TEAM_MEMBERS;
	int PROJECT_LEADER;
	int PROJECT_SCOPE;
	int PROJECT_VISION;
}
