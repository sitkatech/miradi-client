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
import org.conservationmeasures.eam.views.budget.BudgetView;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardAccountingAndFunding;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardBudgetDetail;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardDemo;
import org.conservationmeasures.eam.views.budget.wizard.FinancialOverviewStep;
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
import org.conservationmeasures.eam.views.monitoring.MonitoringView;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringPlanOverviewStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardEditIndicatorsStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardFocusStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardSelectMethodsStep;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardImportStep;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardPanel;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardProjectCreateStep;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectOverviewStep;
import org.conservationmeasures.eam.views.schedule.ScheduleView;
import org.conservationmeasures.eam.views.schedule.wizard.ScheduleOverviewStep;
import org.conservationmeasures.eam.views.strategicplan.StrategicPlanView;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanOverviewStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanViewAllGoals;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanViewAllObjectives;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjecScope;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjectLeader;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjectVision;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineTeamMembers;
import org.conservationmeasures.eam.views.summary.wizard.SummaryOverviewStep;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatMatrixOverviewStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckBundleStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckTotalsStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardChooseBundle;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardIrreversibilityStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardPanel;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardScopeStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardSeverityStep;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanCreateResourcesStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanDevelopActivitiesAndTasksStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanDevelopMethodsAndTasksStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanOverviewStep;

public class WizardPanel extends JPanel implements IWizardPanel
{
	public WizardPanel(MainWindow mainWindowToUse)
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
			String currentView = mainWindow.getCurrentView().cardName();
			
			if (currentView.equals(ThreatMatrixView.getViewName()))
				setupStepsThreatMatrixView();
				
			if (currentView.equals(DiagramView.getViewName()))
				setupStepsDiagramView();
			
			if (currentView.equals(MonitoringView.getViewName()))
				setupStepsMonitoringView();
			
			if (currentView.equals(WorkPlanView.getViewName()))
				setupWorkPlanView();
			
			if (currentView.equals(SummaryView.getViewName()))
				setupStepsSummaryView();
				
			if (currentView.equals(ScheduleView.getViewName()))
				setupScheduleView();
			
			if (currentView.equals(StrategicPlanView.getViewName()))
				setupStrategicPlanView();
			
			if (currentView.equals(BudgetView.getViewName()))
				setupBudgetView();
			
			if (currentView.equals(NoProjectView.getViewName()))
				setupNoProjectView();
			
			setStep("");
		}
		catch (Exception e)
		{
			EAM.logError("Wizard load failed");
			EAM.logException(e);
		}
	}
	
	private void setupNoProjectView() throws Exception
	{
		addStep(new NoProjectOverviewStep((NoProjectWizardPanel)this));
		addStep(new NoProjectWizardImportStep((NoProjectWizardPanel)this));
		addStep(new NoProjectWizardProjectCreateStep((NoProjectWizardPanel)this));
	}
	
	private void setupBudgetView()
	{
		addStep(new FinancialOverviewStep(this));
		addStep(new BudgetWizardAccountingAndFunding(this));
		addStep(new BudgetWizardBudgetDetail(this));
		addStep(new BudgetWizardDemo(this));
	}
	
	private void setupStrategicPlanView()
	{
		addStep(new StrategicPlanOverviewStep(this));
		addStep(new StrategicPlanViewAllGoals(this));
		addStep(new StrategicPlanViewAllObjectives(this));
	}
	
	private void setupScheduleView()
	{
		addStep(new ScheduleOverviewStep(this));
	}

	private void setupStepsSummaryView()
	{
		addStep(new SummaryOverviewStep(this));
		addStep(new SummaryWizardDefineTeamMembers(this));
		addStep(new SummaryWizardDefineProjectLeader(this));
		addStep(new SummaryWizardDefineProjecScope(this));
		addStep(new SummaryWizardDefineProjectVision(this));
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

	public void setupStepsMonitoringView()
	{
		addStep(new MonitoringPlanOverviewStep(this));
		addStep(new MonitoringWizardEditIndicatorsStep(this));
	}
	
	public void setupWorkPlanView()
	{
		addStep(new WorkPlanOverviewStep(this));
		addStep(new MonitoringWizardSelectMethodsStep(this));
		addStep(new WorkPlanDevelopActivitiesAndTasksStep(this));
		addStep(new WorkPlanDevelopMethodsAndTasksStep(this));
		addStep(new WorkPlanCreateResourcesStep(this));
		addStep(new WorkPlanAssignResourcesStep(this));
	}
	
	public void addStep(SkeletonWizardStep step)
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
	
	public void setStep(Class newStep) throws Exception
	{
		setStep(newStep.getSimpleName());
	}
	
	public void setStep(String newStep) throws Exception
	{
		if (newStep.equals(""))
			newStep = currentStepName = removeSpaces(mainWindow.getCurrentView().cardName()) + "OverviewStep";
		
		WizardStepEntry entryCur = stepTable.findStep(currentStepName);
		String viewNameCur = entryCur.getViewName();
		
		WizardStepEntry entryNew = stepTable.findStep(newStep);
		String viewNameNew = entryNew.getViewName();
	
		
		//FIXME: view switch should not happen here
		if (!viewNameNew.equals(viewNameCur))
		{
			mainWindow.getProject().executeCommand(new CommandSwitchView(viewNameNew));
			SkeletonWizardStep stepClass = stepTable.findStep(newStep).getStepClass();
			mainWindow.getCurrentView().jump(stepClass.getClass());
			return;
		}
		SkeletonWizardStep stepClass = stepTable.findStep(newStep).getStepClass();
		if (stepClass!=null)
		{
			currentStepName = newStep;
			stepClass.refresh();
			setContents(stepClass);
		}

	}

	private String removeSpaces(String name)
	{
		return name.replaceAll(" ", "");
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
		loadMonitoringViewSteps();
		loadWorkPlanSteps();
		loadSummarySteps();
		loadScheduleSteps();
		loadStrategicPlanSteps();
		loadBudgetSteps();
		loadNoProjectSteps();
		
		setUpSequences1();
	}
	
	private void loadNoProjectSteps()
	{
		final String viewName = NoProjectView.getViewName();
		
		loadStep(NoProjectOverviewStep.class, viewName);
		
		WizardStepEntry stepEntry1 = loadStep(NoProjectWizardImportStep.class, viewName);
		stepEntry1.loadControl("Back", NoProjectOverviewStep.class);
		
		WizardStepEntry stepEntry2 = loadStep(NoProjectWizardProjectCreateStep.class, viewName);
		stepEntry2.loadControl("Back", NoProjectOverviewStep.class);
	}
	
	private void loadBudgetSteps()
	{
		final String viewName = BudgetView.getViewName();
		
		loadStep(FinancialOverviewStep.class, viewName);
		loadStep(BudgetWizardAccountingAndFunding.class, viewName);
		loadStep(BudgetWizardBudgetDetail.class, viewName);
		loadStep(BudgetWizardDemo.class, viewName);
	}
	
	private void loadStrategicPlanSteps()
	{
		final String viewName = StrategicPlanView.getViewName();
		
		loadStep(StrategicPlanOverviewStep.class, viewName);
		loadStep(StrategicPlanViewAllGoals.class, viewName);
		loadStep(StrategicPlanViewAllObjectives.class, viewName);
	}
	
	private void loadScheduleSteps()
	{
		final String viewName = ScheduleView.getViewName();
		
		loadStep(ScheduleOverviewStep.class, viewName);
	}
	
	public void loadSummarySteps()
	{
		final String viewName = SummaryView.getViewName();
		
		loadStep(SummaryOverviewStep.class, viewName);
		loadStep(SummaryWizardDefineTeamMembers.class, viewName);
		loadStep(SummaryWizardDefineProjectLeader.class, viewName);;
		loadStep(SummaryWizardDefineProjecScope.class, viewName);
		loadStep(SummaryWizardDefineProjectVision.class, viewName);
	}
	
	public void loadWorkPlanSteps()
	{
		final String viewName = WorkPlanView.getViewName();
		
		loadStep(WorkPlanOverviewStep.class, viewName);
		loadStep(MonitoringWizardSelectMethodsStep.class, viewName);;
		loadStep(WorkPlanDevelopActivitiesAndTasksStep.class, viewName);
		loadStep(WorkPlanDevelopMethodsAndTasksStep.class, viewName);
		loadStep(WorkPlanCreateResourcesStep.class, viewName);
		loadStep(WorkPlanAssignResourcesStep.class, viewName);
	}
	
	public void loadMonitoringViewSteps()
	{		
		final String viewName = MonitoringView.getViewName();
	
		loadStep(MonitoringPlanOverviewStep.class, viewName);
		loadStep(MonitoringWizardEditIndicatorsStep.class, viewName);
	}
	
	public void loadDigramViewSteps()
	{
		final String viewName = DiagramView.getViewName();
		
		loadStep(DiagramOverviewStep.class, viewName);
		loadStep(DiagramWizardProjectScopeStep.class, viewName);
		loadStep(DiagramWizardVisionStep.class, viewName);
		loadStep(DiagramWizardDefineTargetsStep.class, viewName);
		loadStep(DiagramWizardReviewAndModifyTargetsStep.class, viewName);
		loadStep(DescribeTargetStatusStep.class, viewName);
		loadStep(DiagramWizardIdentifyDirectThreatStep.class, viewName);
		loadStep(DiagramWizardLinkDirectThreatsToTargetsStep.class, viewName);
		loadStep(DiagramWizardIdentifyIndirectThreatStep.class, viewName);		
		loadStep(DiagramWizardConstructChainsStep.class, viewName);	
		loadStep(DiagramWizardReviewModelAndAdjustStep.class, viewName);		
		loadStep(SelectChainStep.class, viewName);		
		loadStep(DevelopDraftStrategiesStep.class, viewName);
		loadStep(RankDraftStrategiesStep.class, viewName);
		loadStep(EditAllStrategiesStep.class, viewName);
		loadStep(StrategicPlanDevelopGoalStep.class, viewName);
		loadStep(StrategicPlanDevelopObjectivesStep.class, viewName);		
		loadStep(MonitoringWizardFocusStep.class, viewName);		
		loadStep(MonitoringWizardDefineIndicatorsStep.class, viewName);
	}


	public void loadThreatMatrixViewSteps()
	{
		final String viewName = ThreatMatrixView.getViewName();
		
		WizardStepEntry stepEntry1 = loadStep(ThreatMatrixOverviewStep.class, viewName);
		//TODO: View:Diagram...should be Step:StepName or support both
		stepEntry1.loadControl("View:Diagram", DiagramOverviewStep.class);

		WizardStepEntry stepEntry2 = loadStep(ThreatRatingWizardChooseBundle.class, viewName);
		stepEntry2.loadControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		loadStep(ThreatRatingWizardScopeStep.class, viewName);
		loadStep(ThreatRatingWizardSeverityStep.class, viewName);
		loadStep(ThreatRatingWizardIrreversibilityStep.class, viewName);

		WizardStepEntry stepEntry3 = loadStep(ThreatRatingWizardCheckBundleStep.class, viewName);
		stepEntry3.loadNext(ThreatRatingWizardChooseBundle.class); 

		WizardStepEntry stepEntry4 = loadStep(ThreatRatingWizardCheckTotalsStep.class, viewName);
		stepEntry4.loadNext(ThreatMatrixOverviewStep.class); 
	}


	public void setUpSequences1()
	{
		Class[] entries = 
		{
				SummaryOverviewStep.class,
				SummaryWizardDefineTeamMembers.class,
				SummaryWizardDefineProjectLeader.class,
				SummaryWizardDefineProjecScope.class,
				SummaryWizardDefineProjectVision.class,
				
				DiagramOverviewStep.class,
				DiagramWizardProjectScopeStep.class,
				DiagramWizardVisionStep.class,
				DiagramWizardDefineTargetsStep.class,
				DiagramWizardReviewAndModifyTargetsStep.class,
				DescribeTargetStatusStep.class,
				DiagramWizardIdentifyDirectThreatStep.class,
				DiagramWizardLinkDirectThreatsToTargetsStep.class,
				DiagramWizardIdentifyIndirectThreatStep.class,		
				DiagramWizardConstructChainsStep.class,	
				DiagramWizardReviewModelAndAdjustStep.class,		
				
				StrategicPlanOverviewStep.class,
				StrategicPlanDevelopGoalStep.class,
				StrategicPlanViewAllGoals.class,
				StrategicPlanDevelopObjectivesStep.class, 	
				StrategicPlanViewAllObjectives.class,

				SelectChainStep.class,		
				DevelopDraftStrategiesStep.class,
				RankDraftStrategiesStep.class,
				EditAllStrategiesStep.class,
				
				ThreatMatrixOverviewStep.class,
				ThreatRatingWizardChooseBundle.class,
				ThreatRatingWizardScopeStep.class,
				ThreatRatingWizardSeverityStep.class,
				ThreatRatingWizardIrreversibilityStep.class,
				ThreatRatingWizardCheckBundleStep.class,
				
				EditAllStrategiesStep.class,
				
				MonitoringPlanOverviewStep.class,
				MonitoringWizardFocusStep.class,
				MonitoringWizardDefineIndicatorsStep.class,
				
				MonitoringWizardEditIndicatorsStep.class,
				
				WorkPlanOverviewStep.class,
				MonitoringWizardSelectMethodsStep.class,
				WorkPlanDevelopActivitiesAndTasksStep.class,
				WorkPlanDevelopMethodsAndTasksStep.class,
				WorkPlanCreateResourcesStep.class,
				WorkPlanAssignResourcesStep.class,
				
				FinancialOverviewStep.class, 
				BudgetWizardAccountingAndFunding.class,
				BudgetWizardBudgetDetail.class,
				BudgetWizardDemo.class, 
				
				ScheduleOverviewStep.class,
		};
		
		sequenceSteps(entries);
	}
	

	public void sequenceSteps(Class[] entries)
	{
		for (int i=0; i<=entries.length-2; ++i)
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
	
	void loadNext(Class controlStep)
	{
		loadControl("Next", controlStep);
	}
	
	void loadBack(Class controlStep)
	{
		loadControl("Back", controlStep);
	}

	
	WizardControl findControl(String controlName)
	{
		WizardControl control = (WizardControl)entries.get(controlName);
		if (control==null)
			EAM.logError("CONTROL ("+ controlName +") NOT FOUND IN STEP=:" + stepName);
		return control;
	}

	SkeletonWizardStep getStepClass()
	{
		if (step==null)
			EAM.logError("Class for step name ("+ stepName +") in view ("+ viewName +") NOT FOUND");
		return step;
	}
	
	void setStepClass(SkeletonWizardStep stepToUse)
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
	private SkeletonWizardStep step;
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
