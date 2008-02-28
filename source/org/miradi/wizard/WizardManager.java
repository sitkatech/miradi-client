/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard;

import java.util.Hashtable;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.views.noproject.NoProjectView;
import org.miradi.views.summary.SummaryView;
import org.miradi.wizard.diagram.DescribeTargetStatusStep;
import org.miradi.wizard.diagram.DevelopDraftStrategiesStep;
import org.miradi.wizard.diagram.DiagramOverviewStep;
import org.miradi.wizard.diagram.DiagramWizardCompleteResultsChainLinks;
import org.miradi.wizard.diagram.DiagramWizardConstructChainsStep;
import org.miradi.wizard.diagram.DiagramWizardConstructInitialResultsChain;
import org.miradi.wizard.diagram.DiagramWizardDefineTargetsStep;
import org.miradi.wizard.diagram.DiagramWizardGoodResultsChainCriteriaReview;
import org.miradi.wizard.diagram.DiagramWizardIdentifyDirectThreatStep;
import org.miradi.wizard.diagram.DiagramWizardIdentifyIndirectThreatStep;
import org.miradi.wizard.diagram.DiagramWizardLinkDirectThreatsToTargetsStep;
import org.miradi.wizard.diagram.DiagramWizardProjectScopeStep;
import org.miradi.wizard.diagram.DiagramWizardResultsChainStep;
import org.miradi.wizard.diagram.DiagramWizardReviewAndModifyTargetsStep;
import org.miradi.wizard.diagram.DiagramWizardReviewModelAndAdjustStep;
import org.miradi.wizard.diagram.DiagramWizardVisionStep;
import org.miradi.wizard.diagram.RankDraftStrategiesStep;
import org.miradi.wizard.diagram.SelectChainStep;
import org.miradi.wizard.diagram.TargetStressesStep;
import org.miradi.wizard.library.LibraryOverviewStep;
import org.miradi.wizard.map.MapOverviewStep;
import org.miradi.wizard.noproject.NoProjectOverviewStep;
import org.miradi.wizard.noproject.WelcomeImportStep;
import org.miradi.wizard.noproject.WelcomeCreateStep;
import org.miradi.wizard.planning.BudgetWizardAccountingAndFunding;
import org.miradi.wizard.planning.BudgetWizardBudgetDetail;
import org.miradi.wizard.planning.BudgetWizardDemo;
import org.miradi.wizard.planning.FinancialOverviewStep;
import org.miradi.wizard.planning.MonitoringPlanOverviewStep;
import org.miradi.wizard.planning.MonitoringWizardDefineIndicatorsStep;
import org.miradi.wizard.planning.MonitoringWizardFocusStep;
import org.miradi.wizard.planning.PlanningOverviewStep;
import org.miradi.wizard.planning.PlanningWizardFinalizeMonitoringPlanStep;
import org.miradi.wizard.planning.PlanningWizardFinalizeStrategicPlanStep;
import org.miradi.wizard.planning.StrategicPlanDevelopGoalStep;
import org.miradi.wizard.planning.StrategicPlanDevelopObjectivesStep;
import org.miradi.wizard.planning.StrategicPlanOverviewStep;
import org.miradi.wizard.planning.WorkPlanAssignResourcesStep;
import org.miradi.wizard.planning.WorkPlanCreateResourcesStep;
import org.miradi.wizard.planning.WorkPlanDevelopActivitiesAndTasksStep;
import org.miradi.wizard.planning.WorkPlanDevelopMethodsAndTasksStep;
import org.miradi.wizard.planning.WorkPlanOverviewStep;
import org.miradi.wizard.reports.ReportsOverviewStep;
import org.miradi.wizard.schedule.ScheduleOverviewStep;
import org.miradi.wizard.summary.SummaryOverviewStep;
import org.miradi.wizard.summary.SummaryWizardDefineProjecScope;
import org.miradi.wizard.summary.SummaryWizardDefineProjectVision;
import org.miradi.wizard.summary.SummaryWizardDefineTeamMembers;
import org.miradi.wizard.summary.SummaryWizardRolesAndResponsibilities;
import org.miradi.wizard.targetviability.TargetViability3Step;
import org.miradi.wizard.targetviability.TargetViability4Step;
import org.miradi.wizard.targetviability.TargetViability5Step;
import org.miradi.wizard.targetviability.TargetViability6Step;
import org.miradi.wizard.targetviability.TargetViability7Step;
import org.miradi.wizard.targetviability.TargetViability8Step;
import org.miradi.wizard.targetviability.TargetViabilityMethodChoiceStep;
import org.miradi.wizard.targetviability.TargetViabilityOverviewAfterDetailedModeStep;
import org.miradi.wizard.targetviability.TargetViabilityOverviewStep;
import org.miradi.wizard.threatmatrix.ThreatMatrixOverviewStep;
import org.miradi.wizard.threatmatrix.ThreatRatingWizardCheckBundleStep;
import org.miradi.wizard.threatmatrix.ThreatRatingWizardCheckTotalsStep;
import org.miradi.wizard.threatmatrix.ThreatRatingWizardChooseBundle;
import org.miradi.wizard.threatmatrix.ThreatRatingWizardIrreversibilityStep;
import org.miradi.wizard.threatmatrix.ThreatRatingWizardScopeStep;
import org.miradi.wizard.threatmatrix.ThreatRatingWizardSeverityStep;
import org.miradi.wizard.threatmatrix.ThreatSimpleOverviewStep;
import org.miradi.wizard.threatmatrix.ThreatStressCheckThreatRatingStep;
import org.miradi.wizard.threatmatrix.ThreatStressOverviewStep;
import org.miradi.wizard.threatmatrix.ThreatStressRateIrreversibilityAndContributionStep;
import org.miradi.wizard.threatmatrix.ThreatStressRateStressAndSeverityStep;


public class WizardManager
{
	
	public WizardManager(MainWindow mainWindowToUse) throws Exception
	{
		stepEntries = new Hashtable();
		mainWindow = mainWindowToUse;
		setCurrentStepName(getOverviewStepName(NoProjectView.getViewName()));
	}
	
	public void setUpSteps(WizardPanel panel) throws Exception
	{
		createNoProjectStepEntries(panel);
		createSummaryStepEntries(panel);
		createDigramViewStepEntries(panel);
		createTargetViabilityStepEntries(panel);
		createThreatMatrixViewStepEntries(panel);
		createPlanningViewStepEntries(panel);
		createStrategicPlanStepEntries(panel);
		createMonitoringViewStepEntries(panel);
		createWorkPlanStepEntries(panel);
		createBudgetStepEntries(panel);
		createReportViewStepEntries(panel);
		createScheduleStepEntries(panel);
		createMapViewStepEntries(panel);
		createImagesViewStepEntries(panel);
	}
	
	public String getCurrentStepName()
	{
		ProjectMetadata metadata = getProject().getMetadata();
		if(metadata == null)
			return nonProjectCurrentStepName;
		
		return  metadata.getCurrentWizardScreenName();
	}

	private void setCurrentStepName(String newStepName) throws CommandFailedException, Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();
		if(metadata == null)
		{
			nonProjectCurrentStepName = newStepName;
			mainWindow.refreshWizard();
			return;
		}
		
		ORef projectMetadataRef = metadata.getRef();
		getProject().executeCommand(new CommandSetObjectData(projectMetadataRef, ProjectMetadata.TAG_CURRENT_WIZARD_SCREEN_NAME, newStepName));
	}

	public void setStep(Class stepClass) throws Exception
	{
		String name = stripJumpPrefix(stepClass);
		setStep(name);
	}
	
	public void setStep(String newStepName) throws Exception
	{
		SkeletonWizardStep newStep = findStep(newStepName);
		
		if (newStep == null)
		{
			EAM.logError("WizardManager couldnt find step " + newStepName);
			return;
		}
		
		setCurrentStepName(newStepName);

		String newViewName = newStep.getViewName();
		String currentViewName = getProject().getCurrentView();
		if(!newViewName.equals(currentViewName))
		{
			getProject().switchToView(newViewName);
		}

	}

	private SkeletonWizardStep getDefaultStep()
	{
		String stepName = getOverviewStepName(SummaryView.getViewName());
		return findStep(stepName);
	}

	public SkeletonWizardStep getCurrentStep()
	{
		SkeletonWizardStep step = findStep(getCurrentStepName());
		if(step == null)
			step = getDefaultStep();
		return step;
	}

	private Project getProject()
	{
		return mainWindow.getProject();
	}

	public void createNoProjectStepEntries(WizardPanel panel) throws Exception
	{	
		createStepEntry(new NoProjectOverviewStep(panel));
		
		createStepEntry(new WelcomeImportStep(panel))
			.createBackControl(NoProjectOverviewStep.class);
		
		createStepEntry(new WelcomeCreateStep(panel))
			.createBackControl(NoProjectOverviewStep.class);
	}
	
	public void createBudgetStepEntries(WizardPanel panel)
	{
		createStepEntry(new FinancialOverviewStep(panel));
	}
	
	public void createStrategicPlanStepEntries(WizardPanel panel)
	{
		createStepEntry(new StrategicPlanOverviewStep(panel));
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
		createStepEntry(new SummaryWizardRolesAndResponsibilities(panel));
		createStepEntry(new SummaryWizardDefineProjecScope(panel));
		createStepEntry(new SummaryWizardDefineProjectVision(panel));
	}
	
	public void createWorkPlanStepEntries(WizardPanel panel)
	{
		createStepEntry(new WorkPlanOverviewStep(panel));
	}
	
	public void createMonitoringViewStepEntries(WizardPanel panel)
	{		
		createStepEntry(new MonitoringPlanOverviewStep(panel));
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
					createControl("DoneViabilityAnalysis", TargetStressesStep.class).
					createControl("DetailedViability", TargetViability3Step.class);
		createStepEntry(new DescribeTargetStatusStep(panel)).createControl(CONTROL_NEXT, TargetViabilityOverviewStep.class);
		createStepEntry(new TargetViability3Step(panel)).createControl(CONTROL_BACK, TargetViabilityMethodChoiceStep.class);
		createStepEntry(new TargetViability4Step(panel));
		createStepEntry(new TargetViability5Step(panel));
		createStepEntry(new TargetViability6Step(panel));
		createStepEntry(new TargetViability7Step(panel));
		createStepEntry(new TargetViability8Step(panel));
		
		createStepEntry(new TargetStressesStep(panel))
			.createControl(CONTROL_BACK, TargetViabilityMethodChoiceStep.class);
		
		createStepEntry(new DiagramWizardIdentifyDirectThreatStep(panel));
		createStepEntry(new DiagramWizardLinkDirectThreatsToTargetsStep(panel));
		createStepEntry(new DiagramWizardIdentifyIndirectThreatStep(panel));		
		createStepEntry(new DiagramWizardConstructChainsStep(panel));	
		createStepEntry(new DiagramWizardReviewModelAndAdjustStep(panel));		
		createStepEntry(new SelectChainStep(panel));		
		createStepEntry(new DevelopDraftStrategiesStep(panel));
		createStepEntry(new RankDraftStrategiesStep(panel));
		createStepEntry(new StrategicPlanDevelopGoalStep(panel));
		createStepEntry(new StrategicPlanDevelopObjectivesStep(panel));		
		createStepEntry(new MonitoringWizardFocusStep(panel));		
		createStepEntry(new MonitoringWizardDefineIndicatorsStep(panel));
		createStepEntry(new PlanningWizardFinalizeMonitoringPlanStep(panel));
	
		createStepEntry(new DiagramWizardResultsChainStep(panel));
		createStepEntry(new DiagramWizardConstructInitialResultsChain(panel));
		createStepEntry(new DiagramWizardCompleteResultsChainLinks(panel));
		createStepEntry(new DiagramWizardGoodResultsChainCriteriaReview(panel));
	}


	public void createThreatMatrixViewStepEntries(WizardPanel panel) throws Exception
	{
		//TODO: View:Diagram...should be Step:StepName or support both
		
		createStepEntry(new ThreatMatrixOverviewStep(panel))
			.createControl("View:Diagram", DiagramOverviewStep.class)
			.createControl(ThreatMatrixOverviewStep.THREAT_OVERVIEW_STRESS_MODE, ThreatStressOverviewStep.class);
		
		createStepEntry(new ThreatSimpleOverviewStep(panel));
		createStepEntry(new ThreatRatingWizardChooseBundle(panel))
			.createControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		createStepEntry(new ThreatRatingWizardScopeStep(panel));
		createStepEntry(new ThreatRatingWizardSeverityStep(panel));
		createStepEntry(new ThreatRatingWizardIrreversibilityStep(panel));

		createStepEntry(new ThreatRatingWizardCheckBundleStep(panel))
			.createNextControl(ThreatRatingWizardChooseBundle.class); 

		createStepEntry(new ThreatRatingWizardCheckTotalsStep(panel))
			.createBackControl(ThreatStressOverviewStep.class)
			.createControl(ThreatRatingWizardCheckTotalsStep.END_OF_THREAT_SIMPLE_BRANCH, ThreatRatingWizardChooseBundle.class); 

		createStepEntry(new ThreatStressOverviewStep(panel))
			.createBackControl(ThreatMatrixOverviewStep.class)
			.createControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		createStepEntry(new ThreatStressRateStressAndSeverityStep(panel));
		createStepEntry(new ThreatStressRateIrreversibilityAndContributionStep(panel));
		createStepEntry(new ThreatStressCheckThreatRatingStep(panel))
			.createNextControl(ThreatStressOverviewStep.class);

	}

	public void createPlanningViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new PlanningOverviewStep(panel));
		createStepEntry(new PlanningWizardFinalizeStrategicPlanStep(panel));
		createStepEntry(new WorkPlanDevelopActivitiesAndTasksStep(panel));
		createStepEntry(new WorkPlanDevelopMethodsAndTasksStep(panel));
		createStepEntry(new WorkPlanCreateResourcesStep(panel));
		createStepEntry(new WorkPlanAssignResourcesStep(panel));
		createStepEntry(new BudgetWizardAccountingAndFunding(panel));
		createStepEntry(new BudgetWizardBudgetDetail(panel));
		createStepEntry(new BudgetWizardDemo(panel));
	}

	public void createReportViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new ReportsOverviewStep(panel));
	}
	
	public void createMapViewStepEntries(WizardPanel panel)
	{		
		createStepEntry(new MapOverviewStep(panel));
	}
	
	public void createImagesViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new LibraryOverviewStep(panel));
	}
	

	static Class[] getSequence()
	{
		Class[] entries = 
		{
				SummaryOverviewStep.class,
				
				// STEP 1A
				SummaryWizardDefineTeamMembers.class,
				SummaryWizardRolesAndResponsibilities.class,
				
				// STEP 1B
				SummaryWizardDefineProjecScope.class,

				// (later this will go here)
				//MapOverviewStep.class,

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
				
				TargetStressesStep.class,

				// STEP 1C
				DiagramWizardIdentifyDirectThreatStep.class,
				DiagramWizardLinkDirectThreatsToTargetsStep.class,

				ThreatMatrixOverviewStep.class,
				
				// NOTE: The following is a loop
				ThreatSimpleOverviewStep.class,
				ThreatRatingWizardChooseBundle.class,
				ThreatRatingWizardScopeStep.class,
				ThreatRatingWizardSeverityStep.class,
				ThreatRatingWizardIrreversibilityStep.class,
				ThreatRatingWizardCheckBundleStep.class,
				
				// NOTE: The following is a loop
				ThreatStressOverviewStep.class,
				ThreatStressRateStressAndSeverityStep.class,
				ThreatStressRateIrreversibilityAndContributionStep.class,
				ThreatStressCheckThreatRatingStep.class,
				
				ThreatRatingWizardCheckTotalsStep.class,

				// STEP 1D
				DiagramWizardIdentifyIndirectThreatStep.class,	
				// need AssessStakeholders here
				DiagramWizardConstructChainsStep.class,	
				DiagramWizardReviewModelAndAdjustStep.class,		
				
				// STEP 2A
//				StrategicPlanOverviewStep.class,
				StrategicPlanDevelopGoalStep.class,
				
				SelectChainStep.class,		
				DevelopDraftStrategiesStep.class,
				
				RankDraftStrategiesStep.class,

				DiagramWizardResultsChainStep.class,
				DiagramWizardConstructInitialResultsChain.class,
				DiagramWizardCompleteResultsChainLinks.class,
				DiagramWizardGoodResultsChainCriteriaReview.class,

				StrategicPlanDevelopObjectivesStep.class, 	

				PlanningOverviewStep.class,
				PlanningWizardFinalizeStrategicPlanStep.class,

				// STEP 2B
//				MonitoringPlanOverviewStep.class,
				MonitoringWizardFocusStep.class,
				MonitoringWizardDefineIndicatorsStep.class,
				PlanningWizardFinalizeMonitoringPlanStep.class,

				// STEP 3A
				WorkPlanOverviewStep.class,
				WorkPlanDevelopActivitiesAndTasksStep.class,
				
				WorkPlanDevelopMethodsAndTasksStep.class,
				WorkPlanCreateResourcesStep.class,
				WorkPlanAssignResourcesStep.class,

				// (later this will go here)
				//ScheduleOverviewStep.class,
				
				// STEP 3B
				BudgetWizardAccountingAndFunding.class,
				BudgetWizardBudgetDetail.class,
				BudgetWizardDemo.class, 
				
				// STEP 4A
//				FinancialOverviewStep.class, 
				
				// NOT STEPS
				ReportsOverviewStep.class,
				MapOverviewStep.class,
				ScheduleOverviewStep.class,
				LibraryOverviewStep.class,
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
	
	public void setOverViewStep(String viewName) throws Exception
	{
		setStep(getOverviewStepName(viewName));
	}

	public String getOverviewStepName(String viewName)
	{
		return removeSpaces(viewName) + "OverviewStep";
	}
	
	private String removeSpaces(String name)
	{
		return name.replaceAll(" ", "");
	}

	public static String CONTROL_NEXT = "Next";
	public static String CONTROL_BACK = "Back";

	private MainWindow mainWindow;
	private Hashtable stepEntries;
	
	private String nonProjectCurrentStepName;
}

