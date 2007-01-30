/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckBundleStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckTotalsStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardChooseBundle;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardIrreversibilityStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatMatrixOverviewStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardPanel;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardScopeStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardSeverityStep;

public class NewWizardPanel extends JPanel implements IWizardPanel
{
	public NewWizardPanel(MainWindow mainWindowToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		setFocusCycleRoot(true);
		currentStepName =  "";
		setupSteps();
	}

	private void setupSteps()
	{
		try
		{
			setupStepsThreatMatrixView();
			setInitialStep();
		}
		catch (Exception e)
		{
			EAM.logError("Wizard load failed");
			EAM.logException(e);
		}
	}

	private void setupStepsThreatMatrixView() throws Exception
	{
		addStep(new ThreatMatrixOverviewStep((ThreatRatingWizardPanel)this));
		addStep(new ThreatRatingWizardChooseBundle((ThreatRatingWizardPanel)this));
		addStep(ThreatRatingWizardScopeStep.create((ThreatRatingWizardPanel)this, mainWindow.getProject()));
		addStep(ThreatRatingWizardSeverityStep.create((ThreatRatingWizardPanel)this, mainWindow.getProject()));
		addStep(ThreatRatingWizardIrreversibilityStep.create((ThreatRatingWizardPanel)this, mainWindow.getProject()));
		addStep(new ThreatRatingWizardCheckBundleStep((ThreatRatingWizardPanel)this));
		addStep(new ThreatRatingWizardCheckTotalsStep((ThreatRatingWizardPanel)this));

	}

	private void setInitialStep() throws Exception
	{
		currentStepName = mainWindow.getCurrentView().cardName() + "OverviewStep";
		setStep(currentStepName);
	}
	
	
	private void addStep(SkeletonWizardStep step)
	{
		String name = step.getClass().getSimpleName();
		WizardStepEntry entry = stepTable.findStep(name);
		if (mainWindow.getCurrentView().cardName().equals(entry.getViewName()));
		{
			stepClasses.put(step.getClass().getSimpleName(), step);
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
		WizardStepEntry entry = stepTable.findStep(currentStepName);
		WizardControl control = entry.findControl(controlName);
		setStep(control.getStepName());
	}
	
	//TODO: will go away when all wizards are converted, called by SkeletonWizardStep
	public void next() throws Exception
	{
		control("Next");
	}
	
	//TODO: will go away when all wizards are converted, called by SkeletonWizardStep
	public void previous() throws Exception
	{
		control("Back");
	}

	//TODO: not used but need do to interface: will go away when all wizards are converted
	public void setStep(int newStep) throws Exception
	{
	}
	
	private void setStep(String newStep) throws Exception
	{
		WizardStepEntry entryCur = stepTable.findStep(currentStepName);
		String viewNameCur = entryCur.getViewName();
		
		WizardStepEntry entryNew = stepTable.findStep(newStep);
		String viewNameNew = entryNew.getViewName();
		
		if (!viewNameNew.equals(viewNameCur))
		{
			//TODO: should be in a single undo/redo transaction
			mainWindow.getProject().executeCommand(new CommandSwitchView(viewNameNew));
			EAM.logWarning("During wizard migration the target step may not be correclty opened:" + newStep);
			mainWindow.getCurrentView().jump(newStep);
		}
		else {
			SkeletonWizardStep stepClass = (SkeletonWizardStep)stepClasses.get(newStep);
			currentStepName = newStep;
			stepClass.refresh();
			setContents(stepClass);
		}
	}
	
	//TODO: not used but need do to interface: will go away when all wizards are converted
	public Class makeStepMarker(String stepName)
	{
		return null;
	}
	
	public void refresh() throws Exception
	{
		SkeletonWizardStep stepClass = (SkeletonWizardStep)stepClasses.get(currentStepName);
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
	
	//TODO: not used but need do to interface: will go away when all wizards are converted
	public int getCurrentStep()
	{
		return 0;
	}
	
	protected MainWindow mainWindow;
	public String currentStepName;
	private static StepTable stepTable = new StepTable();
	private static Hashtable stepClasses = new Hashtable();

}

class StepTable extends Hashtable
{
	StepTable()
	{
		load();
	}
	
	void load() 
	{
		String[] controlNames1 = {"Next" , "Back" , "View:Diagram"};
		String[] steps1 = {
				"ThreatRatingWizardChooseBundle", 
				"DiagramWizardLinkDirectThreatsToTargetsStep", 
				"DiagramWizardOverviewStep"};
		WizardStepEntry stepEntry = new WizardStepEntry( 
				"ThreatMatrixOverviewStep",  ThreatMatrixView.getViewName(), controlNames1, steps1);
		put(stepEntry.getStepName(),stepEntry);
		
		
		String[] controlNames2 = {"Next" , "Back" , "Done"};
		String[] steps2 = {
				"ThreatRatingWizardScopeStep", 
				"ThreatMatrixOverviewStep", 
				"ThreatRatingWizardCheckTotalsStep"};
		stepEntry = new WizardStepEntry( 
				"ThreatRatingWizardChooseBundle",  ThreatMatrixView.getViewName(), controlNames2, steps2);
		put(stepEntry.getStepName(),stepEntry);
		
		
		String[] controlNames3 = {"Next" , "Back"};
		String[] steps3 = {
				"ThreatRatingWizardSeverityStep", 
				"ThreatRatingWizardChooseBundle"};
		stepEntry = new WizardStepEntry( 
				"ThreatRatingWizardScopeStep",  ThreatMatrixView.getViewName(), controlNames3, steps3);
		put(stepEntry.getStepName(),stepEntry);
		
		
		String[] controlNames4 = {"Next" , "Back"};
		String[] steps4 = {
				"ThreatRatingWizardIrreversibilityStep", 
				"ThreatRatingWizardScopeStep"};
		stepEntry = new WizardStepEntry( 
				"ThreatRatingWizardSeverityStep",  ThreatMatrixView.getViewName(), controlNames4, steps4);
		put(stepEntry.getStepName(),stepEntry);
		
		
		String[] controlNames5 = {"Next" , "Back"};
		String[] steps5 = {
				"ThreatRatingWizardCheckBundleStep", 
				"ThreatRatingWizardSeverityStep"};
		stepEntry = new WizardStepEntry( 
				"ThreatRatingWizardIrreversibilityStep",  ThreatMatrixView.getViewName(), controlNames5, steps5);
		put(stepEntry.getStepName(),stepEntry);
		
		
		String[] controlNames6 = {"Next" , "Back"};
		String[] steps6 = {
				"DiagramWizardIdentifyIndirectThreatStep", 
				"ThreatRatingWizardIrreversibilityStep"};
		stepEntry = new WizardStepEntry( 
				"ThreatRatingWizardCheckBundleStep",  ThreatMatrixView.getViewName(), controlNames6, steps6);
		put(stepEntry.getStepName(),stepEntry);
		
		
		String[] controlNames7 = {"Next" , "Back"};
		String[] steps7 = {
				"ThreatMatrixOverviewStep", 
				"ThreatMatrixOverviewStep"};
		stepEntry = new WizardStepEntry( 
				"ThreatRatingWizardCheckTotalsStep",  ThreatMatrixView.getViewName(), controlNames7, steps7);
		put(stepEntry.getStepName(),stepEntry);
		
		
		String[] controlNames8 = {};
		String[] steps8 = {};
		stepEntry = new WizardStepEntry( 
				"DiagramWizardOverviewStep",  DiagramView.getViewName(), controlNames8, steps8);
		put(stepEntry.getStepName(),stepEntry);
		
		String[] controlNames9 = {};
		String[] steps9 = {};
		stepEntry = new WizardStepEntry( 
				"DiagramWizardLinkDirectThreatsToTargetsStep", DiagramView.getViewName(), controlNames9, steps9);
		put(stepEntry.getStepName(),stepEntry);
		
		
	}

	WizardStepEntry findStep(String stepName)
	{
		WizardStepEntry entry =(WizardStepEntry)get(stepName);
		if (entry==null)
			System.out.println("ENTRY NOT FOUND FOR STEP NAME=:" + stepName);
		return entry;
	}
	
}

class WizardStepEntry extends Hashtable
{
	WizardStepEntry(String stepNameToUse, String viewNameToUse, String[] controlNames, String[] steps)
	{
		viewName = viewNameToUse;
		stepName = stepNameToUse;
		load(controlNames, steps);
	}
	
	void load(String[] controlNames, String[] steps) 
	{
		for (int i=0; i<controlNames.length; ++i)
		{
			put(controlNames[i], new WizardControl(controlNames[i],steps[i]));
		}
	}
	
	WizardControl findControl(String controlName)
	{
		WizardControl control = (WizardControl)get(controlName);
		if (control==null)
			System.out.println("CONTROL ("+ controlName +") NOT FOUND IN STEP=:" + stepName);
		return control;
	}

	
	String getViewName()
	{
		return viewName;
	}
	
	String getStepName()
	{
		return stepName;
	}

	
	String stepName;
	String viewName;
}

class WizardControl
{
	WizardControl(String controlNameToUse, String stepNameToUse)
	{
		controlName = controlNameToUse;
		stepName = stepNameToUse;
	}

	String getStepName()
	{
		return stepName;
	}
	
	String stepName;
	String controlName;
}
