/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.noproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.martus.swing.HyperlinkHandler;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.FlexibleWidthHtmlViewer;
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
			add(new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, 
					EAM.text("<div class='WizardText'><table><tr>" +
							"<td>New Project Filename: </td>" +
							"<td><input type='text' name='NewProjectName' value=''></input></td>" +
							"</tr></table>")));
			
			add(new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler,
					EAM.text("<div class='WizardText'><p class='hint'>" +
							"NOTE: Project filenames can contain letters, numbers, spaces, periods, and dashes.</p>")));
			
			add(new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, 
					EAM.text("<div class='WizardText'>" +
							"<p><input type='submit' name='Back' value='&lt; Previous'></input>" +
							"&nbsp;&nbsp;&nbsp;&nbsp;" +
							"<input type='submit' name='Next' value='Next &gt;'></input></p><br>")));
		}
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

	private JPanel rightSidePanel;
	private MiradiHtmlViewer introHtml;
	private JTextComponent newProjectNameField;
}
