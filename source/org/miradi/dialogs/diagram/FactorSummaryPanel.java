/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import javax.swing.Icon;

import org.miradi.actions.ActionEditStrategyProgressReports;
import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpDevelopDraftStrategiesStep;
import org.miradi.actions.jump.ActionJumpDiagramOverviewStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardResultsChainStep;
import org.miradi.diagram.factortypes.FactorType;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.ViewData;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.HabitatAssociationQuestion;
import org.miradi.questions.StrategyClassificationQuestion;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.ThreatClassificationQuestion;
import org.miradi.utils.ObjectsActionButton;

public class FactorSummaryPanel extends ObjectDataInputPanel
{
	public FactorSummaryPanel(MainWindow mainWindowToUse, DiagramFactor factorToEdit) throws Exception
	{
		super(mainWindowToUse.getProject(), factorToEdit.getWrappedORef());
		
		mainWindow = mainWindowToUse;
		currentDiagramFactor = factorToEdit;
		setObjectRefs(new ORef[] {factorToEdit.getWrappedORef(), factorToEdit.getRef(),});
		
		
		ObjectDataInputField shortLabelField = createShortStringField(Factor.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Factor.TAG_LABEL);
		
		//TODO extract a local factor var. instead of getFactor
		addFieldsOnOneLine(FactorType.getFactorTypeLabel(getFactor()), FactorType.getFactorIcon(getFactor()), new ObjectDataInputField[]{shortLabelField, labelField});
		addField(createMultilineField(Factor.TAG_TEXT));

		ObjectDataInputField fontField = createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		ObjectDataInputField colorField = createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		ObjectDataInputField styleField = createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion());
		addFieldsOnOneLine(EAM.text("Font"), new ObjectDataInputField[]{fontField, colorField, styleField});
		

		if (getFactor().isDirectThreat())
		{
			addField(createClassificationChoiceField(Cause.TAG_TAXONOMY_CODE, new ThreatClassificationQuestion()));
			detailIcon = new DirectThreatIcon();
		}
		
		if(getFactor().isContributingFactor())
		{
			detailIcon = new ContributingFactorIcon();
		}

		if(getFactor().isStrategy())
		{
			
			addOptionalDraftStatusCheckBox(Strategy.TAG_STATUS);
			addField(createClassificationChoiceField(Strategy.TAG_TAXONOMY_CODE, new StrategyClassificationQuestion()));

			ObjectDataInputField impactField = createRatingChoiceField(Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion());
			ObjectDataInputField feasibilityField = createRatingChoiceField(Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion());
			ObjectDataInputField prioritySummaryField = createReadOnlyChoiceField(Strategy.PSEUDO_TAG_RATING_SUMMARY, new StrategyRatingSummaryQuestion());
			addFieldsOnOneLine(EAM.text("Priority"), new ObjectDataInputField[] {impactField, feasibilityField, prioritySummaryField});
			 
			ObjectsActionButton editProgressReportButton = createObjectsActionButton(getActions().getObjectsAction(ActionEditStrategyProgressReports.class), getPicker());
			ObjectDataInputField readOnlyProgressReportsList = createReadOnlyObjectList(Strategy.getObjectType(), Strategy.TAG_PROGRESS_REPORT_REFS);
			addFieldWithEditButton(EAM.text("Progress Reports"), readOnlyProgressReportsList, editProgressReportButton);
			
			detailIcon = new StrategyIcon();
		}

		if(getFactor().isTarget())
		{
			addField(createStringField(Target.TAG_SPECIES_LATIN_NAME));
			addField(createCodeListField(Target.getObjectType(), Target.TAG_HABITAT_ASSOCIATION, new HabitatAssociationQuestion(), 1));
			
			detailIcon = new TargetIcon();
		}
				
		addField(createReadOnlyObjectList(getFactor().getType(), Factor.PSEUDO_TAG_DIAGRAM_REFS));
		addField(createMultilineField(Factor.TAG_COMMENT));		
	
		
		updateFieldsFromProject();
	}


	private void addOptionalDraftStatusCheckBox(String tag)
	{
		if (!inChainMode())
			return;
		
		ObjectDataInputField field = createCheckBoxField(tag, Strategy.STATUS_DRAFT, Strategy.STATUS_REAL);
		addField(field);
	}


	private boolean inChainMode()
	{
		try
		{
			String mode = getProject().getCurrentViewData().getData(ViewData.TAG_CURRENT_MODE);
			return (mode.equals(ViewData.MODE_STRATEGY_BRAINSTORM));
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return false;
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
