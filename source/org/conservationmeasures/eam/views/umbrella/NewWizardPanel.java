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

import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanHowToConstructStep;
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.wizard.DescribeTargetStatusStep;
import org.conservationmeasures.eam.views.diagram.wizard.DevelopDraftStrategiesStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramOverviewStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardConstructChainsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardLinkDirectThreatsToTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardProjectScopeStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardReviewAndModifyTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardReviewModelAndAdjustStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardVisionStep;
import org.conservationmeasures.eam.views.diagram.wizard.EditAllStrategiesStep;
import org.conservationmeasures.eam.views.diagram.wizard.RankDraftStrategiesStep;
import org.conservationmeasures.eam.views.diagram.wizard.SelectChainStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringOverviewStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardEditIndicatorsStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardFocusStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanHowToConstructStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanViewAllGoals;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanViewAllObjectives;
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
			if (mainWindow.getCurrentView().cardName().equals(ThreatMatrixView.getViewName()))
				setupStepsThreatMatrixView();
				
			if (mainWindow.getCurrentView().cardName().equals(DiagramView.getViewName()))
				setupStepsDiagramView();
				
			setStep("");
		}
		catch (Exception e)
		{
			EAM.logError("Wizard load failed");
			EAM.logException(e);
		}
	}

	
	private void setupStepsDiagramView() throws Exception
	{
		addStep(new DiagramOverviewStep(this));
		addStep(new DiagramWizardProjectScopeStep(this));
		addStep(new DiagramWizardVisionStep(this));
		addStep(new DiagramWizardDefineTargetsStep(this));
		addStep(new DiagramWizardReviewAndModifyTargetsStep(this));
		addStep(new DescribeTargetStatusStep(this));
		addStep(new DiagramWizardIdentifyDirectThreatStep(this));
		addStep(new DiagramWizardLinkDirectThreatsToTargetsStep(this));
		addStep(new DiagramWizardIdentifyIndirectThreatStep(this));
		addStep(new DiagramWizardConstructChainsStep(this));
		addStep(new DiagramWizardReviewModelAndAdjustStep(this));
		addStep(new SelectChainStep(this));
		addStep(new DevelopDraftStrategiesStep(this));
		addStep(new RankDraftStrategiesStep(this));
		addStep(new EditAllStrategiesStep(this));
		addStep(new StrategicPlanDevelopGoalStep(this));
		addStep(new StrategicPlanDevelopObjectivesStep(this));	
		addStep(new MonitoringWizardFocusStep(this));
		addStep(new MonitoringWizardDefineIndicatorsStep(this));
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

	
	public void addStep(WizardStep step)
	{
		stepTable.findStep(step.getClass().getSimpleName()).setStepClass(step);
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
			mainWindow.getCurrentView().jump(newStep);
		}
		else
		{
			SkeletonWizardStep stepClass = stepTable.findStep(newStep).getStepClass();
			if (stepClass!=null)
			{
				currentStepName = newStep;
				stepClass.refresh();
				setContents(stepClass);
			}
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
	
	public StepTable()
	{
		loadDigramViewSteps();
		loadThreatMatrixViewSteps();
		
		setUpSequences();
	}
	
	public void loadDigramViewSteps()
	{
		final String viewName = DiagramView.getViewName();
		
		WizardStepEntry stepEntry = loadStep(DiagramOverviewStep.class, viewName);
		stepEntry.loadNextBack(DiagramWizardProjectScopeStep.class, DiagramWizardVisionStep.class);
		
		stepEntry = loadStep(DiagramWizardProjectScopeStep.class, viewName);
		stepEntry.loadNextBack(DiagramWizardVisionStep.class, DiagramOverviewStep.class);
		
		stepEntry = loadStep(DiagramWizardVisionStep.class, viewName);
		stepEntry.loadNextBack(DiagramWizardDefineTargetsStep.class, DiagramWizardProjectScopeStep.class);
		
		stepEntry = loadStep(DiagramWizardDefineTargetsStep.class, viewName);
		stepEntry.loadNextBack(DiagramWizardReviewAndModifyTargetsStep.class, DiagramWizardVisionStep.class);
		
		stepEntry = loadStep(DiagramWizardReviewAndModifyTargetsStep.class, viewName);
		stepEntry.loadNextBack(DescribeTargetStatusStep.class, DiagramWizardDefineTargetsStep.class);
		
		stepEntry = loadStep(DescribeTargetStatusStep.class, viewName);
		stepEntry.loadNextBack(DiagramWizardIdentifyDirectThreatStep.class, DiagramWizardReviewAndModifyTargetsStep.class);
		
		stepEntry = loadStep(DiagramWizardIdentifyDirectThreatStep.class, viewName);
		stepEntry.loadNextBack(DiagramWizardLinkDirectThreatsToTargetsStep.class, DescribeTargetStatusStep.class);
		
		stepEntry = loadStep(DiagramWizardLinkDirectThreatsToTargetsStep.class, viewName);
		stepEntry.loadNextBack(ThreatMatrixOverviewStep.class, DiagramWizardIdentifyDirectThreatStep.class);
		
		stepEntry = loadStep(DiagramWizardIdentifyIndirectThreatStep.class, viewName);		
		stepEntry.loadNextBack(DiagramWizardConstructChainsStep.class, ThreatRatingWizardCheckTotalsStep.class);
		
		stepEntry = loadStep(DiagramWizardConstructChainsStep.class, viewName);	
		stepEntry.loadNextBack(DiagramWizardReviewModelAndAdjustStep.class, DiagramWizardIdentifyIndirectThreatStep.class);
		
		stepEntry = loadStep(DiagramWizardReviewModelAndAdjustStep.class, viewName);		
		stepEntry.loadNextBack(ActionJumpStrategicPlanHowToConstructStep.class, DiagramWizardConstructChainsStep.class);
		
		stepEntry = loadStep(SelectChainStep.class, viewName);		
		stepEntry.loadNextBack(DevelopDraftStrategiesStep.class, StrategicPlanViewAllObjectives.class);
		
		stepEntry = loadStep(DevelopDraftStrategiesStep.class, viewName);
		stepEntry.loadNextBack(RankDraftStrategiesStep.class, SelectChainStep.class);
		
		stepEntry = loadStep(RankDraftStrategiesStep.class, viewName);
		stepEntry.loadNextBack(EditAllStrategiesStep.class, DevelopDraftStrategiesStep.class);
		
		stepEntry = loadStep(EditAllStrategiesStep.class, viewName);
		stepEntry.loadNextBack(ActionJumpMonitoringOverviewStep.class, RankDraftStrategiesStep.class);
		
		stepEntry = loadStep(StrategicPlanDevelopGoalStep.class, viewName);
		stepEntry.loadNextBack(StrategicPlanViewAllGoals.class, StrategicPlanHowToConstructStep.class);
		
		stepEntry = loadStep(StrategicPlanDevelopObjectivesStep.class, viewName);		
		stepEntry.loadNextBack(StrategicPlanViewAllObjectives.class, StrategicPlanViewAllGoals.class);
		
		stepEntry = loadStep(MonitoringWizardFocusStep.class, viewName);		
		stepEntry.loadNextBack(MonitoringWizardDefineIndicatorsStep.class, MonitoringOverviewStep.class);
		
		stepEntry = loadStep(MonitoringWizardDefineIndicatorsStep.class, viewName);
		stepEntry.loadNextBack(MonitoringWizardEditIndicatorsStep.class, MonitoringWizardDefineIndicatorsStep.class);
	}


	public void loadThreatMatrixViewSteps()
	{
		final String viewName = ThreatMatrixView.getViewName();
		
		WizardStepEntry stepEntry = loadStep(ThreatMatrixOverviewStep.class, viewName);
		stepEntry.loadNextBack(null, DiagramWizardLinkDirectThreatsToTargetsStep.class);
		stepEntry.loadControl("View:Diagram", DiagramOverviewStep.class);

		stepEntry = loadStep(ThreatRatingWizardChooseBundle.class, viewName);
		stepEntry.loadControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		stepEntry = loadStep(ThreatRatingWizardScopeStep.class, viewName);
		stepEntry = loadStep(ThreatRatingWizardSeverityStep.class, viewName);
		stepEntry = loadStep(ThreatRatingWizardIrreversibilityStep.class, viewName);
		
		stepEntry = loadStep(ThreatRatingWizardCheckBundleStep.class, viewName);
		stepEntry.loadNextBack(ThreatRatingWizardChooseBundle.class, null);

		stepEntry = loadStep(ThreatRatingWizardCheckTotalsStep.class, viewName);
		stepEntry.loadNextBack(ThreatMatrixOverviewStep.class, ThreatRatingWizardChooseBundle.class);
	}

	
	//TODO: Later when all issues have been resolved these can be loaded into an array and linked up.
	public void setUpSequences()
	{
		Class[] entries = 
		{
				ThreatMatrixOverviewStep.class,
				ThreatRatingWizardChooseBundle.class,
				ThreatRatingWizardScopeStep.class,
				ThreatRatingWizardSeverityStep.class,
				ThreatRatingWizardIrreversibilityStep.class,
				ThreatRatingWizardCheckBundleStep.class,
		};
		
		sequenceSteps(entries);
	}
	

	public void sequenceSteps(Class[] entries)
	{
		for (int i=0; i<entries.length-2; ++i)
		{
			findStep(entries[i]).loadControl("Next", entries[i+1]);
			findStep(entries[i+1]).loadControl("Back", entries[i]);
		}
	}
	
	
	private WizardStepEntry loadStep(Class wizardStep, String viewName)
	{
		WizardStepEntry stepEntry = new WizardStepEntry(wizardStep.getSimpleName(), viewName);
		put(stepEntry.getStepName(),stepEntry);
		return stepEntry;
	}
	

	WizardStepEntry findStep(Class stepClass)
	{
		return findStep(stepClass.getSimpleName());
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
	WizardStepEntry(String stepNameToUse, String viewNameToUse)
	{
		viewName = viewNameToUse;
		stepName = stepNameToUse;
		entries = new Hashtable();
	}
	
	
	void loadControl(String controlName , Class controlStep)
	{
		entries.put(controlName, new WizardControl(controlName, controlStep.getSimpleName()));
	}
	
	void loadNextBack(Class controlStepNameNext , Class controlStepBack)
	{
		if (controlStepNameNext!=null)
			entries.put("Next", new WizardControl("Next", controlStepNameNext.getSimpleName()));
		if (controlStepBack!=null)
			entries.put("Back", new WizardControl("Back", controlStepBack.getSimpleName()));
	}

	
	WizardControl findControl(String controlName)
	{
		WizardControl control = (WizardControl)entries.get(controlName);
		if (control==null)
			EAM.logError("CONTROL ("+ controlName +") NOT FOUND IN STEP=:" + stepName);
		return control;
	}

	WizardStep getStepClass()
	{
		return step;
	}
	
	void setStepClass(WizardStep stepToUse)
	{
		step = stepToUse;
	}
	
	String getViewName()
	{
		return viewName;
	}
	
	String getStepName()
	{
		return stepName;
	}
	
	private String stepName;
	private String viewName;
	private Hashtable entries;
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
