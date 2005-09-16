/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.conservationmeasures.eam.commands.CommandInterviewSetStep;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

abstract class WizardButtonHandler implements ActionListener
{
	WizardButtonHandler(BaseProject projectToUse)
	{
		project = projectToUse;
	}
	
	abstract String getDestinationStep(InterviewStepModel currentStep);
	
	public void actionPerformed(ActionEvent event)
	{
		InterviewStepModel currentStep = project.getCurrentInterviewStep();
		String destinationStepName = getDestinationStep(currentStep);
		CommandInterviewSetStep command = new CommandInterviewSetStep(destinationStepName);
		try
		{
			project.executeCommand(command);
		}
		catch (CommandFailedException e)
		{
			EAM.errorDialog("Internal error: " + e);
		}
	}
	
	BaseProject project;
}