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
import org.conservationmeasures.eam.wizard.WizardPanel;

public class NoProjectWizardImportStep extends NoProjectWizardStep
{

	public NoProjectWizardImportStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		String html = EAM.loadResourceFile(getClass(), "WelcomeImport.html");
		LeftSideTextPanel left = new LeftSideTextPanel(getMainWindow(), html, this);
		
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(left);
		panel.add(projectList);
		
		add(panel, BorderLayout.CENTER);
	}

}
