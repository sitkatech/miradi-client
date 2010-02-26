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
import org.miradi.dialogs.diagram.AbstractStrategyPropertiesPanel;
import org.miradi.dialogs.diagram.ConceptualModelPropertiesPanel;
import org.miradi.dialogs.diagram.ResultsChainPropertiesPanel;
import org.miradi.dialogs.diagram.StrategyPropertiesPanel;
import org.miradi.dialogs.fundingsource.FundingSourcePropertiesPanel;
import org.miradi.dialogs.goal.GoalPropertiesPanel;
import org.miradi.dialogs.objective.ObjectivePropertiesPanel;
import org.miradi.dialogs.planning.MeasurementPropertiesPanel;
import org.miradi.dialogs.resource.ResourcePropertiesPanel;
import org.miradi.dialogs.viability.AbstractIndicatorPropertiesPanel;
import org.miradi.dialogs.viability.IndicatorPropertiesPanelWithBudgetPanels;
import org.miradi.dialogs.viability.NonDiagramAbstractTargetPropertiesPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AccountingCode;
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
		goalPropertiesPanel = new GoalPropertiesPanel(getProject(), getMainWindow().getActions());
		objectivePropertiesPanel = new ObjectivePropertiesPanel(getProject(), getMainWindow().getActions());
		indicatorPropertiesPanel = createIndicatorPropertiesPanel();
		strategyPropertiesPanel = createStrategyPropertiesPanel();
		taskPropertiesInputPanel = createTaskPropertiesPanel();
		measurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
		targetPropertiesPanel = new NonDiagramAbstractTargetPropertiesPanel(getProject(), Target.getObjectType());
		humanWelfareTargetPropertiesPanel = new NonDiagramAbstractTargetPropertiesPanel(getProject(), HumanWelfareTarget.getObjectType());
		threatPropertiesPanel = new PlanningViewDirectThreatPropertiesPanel(getProject());
		contributingFactorPropertiesPanel = new PlanningViewContributingFactorPropertiesPanel(getProject());
		intermediateResultPropertiesPanel = new PlanningViewIntermediateResultPropertiesPanel(getProject());
		threatReductionResultPropertiesPanel = new PlanningViewThreatReductionResultPropertiesPanel(getProject());
		resultsChainPropertiesPanel = new ResultsChainPropertiesPanel(getProject(), ORef.INVALID);
		conceptualModelPropertiesPanel = new ConceptualModelPropertiesPanel(getProject(), ORef.INVALID);
		projectResourcePropertiesPanel = new ResourcePropertiesPanel(getProject(), BaseId.INVALID);
		fundingSourcePropertiesPanel = new FundingSourcePropertiesPanel(getProject(), BaseId.INVALID);
		accountingCodePropertiesPanel = new AccountingCodePropertiesPanel(getProject(), BaseId.INVALID);
		
		blankPropertiesPanel = new BlankPropertiesPanel(getProject());
		
		addPanel(goalPropertiesPanel);
		addPanel(objectivePropertiesPanel);
		addPanel(indicatorPropertiesPanel);
		addPanel(strategyPropertiesPanel);
		addPanel(taskPropertiesInputPanel);
		addPanel(measurementPropertiesPanel);
		addPanel(targetPropertiesPanel);
		addPanel(humanWelfareTargetPropertiesPanel);
		addPanel(threatPropertiesPanel);
		addPanel(contributingFactorPropertiesPanel);
		addPanel(intermediateResultPropertiesPanel);
		addPanel(threatReductionResultPropertiesPanel);
		addPanel(resultsChainPropertiesPanel);
		addPanel(conceptualModelPropertiesPanel);
		addPanel(projectResourcePropertiesPanel);
		addPanel(fundingSourcePropertiesPanel);
		addPanel(accountingCodePropertiesPanel);
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
		
		ORef firstRef = orefsToUse[DEEPEST_INDEX];
		int objectType = firstRef.getObjectType();
		if (Goal.getObjectType() == objectType)
			return goalPropertiesPanel;
		
		if (Objective.getObjectType() == objectType)
			return objectivePropertiesPanel;
		
		if (Indicator.getObjectType() == objectType)
			return indicatorPropertiesPanel;
		
		if (Strategy.getObjectType() == objectType)
			return strategyPropertiesPanel;
		
		if (Task.getObjectType() == objectType)
			return taskPropertiesInputPanel;
		
		if (Measurement.getObjectType() == objectType)
			return measurementPropertiesPanel;
		
		if (Target.getObjectType() == objectType)
			return targetPropertiesPanel;
		
		if (HumanWelfareTarget.is(objectType))
			return humanWelfareTargetPropertiesPanel;
		
		if (Cause.getObjectType() == objectType)
			return getCausePropertiesPanel(firstRef);
		
		if (IntermediateResult.getObjectType() == objectType)
			return intermediateResultPropertiesPanel;
		
		if (ThreatReductionResult.getObjectType() == objectType)
			return threatReductionResultPropertiesPanel;
	
		if (ResultsChainDiagram.is(objectType))
			return resultsChainPropertiesPanel;
		
		if (ConceptualModelDiagram.is(objectType))
			return conceptualModelPropertiesPanel;
		
		if (ProjectResource.is(objectType))
			return projectResourcePropertiesPanel;
		
		if (FundingSource.is(objectType))
			return fundingSourcePropertiesPanel;
		
		if (AccountingCode.is(objectType))
			return accountingCodePropertiesPanel;
		
		return blankPropertiesPanel;
	}

	private MinimalFactorPropertiesPanel getCausePropertiesPanel(ORef causeRef)
	{
		Cause cause = Cause.find(getProject(), causeRef);
		if (cause.isDirectThreat())
			return threatPropertiesPanel;
		
		return contributingFactorPropertiesPanel;
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
	private BlankPropertiesPanel blankPropertiesPanel;
}
