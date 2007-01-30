/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.MainWindow;

public class WizardPanel extends JPanel implements IWizardPanel
{
	public WizardPanel(MainWindow mainWindowToUse)
	{
		super(new BorderLayout());
		steps = new SkeletonWizardStep[0];
		mainWindow = mainWindowToUse;
		setFocusCycleRoot(true);
	}


	public void setContents(JPanel contents)
	{
		removeAll();

		add(contents, BorderLayout.CENTER);
		allowSplitterToHideUsCompletely();
		revalidate();
		repaint();
	}
	

	public void next() throws Exception
	{
		int nextStep = currentStep + 1;
		if(nextStep >= steps.length)
			nextStep = 0;
		
		setStep(nextStep);
	}
	

	public void previous() throws Exception
	{
		int nextStep = currentStep - 1;
		if(nextStep < 0)
			return;
		
		setStep(nextStep);
	}
	
	private void allowSplitterToHideUsCompletely()
	{
		setMinimumSize(new Dimension(0, 0));
	}
	

	public void setStep(int newStep) throws Exception
	{
		currentStep = newStep;
		steps[currentStep].refresh();
		setContents(steps[currentStep]);
	}
	

	public void refresh() throws Exception
	{
		for(int i = 0; i < steps.length; ++i)
			steps[i].refresh();
		validate();
	}
	
	public int addStep(SkeletonWizardStep step)
	{
		List existingSteps = Arrays.asList(steps);
		Vector newSteps = new Vector(existingSteps);
		newSteps.add(step);
		steps = (SkeletonWizardStep[])newSteps.toArray(new SkeletonWizardStep[0]);
		return (steps.length - 1);
	}
	

	public void jump(Class stepMarker) throws Exception
	{
		throw new RuntimeException("Step not in this view: " + stepMarker);
	}


	public int getCurrentStep()
	{
		return currentStep;
	}

	public void control(String controlName) throws Exception
	{
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	SkeletonWizardStep[] steps;
	protected int currentStep;
	MainWindow mainWindow;

}

