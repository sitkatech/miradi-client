/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardFocusStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditAllStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardEditIndicatorsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanOverview;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class MonitoringPlanWizardPanel extends WizardPanel
{
	public MonitoringPlanWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		actions = mainWindow.getActions();
		
		OVERVIEW = addStep(new MonitoringOverviewStep(this));
		EDIT_INDICATORS = addStep(new MonitoringWizardEditIndicatorsStep(this));
		setStep(OVERVIEW);
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpMonitoringOverviewStep.class))
			setStep(OVERVIEW);
		else if(stepMarker.equals(ActionJumpMonitoringWizardEditIndicatorsStep.class))
			setStep(EDIT_INDICATORS);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void previous() throws Exception
	{
		if(currentStep == EDIT_INDICATORS)
			actions.get(ActionJumpMonitoringWizardDefineIndicatorsStep.class).doAction();
		else if (currentStep == OVERVIEW)
			actions.get(ActionJumpEditAllStrategiesStep.class).doAction();
		
		super.previous();
	}
	
	public void next() throws Exception
	{
		if(currentStep == OVERVIEW)
			actions.get(ActionJumpMonitoringWizardFocusStep.class).doAction();
		else if(currentStep == EDIT_INDICATORS)
			actions.get(ActionJumpWorkPlanOverview.class).doAction();
		
		super.next();
	}
	
	Actions actions;

	int OVERVIEW;
	int EDIT_INDICATORS;
	int SELECT_METHODS;
}
