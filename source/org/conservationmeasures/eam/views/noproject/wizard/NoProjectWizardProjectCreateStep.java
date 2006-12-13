/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;

public class NoProjectWizardProjectCreateStep extends NoProjectWizardStep
{

	public NoProjectWizardProjectCreateStep(NoProjectWizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		String html = EAM.loadResourceFile(getClass(), "WelcomeProjectCreate.html");
		LeftSideTextPanel left = new LeftSideTextPanel(html, wizardToUse);
		
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(left);
		panel.add(projectList);
		
		add(panel, BorderLayout.CENTER);
	}

}
