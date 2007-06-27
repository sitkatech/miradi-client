/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import java.util.Hashtable;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
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
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardCompleteResultsChainLinks;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardConstructChainsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardConstructInitialResultsChain;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardGoodResultsChainCriteriaReview;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardLinkDirectThreatsToTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardProjectScopeStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardResultsChainStep;
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
import org.conservationmeasures.eam.views.targetviability.TargetViabilityView;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability3Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability4Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability5Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability6Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability7Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability8Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViabilityMethodChoiceStep;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViabilityOverviewAfterDetailedModeStep;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViabilityOverviewStep;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatMatrixOverviewStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckBundleStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckTotalsStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardChooseBundle;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardIrreversibilityStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardScopeStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardSeverityStep;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanCreateResourcesStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanDevelopActivitiesAndTasksStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanDevelopMethodsAndTasksStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanOverviewStep;


public class WizardManager
{
	
	public WizardManager(MainWindow mainWindowToUse)
	{
		stepEntries = new Hashtable();
		mainWindow = mainWindowToUse;
	}
	
	public void setUpSteps(UmbrellaView view, WizardPanel panel) throws Exception
	{
		String currentView = view.cardName();
		
		if (currentView.equals(ThreatMatrixView.getViewName()))
			createThreatMatrixViewStepEntries(panel);
			
		if (currentView.equals(DiagramView.getViewName()))
			createDigramViewStepEntries(panel);
		
		if (currentView.equals(MonitoringView.getViewName()))
			createMonitoringViewStepEntries(panel);
		
		if (currentView.equals(WorkPlanView.getViewName()))
			createWorkPlanStepEntries(panel);
		
		if (currentView.equals(SummaryView.getViewName()))
			createSummaryStepEntries(panel);
			
		if (currentView.equals(ScheduleView.getViewName()))
			createScheduleStepEntries(panel);
		
		if (currentView.equals(StrategicPlanView.getViewName()))
			createStrategicPlanStepEntries(panel);
		
		if (currentView.equals(BudgetView.getViewName()))
			createBudgetStepEntries(panel);
		
		if (currentView.equals(NoProjectView.getViewName()))
			createNoProjectStepEntries(panel);
		
		if (currentView.equals(TargetViabilityView.getViewName()))
			createTargetViabilityStepEntries(panel);
	}
	
	
	public String setStep(Class step, String currentStepName) throws Exception
	{
		String name = stripJumpPrefix(step);
		return setStep(name, currentStepName);
	}
	
	//TODO: To support view that are not using wizard fraimwork: need to convert them; this method should be removed after all coverted
	public String setStep(Class step) throws Exception
	{
		String name = stripJumpPrefix(step);
		SkeletonWizardStep newStepClass = findStep(name);
		String viewNameNew = newStepClass.getWizard().getViewName();
		doJump(newStepClass, newStepClass, viewNameNew);
		return "";
	}
	
	public String setStep(String newStep, String currentStepName) throws Exception
	{	
		SkeletonWizardStep currentStepClass = findStep(currentStepName);
		String viewNameCur = currentStepClass.getWizard().getViewName();
		
		SkeletonWizardStep newStepClass = findStep(newStep);
		
		if (newStepClass==null) 
			return currentStepName;
		
		String viewNameNew = newStepClass.getWizard().getViewName();

		if (!viewNameNew.equals(viewNameCur))
		{
			doJump(currentStepClass, newStepClass, viewNameNew);
			return currentStepName;
		}

		newStepClass.refresh();

		//TODO: this belongs in mainWindow
		if (mainWindow.getWizard()!=null)
		{
				mainWindow.getWizard().setContents(newStepClass);
				mainWindow.getWizard().refresh();
				mainWindow.validate();
				mainWindow.restorePreviousDividerLocation();
		}
		return newStep;
	}

	//TODO: view switch should not happen here (Richard, with Kevin)
	private void doJump(SkeletonWizardStep currentStepClass, SkeletonWizardStep newStepClass, String viewNameNew) throws CommandFailedException, Exception
	{
		mainWindow.getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			mainWindow.getProject().executeCommand(new CommandSwitchView(viewNameNew));
			newStepClass.getWizard().jump(newStepClass.getClass());
		}
		finally
		{
			mainWindow.getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	public void createNoProjectStepEntries(WizardPanel panel) throws Exception
	{	
		createStepEntry(new NoProjectOverviewStep(panel))
			.createControl("Import",NoProjectWizardImportStep.class)
			.createControl("NewProject",NoProjectWizardProjectCreateStep.class);
		
		createStepEntry(new NoProjectWizardImportStep(panel))
			.createBackControl(NoProjectOverviewStep.class);
		
		createStepEntry(new NoProjectWizardProjectCreateStep(panel))
			.createBackControl(NoProjectOverviewStep.class);
	}
	
	public void createBudgetStepEntries(WizardPanel panel)
	{
		createStepEntry(new FinancialOverviewStep(panel));
		createStepEntry(new BudgetWizardAccountingAndFunding(panel));
		createStepEntry(new BudgetWizardBudgetDetail(panel));
		createStepEntry(new BudgetWizardDemo(panel));
	}
	
	public void createStrategicPlanStepEntries(WizardPanel panel)
	{
		createStepEntry(new StrategicPlanOverviewStep(panel));
		createStepEntry(new StrategicPlanViewAllGoals(panel));
		createStepEntry(new StrategicPlanViewAllObjectives(panel));
	}
	
	public void createScheduleStepEntries(WizardPanel panel)
	{
		createStepEntry(new ScheduleOverviewStep(panel));
	}
	
	public void createTargetViabilityStepEntries(WizardPanel panel)
	{
		createStepEntry(new TargetViabilityOverviewStep(panel)).
				createControl(CONTROL_NEXT, TargetViabilityMethodChoiceStep.class);
		createStepEntry(new TargetViabilityOverviewAfterDetailedModeStep(panel)).
				createControl(CONTROL_NEXT, TargetViabilityMethodChoiceStep.class);
	}
	
	public void createSummaryStepEntries(WizardPanel panel)
	{
		createStepEntry(new SummaryOverviewStep(panel));
		createStepEntry(new SummaryWizardDefineTeamMembers(panel));
		createStepEntry(new SummaryWizardDefineProjectLeader(panel));;
		createStepEntry(new SummaryWizardDefineProjecScope(panel));
		createStepEntry(new SummaryWizardDefineProjectVision(panel));
	}
	
	public void createWorkPlanStepEntries(WizardPanel panel)
	{
		createStepEntry(new WorkPlanOverviewStep(panel));
		createStepEntry(new MonitoringWizardSelectMethodsStep(panel));;
		createStepEntry(new WorkPlanDevelopActivitiesAndTasksStep(panel));
		createStepEntry(new WorkPlanDevelopMethodsAndTasksStep(panel));
		createStepEntry(new WorkPlanCreateResourcesStep(panel));
		createStepEntry(new WorkPlanAssignResourcesStep(panel));
	}
	
	public void createMonitoringViewStepEntries(WizardPanel panel)
	{		
		createStepEntry(new MonitoringPlanOverviewStep(panel));
		createStepEntry(new MonitoringWizardEditIndicatorsStep(panel));
	}
	
	public void createDigramViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new DiagramOverviewStep(panel));
		createStepEntry(new DiagramWizardProjectScopeStep(panel));
		createStepEntry(new DiagramWizardVisionStep(panel));
		createStepEntry(new DiagramWizardDefineTargetsStep(panel));
		createStepEntry(new DiagramWizardReviewAndModifyTargetsStep(panel));
		//TODO rename TargetViability Classes to identif which step is which
		createStepEntry(new TargetViabilityMethodChoiceStep(panel)).
					createControl("DoneViabilityAnalysis", DiagramWizardIdentifyDirectThreatStep.class).
					createControl("DetailedViability", TargetViability3Step.class);
		createStepEntry(new DescribeTargetStatusStep(panel)).createControl(CONTROL_NEXT, TargetViabilityOverviewStep.class);
		createStepEntry(new TargetViability3Step(panel)).createControl(CONTROL_BACK, TargetViabilityMethodChoiceStep.class);
		createStepEntry(new TargetViability4Step(panel));
		createStepEntry(new TargetViability5Step(panel));
		createStepEntry(new TargetViability6Step(panel));
		createStepEntry(new TargetViability7Step(panel));
		createStepEntry(new TargetViability8Step(panel));
		
		createStepEntry(new DiagramWizardIdentifyDirectThreatStep(panel)).createControl(CONTROL_BACK, TargetViabilityMethodChoiceStep.class);
		createStepEntry(new DiagramWizardLinkDirectThreatsToTargetsStep(panel));
		createStepEntry(new DiagramWizardIdentifyIndirectThreatStep(panel));		
		createStepEntry(new DiagramWizardConstructChainsStep(panel));	
		createStepEntry(new DiagramWizardReviewModelAndAdjustStep(panel));		
		createStepEntry(new SelectChainStep(panel));		
		createStepEntry(new DevelopDraftStrategiesStep(panel));
		createStepEntry(new RankDraftStrategiesStep(panel));
		createStepEntry(new EditAllStrategiesStep(panel));
		createStepEntry(new StrategicPlanDevelopGoalStep(panel));
		createStepEntry(new StrategicPlanDevelopObjectivesStep(panel));		
		createStepEntry(new MonitoringWizardFocusStep(panel));		
		createStepEntry(new MonitoringWizardDefineIndicatorsStep(panel));
	
		createStepEntry(new DiagramWizardResultsChainStep(panel));
		createStepEntry(new DiagramWizardConstructInitialResultsChain(panel));
		createStepEntry(new DiagramWizardCompleteResultsChainLinks(panel));
		createStepEntry(new DiagramWizardGoodResultsChainCriteriaReview(panel));
	}


	public void createThreatMatrixViewStepEntries(WizardPanel panel) throws Exception
	{
		//TODO: View:Diagram...should be Step:StepName or support both
		
		createStepEntry(new ThreatMatrixOverviewStep(panel))
			.createControl("View:Diagram", DiagramOverviewStep.class);

		createStepEntry(new ThreatRatingWizardChooseBundle(panel))
			.createControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		createStepEntry(new ThreatRatingWizardScopeStep(panel));
		createStepEntry(new ThreatRatingWizardSeverityStep(panel));
		createStepEntry(new ThreatRatingWizardIrreversibilityStep(panel));

		createStepEntry(new ThreatRatingWizardCheckBundleStep(panel))
			.createNextControl(ThreatRatingWizardChooseBundle.class); 

		createStepEntry(new ThreatRatingWizardCheckTotalsStep(panel))
			.createBackControl(ThreatRatingWizardChooseBundle.class); 
	}


	static Class[] getSequence()
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

				TargetViabilityMethodChoiceStep.class,
				DescribeTargetStatusStep.class,
				TargetViabilityOverviewStep.class,
				
				TargetViability3Step.class,
				TargetViability4Step.class,
				TargetViability5Step.class,
				TargetViability6Step.class,
				TargetViability7Step.class,
				TargetViability8Step.class,
				TargetViabilityOverviewAfterDetailedModeStep.class,			

				DiagramWizardIdentifyDirectThreatStep.class,
				DiagramWizardLinkDirectThreatsToTargetsStep.class,
				
				ThreatMatrixOverviewStep.class,
				ThreatRatingWizardChooseBundle.class,
				ThreatRatingWizardScopeStep.class,
				ThreatRatingWizardSeverityStep.class,
				ThreatRatingWizardIrreversibilityStep.class,
				ThreatRatingWizardCheckBundleStep.class,
				ThreatRatingWizardCheckTotalsStep.class,
				
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
				
				DiagramWizardResultsChainStep.class,
				DiagramWizardConstructInitialResultsChain.class,
				DiagramWizardCompleteResultsChainLinks.class,
				DiagramWizardGoodResultsChainCriteriaReview.class,

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
		
		return entries;
		
	}
	
	private SkeletonWizardStep createStepEntry(SkeletonWizardStep step)
	{
		stepEntries.put(step.getClass().getSimpleName(),step);
		return step;
	}

	
	public String stripJumpPrefix(Class stepMarker)
	{
		String name = stepMarker.getSimpleName();
		final String prefix = "ActionJump";
		if (name.startsWith(prefix))
			name = name.substring(prefix.length());
		return name;
	}
	
	
	public boolean isValidStep(Class stepMarker)
	{
		String name = stripJumpPrefix(stepMarker);
		return (stepEntries.get(name)!=null);
	}
	
	public SkeletonWizardStep findStep(String stepName)
	{
		SkeletonWizardStep step =(SkeletonWizardStep)stepEntries.get(stepName);
		if (step==null)
			EAM.logVerbose("ENTRY NOT FOUND FOR STEP NAME=:" + stepName);
		return step;
	}
	
	public Class findControlTargetStep(String controlName, SkeletonWizardStep step)
	{
		Class targetStep = step.getControl(controlName);
		
		if (targetStep==null)
			return doDeferedSequenceLookup(controlName,step);

		return targetStep;
	}
	
	Class doDeferedSequenceLookup(String controlName, SkeletonWizardStep step)
	{
		Class name = getDestinationStep(controlName, step);
		return name;
	}

	public Class getDestinationStep(String controlName, SkeletonWizardStep step)
	{
		Class[] sequences = WizardManager.getSequence();

		int position = findPositionInSequence(sequences, step);
		if (position<0)
			return null;
		
		if (controlName.equals(CONTROL_NEXT))
			++position;
		
		if (controlName.equals(CONTROL_BACK))
			--position;
		
		if (position<0 || position>=sequences.length)
			return null;
		
		return sequences[position];
	}


	private int findPositionInSequence(Class[] sequences, SkeletonWizardStep step)
	{
		for (int i=0; i<sequences.length; ++i)
			if (sequences[i].getSimpleName().equals(getStepName(step))) 
				return i;
		return -1;
	}
	
	public static String getStepName(SkeletonWizardStep step)
	{
		return step.getClass().getSimpleName();
	}
	
	public static String CONTROL_NEXT = "Next";
	public static String CONTROL_BACK = "Back";
	MainWindow mainWindow;
	Hashtable stepEntries;
}

