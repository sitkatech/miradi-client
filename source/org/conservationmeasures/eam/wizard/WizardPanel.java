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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class WizardPanel extends JPanel
{
	public WizardPanel(MainWindow mainWindowToUse, UmbrellaView viewToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		wizardManager = mainWindow.getWizardManager();
		setFocusCycleRoot(true);
		view = viewToUse;
		navigationButtons = createNavigationButtons();
		setupSteps();
	}

	private void setupSteps()
	{
		try
		{
			wizardManager.setUpSteps(view,this);
			setOverViewStep(view);
		}
		catch (Exception e)
		{
			String body = EAM.text("Wizard load failed for view: ") + view.cardName();
			EAM.errorDialog(body);
			EAM.logError(body);
			EAM.logException(e);
		}
	}

	public void setOverViewStep(UmbrellaView view) throws Exception
	{
		String defaultStepName = removeSpaces(view.cardName()) + "OverviewStep";
		currentStepName = wizardManager.setStep(defaultStepName, defaultStepName);
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
	
	public void control(String controlName) throws Exception
	{
		SkeletonWizardStep step = wizardManager.findStep(currentStepName);
		Class destinationStepClass = wizardManager.findControlTargetStep(controlName, step);
		if (destinationStepClass==null)
		{
			String errorText = "Control ("+ controlName +") not found for step: " + wizardManager.getStepName(step);
			reportError(EAM.text(errorText));
		}

		jump(destinationStepClass);
	}

	private void reportError(String msg)
	{
		EAM.logError(msg);
		EAM.errorDialog(msg);
	}

	private String removeSpaces(String name)
	{
		return name.replaceAll(" ", "");
	}

	public void refresh() throws Exception
	{
		SkeletonWizardStep stepClass = wizardManager.findStep(currentStepName);
		stepClass.refresh();
		stepClass.validate();
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		currentStepName = wizardManager.setStep(stepMarker, currentStepName);
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private void allowSplitterToHideUsCompletely()
	{
		setMinimumSize(new Dimension(0, 0));
	}
	
	public UmbrellaView getView()
	{
		return view;
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
	
	protected UmbrellaView view;
	protected MainWindow mainWindow;
	public String currentStepName;
	private WizardManager wizardManager;
	Component navigationButtons;
}
