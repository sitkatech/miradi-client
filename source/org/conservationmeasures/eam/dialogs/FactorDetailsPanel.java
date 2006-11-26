package org.conservationmeasures.eam.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyChoiceDialogField;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyRatingDisplayField;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TaxonomyItem;
import org.conservationmeasures.eam.objecthelpers.TaxonomyLoader;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.RatingChoice;
import org.conservationmeasures.eam.ratings.StrategyCostQuestion;
import org.conservationmeasures.eam.ratings.StrategyDurationQuestion;
import org.conservationmeasures.eam.ratings.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.ratings.StrategyImpactQuestion;
import org.conservationmeasures.eam.ratings.StrategyRatingSummary;
import org.martus.swing.UiCheckBox;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextArea;

public class FactorDetailsPanel extends ObjectDataInputPanel
{
	public FactorDetailsPanel(Project projectToUse, DiagramFactor factorToEdit)
	{
		super(projectToUse, factorToEdit.getType(), factorToEdit.getWrappedId());
		currentDiagramFactor = factorToEdit;
		
		statusCheckBox = new UiCheckBox(EAM.text("Label|Draft"));

		if(factorToEdit.isDirectThreat())
		{
			add(new UiLabel(EAM.text("Label|IUCN-CMP Classification")));
			add(createThreatClassificationDropdown());
		}

		if(factorToEdit.isStrategy())
		{
			add(new UiLabel(EAM.text("Label|Status")));
			statusCheckBox.setSelected(factorToEdit.isStatusDraft());
			statusCheckBox.addItemListener(new StatusChangeHandler());
			add(statusCheckBox);

			add(new UiLabel(EAM.text("Label|IUCN-CMP Classification")));
			add(createStrategyClassificationDropdown());
			
			String impactTag = Strategy.TAG_IMPACT_RATING;
			StrategyImpactQuestion impactQuestion = new StrategyImpactQuestion(impactTag);
			add(new UiLabel(impactQuestion.getLabel()));
			LegacyChoiceDialogField impactField = new LegacyChoiceDialogField(impactQuestion);
			impactComponent = (UiComboBox)impactField.getComponent();
			add(impactComponent);
			impactField.selectCode(factorToEdit.getUnderlyingObject().getData(impactTag));
			impactComponent.addItemListener(new ImpactChangeHandler());
			
			String durationTag = Strategy.TAG_DURATION_RATING;
			StrategyDurationQuestion durationQuestion = new StrategyDurationQuestion(durationTag);
			add(new UiLabel(durationQuestion.getLabel()));
			LegacyChoiceDialogField durationField = new LegacyChoiceDialogField(durationQuestion);
			durationComponent = (UiComboBox)durationField.getComponent();
			add(durationComponent);
			durationField.selectCode(factorToEdit.getUnderlyingObject().getData(durationTag));
			durationComponent.addItemListener(new DurationChangeHandler());
			
			String feasibilityTag = Strategy.TAG_FEASIBILITY_RATING;
			StrategyFeasibilityQuestion feasibilityQuestion = new StrategyFeasibilityQuestion(feasibilityTag);
			add(new UiLabel(feasibilityQuestion.getLabel()));
			LegacyChoiceDialogField feasibilityField = new LegacyChoiceDialogField(feasibilityQuestion);
			feasibilityComponent = (UiComboBox)feasibilityField.getComponent();
			add(feasibilityComponent);
			feasibilityField.selectCode(factorToEdit.getUnderlyingObject().getData(feasibilityTag));
			feasibilityComponent.addItemListener(new FeasibilityChangeHandler());
			
			String costTag = Strategy.TAG_COST_RATING;
			StrategyCostQuestion costQuestion = new StrategyCostQuestion(costTag);
			add(new UiLabel(costQuestion.getLabel()));
			LegacyChoiceDialogField costField = new LegacyChoiceDialogField(costQuestion);
			costComponent = (UiComboBox)costField.getComponent();
			add(costComponent);
			costField.selectCode(factorToEdit.getUnderlyingObject().getData(costTag));
			costComponent.addItemListener(new CostChangeHandler());

			add(new UiLabel(EAM.text("Label|Rating")));
			LegacyRatingDisplayField ratingSummaryField = new LegacyRatingDisplayField(new StrategyRatingSummary(""));
			ratingComponent = (UiLabel)ratingSummaryField.getComponent();
			add(ratingComponent);
			updateRating();
		}

		add(new UiLabel(EAM.text("Label|Comments")));
		add(createComment(factorToEdit.getComment()));
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

	JComponent createThreatClassificationDropdown()
	{
		String[] choices = { "error processing classifications", };
		try
		{
			TaxonomyItem[] taxonomyItems = TaxonomyLoader
					.load("ThreatTaxonomies.txt");
			dropdownThreatClassification = new UiComboBox(taxonomyItems);

			String taxonomyCode = getCurrentDiagramFactor().getUnderlyingObject()
					.getData(Cause.TAG_TAXONOMY_CODE);

			TaxonomyItem foundTaxonomyItem = SearchTaxonomyClassificationsForCode(
					taxonomyItems, taxonomyCode);

			if(foundTaxonomyItem == null)
			{
				String errorMessage = "Threat not found in table ; please make another selection";
				EAM.errorDialog(EAM.text(errorMessage));
				foundTaxonomyItem = taxonomyItems[0];
			}

			dropdownThreatClassification.setSelectedItem(foundTaxonomyItem);

			dropdownThreatClassification
					.addActionListener(new ThreatClassificationChangeHandler());

			return dropdownThreatClassification;

		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new UiComboBox(choices);
		}
	}

	JComponent createStrategyClassificationDropdown()
	{
		String[] choices = { "error processing classifications", };
		try
		{
			TaxonomyItem[] taxonomyItems = TaxonomyLoader
					.load("StrategyTaxonomies.txt");
			dropdownStrategyClassification = new UiComboBox(taxonomyItems);
			
			String taxonomyCode = getCurrentDiagramFactor().getUnderlyingObject()
			.getData(Cause.TAG_TAXONOMY_CODE);

			TaxonomyItem foundTaxonomyItem = SearchTaxonomyClassificationsForCode(
					taxonomyItems, taxonomyCode);

			if(foundTaxonomyItem == null)
			{
				String errorMessage = "Strategy not found in table ; please make another selection";
				EAM.errorDialog(EAM.text(errorMessage));
				foundTaxonomyItem = taxonomyItems[0];
			}

			dropdownStrategyClassification.setSelectedItem(foundTaxonomyItem);

			dropdownStrategyClassification
				.addActionListener(new StrategyClassificationChangeHandler());

			return dropdownStrategyClassification;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new UiComboBox(choices);
		}

	}
	
	TaxonomyItem SearchTaxonomyClassificationsForCode(
			TaxonomyItem[] taxonomyItems, String taxonomyCode)
	{
		for(int i = 0; i < taxonomyItems.length; i++)
		{
			if(taxonomyItems[i].getTaxonomyCode().equals(taxonomyCode))
				return taxonomyItems[i];
		}
		return null;
	}

	class ThreatClassificationChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			TaxonomyItem taxonomyItem = getThreatTaxonomyItem();
			actionSaveTaxonomySelection(dropdownThreatClassification, taxonomyItem);
		}

	}
	
	class StrategyClassificationChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			TaxonomyItem taxonomyItem = getStrategyTaxonomyItem();
			actionSaveTaxonomySelection(dropdownStrategyClassification, taxonomyItem);
		}
	}
	
	private void actionSaveTaxonomySelection(UiComboBox thisComboBox, TaxonomyItem taxonomyItem)
	{
		try
		{
			int type = ObjectType.MODEL_NODE;
			// TODO: This looks wrong...could be called for Strategy or DirectThreat
			String tag = Cause.TAG_TAXONOMY_CODE;
		
			if(taxonomyItem != null)
			{
				String taxonomyCode = taxonomyItem.getTaxonomyCode();
				CommandSetObjectData cmd = new CommandSetObjectData(type,
						getCurrentFactorId(), tag, taxonomyCode);
				getProject().executeCommand(cmd);
			}
			else 
			{
				EAM.errorDialog(EAM
						.text("Please choose a specific classification not a catagory"));
				String code = getCurrentDiagramFactor().getUnderlyingObject().getData(tag);
				for (int i=0; i<thisComboBox.getItemCount(); i++) 
				{
					TaxonomyItem foundItem = (TaxonomyItem)thisComboBox.getItemAt(i);
					if (code.equals(foundItem.getTaxonomyCode())) 
						thisComboBox.setSelectedIndex(i);
				}
				
			}
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
			EAM.errorDialog("That action failed due to an unknown error");
		}
	}
	
	class ImpactChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			try
			{
				String tag = Strategy.TAG_IMPACT_RATING;
				String impact = getSelectedImpactCode();
				CommandSetObjectData cmd = new CommandSetObjectData(getCurrentDiagramFactor().getType(),
						getCurrentFactorId(), tag, impact);
				getProject().executeCommand(cmd);
				updateRating();
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}

	}

	class DurationChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			try
			{
				String tag = Strategy.TAG_DURATION_RATING;
				String duration = getSelectedDurationCode();
				CommandSetObjectData cmd = new CommandSetObjectData(getCurrentDiagramFactor().getType(),
						getCurrentFactorId(), tag, duration);
				getProject().executeCommand(cmd);
				updateRating();
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}

	}

	class FeasibilityChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			try
			{
				String tag = Strategy.TAG_FEASIBILITY_RATING;
				String feasibility = getSelectedFeasibilityCode();
				CommandSetObjectData cmd = new CommandSetObjectData(getCurrentDiagramFactor().getType(),
						getCurrentFactorId(), tag, feasibility);
				getProject().executeCommand(cmd);
				updateRating();
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}

	}

	class CostChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			try
			{
				String tag = Strategy.TAG_COST_RATING;
				String cost = getSelectedCostCode();
				CommandSetObjectData cmd = new CommandSetObjectData(getCurrentDiagramFactor().getType(),
						getCurrentFactorId(), tag, cost);
				getProject().executeCommand(cmd);
				updateRating();
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}

	}

	void updateRating()
	{
		RatingChoice rating = ((Strategy)getCurrentDiagramFactor().getUnderlyingObject()).getStrategyRating();
		ratingComponent.setText(rating.getCode());
	}
	
	Command buildStatusCommand()
	{
		String newValue = Strategy.STATUS_REAL;
		if(statusCheckBox.isSelected())
			newValue = Strategy.STATUS_DRAFT;
		return new CommandSetObjectData(ObjectType.MODEL_NODE, currentDiagramFactor
				.getDiagramFactorId(), Strategy.TAG_STATUS,
				newValue);
	}

	public JComponent createComment(String comment)
	{
		commentField = new UiTextArea(4, 25);
		commentField.setWrapStyleWord(true);
		commentField.setLineWrap(true);
		commentField.setText(comment);
		commentField.addFocusListener(new CommentFocusHandler());

		JScrollPane component = new JScrollPane(commentField);
		return component;
	}

	class CommentFocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent event)
		{
		}

		public void focusLost(FocusEvent event)
		{
			String newComment = getComment();
			if(newComment.equals(getCurrentDiagramFactor().getComment()))
				return;
			try
			{
				int type = ObjectType.MODEL_NODE;
				String tag = Factor.TAG_COMMENT;
				CommandSetObjectData cmd = new CommandSetObjectData(type,
						getCurrentFactorId(), tag, newComment);
				getProject().executeCommand(cmd);
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}

	}


	public String getSelectedImpactCode()
	{
		RatingChoice selected = getSelectedImpactChoice();
		if(selected == null)
			return "";
		return selected.getCode();
	}

	private RatingChoice getSelectedImpactChoice()
	{
		RatingChoice selected = (RatingChoice)impactComponent.getSelectedItem();
		return selected;
	}
	
	public String getSelectedDurationCode()
	{
		RatingChoice selected = getSelectedDurationChoice();
		if(selected == null)
			return "";
		return selected.getCode();
	}

	private RatingChoice getSelectedDurationChoice()
	{
		RatingChoice selected = (RatingChoice)durationComponent.getSelectedItem();
		return selected;
	}
	
	public String getSelectedFeasibilityCode()
	{
		RatingChoice selected = getSelectedFeasibilityChoice();
		if(selected == null)
			return "";
		return selected.getCode();
	}

	private RatingChoice getSelectedFeasibilityChoice()
	{
		RatingChoice selected = (RatingChoice)feasibilityComponent.getSelectedItem();
		return selected;
	}

	public String getSelectedCostCode()
	{
		RatingChoice selected = getSelectedCostChoice();
		if(selected == null)
			return "";
		return selected.getCode();
	}

	private RatingChoice getSelectedCostChoice()
	{
		RatingChoice selected = (RatingChoice)costComponent.getSelectedItem();
		return selected;
	}

	public TaxonomyItem getThreatTaxonomyItem()
	{
		TaxonomyItem taxonomyItem = (TaxonomyItem) dropdownThreatClassification.getSelectedItem();
		
		if (!taxonomyItem.isLeaf()) 
			return null;

		return taxonomyItem;
	}

	public TaxonomyItem getStrategyTaxonomyItem()
	{
		TaxonomyItem taxonomyItem = (TaxonomyItem) dropdownStrategyClassification.getSelectedItem();
		
		if (!taxonomyItem.isLeaf()) 
			return null;
		
		return taxonomyItem;
	}
	
	public String getComment()
	{
		return commentField.getText();
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

	private DiagramFactor currentDiagramFactor;
	private UiCheckBox statusCheckBox;
	private UiComboBox dropdownThreatClassification;
	private UiComboBox dropdownStrategyClassification;
	private UiComboBox impactComponent;
	private UiComboBox durationComponent;
	private UiComboBox feasibilityComponent;
	private UiComboBox costComponent;
	private UiLabel ratingComponent;
	private UiTextArea commentField;
}
