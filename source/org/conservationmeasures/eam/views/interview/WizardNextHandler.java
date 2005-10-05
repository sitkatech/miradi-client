/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import org.conservationmeasures.eam.main.Project;

class WizardNextHandler extends WizardButtonHandler
{
	WizardNextHandler(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public String getDestinationStepName(InterviewStepModel currentStep)
	{
		return currentStep.getNextStepName();
	}
}