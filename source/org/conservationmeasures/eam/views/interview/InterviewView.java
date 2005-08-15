/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class InterviewView extends UmbrellaView
{
	public InterviewView(MainWindow mainWindow)
	{
		super(mainWindow);
		setToolBar(new InterviewToolBar(mainWindow.getActions()));
		wizard = new Wizard();
		navigationButtons = new NavigationButtons(wizard);
		setLayout(new BorderLayout());
		add(wizard, BorderLayout.CENTER);
		add(navigationButtons, BorderLayout.AFTER_LAST_LINE);
		setBorder(new LineBorder(Color.BLACK));
	}
	
	static class NavigationButtons extends JPanel
	{
		NavigationButtons(Wizard wizardToUse)
		{
			wizard = wizardToUse;
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			previousButton = new JButton(EAM.text("Button|< Previous"));
			previousButton.addActionListener(new WizardPreviousHandler(wizardToUse));
			nextButton = new JButton(EAM.text("Button|Next >"));
			nextButton.addActionListener(new WizardNextHandler(wizardToUse));
			add(previousButton);
			add(nextButton);
		}
		
		JButton previousButton;
		JButton nextButton;
		Wizard wizard;
	}
	
	static class WizardNextHandler implements ActionListener
	{
		WizardNextHandler(Wizard wizardToControl)
		{
			wizard = wizardToControl;
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			wizard.doNext();
		}

		Wizard wizard;
	}
	
	static class WizardPreviousHandler implements ActionListener
	{
		WizardPreviousHandler(Wizard wizardToControl)
		{
			wizard = wizardToControl;
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			wizard.doPrevious();
		}

		Wizard wizard;
	}
	
	static class Wizard extends JPanel
	{
		Wizard()
		{
			setLayout(new CardLayout());
			steps = new Vector();
			addStep(createWelcomeStep());
			addStep(createPrinciple1ATask2Step1());
			currentStep = 0;
		}
		
		public void addStep(WizardStep step)
		{
			steps.add(step);
			add(step, step.getStepName());
		}
		
		public WizardStep createWelcomeStep()
		{
			return new WizardStep("welcome", textWelcome);
		}
		
		public WizardStep createPrinciple1ATask2Step1()
		{
			return new WizardStep("P1aT2S1", textPrinciple1ATask2Step1);
		}
		
		public void doNext()
		{
			if(currentStep >= steps.size() - 1)
				return;
			++currentStep;
			CardLayout layout = (CardLayout)getLayout();
			WizardStep step = (WizardStep)steps.get(currentStep);
			layout.show(this, step.getStepName());
				
		}
		
		public void doPrevious()
		{
			EAM.logWarning("doPrevious");
		}
		
		int currentStep;
		Vector steps;
	}
	
	static class WizardStep extends JPanel
	{
		public WizardStep(String stepNameToUse, String contentsToUse)
		{
			stepName = stepNameToUse;
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			add(new JLabel(contentsToUse));
		}
		
		String getStepName()
		{
			return stepName;
		}
		
		String stepName;
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "interview";
	}
	
	Wizard wizard;
	NavigationButtons navigationButtons;

	private static final String textWelcome = "<html><h1>Interview</h1>" +
	"<p>This view will walk the user through a series of questions.</p></html>";
	
	private static final String textPrinciple1ATask2Step1 = "<html>" +
	"<table width='600'><tr><td align='left' valign='top'>" +
	"<p><font size='6'>Step 1.  Conceptualize</font></p>" +
	"<font size='5'>&nbsp;&nbsp;Principle 1A.  Be clear and specific about the issue or problem</font></p>" +
	"<hr></hr>" +
	"<p><strong>Task 2. Define the scope of the area or theme</strong></p>" +
	"<br></br>" +
	"<p>Most conservation projects will focus on a defined geographic <u><em>project area</em></u> " + 
	"that contains the biodiversity that is of interest.  " + 
	"In a few cases, a conservation project may not focus on biodiversity in a specific area, " + 
	"but instead will have a <u><em>theme</em></u> that focuses on a population of wide-ranging animals, " + 
	"such as migratory birds.</p>" +
	"<br></br>" +
	"<p>Describe in a few sentences the project area or theme for your project:</p>" +
	"</td></tr></table>" + 
	"</html>";

	//private static final String dataPrinciple1ATask2Step1 = "Our community's traditional fishing grounds and adjacent shore areas in Our Bay.";
}
