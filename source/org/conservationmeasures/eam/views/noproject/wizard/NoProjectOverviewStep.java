/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;

public class NoProjectOverviewStep extends NoProjectWizardStep
{
	public NoProjectOverviewStep(NoProjectWizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		String html = EAM.loadResourceFile(getClass(), "WelcomeNew.html");
		left = new LeftSideTextPanelWithNews(wizardToUse.getMainWindow(), html, wizardToUse);
		
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(left);
		panel.add(projectList);
		
		add(panel, BorderLayout.CENTER);
	}
	
	public void refresh() throws Exception
	{
		super.refresh();
		left.refresh();
	}
	
	LeftSideTextPanel left;
}
