/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.actions.ActionImportMpf;
import org.miradi.actions.ActionImportMpz;
import org.miradi.actions.ActionImportXmpz2;
import org.miradi.actions.MiradiAction;
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
			
			String intro = EAM.text(
						"<div class='WizardText'>" +
						"Miradi is able to import three different kinds of project files.  To import a Miradi, XMPZ2, or MPZ file, click the button for the <BR>" +
						"appropriate file type, select the file in the dialog that pops up, and then click <strong>Import</strong>.<BR><BR>" +
						"(Note: If you need to import a ConPro/Miradi Exchange File (.cpmz), please contact Miradi support.)" +
						"</div>"
			);

			add(new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, intro));

			String buttonsText = EAM.text("<div class='WizardText'>" +	
					"<p><table>" +
					"<tr>" +
					"<td><input type='submit' name='Back' value='&lt; Previous'></input></td>" + 
					"<td><input type='submit' name='" + CONTROL_IMPORT_MIRADI + "' value='Miradi File (.Miradi)'></input></td>" +
					"</tr>" +
					
					"<tr>" +
					"<td></td>" + 
					"<td><input type='submit' name='" + CONTROL_IMPORT_XMPZ2 + "' value='Miradi Share Project File (.xmpz2)'></input></td>" +
					"</tr>" +

					"<tr>" +
					"<td></td>" + 
					"<td><input type='submit' name='" + CONTROL_IMPORT_MPZ + "' value='Old Miradi Project (.mpz)'></input></td>" +
					"</tr>" + 					
					"</table></p><br>") ;

			add(new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, buttonsText));
		}

	}
	
	@Override
	public Class getControl(String controlName)
	{
		if(controlName.equals(CONTROL_IMPORT_MPZ))
			return getClass();
		
		if (controlName.equals(CONTROL_IMPORT_XMPZ2))
			return getClass();
		
		if (controlName.equals(CONTROL_IMPORT_MIRADI))
			return getClass();
		
		return super.getControl(controlName);
	}

	@Override
	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.equals(CONTROL_IMPORT_MIRADI))
			{
				MiradiAction action = getMainWindow().getActions().get(ActionImportMpf.class);
				action.doAction();
			}
			if(buttonName.equals(CONTROL_IMPORT_XMPZ2))
			{
				MiradiAction action = getMainWindow().getActions().get(ActionImportXmpz2.class);
				action.doAction();
			}
			if(buttonName.equals(CONTROL_IMPORT_MPZ))
			{
				MiradiAction action = getMainWindow().getActions().get(ActionImportMpz.class);
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

	public static final String CONTROL_IMPORT_MIRADI = "ImportMiradiProject";
	public static final String CONTROL_IMPORT_XMPZ2 = "ImportXmpz2";
	public static final String CONTROL_IMPORT_MPZ = "ImportMpz";
}
