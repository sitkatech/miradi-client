package org.conservationmeasures.eam.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JComponent;

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
import org.conservationmeasures.eam.objecthelpers.TaxonomyItem;
import org.conservationmeasures.eam.objecthelpers.TaxonomyLoader;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StrategyCostQuestion;
import org.conservationmeasures.eam.questions.StrategyDurationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummary;
import org.conservationmeasures.eam.questions.TargetStatusQuestion;
import org.martus.swing.UiCheckBox;
import org.martus.swing.UiComboBox;
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
			// FIXME: Convert to new mechanism
			add(new UiLabel(EAM.fieldLabel(ObjectType.FACTOR,  Cause.TAG_TAXONOMY_CODE)));
			add(createThreatClassificationDropdown());
			detailIcon = new DirectThreatIcon();
		}
		
		if(factorToEdit.isContributingFactor())
		{
			detailIcon = new ContributingFactorIcon();
		}

		if(factorToEdit.isStrategy())
		{
			addField(createStringField(Strategy.TAG_SHORT_LABEL));
			
			// FIXME: Convert to new mechanism (create status field or just checkbox field?)
			add(new UiLabel(EAM.text("Label|Status")));
			statusCheckBox.setSelected(factorToEdit.isStatusDraft());
			statusCheckBox.addItemListener(new StatusChangeHandler());
			add(statusCheckBox);

			// FIXME: Convert to new mechanism
			add(new UiLabel(EAM.fieldLabel(ObjectType.FACTOR,  Cause.TAG_TAXONOMY_CODE)));
			add(createStrategyClassificationDropdown());
			
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

	
	public Icon getIcon()
	{
		return detailIcon;
	}
	
	private Icon detailIcon;
	
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

	private JComponent createThreatClassificationDropdown()
	{
		String[] choices = { "error processing classifications", };
		try
		{
			TaxonomyItem[] taxonomyItems = TaxonomyLoader
					.load(TaxonomyLoader.THREAT_TAXONOMIES_FILE);
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

	private JComponent createStrategyClassificationDropdown()
	{
		String[] choices = { "error processing classifications", };
		try
		{
			TaxonomyItem[] taxonomyItems = TaxonomyLoader
					.load(TaxonomyLoader.STRATEGY_TAXONOMIES_FILE);
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
	
	private TaxonomyItem SearchTaxonomyClassificationsForCode(
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
			int type = ObjectType.FACTOR;
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
	
	Command buildStatusCommand()
	{
		String newValue = Strategy.STATUS_REAL;
		if(statusCheckBox.isSelected())
			newValue = Strategy.STATUS_DRAFT;

		return new CommandSetObjectData(ObjectType.FACTOR, getCurrentFactorId(), 
				Strategy.TAG_STATUS, newValue);
	}


	private TaxonomyItem getThreatTaxonomyItem()
	{
		TaxonomyItem taxonomyItem = (TaxonomyItem) dropdownThreatClassification.getSelectedItem();
		
		if (!taxonomyItem.isLeaf()) 
			return null;

		return taxonomyItem;
	}

	private TaxonomyItem getStrategyTaxonomyItem()
	{
		TaxonomyItem taxonomyItem = (TaxonomyItem) dropdownStrategyClassification.getSelectedItem();
		
		if (!taxonomyItem.isLeaf()) 
			return null;
		
		return taxonomyItem;
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
}
