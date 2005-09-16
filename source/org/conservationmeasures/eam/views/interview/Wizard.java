/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInterviewSetStep;
import org.conservationmeasures.eam.commands.CommandWizardPrevious;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.interview.elements.ElementData;
import org.martus.swing.UiVBox;

class Wizard extends JPanel implements CommandExecutedListener
{
	Wizard(BaseProject projectToUse)
	{
		project = projectToUse;
		
		setLayout(new BorderLayout());
		navigationButtons = new WizardNavigationButtons(this);
		
		add(navigationButtons, BorderLayout.AFTER_LAST_LINE);
		
		project.addCommandExecutedListener(this);
	}
	
	public void doNext()
	{
		String nextStep = project.getCurrentInterviewStep().getNextStepName();
		CommandInterviewSetStep command = new CommandInterviewSetStep(nextStep);
		executeNavigationButton(command);
	}

	public void doPrevious()
	{
		CommandWizardPrevious command = new CommandWizardPrevious(project.getCurrentInterviewStepName());
		executeNavigationButton(command);
	}
	
	private void executeNavigationButton(Command command)
	{
		try
		{
			project.executeCommand(command);
		}
		catch (CommandFailedException e)
		{
			EAM.errorDialog("Internal error: " + e);
		}

		// FIXME: I think this is not needed
		showCurrentProjectStep();
	}
	
	public void showCurrentProjectStep()
	{
		if(stepHolder != null)
			remove(stepHolder);
		
		stepHolder = new UiVBox();
		InterviewModel model = project.getInterviewModel();
		InterviewStepModel stepModel = model.getCurrentStep();
		for(int i=0; i < stepModel.getElementCount(); ++i)
		{
			ElementData element = stepModel.getElement(i);
			stepHolder.add(element.createComponent());
		}
		add(stepHolder, BorderLayout.CENTER);
		validate();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		showCurrentProjectStep();
	}

	BaseProject project;
	WizardNavigationButtons navigationButtons;
	UiVBox stepHolder;
}
