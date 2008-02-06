/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionImportZippedProjectFile;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.wizard.WizardManager;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class NoProjectWizardImportStep extends NoProjectWizardStep
{

	public NoProjectWizardImportStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		String html = EAM.loadResourceFile(getClass(), "WelcomeImport.html");
		leftTop = new LeftSideTextPanel(getMainWindow(), html, this);
		
		JPanel left = new JPanel(new BorderLayout());
		left.add(leftTop, BorderLayout.BEFORE_FIRST_LINE);
		left.add(projectList, BorderLayout.CENTER);
		
		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		mainPanel.setBackground(AppPreferences.WIZARD_BACKGROUND);
		mainPanel.add(left);
		JPanel rightSidePanel = new JPanel();
		rightSidePanel.setBackground(AppPreferences.WIZARD_BACKGROUND);
		mainPanel.add(rightSidePanel);
		
		add(mainPanel, BorderLayout.CENTER);
	}
	
	public void refresh() throws Exception
	{
		leftTop.refresh();
		super.refresh();
	}
	
	public Class getControl(String controlName)
	{
		if(controlName.equals(WizardManager.CONTROL_NEXT))
			return getClass();
		return super.getControl(controlName);
	}


	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.equals(WizardManager.CONTROL_NEXT))
			{
				EAMAction action = getMainWindow().getActions().get(ActionImportZippedProjectFile.class);
				action.doAction();
			}
			else 
			{
				super.buttonPressed(buttonName);
			}
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to process request: ") + e);
		}
	}

	LeftSideTextPanel leftTop;
	

}
