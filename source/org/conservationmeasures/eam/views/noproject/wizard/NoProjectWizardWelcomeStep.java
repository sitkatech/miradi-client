package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;

public class NoProjectWizardWelcomeStep extends NoProjectWizardStep
{
	public NoProjectWizardWelcomeStep(NoProjectWizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		String html = EAM.loadResourceFile(getClass(), "WelcomeNew.html");
		LeftSideTextPanel left = new LeftSideTextPanel(html, wizardToUse);
		
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(left);
		panel.add(projectList);
		
		add(panel, BorderLayout.CENTER);
	}

}
