/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.budget.BudgetView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.monitoring.MonitoringView;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardPanel;
import org.conservationmeasures.eam.views.schedule.ScheduleView;
import org.conservationmeasures.eam.views.strategicplan.StrategicPlanView;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardPanel;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;

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
			
			currentStepName = removeSpaces(view.cardName()) + "OverviewStep";
			setStep(currentStepName);
		}
		catch (Exception e)
		{
			String body = EAM.text("Wizard load failed for view: ") + view.cardName();
			EAM.errorDialog(body);
			EAM.logError(body);
			EAM.logException(e);
		}
	}
	
	private void addNoProjectViewSteps() throws Exception
	{
		wizardManager.createNoProjectStepEntries((NoProjectWizardPanel)this);
	}
	
	private void addBudgetViewSteps()
	{
		wizardManager.createBudgetStepEntries(this);
	}
	
	private void addStrategicPlanViewSteps()
	{
		wizardManager.createStrategicPlanStepEntries(this);
	}
	
	private void addScheduleViewSteps()
	{
		wizardManager.createScheduleStepEntries(this);
	}

	private void addSummaryViewSteps()
	{
		wizardManager.createSummaryStepEntries(this);
	}
	
	private void addDiagramViewSteps() throws Exception
	{
		wizardManager.createDigramViewStepEntries(this);
	}
	
	private void addThreatMatrixViewSteps() throws Exception
	{
		wizardManager.createThreatMatrixViewStepEntries((ThreatRatingWizardPanel)this);
	}

	public void addMonitoringViewSteps()
	{
		wizardManager.createMonitoringViewStepEntries(this);
	}
	
	public void addWorkPlanViewSteps()
	{
		wizardManager.createWorkPlanStepEntries(this);
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
		WizardStepEntry entry = wizardManager.findStep(currentStepName);
		jump(entry.findControlTargetStep(controlName));
	}


	public void setStep(Class newStep) throws Exception
	{
		setStep(newStep.getSimpleName());
	}
	
	public void setStep(String newStep) throws Exception
	{
		currentStepName = wizardManager.setStep(newStep, currentStepName);
	}
	
	private String removeSpaces(String name)
	{
		return name.replaceAll(" ", "");
	}

	
	public void refresh() throws Exception
	{
		SkeletonWizardStep stepClass = wizardManager.findStep(currentStepName).getStepClass();
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
	
	public UmbrellaView getView()
	{
		return view;
	}
	
	protected UmbrellaView view;
	protected MainWindow mainWindow;
	public String currentStepName;
	//TODO: this should not be a static but is really a wizard manager and should be pulled from above.
	private static WizardManager wizardManager = new WizardManager();

}
