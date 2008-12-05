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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.martus.swing.HyperlinkHandler;
import org.miradi.actions.ActionImportZippedConproProject;
import org.miradi.actions.ActionImportZippedProjectFile;
import org.miradi.actions.EAMAction;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.utils.FlexibleWidthHtmlViewer;
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

			String buttonsText = EAM.text("<div class='WizardText'>" +	
					"<p><table>" + 
					"<tr>" +
					"<td><input type='submit' name='Back' value='&lt; Previous'></input></td>" +
					"<td><input type='submit' name='Import Miradi Project' value='Miradi Project (.mpz)'></input></td>" +
					"</tr>" +
					
					"<tr>" +
					"<td></td>" + 
					"<td><input type='submit' name='Import ConPro Project' value='ConPro/Miradi Exchange File (.cpmz)'></input></td>" +
					"</tr>" +
					"</table></p><br>") ; 

			add(new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, buttonsText));
		}

	}
	
	public Class getControl(String controlName)
	{
		if(controlName.equals(CONTROL_IMPORT_MIRADI))
			return getClass();
		
		return super.getControl(controlName);
	}

	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.equals(CONTROL_IMPORT_MIRADI))
			{
				EAMAction action = getMainWindow().getActions().get(ActionImportZippedProjectFile.class);
				action.doAction();
			}
			if (buttonName.equals(CONTROL_IMPORT_CONPRO))
			{
				EAMAction action = getMainWindow().getActions().get(ActionImportZippedConproProject.class);
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

	public static final String CONTROL_IMPORT_CONPRO = "Import ConPro Project";
	public static final String CONTROL_IMPORT_MIRADI = "Import Miradi Project";
}
