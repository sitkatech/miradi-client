/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardResultsChainStep;
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
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.DiagramFactorFontColorQuestion;
import org.conservationmeasures.eam.questions.DiagramFactorFontSizeQuestion;
import org.conservationmeasures.eam.questions.DiagramFactorFontStyleQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.StrategyClassificationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;
import org.conservationmeasures.eam.questions.ThreatClassificationQuestion;
import org.martus.swing.UiLabel;

public class FactorSummaryPanel extends ObjectDataInputPanel
{
	public FactorSummaryPanel(Project projectToUse, DiagramFactor factorToEdit) throws Exception
	{
		super(projectToUse, factorToEdit.getWrappedORef());
		currentDiagramFactor = factorToEdit;

		setObjectRefs(new ORef[] {factorToEdit.getWrappedORef(), factorToEdit.getRef()});
		addField(createShortStringField(Factor.TAG_SHORT_LABEL));
		addField(createStringField(Factor.TAG_TEXT));
		
		if (getFactor().isDirectThreat())
		{
			addField(createClassificationChoiceField(new ThreatClassificationQuestion(Cause.TAG_TAXONOMY_CODE)));
			detailIcon = new DirectThreatIcon();
		}
		
		if(getFactor().isContributingFactor())
		{
			detailIcon = new ContributingFactorIcon();
		}

		if(getFactor().isStrategy())
		{
			
			addOptionalDraftStatusCheckBox(Strategy.TAG_STATUS);
			addField(createClassificationChoiceField(new StrategyClassificationQuestion(Strategy.TAG_TAXONOMY_CODE)));
			addField(createRatingChoiceField(new StrategyImpactQuestion(Strategy.TAG_IMPACT_RATING)));
			addField(createRatingChoiceField(new StrategyFeasibilityQuestion(Strategy.TAG_FEASIBILITY_RATING)));
			addField(createReadOnlyChoiceField(new StrategyRatingSummaryQuestion(Strategy.PSEUDO_TAG_RATING_SUMMARY)));
			detailIcon = new StrategyIcon();
		}

		addField(createChoiceField(DiagramFactor.getObjectType(), new DiagramFactorFontSizeQuestion(DiagramFactor.TAG_FONT_SIZE)));
		addField(createChoiceField(DiagramFactor.getObjectType(), new DiagramFactorFontColorQuestion(DiagramFactor.TAG_FOREGROUND_COLOR)));
		addField(createChoiceField(DiagramFactor.getObjectType(), new DiagramFactorFontStyleQuestion(DiagramFactor.TAG_FONT_STYLE)));
		addField(createMultilineField(Factor.TAG_COMMENT));		
		
		
		if(getFactor().isTarget())
		{
			addField(createStringField(Target.TAG_SPECIES_LATIN_NAME));
			targetRatingField = createRatingChoiceField(new StatusQuestion(Target.TAG_TARGET_STATUS));
			ratingFieldLabel = new PanelFieldLabel(targetRatingField.getObjectType(), targetRatingField.getTag());
			addFieldWithCustomLabel(targetRatingField, ratingFieldLabel);
			
			justificationField = createStringField(Target.TAG_CURRENT_STATUS_JUSTIFICATION);
			justificationFieldLabel = new PanelFieldLabel(justificationField.getObjectType(), justificationField.getTag());
			addFieldWithCustomLabel(justificationField, justificationFieldLabel);
			
			detailIcon = new TargetIcon();
			updateEditabilityOfTargetStatusField();
		}
		
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
	
	private Icon detailIcon;
	private DiagramFactor currentDiagramFactor;
	private UiLabel ratingFieldLabel;
	private ObjectDataInputField targetRatingField;
	private UiLabel justificationFieldLabel;
	private ObjectDataInputField justificationField;
}
