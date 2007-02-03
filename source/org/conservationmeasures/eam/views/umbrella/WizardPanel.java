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
				addThreatMatrixViewSteps(currentView);
				
			if (currentView.equals(DiagramView.getViewName()))
				addDiagramViewSteps(currentView);
			
			if (currentView.equals(MonitoringView.getViewName()))
				addMonitoringViewSteps(currentView);
			
			if (currentView.equals(WorkPlanView.getViewName()))
				addWorkPlanViewSteps(currentView);
			
			if (currentView.equals(SummaryView.getViewName()))
				addSummaryViewSteps(currentView);
				
			if (currentView.equals(ScheduleView.getViewName()))
				addScheduleViewSteps(currentView);
			
			if (currentView.equals(StrategicPlanView.getViewName()))
				addStrategicPlanViewSteps(currentView);
			
			if (currentView.equals(BudgetView.getViewName()))
				addBudgetViewSteps(currentView);
			
			if (currentView.equals(NoProjectView.getViewName()))
				addNoProjectViewSteps(currentView);
			
			setStep("");
		}
		catch (Exception e)
		{
			String body = EAM.text("Wizard load failed for view: ") + view.cardName();
			EAM.errorDialog(body);
			EAM.logError(body);
			EAM.logException(e);
		}
	}
	
	private void addNoProjectViewSteps(String currentView) throws Exception
	{
		stepTable.createNoProjectStepEntries((NoProjectWizardPanel)this, currentView);
	}
	
	private void addBudgetViewSteps(String currentView)
	{
		stepTable.createBudgetStepEntries(this, currentView);
	}
	
	private void addStrategicPlanViewSteps(String currentView)
	{
		stepTable.createStrategicPlanStepEntries(this, currentView);
	}
	
	private void addScheduleViewSteps(String currentView)
	{
		stepTable.createScheduleStepEntries(this, currentView);
	}

	private void addSummaryViewSteps(String currentView)
	{
		stepTable.createSummaryStepEntries(this, currentView);
	}
	
	private void addDiagramViewSteps(String currentView) throws Exception
	{
		stepTable.createDigramViewStepEntries(this, currentView);
	}
	
	private void addThreatMatrixViewSteps(String currentView) throws Exception
	{
		stepTable.createThreatMatrixViewStepEntries((ThreatRatingWizardPanel)this, currentView);
	}

	public void addMonitoringViewSteps(String currentView)
	{
		stepTable.createMonitoringViewStepEntries(this, currentView);
	}
	
	public void addWorkPlanViewSteps(String currentView)
	{
		stepTable.createWorkPlanStepEntries(this, currentView);
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
		jump(entry.findControlTargetStep(controlName));
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
	
	public void createNoProjectStepEntries(NoProjectWizardPanel panel, String viewName) throws Exception
	{	
		createStepEntry(new NoProjectOverviewStep(panel), viewName);
		
		createStepEntry(new NoProjectWizardImportStep(panel), viewName)
			.createBackControl(NoProjectOverviewStep.class);
		
		createStepEntry(new NoProjectWizardProjectCreateStep(panel), viewName)
			.createBackControl(NoProjectOverviewStep.class);
	}
	
	public void createBudgetStepEntries(WizardPanel panel, String viewName)
	{
		createStepEntry(new FinancialOverviewStep(panel), viewName);
		createStepEntry(new BudgetWizardAccountingAndFunding(panel), viewName);
		createStepEntry(new BudgetWizardBudgetDetail(panel), viewName);
		createStepEntry(new BudgetWizardDemo(panel), viewName);
	}
	
	public void createStrategicPlanStepEntries(WizardPanel panel, String viewName)
	{
		createStepEntry(new StrategicPlanOverviewStep(panel), viewName);
		createStepEntry(new StrategicPlanViewAllGoals(panel), viewName);
		createStepEntry(new StrategicPlanViewAllObjectives(panel), viewName);
	}
	
	public void createScheduleStepEntries(WizardPanel panel, String viewName)
	{
		createStepEntry(new ScheduleOverviewStep(panel), viewName);
	}
	
	public void createSummaryStepEntries(WizardPanel panel, String viewName)
	{
		createStepEntry(new SummaryOverviewStep(panel), viewName);
		createStepEntry(new SummaryWizardDefineTeamMembers(panel), viewName);
		createStepEntry(new SummaryWizardDefineProjectLeader(panel), viewName);;
		createStepEntry(new SummaryWizardDefineProjecScope(panel), viewName);
		createStepEntry(new SummaryWizardDefineProjectVision(panel), viewName);
	}
	
	public void createWorkPlanStepEntries(WizardPanel panel, String viewName)
	{
		createStepEntry(new WorkPlanOverviewStep(panel), viewName);
		createStepEntry(new MonitoringWizardSelectMethodsStep(panel), viewName);;
		createStepEntry(new WorkPlanDevelopActivitiesAndTasksStep(panel), viewName);
		createStepEntry(new WorkPlanDevelopMethodsAndTasksStep(panel), viewName);
		createStepEntry(new WorkPlanCreateResourcesStep(panel), viewName);
		createStepEntry(new WorkPlanAssignResourcesStep(panel), viewName);
	}
	
	public void createMonitoringViewStepEntries(WizardPanel panel, String viewName)
	{		
		createStepEntry(new MonitoringPlanOverviewStep(panel), viewName);
		createStepEntry(new MonitoringWizardEditIndicatorsStep(panel), viewName);
	}
	
	public void createDigramViewStepEntries(WizardPanel panel, String viewName)
	{
		createStepEntry(new DiagramOverviewStep(panel), viewName);
		createStepEntry(new DiagramWizardProjectScopeStep(panel), viewName);
		createStepEntry(new DiagramWizardVisionStep(panel), viewName);
		createStepEntry(new DiagramWizardDefineTargetsStep(panel), viewName);
		createStepEntry(new DiagramWizardReviewAndModifyTargetsStep(panel), viewName);
		createStepEntry(new DescribeTargetStatusStep(panel), viewName);
		createStepEntry(new DiagramWizardIdentifyDirectThreatStep(panel), viewName);
		createStepEntry(new DiagramWizardLinkDirectThreatsToTargetsStep(panel), viewName);
		createStepEntry(new DiagramWizardIdentifyIndirectThreatStep(panel), viewName);		
		createStepEntry(new DiagramWizardConstructChainsStep(panel), viewName);	
		createStepEntry(new DiagramWizardReviewModelAndAdjustStep(panel), viewName);		
		createStepEntry(new SelectChainStep(panel), viewName);		
		createStepEntry(new DevelopDraftStrategiesStep(panel), viewName);
		createStepEntry(new RankDraftStrategiesStep(panel), viewName);
		createStepEntry(new EditAllStrategiesStep(panel), viewName);
		createStepEntry(new StrategicPlanDevelopGoalStep(panel), viewName);
		createStepEntry(new StrategicPlanDevelopObjectivesStep(panel), viewName);		
		createStepEntry(new MonitoringWizardFocusStep(panel), viewName);		
		createStepEntry(new MonitoringWizardDefineIndicatorsStep(panel), viewName);
	}


	public void createThreatMatrixViewStepEntries(ThreatRatingWizardPanel panel, String viewName) throws Exception
	{
		//TODO: View:Diagram...should be Step:StepName or support both
		
		createStepEntry(new ThreatMatrixOverviewStep(panel), viewName)
			.createControl("View:Diagram", DiagramOverviewStep.class);


		createStepEntry(new ThreatRatingWizardChooseBundle(panel), viewName)
			.createControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		createStepEntry(new ThreatRatingWizardScopeStep(panel), viewName);
		createStepEntry(new ThreatRatingWizardSeverityStep(panel), viewName);
		createStepEntry(new ThreatRatingWizardIrreversibilityStep(panel), viewName);

		createStepEntry(new ThreatRatingWizardCheckBundleStep(panel), viewName)
			.createNextControl(ThreatRatingWizardChooseBundle.class); 

		createStepEntry(new ThreatRatingWizardCheckTotalsStep(panel), viewName)
			.createNextControl(ThreatMatrixOverviewStep.class)
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
		
		return entries;
		
	}
	
	private WizardStepEntry createStepEntry(SkeletonWizardStep step, String viewName)
	{
		WizardStepEntry stepEntry = new WizardStepEntry(step.getClass().getSimpleName(), viewName);
		stepEntry.setStepClass(step);
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
	
	//TODO: add control directly to wizard step; get rid of WizardControl class
	WizardStepEntry createControl(String controlName , Class controlStep)
	{
		entries.put(controlName, new WizardControl(controlName, controlStep.getSimpleName()));
		return this;
	}
	
	WizardStepEntry createNextControl(Class controlStep)
	{
		return createControl("Next", controlStep);
	}
	
	WizardStepEntry createBackControl(Class controlStep)
	{
		return createControl("Back", controlStep);
	}

	
	String findControlTargetStep(String controlName)
	{
		WizardControl control = (WizardControl)entries.get(controlName);
		
		if (control==null)
			return doDeferedLookup(controlName);

		return control.getStepName();
	}


	String doDeferedLookup(String controlName)
	{
		Class[] sequences = StepTable.getSequence();

		int found = findPositionInSequence(sequences);
			
		if (found<0)
		{
			reportError(EAM.text("Step not found in sequence table: ") + stepName);
			return null;
		}
		
		if (controlName.equals("Next"))
		{
			String name = sequences[0].getSimpleName();
			if (found != sequences.length-1)
				name = sequences[found+1].getSimpleName();
			return name;
		}
		
		if (controlName.equals("Back"))
		{
			String name = sequences[sequences.length-1].getSimpleName();
			if (found != 0)
				name = sequences[found-1].getSimpleName();
			return name;
			
		}
		
		reportError(EAM.text("Control not specified: ") + stepName);
		return null;
	}

	private void reportError(String msg)
	{
		EAM.logError(msg);
		EAM.errorDialog(msg);
	}


	private int findPositionInSequence(Class[] sequences)
	{
		for (int i=0; i<sequences.length; ++i)
			if (sequences[i].getSimpleName().equals(stepName)) 
				return i;
		return -1;
	}
	
	void setStepClass(SkeletonWizardStep stepToUse)
	{
		step = stepToUse;
	}
	
	SkeletonWizardStep getStepClass()
	{
		return step;
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

//TODO: THis class will go away once controls are stored directly in step classes
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
