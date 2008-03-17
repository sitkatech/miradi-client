/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpDevelopDraftStrategiesStep;
import org.miradi.actions.jump.ActionJumpDiagramOverviewStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardResultsChainStep;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.DraftStrategyIcon;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.ids.DiagramFactorId;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;

public class FactorSummaryPanel extends ObjectDataInputPanel
{
	public FactorSummaryPanel(MainWindow mainWindowToUse, DiagramFactor diagramFactorToEdit) throws Exception
	{
		super(mainWindowToUse.getProject(), diagramFactorToEdit.getWrappedORef());
		setLayout(new OneColumnGridLayout());
		
		mainWindow = mainWindowToUse;
		currentDiagramFactor = diagramFactorToEdit;
		setObjectRefs(new ORef[] {diagramFactorToEdit.getWrappedORef(), diagramFactorToEdit.getRef(),});
		
		addSubPanelWithTitledBorder(new FactorSummaryCorePanel(getProject(), getActions(), diagramFactorToEdit));
		if(getFactor().isStrategy())
			addSubPanelWithTitledBorder(new ForecastSubPanel(mainWindowToUse, diagramFactorToEdit.getWrappedORef()));
		addSubPanelWithTitledBorder(new FactorSummaryCommentsPanel(getProject(), getActions(), diagramFactorToEdit.getWrappedType()));
		
		detailIcon = createIcon();
		
		setObjectRefs(new ORef[] {diagramFactorToEdit.getWrappedORef(), diagramFactorToEdit.getRef(),});
		updateFieldsFromProject();
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
			return ActionJumpDiagramWizardResultsChainStep.class;
		else if (factor.isThreatReductionResult())
			return ActionJumpDiagramWizardResultsChainStep.class;
		return ActionJumpDiagramOverviewStep.class;
	}
	
	private Actions getActions()
	{
		return mainWindow.getActions();
	}
	
	private MainWindow mainWindow;
	private Icon detailIcon;
	private DiagramFactor currentDiagramFactor;
}
