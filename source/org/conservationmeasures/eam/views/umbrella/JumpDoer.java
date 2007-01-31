/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.actions.jump.ActionJumpAssignResources;
import org.conservationmeasures.eam.actions.jump.ActionJumpBudgetFutureDemo;
import org.conservationmeasures.eam.actions.jump.ActionJumpDesignateLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopActivitiesAndTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopMonitoringMethodsAndTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopSchedule;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardDescribeTargetStatusStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardProjectScopeStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardVisionStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditAllStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardEditIndicatorsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardFocusStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectMethod;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanHowToConstructStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatMatrixOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatRatingWizardCheckTotalsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanViewAllObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanOverview;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.utils.JumpLocation;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.budget.BudgetView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.monitoring.MonitoringView;
import org.conservationmeasures.eam.views.schedule.ScheduleView;
import org.conservationmeasures.eam.views.strategicplan.StrategicPlanView;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;

public class JumpDoer extends MainWindowDoer
{
	public JumpDoer(Class actionClassToUse)
	{
		actionClass = actionClassToUse;
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		JumpLocation jumpTo = createJumpLocation(actionClass);
		if(jumpTo == null)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			JumpLocation jumpTo = createJumpLocation(actionClass);
			String view = jumpTo.getView();
			if(!getProject().getCurrentView().equals(view))
			{
				getMainWindow().setSplitterLocationToMiddle(view);
				getProject().executeCommand(new CommandSwitchView(jumpTo.getView()));
			}
			
			// FIXME: This really should be a Command so it is undoable,
			// but that would require us to be able to obtain the current 
			// step marker no matter where we are, which isn't possible yet
			getMainWindow().jump(jumpTo.getStepMarker());
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	public JumpLocation createJumpLocation(Class jumpActionClass)
	{
		String jumpToView = getViewForAction(jumpActionClass);
		if(jumpToView == null)
			return null;
		
		return new JumpLocation(jumpToView, jumpActionClass);
	}
	
	String getViewForAction(Class jumpActionClass)
	{
		if(jumpActionClass.equals(ActionJumpSelectTeam.class))
			return SummaryView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDesignateLeader.class))
			return SummaryView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDiagramWizardProjectScopeStep.class))
			return SummaryView.getViewName();

		if(jumpActionClass.equals(ActionJumpDiagramWizardVisionStep.class))
			return SummaryView.getViewName();
		
		
		if(jumpActionClass.equals(ActionJumpDiagramWizardDefineTargetsStep.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDiagramWizardDescribeTargetStatusStep.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpIdentifyDiagramWizardIdentifyDirectThreatStep.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDiagramWizardIdentifyIndirectThreatStep.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpStrategicPlanDevelopGoalStep.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpStrategicPlanDevelopObjectivesStep.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpMonitoringWizardFocusStep.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpSelectChainStep.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpRankDraftStrategiesStep.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpEditAllStrategiesStep.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpDiagramWizardReviewModelAndAdjustStep.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDiagramWizardReviewModelAndAdjustStep.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpMonitoringWizardDefineIndicatorsStep.class))
			return DiagramView.getViewName();
		
		

		if(jumpActionClass.equals(ActionJumpThreatMatrixOverviewStep.class))
			return ThreatMatrixView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpThreatRatingWizardCheckTotalsStep.class))
			return ThreatMatrixView.getViewName();
		

		if (jumpActionClass.equals(ActionJumpStrategicPlanViewAllGoals.class))
			return StrategicPlanView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpStrategicPlanViewAllObjectives.class))
			return StrategicPlanView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpStrategicPlanHowToConstructStep.class))
			return StrategicPlanView.getViewName();
		

		if (jumpActionClass.equals(ActionJumpMonitoringWizardEditIndicatorsStep.class))
			return MonitoringView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpMonitoringOverviewStep.class))
			return MonitoringView.getViewName();
		
		
		if (jumpActionClass.equals(ActionJumpSelectMethod.class))
			return WorkPlanView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpWorkPlanAssignResourcesStep.class))
			return WorkPlanView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpDevelopActivitiesAndTasks.class))
			return WorkPlanView.getViewName();

		if (jumpActionClass.equals(ActionJumpDevelopMonitoringMethodsAndTasks.class))
			return WorkPlanView.getViewName();

		if (jumpActionClass.equals(ActionJumpAssignResources.class))
			return WorkPlanView.getViewName();

		
		if (jumpActionClass.equals(ActionJumpDevelopBudgets.class))
			return BudgetView.getViewName();
		if(jumpActionClass.equals(ActionJumpBudgetFutureDemo.class))
			return BudgetView.getViewName();

		
		if (jumpActionClass.equals(ActionJumpDevelopSchedule.class))
			return ScheduleView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpDiagramOverviewStep.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpWorkPlanOverview.class))
			return WorkPlanView.getViewName();
		
		return null;
	}

	Class actionClass;
}
