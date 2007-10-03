/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpReviewStratAndMonPlansStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpScheduleOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanDevelopActivitiesAndTasksStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanDevelopMethodsAndTasksStep;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;

public class ProcessMenu4a extends MiradiMenu
{
	public ProcessMenu4a(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_4A, actions);
		setMnemonic(KeyEvent.VK_D);
		setIcon(new MiradiResourceImageIcon("icons/blankicon.png"));
		
		addMenuItem(ActionJumpReviewStratAndMonPlansStep.class, KeyEvent.VK_R);
		addMenuItem(ActionJumpWorkPlanDevelopActivitiesAndTasksStep.class, KeyEvent.VK_A);
		addMenuItem(ActionJumpWorkPlanDevelopMethodsAndTasksStep.class, KeyEvent.VK_M);
		addMenuItem(ActionJumpWorkPlanAssignResourcesStep.class, KeyEvent.VK_T);
		addMenuItem(ActionJumpScheduleOverviewStep.class, KeyEvent.VK_S);
//		addMenuItem(ActionJumpFinancialOverviewStep.class, KeyEvent.VK_F);
	}
}
