/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.CardLayout;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;

class Wizard extends JPanel
{
	Wizard()
	{
		stepHolder = new JPanel();
		stepHolder.setLayout(new CardLayout());
		steps = new Vector();
		currentStep = 0;
		navigationButtons = new WizardNavigationButtons(this);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(stepHolder);
		add(navigationButtons);
	}
	
	public void addStep(WizardStep step)
	{
		steps.add(step);
		stepHolder.add(step, step.getStepName());
	}
	
	public void doNext()
	{
		if(currentStep >= steps.size() - 1)
			return;
		++currentStep;
		CardLayout layout = (CardLayout)stepHolder.getLayout();
		WizardStep step = (WizardStep)steps.get(currentStep);
		layout.show(stepHolder, step.getStepName());
			
	}
	
	public void doPrevious()
	{
		EAM.logWarning("doPrevious");
	}
	
	int currentStep;
	Vector steps;
	JPanel stepHolder;
	WizardNavigationButtons navigationButtons;
}