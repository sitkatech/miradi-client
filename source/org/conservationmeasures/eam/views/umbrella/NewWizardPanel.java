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
import org.conservationmeasures.eam.views.diagram.wizard.DescribeTargetStatusStep;
import org.conservationmeasures.eam.views.diagram.wizard.DevelopDraftStrategiesStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardConstructChainsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardLinkDirectThreatsToTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramOverviewStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardProjectScopeStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardReviewAndModifyTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardReviewModelAndAdjustStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardVisionStep;
import org.conservationmeasures.eam.views.diagram.wizard.EditAllStrategiesStep;
import org.conservationmeasures.eam.views.diagram.wizard.RankDraftStrategiesStep;
import org.conservationmeasures.eam.views.diagram.wizard.SelectChainStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardFocusStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatMatrixOverviewStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckBundleStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckTotalsStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardChooseBundle;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardIrreversibilityStep;
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
				stepTable.loadDigramViewSteps(this);
				
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
	//TODO: this should not be a static but is really a wizard manager and should be pulled from above.
	private static StepTable stepTable = new StepTable();

}

class StepTable extends Hashtable
{
	
	public void loadDigramViewSteps(IWizardPanel panel)
	{
		final String viewName = DiagramView.getViewName();
		
		WizardStepEntry stepEntry = loadStep(new DiagramOverviewStep(panel), viewName);
		stepEntry.loadNextBack(DiagramOverviewStep.class, DiagramWizardProjectScopeStep.class);
		
		stepEntry = loadStep(new DiagramWizardProjectScopeStep(panel), viewName);
		stepEntry.loadNextBack(DiagramWizardVisionStep.class, DiagramOverviewStep.class);
		
		stepEntry = loadStep(new DiagramWizardVisionStep(panel), viewName);
		stepEntry.loadNextBack(DiagramWizardDefineTargetsStep.class, DiagramWizardProjectScopeStep.class);
		
		stepEntry = loadStep(new DiagramWizardDefineTargetsStep(panel), viewName);
		stepEntry.loadNextBack(DiagramWizardReviewAndModifyTargetsStep.class, DiagramWizardVisionStep.class);
		
		stepEntry = loadStep(new DiagramWizardReviewAndModifyTargetsStep(panel), viewName);
		stepEntry.loadNextBack(DescribeTargetStatusStep.class, DiagramWizardDefineTargetsStep.class);
		
		stepEntry = loadStep(new DescribeTargetStatusStep(panel), viewName);
		stepEntry.loadNextBack(DiagramWizardIdentifyDirectThreatStep.class, DiagramWizardReviewAndModifyTargetsStep.class);
		
		stepEntry = loadStep(new DiagramWizardIdentifyDirectThreatStep(panel), viewName);
		stepEntry.loadNextBack(DiagramWizardLinkDirectThreatsToTargetsStep.class, DescribeTargetStatusStep.class);
		
		stepEntry = loadStep(new DiagramWizardLinkDirectThreatsToTargetsStep(panel), viewName);
		stepEntry.loadNextBack(DiagramWizardIdentifyIndirectThreatStep.class, DiagramWizardIdentifyDirectThreatStep.class);
		
		stepEntry = loadStep(new DiagramWizardIdentifyIndirectThreatStep(panel), viewName);		
		stepEntry.loadNextBack(DiagramWizardConstructChainsStep.class, DiagramWizardLinkDirectThreatsToTargetsStep.class);
		
		stepEntry = loadStep(new DiagramWizardConstructChainsStep(panel), viewName);	
		stepEntry.loadNextBack(DiagramWizardReviewModelAndAdjustStep.class, DiagramWizardIdentifyIndirectThreatStep.class);
		
		stepEntry = loadStep(new DiagramWizardReviewModelAndAdjustStep(panel), viewName);		
		stepEntry.loadNextBack(SelectChainStep.class, DiagramWizardConstructChainsStep.class);
		
		stepEntry = loadStep(new SelectChainStep(panel), viewName);		
		stepEntry.loadNextBack(DevelopDraftStrategiesStep.class, DiagramWizardReviewModelAndAdjustStep.class);
		
		stepEntry = loadStep(new DevelopDraftStrategiesStep(panel), viewName);
		stepEntry.loadNextBack(RankDraftStrategiesStep.class, SelectChainStep.class);
		
		stepEntry = loadStep(new RankDraftStrategiesStep(panel), viewName);
		stepEntry.loadNextBack(EditAllStrategiesStep.class, DevelopDraftStrategiesStep.class);
		
		stepEntry = loadStep(new EditAllStrategiesStep(panel), viewName);
		stepEntry.loadNextBack(StrategicPlanDevelopGoalStep.class, RankDraftStrategiesStep.class);
		
		stepEntry = loadStep(new StrategicPlanDevelopGoalStep(panel), viewName);
		stepEntry.loadNextBack(StrategicPlanDevelopObjectivesStep.class, EditAllStrategiesStep.class);
		
		stepEntry = loadStep(new StrategicPlanDevelopObjectivesStep(panel), viewName);		
		stepEntry.loadNextBack(MonitoringWizardFocusStep.class, StrategicPlanDevelopGoalStep.class);
		
		stepEntry = loadStep(new MonitoringWizardFocusStep(panel), viewName);		
		stepEntry.loadNextBack(MonitoringWizardDefineIndicatorsStep.class, StrategicPlanDevelopObjectivesStep.class);
		
		stepEntry = loadStep(new MonitoringWizardDefineIndicatorsStep(panel), viewName);
		stepEntry.loadNextBack(MonitoringWizardDefineIndicatorsStep.class, MonitoringWizardDefineIndicatorsStep.class);
	}


	public void loadThreatMatrixViewSteps(ThreatRatingWizardPanel panel) throws Exception
	{
		final String viewName = ThreatMatrixView.getViewName();
		Project project = panel.mainWindow.getProject();
		
		WizardStepEntry stepEntry = loadStep(new ThreatMatrixOverviewStep(panel), viewName);
		stepEntry.loadNextBack(ThreatRatingWizardChooseBundle.class, DiagramWizardLinkDirectThreatsToTargetsStep.class);
		stepEntry.loadControl("View:Diagram", DiagramOverviewStep.class);

		stepEntry = loadStep(new ThreatRatingWizardChooseBundle(panel), viewName);
		stepEntry.loadNextBack(ThreatRatingWizardScopeStep.class, ThreatMatrixOverviewStep.class);
		stepEntry.loadControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		stepEntry = loadStep(ThreatRatingWizardScopeStep.create(panel, project), viewName);
		stepEntry.loadNextBack(ThreatRatingWizardSeverityStep.class, ThreatRatingWizardChooseBundle.class);
	
		stepEntry = loadStep(ThreatRatingWizardSeverityStep.create(panel, project), viewName);
		stepEntry.loadNextBack(ThreatRatingWizardIrreversibilityStep.class, ThreatRatingWizardScopeStep.class);
		
		stepEntry = loadStep(ThreatRatingWizardIrreversibilityStep.create(panel, project), viewName);
		stepEntry.loadNextBack(ThreatRatingWizardCheckBundleStep.class, ThreatRatingWizardSeverityStep.class);
		
		stepEntry = loadStep(new ThreatRatingWizardCheckBundleStep(panel), viewName);
		stepEntry.loadNextBack(ThreatRatingWizardChooseBundle.class, ThreatRatingWizardIrreversibilityStep.class);

		stepEntry = loadStep(new ThreatRatingWizardCheckTotalsStep(panel) , viewName);
		stepEntry.loadNextBack(ThreatMatrixOverviewStep.class, ThreatRatingWizardChooseBundle.class);
	}

	private WizardStepEntry loadStep(WizardStep wizardStep, String viewName)
	{
		WizardStepEntry stepEntry = new WizardStepEntry(wizardStep.getClass().getSimpleName(),  wizardStep, viewName);
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
	//TODO: If we can change the jump classes later to contain the static reference to the step class (.class) to go to then we can get rid of string step names
	WizardStepEntry(String stepNameToUse, WizardStep wizardStep, String viewNameToUse)
	{
		viewName = viewNameToUse;
		stepName = stepNameToUse;
		entries = new Hashtable();
		step = wizardStep;
	}
	
	
	void loadControl(String controlName , Class controlStep)
	{
		entries.put(controlName, new WizardControl(controlName, controlStep.getSimpleName()));
	}
	
	void loadNextBack(Class controlStepNameNext , Class controlStepBack)
	{
		entries.put("Next", new WizardControl("Next", controlStepNameNext.getSimpleName()));
		entries.put("Back", new WizardControl("Back", controlStepBack.getSimpleName()));
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
