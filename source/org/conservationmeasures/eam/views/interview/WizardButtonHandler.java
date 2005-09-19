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
			Vector dataCommands = getDataToSave();
			CommandInterviewSetStep navigateCommand = getStepNavigateCommand();
			
			project.executeCommand(new CommandBeginTransaction());
			executeSaveDataCommands(dataCommands);
			project.executeCommand(navigateCommand);
			project.executeCommand(new CommandEndTransaction());
		}
		catch (CommandFailedException e)
		{
			e.printStackTrace();
			EAM.errorDialog(EAM.text("Internal error saving data: ") + e.getMessage());
		}
	}

	private CommandInterviewSetStep getStepNavigateCommand()
	{
		InterviewStepModel currentStep = project.getCurrentInterviewStep();
		String destinationStepName = getDestinationStepName(currentStep);
		CommandInterviewSetStep navigateCommand = new CommandInterviewSetStep(destinationStepName);
		return navigateCommand;
	}

	private void executeSaveDataCommands(Vector dataCommands) throws CommandFailedException
	{
		for(int i=0; i < dataCommands.size(); ++i)
			project.executeCommand((Command)dataCommands.get(i));
	}

	private Vector getDataToSave()
	{
		InterviewStepModel currentStep = project.getCurrentInterviewStep();
		Map stepData = currentStep.getData();
		Vector dataCommands = createDataCommands(stepData);
		return dataCommands;
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

	BaseProject project;
}
