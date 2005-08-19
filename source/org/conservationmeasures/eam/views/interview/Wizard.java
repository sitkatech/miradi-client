/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.CardLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import org.conservationmeasures.eam.commands.CommandWizardNext;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiVBox;

class Wizard extends UiVBox implements CommandExecutedListener
{
	Wizard(BaseProject projectToUse)
	{
		project = projectToUse;
		
		stepHolder = new JPanel();
		stepHolder.setLayout(new CardLayout());
		steps = new HashMap();
		navigationButtons = new WizardNavigationButtons(this);
		
		add(stepHolder);
		add(navigationButtons);
		
		currentStepName = project.getCurrentInterviewStepName();
		project.addCommandExecutedListener(this);
	}
	
	public void addStep(WizardStep step)
	{
		steps.put(step.getStepName(), step);
		stepHolder.add(step, step.getStepName());
	}
	
	public void doNext()
	{
		CommandWizardNext command = new CommandWizardNext(currentStepName);
		try
		{
			project.executeCommand(command);
		}
		catch (CommandFailedException e)
		{
			EAM.errorDialog("Internal error: " + e);
		}

		showCurrentProjectStep();
	}
	
	public void showCurrentProjectStep()
	{
		currentStepName = project.getCurrentInterviewStepName();

		CardLayout layout = (CardLayout)stepHolder.getLayout();
		layout.show(stepHolder, currentStepName);
	}

	public void doPrevious()
	{
		EAM.logWarning("doPrevious");
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if(currentStepName.equals(project.getCurrentInterviewStepName()))
			return;
		
		showCurrentProjectStep();
	}

	BaseProject project;
	HashMap steps;
	JPanel stepHolder;
	WizardNavigationButtons navigationButtons;
	String currentStepName;
}
