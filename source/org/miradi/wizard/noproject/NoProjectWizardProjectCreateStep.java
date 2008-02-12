/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.noproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;

public class NoProjectWizardProjectCreateStep extends NoProjectWizardStep
{

	public NoProjectWizardProjectCreateStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		String html = EAM.loadResourceFile(getClass(), "WelcomeProjectCreate.html");
		leftTop = new LeftSideTextPanel(getMainWindow(), html, this);

		JPanel left = new JPanel(new BorderLayout());
		left.add(leftTop, BorderLayout.BEFORE_FIRST_LINE);
		left.add(projectList, BorderLayout.CENTER);
		
		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		mainPanel.setBackground(AppPreferences.getWizardBackgroundColor());
		mainPanel.add(left);
		JPanel rightSidePanel = new JPanel();
		rightSidePanel.setBackground(AppPreferences.getWizardBackgroundColor());
		mainPanel.add(rightSidePanel);
		
		add(mainPanel, BorderLayout.CENTER);
	}
	
	
	public void refresh() throws Exception
	{
		leftTop.refresh();
		super.refresh();
	}
	
	public void setComponent(String name, JComponent component)
	{
		if (name.equals(NEW_PROJECT_NAME))
		{
			newProjectNameField = (JTextComponent)component;
			newProjectNameField.addKeyListener(this);
		}
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
				createProject();
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

	private void createProject()
	{
		String newName = getValue(NEW_PROJECT_NAME);
		if (newName.length()<=0)
			return;
		try 
		{
			Project.validateNewProject(newName);
			File newFile = new File(EAM.getHomeDirectory(),newName);
			getMainWindow().createOrOpenProject(newFile);
		}
		catch (Exception e)
		{
			EAM.notifyDialog(EAM.text("Create Failed:") +e.getMessage());
		}
	}

	public String getValue(String name)
	{
		return newProjectNameField.getText();
	}

	private static final String NEW_PROJECT_NAME = "NewProjectName";

	LeftSideTextPanel leftTop;
	JTextComponent newProjectNameField;
	

}
