/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardProjectScopeStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDesignateLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardVisionStep;
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
		else if(stepMarker.equals(ActionJumpDiagramWizardProjectScopeStep.class))
			setStep(PROJECT_SCOPE);
		else if(stepMarker.equals(ActionJumpDiagramWizardVisionStep.class))
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
			actions.get(ActionJumpDiagramOverviewStep.class).doAction();
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
