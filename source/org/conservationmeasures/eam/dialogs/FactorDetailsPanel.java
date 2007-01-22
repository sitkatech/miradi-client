/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
		return createClassificationDropdown(TaxonomyLoader.THREAT_TAXONOMIES_FILE);
	}

	private JComponent createStrategyClassificationDropdown()
	{
		return  createClassificationDropdown(TaxonomyLoader.STRATEGY_TAXONOMIES_FILE);
	}
	
	
	private UiComboBox createClassificationDropdown(String taxonomyFile)
	{
		String[] choices = { "error processing classifications", };
		try
		{
			TaxonomyItem[] taxonomyItems = TaxonomyLoader.load(taxonomyFile);
			UiComboBox comboBox = new UiComboBox(taxonomyItems);
			
			String taxonomyCode = getCurrentDiagramFactor().getUnderlyingObject()
			.getData(Cause.TAG_TAXONOMY_CODE);

			TaxonomyItem foundTaxonomyItem = SearchTaxonomyClassificationsForCode(
					taxonomyItems, taxonomyCode);

			if(foundTaxonomyItem == null)
			{
				String errorMessage = "Classification not found in table ; please make another selection";
				EAM.errorDialog(EAM.text(errorMessage));
				foundTaxonomyItem = taxonomyItems[0];
			}

			comboBox.setSelectedItem(foundTaxonomyItem);

			comboBox.addActionListener(new ClassificationChangeHandler());
			
			comboBox.addFocusListener(new ClassificationFocusHandler());

			return comboBox;
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
	
	class ClassificationChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			UiComboBox comboBox = (UiComboBox)event.getSource();
			TaxonomyItem taxonomyItem = getStrategyTaxonomyItem(comboBox);
			actionSaveTaxonomySelection(comboBox, taxonomyItem);
		}
	}
	
	private void actionSaveTaxonomySelection(UiComboBox thisComboBox, TaxonomyItem taxonomyItem)
	{
		try
		{
			int type = ObjectType.FACTOR;
			String tag = Cause.TAG_TAXONOMY_CODE;
		
			if(taxonomyItem != null)
			{
				String taxonomyCode = taxonomyItem.getTaxonomyCode();
				CommandSetObjectData cmd = new CommandSetObjectData(type,
						getCurrentFactorId(), tag, taxonomyCode);
				getProject().executeCommand(cmd);
			}
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
			EAM.errorDialog("That action failed due to an unknown error");
		}
	}
	
	class ClassificationFocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
		}

		public void focusLost(FocusEvent e)
		{
			TaxonomyItem taxonomyItem = (TaxonomyItem) ((UiComboBox)e.getSource()).getSelectedItem();
			if (!taxonomyItem.isLeaf())
				EAM.errorDialog("(" + EAM.text(taxonomyItem.getTaxonomyDescription() + ")\n Please choose a specific classification not a category"));
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

	private TaxonomyItem getStrategyTaxonomyItem(UiComboBox comboBox)
	{
		TaxonomyItem taxonomyItem = (TaxonomyItem) comboBox.getSelectedItem();
		
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
	
	private Icon detailIcon;
	private DiagramFactor currentDiagramFactor;
	private UiCheckBox statusCheckBox;
}
