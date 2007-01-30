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
import org.conservationmeasures.eam.project.Project;
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
		setupSteps();
	}

	private void setupSteps()
	{
		try
		{
			if (mainWindow.getCurrentView().cardName().equals(ThreatMatrixView.getViewName()));
				stepTable.loadThreatMatrixViewSteps((ThreatRatingWizardPanel)this);
				
			if (mainWindow.getCurrentView().cardName().equals(DiagramView.getViewName()));
				stepTable.loadDigramViewSteps();
				
			setStep("");
		}
		catch (Exception e)
		{
			EAM.logError("Wizard load failed");
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
		if (newStep.equals(""))
			newStep = currentStepName = mainWindow.getCurrentView().cardName() + "OverviewStep";
		
		WizardStepEntry entryCur = stepTable.findStep(currentStepName);
		String viewNameCur = entryCur.getViewName();
		
		WizardStepEntry entryNew = stepTable.findStep(newStep);
		String viewNameNew = entryNew.getViewName();
		
		if (!viewNameNew.equals(viewNameCur))
		{
			mainWindow.getProject().executeCommand(new CommandSwitchView(viewNameNew));
			EAM.logWarning("During wizard migration the target step may not be correclty opened:" + newStep);
		}

		SkeletonWizardStep stepClass = stepTable.findStep(newStep).getStepClass();
		//TODO: if not needed when all wizard converted.
		if (stepClass!=null)
		{
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
		loadStep("DiagramWizardOverviewStep", null, viewName);
		loadStep("DiagramWizardLinkDirectThreatsToTargetsStep", null, viewName);
	}


	public void loadThreatMatrixViewSteps(ThreatRatingWizardPanel panel) throws Exception
	{
		final String viewName = ThreatMatrixView.getViewName();
		Project project = panel.mainWindow.getProject();
		
		WizardStepEntry stepEntry = loadStep("ThreatMatrixOverviewStep", new ThreatMatrixOverviewStep(panel), viewName);
		stepEntry.loadNextBack("ThreatRatingWizardChooseBundle", "DiagramWizardLinkDirectThreatsToTargetsStep");
		stepEntry.loadControl("View:Diagram", "DiagramWizardOverviewStep");

		stepEntry = loadStep("ThreatRatingWizardChooseBundle", new ThreatRatingWizardChooseBundle(panel), viewName);
		stepEntry.loadNextBack("ThreatRatingWizardScopeStep", "ThreatMatrixOverviewStep");
		stepEntry.loadControl("Done", "ThreatRatingWizardCheckTotalsStep");
		
		stepEntry = loadStep("ThreatRatingWizardScopeStep", ThreatRatingWizardScopeStep.create(panel, project), viewName);
		stepEntry.loadNextBack("ThreatRatingWizardSeverityStep", "ThreatRatingWizardChooseBundle");
	
		stepEntry = loadStep("ThreatRatingWizardSeverityStep", ThreatRatingWizardSeverityStep.create(panel, project), viewName);
		stepEntry.loadNextBack("ThreatRatingWizardIrreversibilityStep", "ThreatRatingWizardScopeStep");
		
		stepEntry = loadStep("ThreatRatingWizardIrreversibilityStep", ThreatRatingWizardIrreversibilityStep.create(panel, project), viewName);
		stepEntry.loadNextBack("ThreatRatingWizardCheckBundleStep", "ThreatRatingWizardSeverityStep");
		
		stepEntry = loadStep("ThreatRatingWizardCheckBundleStep", new ThreatRatingWizardCheckBundleStep(panel), viewName);
		stepEntry.loadNextBack("ThreatRatingWizardChooseBundle", "ThreatRatingWizardIrreversibilityStep");

		stepEntry = loadStep("ThreatRatingWizardCheckTotalsStep", new ThreatRatingWizardCheckTotalsStep(panel) , viewName);
		stepEntry.loadNextBack("ThreatMatrixOverviewStep", "ThreatRatingWizardChooseBundle");
	}

	private WizardStepEntry loadStep(String string, WizardStep wizardStep, String viewName)
	{
		WizardStepEntry stepEntry = new WizardStepEntry(string,  viewName, wizardStep);
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

class WizardStepEntry
{
	WizardStepEntry(String stepNameToUse, String viewNameToUse, WizardStep wizardStep)
	{
		viewName = viewNameToUse;
		stepName = stepNameToUse;
		entries = new Hashtable();
		step = wizardStep;
	}
	

	void loadControl(String controlName , String controlStep)
	{
		entries.put(controlName, new WizardControl(controlName, controlStep));
	}
	
	
	void loadNextBack(String controlStepNameNext , String controlStepBack)
	{
		entries.put("Next", new WizardControl("Next", controlStepNameNext));
		entries.put("Back", new WizardControl("Back", controlStepBack));
	}

	
	WizardControl findControl(String controlName)
	{
		WizardControl control = (WizardControl)entries.get(controlName);
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
	
	private String stepName;
	private String viewName;
	private WizardStep step;
	private Hashtable entries;
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
