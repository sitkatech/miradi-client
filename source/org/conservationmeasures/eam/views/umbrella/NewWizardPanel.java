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
			if (mainWindow.getCurrentView().cardName().equals(ThreatMatrixView.getViewName()));
				stepTable.loadThreatMatrixViewSteps((ThreatRatingWizardPanel)this, mainWindow);
				
			if (mainWindow.getCurrentView().cardName().equals(DiagramView.getViewName()));
				stepTable.loadDigramViewSteps();
				
			setInitialStep();
		}
		catch (Exception e)
		{
			EAM.logError("Wizard load failed");
			EAM.logException(e);
		}
	}



	private void setInitialStep() throws Exception
	{
		currentStepName = mainWindow.getCurrentView().cardName() + "OverviewStep";
		setStep(currentStepName);
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
			SkeletonWizardStep stepClass = stepTable.findStep(newStep).getStepClass();
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
		SkeletonWizardStep stepClass = stepTable.findStep(currentStepName).getStepClass();
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

}

class StepTable extends Hashtable
{
	
	public void loadDigramViewSteps()
	{
		final String viewName = DiagramView.getViewName();
		loadStep(viewName, "DiagramWizardOverviewStep");
		loadStep(viewName, "DiagramWizardLinkDirectThreatsToTargetsStep");
	}


	public void loadThreatMatrixViewSteps(ThreatRatingWizardPanel panel, MainWindow mainWindow) throws Exception
	{
		final String viewName = ThreatMatrixView.getViewName();
		WizardStepEntry stepEntry = loadStep(viewName, "ThreatMatrixOverviewStep");
		stepEntry.loadControl("Next", "ThreatRatingWizardChooseBundle");
		stepEntry.loadControl("Back", "DiagramWizardLinkDirectThreatsToTargetsStep");
		stepEntry.loadControl("View:Diagram", "DiagramWizardOverviewStep");
		stepEntry.setStepClass(new ThreatMatrixOverviewStep(panel));

		stepEntry = loadStep(viewName, "ThreatRatingWizardChooseBundle");
		stepEntry.loadControl("Next", "ThreatRatingWizardScopeStep");
		stepEntry.loadControl("Back", "ThreatMatrixOverviewStep");
		stepEntry.loadControl("Done", "ThreatRatingWizardCheckTotalsStep");
		stepEntry.setStepClass(new ThreatRatingWizardChooseBundle(panel));
		
		stepEntry = loadStep(viewName, "ThreatRatingWizardScopeStep");
		stepEntry.loadControl("Next", "ThreatRatingWizardSeverityStep");
		stepEntry.loadControl("Back", "ThreatRatingWizardChooseBundle");
		stepEntry.setStepClass(ThreatRatingWizardScopeStep.create(panel, mainWindow.getProject()));
	
		stepEntry = loadStep(viewName, "ThreatRatingWizardSeverityStep");
		stepEntry.loadControl("Next", "ThreatRatingWizardIrreversibilityStep");
		stepEntry.loadControl("Back", "ThreatRatingWizardScopeStep");
		stepEntry.setStepClass(ThreatRatingWizardSeverityStep.create(panel, mainWindow.getProject()));
		
		stepEntry = loadStep(viewName, "ThreatRatingWizardIrreversibilityStep");
		stepEntry.loadControl("Next", "ThreatRatingWizardCheckBundleStep");
		stepEntry.loadControl("Back", "ThreatRatingWizardSeverityStep");
		stepEntry.setStepClass(ThreatRatingWizardIrreversibilityStep.create(panel, mainWindow.getProject()));
		
		stepEntry = loadStep(viewName, "ThreatRatingWizardCheckBundleStep");
		stepEntry.loadControl("Next", "ThreatRatingWizardChooseBundle");
		stepEntry.loadControl("Back", "ThreatRatingWizardIrreversibilityStep");
		stepEntry.setStepClass(new ThreatRatingWizardCheckBundleStep(panel));

		stepEntry = loadStep(viewName, "ThreatRatingWizardCheckTotalsStep");
		stepEntry.loadControl("Next", "ThreatMatrixOverviewStep");
		stepEntry.loadControl("Back", "ThreatRatingWizardChooseBundle");
		stepEntry.setStepClass(new ThreatRatingWizardCheckTotalsStep(panel));
	}

	private WizardStepEntry loadStep(final String viewName, final String string)
	{
		WizardStepEntry stepEntry = new WizardStepEntry(string,  viewName);
		put(stepEntry.getStepName(),stepEntry);
		return stepEntry;
	}

	WizardStepEntry findStep(String stepName)
	{
		WizardStepEntry entry =(WizardStepEntry)get(stepName);
		if (entry==null)
			EAM.logError("ENTRY NOT FOUND FOR STEP NAME=:" + stepName);
		return entry;
	}
}

class WizardStepEntry extends Hashtable
{
	WizardStepEntry(String stepNameToUse, String viewNameToUse)
	{
		viewName = viewNameToUse;
		stepName = stepNameToUse;
	}
	

	void loadControl(String controlName , String controlStep)
	{
		put(controlName, new WizardControl(controlName, controlStep));
	}
	

	WizardControl findControl(String controlName)
	{
		WizardControl control = (WizardControl)get(controlName);
		if (control==null)
			EAM.logError("CONTROL ("+ controlName +") NOT FOUND IN STEP=:" + stepName);
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

	WizardStep getStepClass()
	{
		return step;
	}
	
	void setStepClass(WizardStep panel)
	{
		step = panel;
	}
	
	private String stepName;
	private String viewName;
	private WizardStep step;
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
