/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StrategyClassificationQuestion;
import org.conservationmeasures.eam.questions.StrategyCostQuestion;
import org.conservationmeasures.eam.questions.StrategyDurationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummary;
import org.conservationmeasures.eam.questions.TargetStatusQuestion;
import org.conservationmeasures.eam.questions.ThreatClassificationQuestion;
import org.martus.swing.UiCheckBox;
import org.martus.swing.UiLabel;

public class FactorDetailsPanel extends ObjectDataInputPanel
{
	public FactorDetailsPanel(Project projectToUse, DiagramFactor factorToEdit)
	{
		super(projectToUse, factorToEdit.getType(), factorToEdit.getWrappedId());
		currentDiagramFactor = factorToEdit;
		
		statusCheckBox = new UiCheckBox(EAM.text("Label|Draft"));

		if(factorToEdit.isTarget())
		{
			addField(createChoiceField(new TargetStatusQuestion(Target.TAG_TARGET_STATUS)));
			detailIcon = new TargetIcon();
		}
		
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
			addOptionalDraftStatusCheckBox(factorToEdit);
			addField(createClassificationChoiceField(new StrategyClassificationQuestion(Cause.TAG_TAXONOMY_CODE)));
			addField(createChoiceField(new StrategyImpactQuestion(Strategy.TAG_IMPACT_RATING)));
			addField(createChoiceField(new StrategyDurationQuestion(Strategy.TAG_DURATION_RATING)));
			addField(createChoiceField(new StrategyFeasibilityQuestion(Strategy.TAG_FEASIBILITY_RATING)));
			addField(createChoiceField(new StrategyCostQuestion(Strategy.TAG_COST_RATING)));
			addField(createReadOnlyChoiceField(new StrategyRatingSummary(Strategy.PSEUDO_TAG_RATING_SUMMARY)));
			detailIcon = new StrategyIcon();
		}

		addField(createMultilineField(Factor.TAG_COMMENT));
		
		updateFieldsFromProject();
	}


	private void addOptionalDraftStatusCheckBox(DiagramFactor factorToEdit)
	{
		// FIXME: Convert to new mechanism (create status field or just checkbox field?)
		if (inChainMode())
		{
			add(new UiLabel(EAM.text("Label|Status")));
			statusCheckBox.setSelected(factorToEdit.isStatusDraft());
			statusCheckBox.addItemListener(new StatusChangeHandler());
			add(statusCheckBox);
		}
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
				getProject().executeCommand(buildStatusCommand());
				getProject().updateVisibilityOfSingleFactor(getCurrentDiagramFactor());
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
	}
	
	Command buildStatusCommand()
	{
		String newValue = Strategy.STATUS_REAL;
		if(statusCheckBox.isSelected())
			newValue = Strategy.STATUS_DRAFT;

		return new CommandSetObjectData(ObjectType.FACTOR, getCurrentFactorId(), 
				Strategy.TAG_STATUS, newValue);
	}

	
	DiagramFactor getCurrentDiagramFactor()
	{
		return currentDiagramFactor;
	}
	
	FactorId getCurrentFactorId()
	{
		return getCurrentDiagramFactor().getWrappedId();
	}

	public String getPanelDescription()
	{
		return EAM.text("Details");
	}
	
	private Icon detailIcon;
	private DiagramFactor currentDiagramFactor;
	private UiCheckBox statusCheckBox;
}
