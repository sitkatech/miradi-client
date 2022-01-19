/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.diagram;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.*;
import org.miradi.dialogs.assignment.AssignmentsPropertiesSubPanel;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.output.StrategyOutputSubPanel;
import org.miradi.dialogs.progressReport.ProgressReportSubPanel;
import org.miradi.dialogs.resultReport.ResultReportSubPanel;
import org.miradi.icons.*;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;

import javax.swing.*;

public class FactorSummaryPanel extends ObjectDataInputPanelWithSections
{
	public FactorSummaryPanel(MainWindow mainWindowToUse, DiagramFactor diagramFactorToEdit) throws Exception
	{
		super(mainWindowToUse.getProject(), DiagramFactorSchema.getObjectType());
		
		mainWindow = mainWindowToUse;
		currentDiagramFactor = diagramFactorToEdit;
		corePanel = new FactorSummaryCorePanel(getProject(), getActions(), getCurrentDiagramFactor());
		addSubPanelWithTitledBorder(corePanel);

		if (AbstractTarget.isAbstractTarget(getFactor()))
			addSubPanelWithTitledBorder(new TargetStatusPanel(getProject(), getFactor(), getCurrentDiagramFactor().getWrappedFactor().getSchema()));

		if(!getFactor().isAnalyticalQuestion())
			addSubPanelWithTitledBorder(new FactorSummaryCommentsPanel(getProject(), getCurrentDiagramFactor().getWrappedFactor().getSchema()));

		if(getFactor().isStrategy())
			addSubPanelWithTitledBorder(new ProgressReportSubPanel(getMainWindow()));

		if (canHaveResultReportSideTab())
			addSubPanelWithTitledBorder(new ResultReportSubPanel(getMainWindow()));

		if (canHaveWorkPlanSideTab())
		{
			addSubPanelWithTitledBorder(new TimeframePropertiesSubPanel(getProject(), getCurrentDiagramFactor().getWrappedORef()));
			addSubPanelWithTitledBorder(new AssignmentsPropertiesSubPanel(getMainWindow(), getObjectTypeForWorkPlanSideTab(), getPicker()));
		}

		if(getFactor().isStrategy())
			addSubPanelWithTitledBorder(new StrategyOutputSubPanel(getMainWindow()));

		detailIcon = createIcon();
		
		setObjectRefs(new ORef[] {getCurrentDiagramFactor().getWrappedORef(), getCurrentDiagramFactor().getRef(),});
		updateFieldsFromProject();
	}

	private int getObjectTypeForWorkPlanSideTab() throws Exception
	{
		if (Strategy.is(getCurrentDiagramFactor().getWrappedORef()))
			return StrategySchema.getObjectType();
			
		if (Task.is(getCurrentDiagramFactor().getWrappedORef()))
			return TaskSchema.getObjectType();

		throw new Exception("getObjectTypeForWorkPlanSideTab called for non work plan diagram factor : " + getCurrentDiagramFactor().toString());
	}

	private boolean canHaveWorkPlanSideTab()
	{
		if (Strategy.is(getCurrentDiagramFactor().getWrappedORef()))
			return true;

		if (Task.is(getCurrentDiagramFactor().getWrappedORef()))
			return true;

		return false;
	}

	private boolean canHaveResultReportSideTab()
	{
		if (IntermediateResult.is(getCurrentDiagramFactor().getWrappedORef()))
			return true;

		if (ThreatReductionResult.is(getCurrentDiagramFactor().getWrappedORef()))
			return true;

		if (BiophysicalResult.is(getCurrentDiagramFactor().getWrappedORef()))
			return true;

		return false;
	}

	@Override
	public void setFocusOnFirstField()
	{
		corePanel.setFocusOnFirstField();
	}
	
	private Icon createIcon()
	{
		Factor factor = getFactor();
		
		if(factor.isDirectThreat())
			return new DirectThreatIcon();
		if(factor.isContributingFactor())
			return new ContributingFactorIcon();
		if(factor.isTarget())
			return new TargetIcon();
		if(factor.isHumanWelfareTarget())
			return new HumanWelfareTargetIcon();
		if(factor.isIntermediateResult())
			return new IntermediateResultIcon();
		if(factor.isThreatReductionResult())
			return new ThreatReductionResultIcon();
		if(factor.isStrategy())
		{
			if( ((Strategy)factor).isStatusDraft())
				return new DraftStrategyIcon();
			
			return IconManager.getStrategyIcon();
		}

		return null;
	}

	public Factor getFactor()
	{
		return (Factor) getProject().findObject(getCurrentDiagramFactor().getWrappedORef());
	}

	@Override
	public Icon getIcon()
	{
		return detailIcon;
	}
	
	public DiagramFactor getCurrentDiagramFactor()
	{
		return currentDiagramFactor;
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}
	
	@Override
	public Class getJumpActionClass()
	{
		Factor factor = getFactor();
		if (factor.isContributingFactor())
			return ActionJumpDiagramWizardIdentifyIndirectThreatStep.class;
		else if (factor.isDirectThreat())
			return ActionJumpDiagramWizardIdentifyDirectThreatStep.class;
		else if (factor.isStrategy())
			return ActionJumpDevelopDraftStrategiesStep.class;
		else if (factor.isTarget())
			return ActionJumpDiagramWizardDefineTargetsStep.class;
		else if (factor.isHumanWelfareTarget())
			return ActionJumpDiagramWizardHumanWelfareTargetsStep.class;
		else if (factor.isIntermediateResult())
			return ActionJumpDiagramWizardResultsChainSelectStrategyStep.class;
		else if (factor.isThreatReductionResult())
			return ActionJumpDiagramWizardResultsChainSelectStrategyStep.class;
		return ActionJumpDiagramOverviewStep.class;
	}
	
	private Actions getActions()
	{
		return mainWindow.getActions();
	}
	
	private MainWindow mainWindow;
	private Icon detailIcon;
	private DiagramFactor currentDiagramFactor;
	private FactorSummaryCorePanel corePanel;
}
