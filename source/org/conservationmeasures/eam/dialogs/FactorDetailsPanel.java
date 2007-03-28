/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JCheckBox;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.StrategyClassificationQuestion;
import org.conservationmeasures.eam.questions.StrategyCostQuestion;
import org.conservationmeasures.eam.questions.StrategyDurationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;
import org.conservationmeasures.eam.questions.ThreatClassificationQuestion;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;
import org.martus.swing.UiLabel;

public class FactorDetailsPanel extends ObjectDataInputPanel
{
	public FactorDetailsPanel(Project projectToUse, FactorCell factorToEdit) throws Exception
	{
		super(projectToUse, factorToEdit.getType(), factorToEdit.getWrappedId());
		currentDiagramFactor = factorToEdit;

		if(factorToEdit.isDirectThreat())
		{
			addField(createClassificationChoiceField(new ThreatClassificationQuestion(Cause.TAG_TAXONOMY_CODE)));
			detailIcon = new DirectThreatIcon();
		}
		
		if(factorToEdit.isContributingFactor())
		{
			detailIcon = new ContributingFactorIcon();
		}

		if(factorToEdit.isStrategy())
		{
			addField(createStringField(Strategy.TAG_SHORT_LABEL));
			addOptionalDraftStatusCheckBox(Strategy.TAG_STATUS);
			addField(createClassificationChoiceField(new StrategyClassificationQuestion(Cause.TAG_TAXONOMY_CODE)));
			addField(createRatingChoiceField(new StrategyImpactQuestion(Strategy.TAG_IMPACT_RATING)));
			addField(createRatingChoiceField(new StrategyDurationQuestion(Strategy.TAG_DURATION_RATING)));
			addField(createRatingChoiceField(new StrategyFeasibilityQuestion(Strategy.TAG_FEASIBILITY_RATING)));
			addField(createRatingChoiceField(new StrategyCostQuestion(Strategy.TAG_COST_RATING)));
			addField(createReadOnlyChoiceField(new StrategyRatingSummaryQuestion(Strategy.PSEUDO_TAG_RATING_SUMMARY)));
			detailIcon = new StrategyIcon();
		}
		
		
		addField(createMultilineField(Factor.TAG_COMMENT));
		
		
		if(factorToEdit.isTarget())
		{
			addField(createChoiceField(ObjectType.FACTOR, new ViabilityModeQuestion(Target.TAG_VIABILITY_MODE)));
			 targetRatingField = createRatingChoiceField(new StatusQuestion(Target.TAG_TARGET_STATUS));
			addField(targetRatingField);
			addLine(new UiLabel(""), new UiLabel(EAM.text("This 'Status of Target' field is only available in Basic mode")));
			detailIcon = new TargetIcon();
			updateEditabilityOfTargetStatusField();
		}
		
		updateFieldsFromProject();

		if(factorToEdit.isTarget())
			updateEditabilityOfTargetStatusField();
	}


	private void addOptionalDraftStatusCheckBox(String tag)
	{
		if (!inChainMode())
			return;
		
		ObjectDataInputField field = createCheckBoxField(tag, Strategy.STATUS_DRAFT, Strategy.STATUS_REAL);
		((JCheckBox)field.getComponent()).addItemListener(new StatusChangeHandler());
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
		if(getFactor().isTarget() && event.isSetDataCommandWithThisTypeAndTag(ObjectType.FACTOR, Target.TAG_VIABILITY_MODE))
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
		targetRatingField.getComponent().setEnabled(enableRatingField);
	}

	public Factor getFactor()
	{
		return currentDiagramFactor.getUnderlyingObject();
	}

	public Icon getIcon()
	{
		return detailIcon;
	}

	class StatusChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			try
			{
				getProject().updateVisibilityOfSingleFactor(getCurrentDiagramFactorId());
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
	}
	
	public DiagramFactorId getCurrentDiagramFactorId()
	{
		return currentDiagramFactor.getDiagramFactorId();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Details");
	}
	
	private Icon detailIcon;
	private FactorCell currentDiagramFactor;
	private ObjectDataInputField targetRatingField;
}
