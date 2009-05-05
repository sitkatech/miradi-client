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
package org.miradi.dialogs.diagram;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpDevelopDraftStrategiesStep;
import org.miradi.actions.jump.ActionJumpDiagramOverviewStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardResultsChainSelectStrategyStep;
import org.miradi.dialogs.assignment.AssignmentsPropertiesPanel;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.DraftStrategyIcon;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;

public class FactorSummaryPanel extends ObjectDataInputPanelWithSections
{
	public FactorSummaryPanel(MainWindow mainWindowToUse, DiagramFactor diagramFactorToEdit) throws Exception
	{
		super(mainWindowToUse.getProject(), diagramFactorToEdit.getObjectType());
		
		mainWindow = mainWindowToUse;
		currentDiagramFactor = diagramFactorToEdit;
		
		corePanel = new FactorSummaryCorePanel(getProject(), getActions(), diagramFactorToEdit);
		addSubPanelWithTitledBorder(corePanel);
		if(getFactor().isStrategy())
		{
			addSubPanelWithTitledBorder(new ForecastSubPanel(mainWindowToUse, diagramFactorToEdit.getWrappedORef()));
			addSubPanelWithTitledBorder(new AssignmentsPropertiesPanel(getMainWindow(), Task.getObjectType(), getPicker()));
			//FIXME enabled onece the expense properties side tab is working
			//addSubPanelWithTitledBorder(new ExpensesPropertiesPanel(getMainWindow(), Task.getObjectType(), getPicker()));
		}
		
		addSubPanelWithTitledBorder(new FactorSummaryCommentsPanel(getProject(), getActions(), diagramFactorToEdit.getWrappedType()));
		
		detailIcon = createIcon();
		
		setObjectRefs(new ORef[] {diagramFactorToEdit.getWrappedORef(), diagramFactorToEdit.getRef(),});
		updateFieldsFromProject();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		corePanel.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		corePanel.becomeInactive();
		super.becomeInactive();
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
		if(factor.isIntermediateResult())
			return new IntermediateResultIcon();
		if(factor.isThreatReductionResult())
			return new ThreatReductionResultIcon();
		if(factor.isStrategy())
		{
			if( ((Strategy)factor).isStatusDraft())
				return new DraftStrategyIcon();
			return new StrategyIcon();
		}

		return null;
	}


	public Factor getFactor()
	{
		return (Factor) getProject().findObject(currentDiagramFactor.getWrappedORef());
	}

	public Icon getIcon()
	{
		return detailIcon;
	}
	
	public DiagramFactor getCurrentDiagramFactor()
	{
		return currentDiagramFactor;
	}

	public DiagramFactorId getCurrentDiagramFactorId()
	{
		return currentDiagramFactor.getDiagramFactorId();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}
	
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
