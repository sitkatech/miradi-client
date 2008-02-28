/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.noproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.martus.swing.HyperlinkHandler;
import org.miradi.actions.ActionImportZippedProjectFile;
import org.miradi.actions.EAMAction;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;

public class WelcomeImportStep extends NoProjectWizardStep
{

	public WelcomeImportStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		JComponent leftTop = new ImportPanel(this);
		
		JPanel left = new JPanel(new BorderLayout());
		left.add(leftTop, BorderLayout.BEFORE_FIRST_LINE);
		left.add(projectList, BorderLayout.CENTER);
		
		JPanel rightSidePanel = new JPanel();
		rightSidePanel.setBackground(AppPreferences.getWizardBackgroundColor());

		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 60, 0));
		mainPanel.setBackground(AppPreferences.getWizardBackgroundColor());
		mainPanel.add(left);
		mainPanel.add(rightSidePanel);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		add(mainPanel, BorderLayout.CENTER);
	}
	
	class ImportPanel extends OneColumnPanel
	{
		public ImportPanel(HyperlinkHandler hyperlinkHandler) throws Exception
		{
			setBackground(AppPreferences.getWizardBackgroundColor());
			
			String intro = EAM.text("<div class='WizardText'>" +
					"To <strong>import</strong> a " +
					"<a href='Definition:ImportZip' class='definition'>Miradi Project Zip</a> " +
					"file that was exported from Miradi, " +
					"press the <code class='toolbarbutton'>&lt;Next&gt;</code>button below " +
					"to choose the file.");
			add(new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, intro));
			
			add(createNextPreviousButtonPanel(hyperlinkHandler));
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


}
