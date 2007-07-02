/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
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
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.MainWindow;

public class WizardPanel extends JPanel
{
	public WizardPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(new BorderLayout());
		setBackground(AppPreferences.WIZARD_BACKGROUND);
		mainWindow = mainWindowToUse;
		wizardManager = mainWindow.getWizardManager();
		setFocusCycleRoot(true);
		navigationButtons = createNavigationButtons();
		wizardManager.setUpSteps(this);
	}

	String getCurrentStepName()
	{
		return wizardManager.getCurrentStepName();
	}

	public void setContents(JPanel contents)
	{
		removeAll();
		add(contents, BorderLayout.CENTER);
		add(navigationButtons, BorderLayout.AFTER_LAST_LINE);
		allowSplitterToHideUsCompletely();
		revalidate();
		repaint();
	} 
	
	public void refresh() throws Exception
	{
		SkeletonWizardStep stepClass = wizardManager.findStep(getCurrentStepName());
		stepClass.refresh();
		stepClass.validate();
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
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(10));
		box.add(new PanelButton(actions.get(ActionWizardPrevious.class)));
		box.add(Box.createHorizontalStrut(20));
		box.add(new PanelButton(actions.get(ActionWizardNext.class)));
		box.add(Box.createHorizontalGlue());
		return box;
	}
	
	protected MainWindow mainWindow;
	private WizardManager wizardManager;
	Component navigationButtons;
}
