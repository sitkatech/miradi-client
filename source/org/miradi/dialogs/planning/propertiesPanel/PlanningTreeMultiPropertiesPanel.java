/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.OverlaidObjectDataInputPanel;
import org.miradi.dialogs.categoryOne.BudgetCategoryOnePropertiesPanel;
import org.miradi.dialogs.categoryTwo.BudgetCategoryTwoPropertiesPanel;
import org.miradi.dialogs.diagram.AbstractStrategyPropertiesPanel;
import org.miradi.dialogs.diagram.ConceptualModelPropertiesPanel;
import org.miradi.dialogs.diagram.ResultsChainPropertiesPanel;
import org.miradi.dialogs.diagram.StrategyPropertiesPanel;
import org.miradi.dialogs.fundingsource.FundingSourcePropertiesPanel;
import org.miradi.dialogs.goal.GoalPropertiesPanel;
import org.miradi.dialogs.objective.ObjectivePropertiesPanel;
import org.miradi.dialogs.planning.MeasurementPropertiesPanel;
import org.miradi.dialogs.resource.ResourcePropertiesPanel;
import org.miradi.dialogs.subTarget.SubTargetPropertiesPanel;
import org.miradi.dialogs.viability.AbstractIndicatorPropertiesPanel;
import org.miradi.dialogs.viability.IndicatorPropertiesPanelWithBudgetPanels;
import org.miradi.dialogs.viability.NonDiagramAbstractTargetPropertiesPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;

public class PlanningTreeMultiPropertiesPanel extends OverlaidObjectDataInputPanel
{
	public PlanningTreeMultiPropertiesPanel(MainWindow mainWindowToUse, ORef orefToUse) throws Exception
	{
		super(mainWindowToUse, orefToUse);
		
		createPropertiesPanels();
	}
	
	private void createPropertiesPanels() throws Exception
	{
		blankPropertiesPanel = new BlankPropertiesPanel(getProject());
		addPanel(blankPropertiesPanel);
	}

	protected PlanningViewTaskPropertiesPanel createTaskPropertiesPanel() throws Exception
	{
		return new PlanningViewTaskPropertiesPanel(getMainWindow());
	}

	protected AbstractStrategyPropertiesPanel createStrategyPropertiesPanel() throws Exception
	{
		return new StrategyPropertiesPanel(getMainWindow());
	}

	protected AbstractIndicatorPropertiesPanel createIndicatorPropertiesPanel() throws Exception
	{
		return new IndicatorPropertiesPanelWithBudgetPanels(getMainWindow());
	}
	
	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	@Override
	public void selectSectionForTag(String tag)
	{
		currentCard.selectSectionForTag(tag);
	}
	
	@Override
	protected AbstractObjectDataInputPanel findPanel(ORef[] orefsToUse)
	{
		if(orefsToUse.length == 0)
			return blankPropertiesPanel;
		
		try
		{
			ORef firstRef = orefsToUse[DEEPEST_INDEX];
			int objectType = firstRef.getObjectType();
			if (Goal.getObjectType() == objectType)
				return getGoalPropertiesPanel();
			
			if (Objective.getObjectType() == objectType)
				return getObjectivePropertiesPanel();
			
			if (Indicator.getObjectType() == objectType)
				return getIndicatorPropertiesPanel();
			
			if (Strategy.getObjectType() == objectType)
				return getStrategyPropertiesPanel();
			
			if (Task.getObjectType() == objectType)
				return getTaskPropertiesPanel();
			
			if (Measurement.getObjectType() == objectType)
				return getMeasurementPropertiesPanel();
			
			if (Target.getObjectType() == objectType)
				return getBiodiversityTargetPropertiesPanel();
			
			if (HumanWelfareTarget.is(objectType))
				return getHumanWelfareTargetPropertiesPanel();
			
			if (Cause.getObjectType() == objectType)
				return getCausePropertiesPanel(firstRef);
			
			if (IntermediateResult.getObjectType() == objectType)
				return getIntermediateResultPropertiesPanel();
			
			if (ThreatReductionResult.getObjectType() == objectType)
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
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
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
			targetPropertiesPanel = new NonDiagramAbstractTargetPropertiesPanel(getProject(), Target.getObjectType());
			addPanel(targetPropertiesPanel);
		}
		return targetPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getHumanWelfareTargetPropertiesPanel() throws Exception
	{
		if(humanWelfareTargetPropertiesPanel == null)
		{
			humanWelfareTargetPropertiesPanel = new NonDiagramAbstractTargetPropertiesPanel(getProject(), HumanWelfareTarget.getObjectType());
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

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
			
		if (event.isSetDataCommandWithThisTypeAndTag(Target.getObjectType(), Target.TAG_VIABILITY_MODE))
			reloadSelectedRefs();
	}

	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	private static final int DEEPEST_INDEX = 0; 
	
	private GoalPropertiesPanel goalPropertiesPanel;
	private ObjectivePropertiesPanel objectivePropertiesPanel;
	private AbstractIndicatorPropertiesPanel indicatorPropertiesPanel;
	private AbstractStrategyPropertiesPanel strategyPropertiesPanel;
	private PlanningViewTaskPropertiesPanel taskPropertiesInputPanel;
	private NonDiagramAbstractTargetPropertiesPanel targetPropertiesPanel;
	private NonDiagramAbstractTargetPropertiesPanel humanWelfareTargetPropertiesPanel; 
	private PlanningViewIntermediateResultPropertiesPanel intermediateResultPropertiesPanel;
	private PlanningViewThreatReductionResultPropertiesPanel threatReductionResultPropertiesPanel;
	private PlanningViewDirectThreatPropertiesPanel threatPropertiesPanel;
	private PlanningViewContributingFactorPropertiesPanel contributingFactorPropertiesPanel;
	private MeasurementPropertiesPanel measurementPropertiesPanel;
	private ResultsChainPropertiesPanel resultsChainPropertiesPanel;
	private ConceptualModelPropertiesPanel conceptualModelPropertiesPanel;
	private ResourcePropertiesPanel projectResourcePropertiesPanel;
	private FundingSourcePropertiesPanel fundingSourcePropertiesPanel;
	private AccountingCodePropertiesPanel accountingCodePropertiesPanel;
	private BudgetCategoryOnePropertiesPanel categoryOnePropertiesPanel;
	private BudgetCategoryTwoPropertiesPanel categoryTwoPropertiesPanel;
	private SubTargetPropertiesPanel subTargetPropertiesPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
}
