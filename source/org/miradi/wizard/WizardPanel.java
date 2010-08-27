/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JPanel;

import org.miradi.actions.ActionWizardNext;
import org.miradi.actions.ActionWizardPrevious;
import org.miradi.actions.Actions;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;

public class WizardPanel extends JPanel
{
	public WizardPanel(MainWindow mainWindowToUse, WizardTitlePanel wizardTitlePanelToUse) throws Exception
	{
		super(new BorderLayout());
		wizardTitlePanel = wizardTitlePanelToUse;
		
		setBackground(AppPreferences.getWizardBackgroundColor());
		mainWindow = mainWindowToUse;
		wizardManager = mainWindow.getWizardManager();
		setFocusCycleRoot(true);
		navigationButtons = createNavigationButtons();
		wizardManager.setUpSteps(this);
	}

	public void setContents(SkeletonWizardStep contents)
	{
		setStepTitle(contents.getProcessStepTitle());
		setScreenTitle(contents.getWizardScreenTitle() + getSubHeading(contents));
		
		removeAll();
		if(shouldShowWizardTitles())
			add(wizardTitlePanel, BorderLayout.BEFORE_FIRST_LINE);
		add(contents, BorderLayout.CENTER);
		add(navigationButtons, BorderLayout.AFTER_LAST_LINE);
		allowSplitterToHideUsCompletely();
		revalidate();
		repaint();
	}

	private String getSubHeading(SkeletonWizardStep contents)
	{
		if (contents.getSubHeading() == null)
			return "";
		
		return ": " + contents.getSubHeading();
	}

	private boolean shouldShowWizardTitles()
	{
		return mainWindow.getProject().isOpen();
	} 
	
	public void refresh() throws Exception
	{
		SkeletonWizardStep stepClass = wizardManager.getCurrentStep();
		stepClass.refresh();
		stepClass.validate();
	}
	
	public void setStepTitle(String text)
	{
		wizardTitlePanel.setStepTitle(text);
	}
	
	public void setScreenTitle(String text)
	{
		wizardTitlePanel.setScreenTitle(text);
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private void allowSplitterToHideUsCompletely()
	{
		setMinimumSize(new Dimension(0, 0));
	}
	
	Component createNavigationButtons()
	{
		Actions actions = mainWindow.getActions();

		OneRowPanel buttonBar = new OneRowPanel();
		buttonBar.setMargins(5);
		buttonBar.setBackground(AppPreferences.getWizardTitleBackground());
		buttonBar.add(new PanelButton(actions.get(ActionWizardPrevious.class)));
		buttonBar.add(Box.createHorizontalStrut(20));
		buttonBar.add(new PanelButton(actions.get(ActionWizardNext.class)));
		
		return buttonBar;
	}
	
	protected MainWindow mainWindow;
	private WizardManager wizardManager;
	private WizardTitlePanel wizardTitlePanel;
	private Component navigationButtons;
}
