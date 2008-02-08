/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionWizardNext;
import org.conservationmeasures.eam.actions.ActionWizardPrevious;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.layout.OneRowPanel;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.MainWindow;

public class WizardPanel extends JPanel
{
	public WizardPanel(MainWindow mainWindowToUse, WizardTitlePanel wizardTitlePanelToUse) throws Exception
	{
		super(new BorderLayout());
		wizardTitlePanel = wizardTitlePanelToUse;
		
		setBackground(AppPreferences.WIZARD_BACKGROUND);
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
		
		return " (" + contents.getSubHeading() + ")";
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
		buttonBar.setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);
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
