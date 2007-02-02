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
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectOverviewStep;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardImportStep;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardPanel;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardProjectCreateStep;
import org.conservationmeasures.eam.views.schedule.ScheduleView;
import org.conservationmeasures.eam.views.schedule.wizard.ScheduleOverviewStep;
import org.conservationmeasures.eam.views.strategicplan.StrategicPlanView;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanOverviewStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanViewAllGoals;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanViewAllObjectives;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.conservationmeasures.eam.views.summary.wizard.SummaryOverviewStep;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjecScope;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjectLeader;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjectVision;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineTeamMembers;
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
			String currentView = view.cardName();
			
			if (currentView.equals(ThreatMatrixView.getViewName()))
				addThreatMatrixViewSteps();
				
			if (currentView.equals(DiagramView.getViewName()))
				addDiagramViewSteps();
			
			if (currentView.equals(MonitoringView.getViewName()))
				addMonitoringViewSteps();
			
			if (currentView.equals(WorkPlanView.getViewName()))
				addWorkPlanViewSteps();
			
			if (currentView.equals(SummaryView.getViewName()))
				addSummaryViewSteps();
				
			if (currentView.equals(ScheduleView.getViewName()))
				addScheduleViewSteps();
			
			if (currentView.equals(StrategicPlanView.getViewName()))
				addStrategicPlanViewSteps();
			
			if (currentView.equals(BudgetView.getViewName()))
				addBudgetViewSteps();
			
			if (currentView.equals(NoProjectView.getViewName()))
				addNoProjectViewSteps();
			
			setStep("");
		}
		catch (Exception e)
		{
			EAM.logError("Wizard load failed");
			EAM.logException(e);
		}
	}
	
	private void addNoProjectViewSteps() throws Exception
	{
		addStep(new NoProjectOverviewStep((NoProjectWizardPanel)this));
		addStep(new NoProjectWizardImportStep((NoProjectWizardPanel)this));
		addStep(new NoProjectWizardProjectCreateStep((NoProjectWizardPanel)this));
	}
	
	private void addBudgetViewSteps()
	{
		addStep(new FinancialOverviewStep(this));
		addStep(new BudgetWizardAccountingAndFunding(this));
		addStep(new BudgetWizardBudgetDetail(this));
		addStep(new BudgetWizardDemo(this));
	}
	
	private void addStrategicPlanViewSteps()
	{
		addStep(new StrategicPlanOverviewStep(this));
		addStep(new StrategicPlanViewAllGoals(this));
		addStep(new StrategicPlanViewAllObjectives(this));
	}
	
	private void addScheduleViewSteps()
	{
		addStep(new ScheduleOverviewStep(this));
	}

	private void addSummaryViewSteps()
	{
		addStep(new SummaryOverviewStep(this));
		addStep(new SummaryWizardDefineTeamMembers(this));
		addStep(new SummaryWizardDefineProjectLeader(this));
		addStep(new SummaryWizardDefineProjecScope(this));
		addStep(new SummaryWizardDefineProjectVision(this));
	}
	
	private void addDiagramViewSteps() throws Exception
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
	
	private void addThreatMatrixViewSteps() throws Exception
	{
		addStep(new ThreatMatrixOverviewStep((ThreatRatingWizardPanel)this));
		addStep(new ThreatRatingWizardChooseBundle((ThreatRatingWizardPanel)this));
		addStep(new ThreatRatingWizardScopeStep((ThreatRatingWizardPanel)this));
		addStep(new ThreatRatingWizardSeverityStep((ThreatRatingWizardPanel)this));
		addStep(new ThreatRatingWizardIrreversibilityStep((ThreatRatingWizardPanel)this));
		addStep(new ThreatRatingWizardCheckBundleStep((ThreatRatingWizardPanel)this));
		addStep(new ThreatRatingWizardCheckTotalsStep((ThreatRatingWizardPanel)this));
	}

	public void addMonitoringViewSteps()
	{
		addStep(new MonitoringPlanOverviewStep(this));
		addStep(new MonitoringWizardEditIndicatorsStep(this));
	}
	
	public void addWorkPlanViewSteps()
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
		jump(control.getStepName());
	}


	public void setStep(Class newStep) throws Exception
	{
		setStep(newStep.getSimpleName());
	}
	
	
	public void setStep(String newStep) throws Exception
	{
		if (newStep.equals(""))
		{
			newStep = removeSpaces(view.cardName()) + "OverviewStep";
			currentStepName = newStep;
		}
		
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
	
	
	protected UmbrellaView view;
	protected MainWindow mainWindow;
	public String currentStepName;
	//TODO: this should not be a static but is really a wizard manager and should be pulled from above.
	private static StepTable stepTable = new StepTable();

}

class StepTable extends Hashtable
{
	
	public StepTable()
	{
		createDigramViewStepEntries();
		createThreatMatrixViewStepEntries();
		createMonitoringViewStepEntries();
		createWorkPlanStepEntries();
		createSummaryStepEntries();
		createScheduleStepEntries();
		createStrategicPlanStepEntries();
		createBudgetStepEntries();
		createNoProjectStepEntries();
		
		setUpSequences1();
	}
	
	private void createNoProjectStepEntries()
	{
		final String viewName = NoProjectView.getViewName();
		
		createStepEntry(NoProjectOverviewStep.class, viewName);
		
		WizardStepEntry stepEntry1 = createStepEntry(NoProjectWizardImportStep.class, viewName);
		stepEntry1.createBackControl(NoProjectOverviewStep.class);
		
		WizardStepEntry stepEntry2 = createStepEntry(NoProjectWizardProjectCreateStep.class, viewName);
		stepEntry2.createBackControl(NoProjectOverviewStep.class);
	}
	
	private void createBudgetStepEntries()
	{
		final String viewName = BudgetView.getViewName();
		
		createStepEntry(FinancialOverviewStep.class, viewName);
		createStepEntry(BudgetWizardAccountingAndFunding.class, viewName);
		createStepEntry(BudgetWizardBudgetDetail.class, viewName);
		createStepEntry(BudgetWizardDemo.class, viewName);
	}
	
	private void createStrategicPlanStepEntries()
	{
		final String viewName = StrategicPlanView.getViewName();
		
		createStepEntry(StrategicPlanOverviewStep.class, viewName);
		createStepEntry(StrategicPlanViewAllGoals.class, viewName);
		createStepEntry(StrategicPlanViewAllObjectives.class, viewName);
	}
	
	private void createScheduleStepEntries()
	{
		final String viewName = ScheduleView.getViewName();
		
		createStepEntry(ScheduleOverviewStep.class, viewName);
	}
	
	public void createSummaryStepEntries()
	{
		final String viewName = SummaryView.getViewName();
		
		createStepEntry(SummaryOverviewStep.class, viewName);
		createStepEntry(SummaryWizardDefineTeamMembers.class, viewName);
		createStepEntry(SummaryWizardDefineProjectLeader.class, viewName);;
		createStepEntry(SummaryWizardDefineProjecScope.class, viewName);
		createStepEntry(SummaryWizardDefineProjectVision.class, viewName);
	}
	
	public void createWorkPlanStepEntries()
	{
		final String viewName = WorkPlanView.getViewName();
		
		createStepEntry(WorkPlanOverviewStep.class, viewName);
		createStepEntry(MonitoringWizardSelectMethodsStep.class, viewName);;
		createStepEntry(WorkPlanDevelopActivitiesAndTasksStep.class, viewName);
		createStepEntry(WorkPlanDevelopMethodsAndTasksStep.class, viewName);
		createStepEntry(WorkPlanCreateResourcesStep.class, viewName);
		createStepEntry(WorkPlanAssignResourcesStep.class, viewName);
	}
	
	public void createMonitoringViewStepEntries()
	{		
		final String viewName = MonitoringView.getViewName();
	
		createStepEntry(MonitoringPlanOverviewStep.class, viewName);
		createStepEntry(MonitoringWizardEditIndicatorsStep.class, viewName);
	}
	
	public void createDigramViewStepEntries()
	{
		final String viewName = DiagramView.getViewName();
		
		createStepEntry(DiagramOverviewStep.class, viewName);
		createStepEntry(DiagramWizardProjectScopeStep.class, viewName);
		createStepEntry(DiagramWizardVisionStep.class, viewName);
		createStepEntry(DiagramWizardDefineTargetsStep.class, viewName);
		createStepEntry(DiagramWizardReviewAndModifyTargetsStep.class, viewName);
		createStepEntry(DescribeTargetStatusStep.class, viewName);
		createStepEntry(DiagramWizardIdentifyDirectThreatStep.class, viewName);
		createStepEntry(DiagramWizardLinkDirectThreatsToTargetsStep.class, viewName);
		createStepEntry(DiagramWizardIdentifyIndirectThreatStep.class, viewName);		
		createStepEntry(DiagramWizardConstructChainsStep.class, viewName);	
		createStepEntry(DiagramWizardReviewModelAndAdjustStep.class, viewName);		
		createStepEntry(SelectChainStep.class, viewName);		
		createStepEntry(DevelopDraftStrategiesStep.class, viewName);
		createStepEntry(RankDraftStrategiesStep.class, viewName);
		createStepEntry(EditAllStrategiesStep.class, viewName);
		createStepEntry(StrategicPlanDevelopGoalStep.class, viewName);
		createStepEntry(StrategicPlanDevelopObjectivesStep.class, viewName);		
		createStepEntry(MonitoringWizardFocusStep.class, viewName);		
		createStepEntry(MonitoringWizardDefineIndicatorsStep.class, viewName);
	}


	public void createThreatMatrixViewStepEntries()
	{
		final String viewName = ThreatMatrixView.getViewName();
		
		WizardStepEntry stepEntry1 = createStepEntry(ThreatMatrixOverviewStep.class, viewName);
		//TODO: View:Diagram...should be Step:StepName or support both
		stepEntry1.createControl("View:Diagram", DiagramOverviewStep.class);

		WizardStepEntry stepEntry2 = createStepEntry(ThreatRatingWizardChooseBundle.class, viewName);
		stepEntry2.createControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		createStepEntry(ThreatRatingWizardScopeStep.class, viewName);
		createStepEntry(ThreatRatingWizardSeverityStep.class, viewName);
		createStepEntry(ThreatRatingWizardIrreversibilityStep.class, viewName);

		WizardStepEntry stepEntry3 = createStepEntry(ThreatRatingWizardCheckBundleStep.class, viewName);
		stepEntry3.createNextControl(ThreatRatingWizardChooseBundle.class); 

		WizardStepEntry stepEntry4 = createStepEntry(ThreatRatingWizardCheckTotalsStep.class, viewName);
		stepEntry4.createNextControl(ThreatMatrixOverviewStep.class); 
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
			findStep(entries[i]).createNextControl(entries[i+1]);
			findStep(entries[i+1]).createBackControl(entries[i]);
		}
	}
	
	
	private WizardStepEntry createStepEntry(Class wizardStep, String viewName)
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
	
	
	void createControl(String controlName , Class controlStep)
	{
		entries.put(controlName, new WizardControl(controlName, controlStep.getSimpleName()));
	}
	
	void createNextControl(Class controlStep)
	{
		createControl("Next", controlStep);
	}
	
	void createBackControl(Class controlStep)
	{
		createControl("Back", controlStep);
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
