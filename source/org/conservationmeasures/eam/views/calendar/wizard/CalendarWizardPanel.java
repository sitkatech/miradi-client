/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.calendar.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class CalendarWizardPanel extends WizardPanel
{
	public CalendarWizardPanel() throws Exception
	{
		int WELCOME = addStep(new CalendarWizardWelcomeStep(this));
		setStep(WELCOME);
	}
}
