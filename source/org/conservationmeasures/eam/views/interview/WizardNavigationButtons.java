/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;

public class WizardNavigationButtons extends JPanel
{
	WizardNavigationButtons(Wizard wizardToUse)
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
	
	JButton previousButton;
	JButton nextButton;
	Wizard wizard;
}
