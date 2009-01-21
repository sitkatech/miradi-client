/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.wizard.noproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.martus.swing.HyperlinkHandler;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnPanel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.ProjectNameRestrictedTextField;
import org.miradi.wizard.MiradiHtmlViewer;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;

public class WelcomeCreateStep extends NoProjectWizardStep
{

	public WelcomeCreateStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		JComponent leftTop = new CreatePanel(this);

		JPanel left = new JPanel(new BorderLayout());
		left.add(leftTop, BorderLayout.BEFORE_FIRST_LINE);
		left.add(projectList, BorderLayout.CENTER);
		
		rightSidePanel = new JPanel();
		rightSidePanel.setBackground(AppPreferences.getWizardBackgroundColor());
		
		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 60, 0));
		mainPanel.setBackground(AppPreferences.getWizardBackgroundColor());
		mainPanel.add(left);
		mainPanel.add(rightSidePanel);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

		add(mainPanel, BorderLayout.CENTER);
	}
	
	class CreatePanel extends OneColumnPanel
	{
		public CreatePanel(HyperlinkHandler hyperlinkHandler) throws Exception
		{
			setBackground(AppPreferences.getWizardBackgroundColor());
			
			String intro = EAM.text("<div class='WizardText'>To <strong>create a new project</strong>, " +
			"enter the filename in the input field below, " +
			"and press the <code class='toolbarbutton'>&lt;Next&gt;</code> button.");
			introHtml = new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, intro);
			add(introHtml);
				
			addTextFieldPanel();
				
			String hintText = "<div class='WizardText'><p class='hint'>" + getLegalProjectNameNote();
			add(new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, hintText));
			
			add(createNextPreviousButtonPanel(hyperlinkHandler));
		}

		private void addTextFieldPanel()
		{
			newProjectNameField = new ProjectNameRestrictedTextField();
			TwoColumnPanel textFieldPanel = new TwoColumnPanel();
			textFieldPanel.setBackground(AppPreferences.getWizardBackgroundColor());
			textFieldPanel.add(new PanelTitleLabel(EAM.text("New Project Filename:")));
			textFieldPanel.add(newProjectNameField);
			add(textFieldPanel);
		}
	}

	@Override
	public void refresh() throws Exception
	{
		super.refresh();
		newProjectNameField.requestFocusInWindow();
		newProjectNameField.removeKeyListener(this);
		newProjectNameField.addKeyListener(this);
	}
	
	public static String getLegalProjectNameNote()
	{
		return EAM.text("NOTE: Project filenames can contain letters, numbers, spaces, periods, dashes, and underlines.");
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
		String newName = getProjectName();
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

	public String getProjectName()
	{
		return newProjectNameField.getText();
	}

	private JPanel rightSidePanel;
	private MiradiHtmlViewer introHtml;
	private ProjectNameRestrictedTextField newProjectNameField;
}
