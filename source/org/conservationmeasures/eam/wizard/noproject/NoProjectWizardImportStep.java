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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.wizard.WizardManager;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class NoProjectWizardImportStep extends NoProjectWizardStep
{

	public NoProjectWizardImportStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		String html = EAM.loadResourceFile(getClass(), "WelcomeImport.html");
		left = new LeftSideTextPanel(getMainWindow(), html, this);
		
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(left);
		panel.add(projectList);
		
		add(panel, BorderLayout.CENTER);
	}
	
	public void refresh() throws Exception
	{
		left.refresh();
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

	LeftSideTextPanel left;
	

}
