/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.interview.elements.ElementData;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiVBox;

public class InterviewView extends UmbrellaView implements CommandExecutedListener
{
	public InterviewView(MainWindow mainWindow) throws IOException
	{
		super(mainWindow);
		setBorder(new LineBorder(Color.BLACK));
		setToolBar(new InterviewToolBar(mainWindow.getActions()));
		setLayout(new BorderLayout());
		add(createNavigationButtons(), BorderLayout.AFTER_LAST_LINE);
		
		getProject().addCommandExecutedListener(this);
	}
	
	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Interview";
	}
	
	public Component createNavigationButtons()
	{
		previousButton = new JButton(EAM.text("Button|< Previous"));
		previousButton.addActionListener(new WizardPreviousHandler(getProject()));
		
		nextButton = new JButton(EAM.text("Button|Next >"));
		nextButton.addActionListener(new WizardNextHandler(getProject()));

		Box box = Box.createHorizontalBox();
		box.add(previousButton);
		box.add(nextButton);
		updateButtonStates();
		return box;
	}
	
	public void updateButtonStates()
	{
		InterviewModel model = getProject().getInterviewModel();
		InterviewStepModel currentStep = model.getCurrentStep();
		
		boolean previousButtonEnabled = currentStep.isPreviousAvailable();
		boolean nextButtonEnabled = currentStep.isNextAvailable();
	
		previousButton.setEnabled(previousButtonEnabled);
		nextButton.setEnabled(nextButtonEnabled);
	}
	
	public void showCurrentProjectStep()
	{
		if(stepHolder != null)
			remove(stepHolder);
		
		Project project = getProject();
		stepHolder = new UiVBox();
		stepHolder.setBackground(Color.WHITE);
		InterviewModel model = project.getInterviewModel();
		InterviewStepModel stepModel = model.getCurrentStep();
		for(int i=0; i < stepModel.getElementCount(); ++i)
		{
			ElementData element = stepModel.getElement(i);
			element.createComponent();
			setElementDataFromProject(element);
			JComponent thisComponent = element.getComponent();
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(thisComponent);
			stepHolder.add(panel);
		}
		stepHolder.add(Box.createVerticalGlue());
		add(stepHolder, BorderLayout.CENTER);
		validate();
		updateButtonStates();
	}

	private void setElementDataFromProject(ElementData element)
	{
		if(!element.hasData())
			return;
		
		String value = getProject().getDataValue(element.getFieldName());
		element.setFieldData(value);
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		showCurrentProjectStep();
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
	}
	
	public void commandFailed(Command command, CommandFailedException e)
	{
		
	}
	
	public void becomeActive() throws Exception
	{
	}

	public void becomeInactive() throws Exception
	{
	}



	private UiVBox stepHolder;
	private JButton previousButton;
	private JButton nextButton;
}
