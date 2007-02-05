/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class WizardPanel extends JPanel
{
	public WizardPanel(MainWindow mainWindowToUse, UmbrellaView viewToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		setFocusCycleRoot(true);
		view = viewToUse;
		setupSteps();
	}

	private void setupSteps()
	{
		try
		{
			wizardManager.setUpSteps(view,this);
			//TODO: avoid dubble initialize of current step name 
			currentStepName = removeSpaces(view.cardName()) + "OverviewStep";
			setStep(currentStepName);
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
		WizardStepEntry entry = wizardManager.findStep(currentStepName);
		jump(entry.findControlTargetStep(controlName));
	}


	public void setStep(Class newStep) throws Exception
	{
		setStep(newStep.getSimpleName());
	}
	
	public void setStep(String newStep) throws Exception
	{
		//TODO: this shoud be cleaner when we do not sub class wizard panels
		currentStepName = wizardManager.setStep(newStep, currentStepName);
	}
	
	private String removeSpaces(String name)
	{
		return name.replaceAll(" ", "");
	}

	
	public void refresh() throws Exception
	{
		SkeletonWizardStep stepClass = wizardManager.findStep(currentStepName).getStepClass();
		stepClass.refresh();
		stepClass.validate();
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		String name = stepMarker.getSimpleName();
		if (name.startsWith("ActionJump"))
			name = name.substring("ActionJump".length());
		jump(name);
	}

	public void jump(String stepMarker) throws Exception
	{
			setStep(stepMarker);
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
	//TODO: this should not be a static but is really a wizard manager and should be pulled from above.
	private static WizardManager wizardManager = new WizardManager();

}
