/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.accountingcode.AccountingCodePropertiesPanel;
import org.miradi.dialogs.base.AbstractMultiPropertiesPanel;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.categoryOne.BudgetCategoryOnePropertiesPanel;
import org.miradi.dialogs.categoryTwo.BudgetCategoryTwoPropertiesPanel;
import org.miradi.dialogs.diagram.ConceptualModelPropertiesPanel;
import org.miradi.dialogs.diagram.ResultsChainPropertiesPanel;
import org.miradi.dialogs.diagram.StrategyPropertiesPanel;
import org.miradi.dialogs.fundingsource.FundingSourcePropertiesPanel;
import org.miradi.dialogs.goal.GoalPropertiesPanel;
import org.miradi.dialogs.indicator.MethodPropertiesPanel;
import org.miradi.dialogs.objective.ObjectivePropertiesPanel;
import org.miradi.dialogs.planning.MeasurementPropertiesPanel;
import org.miradi.dialogs.resource.ResourcePropertiesPanel;
import org.miradi.dialogs.strategicPlan.IndicatorPropertiesPanel;
import org.miradi.dialogs.subTarget.SubTargetPropertiesPanel;
import org.miradi.dialogs.task.WorkPlanActivityPropertiesPanel;
import org.miradi.dialogs.viability.NonDiagramAbstractTargetPropertiesPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.*;
import org.miradi.schemas.*;

public class PlanningTreeMultiPropertiesPanel extends AbstractMultiPropertiesPanel
{
	public PlanningTreeMultiPropertiesPanel(MainWindow mainWindowToUse, ORef orefToUse) throws Exception
	{
		super(mainWindowToUse, orefToUse);
		
		blankPropertiesPanel = new BlankPropertiesPanel(getProject());
		addPanel(blankPropertiesPanel);
	}
	
	protected WorkPlanActivityPropertiesPanel createActivityPropertiesPanel() throws Exception
	{
		return new WorkPlanActivityPropertiesPanel(getMainWindow());
	}

	private PlanningViewTaskPropertiesPanel createTaskPropertiesPanel() throws Exception
	{
		return new PlanningViewTaskPropertiesPanel(getMainWindow());
	}

	private StrategyPropertiesPanel createStrategyPropertiesPanel() throws Exception
	{
		return new StrategyPropertiesPanel(getMainWindow());
	}

	private IndicatorPropertiesPanel createIndicatorPropertiesPanel() throws Exception
	{
		return new IndicatorPropertiesPanel(getProject(), getMainWindow());
	}
	
	private MethodPropertiesPanel createMethodPropertiesPanel() throws Exception
	{
		return new MethodPropertiesPanel(getMainWindow());
	}
	
	private ResourceAssignmentPropertiesPanel createResourceAssignmentPropertiesPanel() throws Exception
	{
		return new ResourceAssignmentPropertiesPanel(getMainWindow());
	}

	private ExpenseAssignmentPropertiesPanel createExpenseAssignmentPropertiesPanel() throws Exception
	{
		return new ExpenseAssignmentPropertiesPanel(getMainWindow());
	}

	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	@Override
	protected AbstractObjectDataInputPanel findPanel(ORef[] oRefsToUse)
	{
		if(oRefsToUse.length == 0)
			return blankPropertiesPanel;
		
		try
		{
			ORef firstRef = oRefsToUse[DEEPEST_INDEX];
			int objectType = firstRef.getObjectType();
			if (GoalSchema.getObjectType() == objectType)
				return getGoalPropertiesPanel();
			
			if (ObjectiveSchema.getObjectType() == objectType)
				return getObjectivePropertiesPanel();
			
			if (IndicatorSchema.getObjectType() == objectType)
				return getIndicatorPropertiesPanel();
			
			if (MethodSchema.getObjectType() == objectType)
				return getMethodPropertiesPanel();

			if (StrategySchema.getObjectType() == objectType)
				return getStrategyPropertiesPanel();
			
			if (Task.isActivity(getProject(), firstRef))
				return getActivityPropertiesPanel();
			
			if (TaskSchema.getObjectType() == objectType)
				return getTaskPropertiesPanel();
			
			if (MeasurementSchema.getObjectType() == objectType)
				return getMeasurementPropertiesPanel();
			
			if(FutureStatus.is(objectType))
				return getFutureStatusForViabilityMode(new ORefList(oRefsToUse));
			
			if (TargetSchema.getObjectType() == objectType)
				return getBiodiversityTargetPropertiesPanel();
			
			if (HumanWelfareTarget.is(objectType))
				return getHumanWelfareTargetPropertiesPanel();
			
			if (BiophysicalFactorSchema.getObjectType() == objectType)
				return getBiophysicalFactorPropertiesPanel();
			
			if (BiophysicalResultSchema.getObjectType() == objectType)
				return getBiophysicalResultPropertiesPanel();

			if (CauseSchema.getObjectType() == objectType)
				return getCausePropertiesPanel(firstRef);

			if (IntermediateResultSchema.getObjectType() == objectType)
				return getIntermediateResultPropertiesPanel();
			
			if (ThreatReductionResultSchema.getObjectType() == objectType)
				return getThreatReductionResultPropertiesPanel();

			if (ResultsChainDiagram.is(objectType))
				return getResultsChainPropertiesPanel();
			
			if (ConceptualModelDiagram.is(objectType))
				return getConceptualModelPropertiesPanel();
			
			if (ProjectResource.is(objectType))
				return getProjectResourcePropertiesPanel();
			
			if (FundingSource.is(objectType))
				return getFundingSourcePropertiesPanel();
			
			if (AccountingCode.is(objectType))
				return getAccountingCodePropertiesPanel();
			
			if (BudgetCategoryOne.is(objectType))
				return getBudgetCategoryOnePropertiesPanel();
			
			if (BudgetCategoryTwo.is(objectType))
				return getBudgetCategoryTwoPropertiesPanel();
			
			if (SubTarget.is(objectType))
				return getSubTargetPropertiesPanel();

			if (ResourceAssignment.is(objectType))
				return getResourceAssignmentPropertiesPanel();

			if (ExpenseAssignment.is(objectType))
				return getExpenseAssignmentPropertiesPanel();
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		return blankPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getGoalPropertiesPanel() throws Exception
	{
		if(goalPropertiesPanel == null)
		{
			goalPropertiesPanel = new GoalPropertiesPanel(getProject(), getMainWindow().getActions());
			addPanel(goalPropertiesPanel);
		}
		return goalPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getObjectivePropertiesPanel() throws Exception
	{
		if(objectivePropertiesPanel == null)
		{
			objectivePropertiesPanel = new ObjectivePropertiesPanel(getProject(), getMainWindow().getActions());
			addPanel(objectivePropertiesPanel);
		}
		return objectivePropertiesPanel;
	}

	private AbstractObjectDataInputPanel getIndicatorPropertiesPanel() throws Exception
	{
		if(indicatorPropertiesPanel == null)
		{
			indicatorPropertiesPanel = createIndicatorPropertiesPanel();
			addPanel(indicatorPropertiesPanel);
		}
		return indicatorPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getMethodPropertiesPanel() throws Exception
	{
		if(methodPropertiesPanel == null)
		{
			methodPropertiesPanel = createMethodPropertiesPanel();
			addPanel(methodPropertiesPanel);
		}
		return methodPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getStrategyPropertiesPanel() throws Exception
	{
		if(strategyPropertiesPanel == null)
		{
			strategyPropertiesPanel = createStrategyPropertiesPanel();
			addPanel(strategyPropertiesPanel);
		}
		return strategyPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getTaskPropertiesPanel() throws Exception
	{
		if(taskPropertiesInputPanel == null)
		{
			taskPropertiesInputPanel = createTaskPropertiesPanel();
			addPanel(taskPropertiesInputPanel);
		}
		return taskPropertiesInputPanel;
	}
	
	private AbstractObjectDataInputPanel getActivityPropertiesPanel() throws Exception
	{
		if (activityPropertiesPanel == null)
		{
			activityPropertiesPanel = createActivityPropertiesPanel();
			addPanel(activityPropertiesPanel);
		}
		
		return activityPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getMeasurementPropertiesPanel() throws Exception
	{
		if(measurementPropertiesPanel == null)
		{
			measurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
			addPanel(measurementPropertiesPanel);
		}
		return measurementPropertiesPanel;
	}
	
	private AbstractObjectDataInputPanel getBiodiversityTargetPropertiesPanel() throws Exception
	{
		if(targetPropertiesPanel == null)
		{
			targetPropertiesPanel = new NonDiagramAbstractTargetPropertiesPanel(getProject(), TargetSchema.getObjectType());
			addPanel(targetPropertiesPanel);
		}
		return targetPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getHumanWelfareTargetPropertiesPanel() throws Exception
	{
		if(humanWelfareTargetPropertiesPanel == null)
		{
			humanWelfareTargetPropertiesPanel = new NonDiagramAbstractTargetPropertiesPanel(getProject(), HumanWelfareTargetSchema.getObjectType());
			addPanel(humanWelfareTargetPropertiesPanel);
		}
		return humanWelfareTargetPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getIntermediateResultPropertiesPanel() throws Exception
	{
		if(intermediateResultPropertiesPanel == null)
		{
			intermediateResultPropertiesPanel = new PlanningViewIntermediateResultPropertiesPanel(getProject());
			addPanel(intermediateResultPropertiesPanel);
		}
		return intermediateResultPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getThreatReductionResultPropertiesPanel() throws Exception
	{
		if (threatReductionResultPropertiesPanel == null)
		{
			threatReductionResultPropertiesPanel = new PlanningViewThreatReductionResultPropertiesPanel(getProject());
			addPanel(threatReductionResultPropertiesPanel);
		}
		return threatReductionResultPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getResultsChainPropertiesPanel() throws Exception
	{
		if(resultsChainPropertiesPanel == null)
		{
			resultsChainPropertiesPanel = new ResultsChainPropertiesPanel(getProject(), ORef.INVALID);
			addPanel(resultsChainPropertiesPanel);
		}
		return resultsChainPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getConceptualModelPropertiesPanel() throws Exception
	{
		if(conceptualModelPropertiesPanel == null)
		{
			conceptualModelPropertiesPanel = new ConceptualModelPropertiesPanel(getProject(), ORef.INVALID);
			addPanel(conceptualModelPropertiesPanel);
		}
		return conceptualModelPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getProjectResourcePropertiesPanel() throws Exception
	{
		if(projectResourcePropertiesPanel == null)
		{
			projectResourcePropertiesPanel = new ResourcePropertiesPanel(getProject(), BaseId.INVALID);
			addPanel(projectResourcePropertiesPanel);
		}
		return projectResourcePropertiesPanel;
	}

	private AbstractObjectDataInputPanel getFundingSourcePropertiesPanel() throws Exception
	{
		if(fundingSourcePropertiesPanel == null)
		{
			fundingSourcePropertiesPanel = new FundingSourcePropertiesPanel(getProject());
			addPanel(fundingSourcePropertiesPanel);
		}
		return fundingSourcePropertiesPanel;
	}

	private AbstractObjectDataInputPanel getAccountingCodePropertiesPanel() throws Exception
	{
		if(accountingCodePropertiesPanel == null)
		{
			accountingCodePropertiesPanel = new AccountingCodePropertiesPanel(getProject());
			addPanel(accountingCodePropertiesPanel);
		}
		return accountingCodePropertiesPanel;
	}

	private AbstractObjectDataInputPanel getBudgetCategoryOnePropertiesPanel() throws Exception
	{
		if(categoryOnePropertiesPanel == null)
		{
			categoryOnePropertiesPanel = new BudgetCategoryOnePropertiesPanel(getProject());
			addPanel(categoryOnePropertiesPanel);
		}
		return categoryOnePropertiesPanel;
	}

	private AbstractObjectDataInputPanel getBudgetCategoryTwoPropertiesPanel() throws Exception
	{
		if(categoryTwoPropertiesPanel == null)
		{
			categoryTwoPropertiesPanel = new BudgetCategoryTwoPropertiesPanel(getProject());
			addPanel(categoryTwoPropertiesPanel);
		}
		return categoryTwoPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getSubTargetPropertiesPanel() throws Exception
	{
		if(subTargetPropertiesPanel == null)
		{
			subTargetPropertiesPanel = new SubTargetPropertiesPanel(getProject());
			addPanel(subTargetPropertiesPanel);
		}
		return subTargetPropertiesPanel;
	}

	private MinimalFactorPropertiesPanel getBiophysicalFactorPropertiesPanel() throws Exception
	{
        if(biophysicalFactorPropertiesPanel == null)
        {
            biophysicalFactorPropertiesPanel = new PlanningViewBiophysicalFactorPropertiesPanel(getProject());
            addPanel(biophysicalFactorPropertiesPanel);
        }
        return biophysicalFactorPropertiesPanel;
	}

	private MinimalFactorPropertiesPanel getBiophysicalResultPropertiesPanel() throws Exception
	{
        if(biophysicalResultPropertiesPanel == null)
        {
            biophysicalResultPropertiesPanel = new PlanningViewBiophysicalResultPropertiesPanel(getProject());
            addPanel(biophysicalResultPropertiesPanel);
        }
        return biophysicalResultPropertiesPanel;
	}

	private MinimalFactorPropertiesPanel getCausePropertiesPanel(ORef causeRef) throws Exception
	{
		Cause cause = Cause.find(getProject(), causeRef);
		if (cause.isDirectThreat())
			return getThreatPropertiesPanel();

		return getContributingFactorPropertiesPanel();
	}

	private MinimalFactorPropertiesPanel getContributingFactorPropertiesPanel() throws Exception
	{
		if(contributingFactorPropertiesPanel == null)
		{
			contributingFactorPropertiesPanel = new PlanningViewContributingFactorPropertiesPanel(getProject());
			addPanel(contributingFactorPropertiesPanel);
		}
		return contributingFactorPropertiesPanel;
	}

	private MinimalFactorPropertiesPanel getThreatPropertiesPanel() throws Exception
	{
		if(threatPropertiesPanel == null)
		{
			threatPropertiesPanel = new PlanningViewDirectThreatPropertiesPanel(getProject());
			addPanel(threatPropertiesPanel);
		}
		return threatPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getResourceAssignmentPropertiesPanel() throws Exception
	{
		if (resourceAssignmentPropertiesPanel == null)
		{
			resourceAssignmentPropertiesPanel = createResourceAssignmentPropertiesPanel();
			addPanel(resourceAssignmentPropertiesPanel);
		}

		return resourceAssignmentPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getExpenseAssignmentPropertiesPanel() throws Exception
	{
		if (expenseAssignmentPropertiesPanel == null)
		{
			expenseAssignmentPropertiesPanel = createExpenseAssignmentPropertiesPanel();
			addPanel(expenseAssignmentPropertiesPanel);
		}

		return expenseAssignmentPropertiesPanel;
	}

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
			
		if (event.isSetDataCommandWithThisTypeAndTag(TargetSchema.getObjectType(), Target.TAG_VIABILITY_MODE))
			reloadSelectedRefs();
	}

	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	private static final int DEEPEST_INDEX = 0; 
	
	private GoalPropertiesPanel goalPropertiesPanel;
	private ObjectivePropertiesPanel objectivePropertiesPanel;
	private IndicatorPropertiesPanel indicatorPropertiesPanel;
	private MethodPropertiesPanel methodPropertiesPanel;
	private StrategyPropertiesPanel strategyPropertiesPanel;
	private PlanningViewTaskPropertiesPanel taskPropertiesInputPanel;
	private WorkPlanActivityPropertiesPanel activityPropertiesPanel;
	private NonDiagramAbstractTargetPropertiesPanel targetPropertiesPanel;
	private NonDiagramAbstractTargetPropertiesPanel humanWelfareTargetPropertiesPanel; 
	private PlanningViewIntermediateResultPropertiesPanel intermediateResultPropertiesPanel;
	private PlanningViewThreatReductionResultPropertiesPanel threatReductionResultPropertiesPanel;
	private PlanningViewDirectThreatPropertiesPanel threatPropertiesPanel;
	private PlanningViewContributingFactorPropertiesPanel contributingFactorPropertiesPanel;
    private PlanningViewBiophysicalFactorPropertiesPanel biophysicalFactorPropertiesPanel;
    private PlanningViewBiophysicalResultPropertiesPanel biophysicalResultPropertiesPanel;
	private MeasurementPropertiesPanel measurementPropertiesPanel;
	private ResultsChainPropertiesPanel resultsChainPropertiesPanel;
	private ConceptualModelPropertiesPanel conceptualModelPropertiesPanel;
	private ResourcePropertiesPanel projectResourcePropertiesPanel;
	private FundingSourcePropertiesPanel fundingSourcePropertiesPanel;
	private AccountingCodePropertiesPanel accountingCodePropertiesPanel;
	private BudgetCategoryOnePropertiesPanel categoryOnePropertiesPanel;
	private BudgetCategoryTwoPropertiesPanel categoryTwoPropertiesPanel;
	private SubTargetPropertiesPanel subTargetPropertiesPanel;
	private ResourceAssignmentPropertiesPanel resourceAssignmentPropertiesPanel;
	private ExpenseAssignmentPropertiesPanel expenseAssignmentPropertiesPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
}
