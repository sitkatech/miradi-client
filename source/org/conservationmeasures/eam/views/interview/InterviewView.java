/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class InterviewView extends UmbrellaView
{
	public InterviewView(MainWindow mainWindow)
	{
		super(mainWindow);
		setToolBar(new InterviewToolBar(mainWindow.getActions()));
		wizard = new Wizard();
		wizard.addStep(createWelcomeStep());
		wizard.addStep(createPrinciple1ATask2Step1());

		setLayout(new BorderLayout());
		add(wizard, BorderLayout.CENTER);
		setBorder(new LineBorder(Color.BLACK));
	}
	
	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "interview";
	}
	
	public WizardStep createWelcomeStep()
	{
		return new WizardStep("welcome", textWelcome);
	}
	
	public WizardStep createPrinciple1ATask2Step1()
	{
		return new WizardStep("P1aT2S1", textPrinciple1ATask2Step1);
	}
	
	Wizard wizard;

	static public final String textWelcome = "<html><h1>Interview</h1>" +
	"<p>This view will walk the user through a series of questions.</p></html>";
	
	static public final String textPrinciple1ATask2Step1 = "<html>" +
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
