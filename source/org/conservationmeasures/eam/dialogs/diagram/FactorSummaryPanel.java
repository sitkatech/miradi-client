/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.ActionEditStrategyProgressReports;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardResultsChainStep;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelFieldLabel;
import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.questions.DiagramFactorFontColorQuestion;
import org.conservationmeasures.eam.questions.DiagramFactorFontSizeQuestion;
import org.conservationmeasures.eam.questions.DiagramFactorFontStyleQuestion;
import org.conservationmeasures.eam.questions.HabitatAssociationQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.StrategyClassificationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;
import org.conservationmeasures.eam.questions.ThreatClassificationQuestion;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.martus.swing.UiLabel;

public class FactorSummaryPanel extends ObjectDataInputPanel
{
	public FactorSummaryPanel(MainWindow mainWindowToUse, DiagramFactor factorToEdit) throws Exception
	{
		super(mainWindowToUse.getProject(), factorToEdit.getWrappedORef());
		
		mainWindow = mainWindowToUse;
		currentDiagramFactor = factorToEdit;
		
		ObjectDataInputField shortLabelField = createShortStringField(Factor.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createStringField(Factor.TAG_LABEL);
		
		//TODO extract a local factor var. instead of getFactor
		addFieldsOnOneLine(FactorType.getFactorTypeLabel(getFactor()), FactorType.getFactorIcon(getFactor()), new ObjectDataInputField[]{shortLabelField, labelField});

		setObjectRefs(new ORef[] {factorToEdit.getWrappedORef(), factorToEdit.getRef(),});
		
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
			addField(createRatingChoiceField(Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion()));
			addField(createRatingChoiceField(Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion()));
			addField(createReadOnlyChoiceField(Strategy.PSEUDO_TAG_RATING_SUMMARY, new StrategyRatingSummaryQuestion()));
			 
			ObjectsActionButton editProgressReportButton = createObjectsActionButton(getActions().getObjectsAction(ActionEditStrategyProgressReports.class), getPicker());
			ObjectDataInputField readOnlyProgressReportsList = createReadOnlyObjectList(Strategy.getObjectType(), Strategy.TAG_PROGRESS_REPORT_REFS);
			addFieldWithEditButton(EAM.text("Progress Reports"), readOnlyProgressReportsList, editProgressReportButton);
			
			detailIcon = new StrategyIcon();
		}

		ObjectDataInputField fontField = createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		ObjectDataInputField colorField = createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		ObjectDataInputField styleField = createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion());
		addFieldsOnOneLine(EAM.text("Font"), new ObjectDataInputField[]{fontField, colorField, styleField});
		
		if(getFactor().isTarget())
		{
			addField(createStringField(Target.TAG_SPECIES_LATIN_NAME));
			targetRatingField = createRatingChoiceField(Target.TAG_TARGET_STATUS, new StatusQuestion());
			ratingFieldLabel = new PanelFieldLabel(targetRatingField.getObjectType(), targetRatingField.getTag());
			addFieldWithCustomLabel(targetRatingField, ratingFieldLabel);
			
			justificationField = createStringField(Target.TAG_CURRENT_STATUS_JUSTIFICATION);
			justificationFieldLabel = new PanelFieldLabel(justificationField.getObjectType(), justificationField.getTag());
			addFieldWithCustomLabel(justificationField, justificationFieldLabel);
			addField(createMultiCodeField(Target.getObjectType(), Target.TAG_HABITAT_ASSOCIATION, new HabitatAssociationQuestion(), 1));
			
			detailIcon = new TargetIcon();
			updateEditabilityOfTargetStatusField();
		}
				
		addField(createMultilineField(Factor.TAG_TEXT));
		addField(createMultilineField(Factor.TAG_COMMENT));		
		
		addField(createReadOnlyObjectList(getFactor().getType(), Factor.PSEUDO_TAG_DIAGRAM_REFS));
		
		updateFieldsFromProject();

		if(getFactor().isTarget())
			updateEditabilityOfTargetStatusField();
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

	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if(getFactor().isTarget() && event.isSetDataCommandWithThisTypeAndTag(ObjectType.TARGET, Target.TAG_VIABILITY_MODE))
		{
			updateEditabilityOfTargetStatusField();
		}
	}


	private void updateEditabilityOfTargetStatusField()
	{
		Target target = (Target)getFactor();
		boolean enableRatingField = true;
		if(target.isViabilityModeTNC())
			enableRatingField = false;
		targetRatingField.getComponent().setVisible(enableRatingField);
		ratingFieldLabel.setVisible(enableRatingField);
		justificationField.getComponent().setVisible(enableRatingField);
		justificationFieldLabel.setVisible(enableRatingField);
	}

	public Factor getFactor()
	{
		return (Factor) getProject().findObject(currentDiagramFactor.getWrappedORef());
	}

	public Icon getIcon()
	{
		return detailIcon;
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
	private UiLabel ratingFieldLabel;
	private ObjectDataInputField targetRatingField;
	private UiLabel justificationFieldLabel;
	private ObjectDataInputField justificationField;
}
