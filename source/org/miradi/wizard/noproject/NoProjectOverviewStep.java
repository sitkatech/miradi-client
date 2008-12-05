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
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.miradi.actions.EAMAction;
import org.miradi.actions.jump.ActionJumpWelcomeCreateStep;
import org.miradi.actions.jump.ActionJumpWelcomeImportStep;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnPanel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.Translation;
import org.miradi.wizard.WizardPanel;

public class NoProjectOverviewStep extends NoProjectWizardStep
{
	public NoProjectOverviewStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		JComponent leftTop = new OverviewPanel();
		
		JPanel left = new JPanel(new BorderLayout());
		left.add(leftTop, BorderLayout.BEFORE_FIRST_LINE);
		left.add(projectList, BorderLayout.CENTER);
		
		JPanel right = new JPanel(new BorderLayout());
		right.setBackground(AppPreferences.getWizardBackgroundColor());
		Component strut = Box.createVerticalStrut(40);
		strut.setBackground(AppPreferences.getWizardBackgroundColor());
		right.add(strut, BorderLayout.BEFORE_FIRST_LINE);
		MainNewsPanel newsPanel = new MainNewsPanel(getMainWindow(), this);
		right.add(newsPanel, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 60, 0));
		mainPanel.setBackground(AppPreferences.getWizardBackgroundColor());
		mainPanel.add(left);
		mainPanel.add(right);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		add(mainPanel, BorderLayout.CENTER);
		
		getMainWindow().updateActionStates();
	}
	
	class OverviewPanel extends OneColumnPanel
	{
		public OverviewPanel()
		{
			setGaps(10);
			setBackground(AppPreferences.getWizardBackgroundColor());
			
			add(new FlexibleWidthHtmlViewer(getMainWindow(), EAM.text("<div class='WizardText'>To <strong>start a new project</strong> on this computer, choose:")));
			add(createCreateButtonRow());
			add(createImportButtonRow());
			add(new FlexibleWidthHtmlViewer(getMainWindow(), ""));
		}

		private Component createCreateButtonRow()
		{
			return createButtonRow(ActionJumpWelcomeCreateStep.class, EAM.text("a new project from scratch"));
		}

		private Component createImportButtonRow()
		{
			return createButtonRow(ActionJumpWelcomeImportStep.class, EAM.text("an exported Miradi file (.mpz or .cpmz)"));
		}

		private Component createButtonRow(Class jumpActionClass, String description)
		{
			EAMAction action = getMainWindow().getActions().get(jumpActionClass);
			OneRowPanel panel = new OneRowPanel();
			panel.setGaps(20);
			panel.setBackground(AppPreferences.getWizardBackgroundColor());
			panel.add(new PanelTitleLabel(" "));
			panel.add(new PanelButton(action));
			panel.add(new PanelTitleLabel(description));
			return panel;
		}
	}
	
	@Override
	public void refresh() throws Exception
	{
		super.refresh();
		projectList.refresh();
	}

	@Override
	protected String getTextBelowLogo() throws Exception
	{
		return Translation.getHtmlContent("wizard/NoProjectView/WelcomeIntroduction.html");
	}
	
}
