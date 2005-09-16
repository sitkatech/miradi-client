/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInterviewSetStep;
import org.conservationmeasures.eam.commands.CommandSetData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

abstract class WizardButtonHandler implements ActionListener
{
	WizardButtonHandler(BaseProject projectToUse)
	{
		project = projectToUse;
	}
	
	abstract String getDestinationStepName(InterviewStepModel currentStep);
	
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			project.executeCommand(new CommandBeginTransaction());
			saveData();
			goToAppropriateStep();
			project.executeCommand(new CommandEndTransaction());
		}
		catch (CommandFailedException e)
		{
			e.printStackTrace();
			EAM.errorDialog(EAM.text("Internal error saving data: ") + e.getMessage());
		}
	}
	
	private void saveData() throws CommandFailedException
	{
		EAM.logDebug("WizardButtonHandler.saveData()");
		InterviewStepModel currentStep = project.getCurrentInterviewStep();
		currentStep.copyDataFromComponents();
		Map stepData = currentStep.getData();
		Vector dataCommands = createDataCommands(stepData);
		for(int i=0; i < dataCommands.size(); ++i)
			project.executeCommand((Command)dataCommands.get(i));
	}
	
	public static Vector createDataCommands(Map data)
	{
		Vector commands = new Vector();
		
		Set keys = data.keySet();
		Iterator iter = keys.iterator();
		while(iter.hasNext())
		{
			String fieldName = (String)iter.next();
			String fieldData = (String)data.get(fieldName);
			CommandSetData command = new CommandSetData(fieldName, fieldData);
			commands.add(command);
		}
		
		return commands;
	}

	private void goToAppropriateStep()
	{
		InterviewStepModel currentStep = project.getCurrentInterviewStep();
		String destinationStepName = getDestinationStepName(currentStep);
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
