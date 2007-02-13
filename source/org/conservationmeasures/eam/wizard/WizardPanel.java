/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

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
		setupSteps();
	}

	private void setupSteps()
	{
		try
		{
			wizardManager.setUpSteps(view,this);
			String stepName = removeSpaces(view.cardName()) + "OverviewStep";
			currentStepName = wizardManager.setStep(stepName, stepName);
		}
		catch (Exception e)
		{
			String body = EAM.text("Wizard load failed for view: ") + view.cardName();
			EAM.errorDialog(body);
			EAM.logError(body);
			EAM.logException(e);
		}
	}

	public void setContents(JPanel contents)
	{
		removeAll();
		add(contents, BorderLayout.CENTER);
		allowSplitterToHideUsCompletely();
		revalidate();
		repaint();
	} 
	
	public void control(String controlName) throws Exception
	{
		SkeletonWizardStep step = wizardManager.findStep(currentStepName);
		jump(wizardManager.findControlTargetStep(controlName, step));
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
	
	protected UmbrellaView view;
	protected MainWindow mainWindow;
	public String currentStepName;
	private WizardManager wizardManager;

}
