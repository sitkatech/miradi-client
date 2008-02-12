/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.main.EAM;

public class ProcessMenu4a extends MiradiMenu
{
	public ProcessMenu4a(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_4A, actions);
		setMnemonic(KeyEvent.VK_D);

		addDisabledMenuItem(EAM.text("Develop systems for recording, storing, processing and backing up project data"));
//		addMenuItem(ActionJumpReviewStratAndMonPlansStep.class, KeyEvent.VK_R);
//		addMenuItem(ActionJumpWorkPlanAssignResourcesStep.class, KeyEvent.VK_T);
//		addMenuItem(ActionJumpScheduleOverviewStep.class, KeyEvent.VK_S);
//		addMenuItem(ActionJumpFinancialOverviewStep.class, KeyEvent.VK_F);
	}
}
