/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.SimpleThreatRatingFramework;
import org.conservationmeasures.eam.utils.UiComboBoxWithSaneActionFiring;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;

public class ThreatRatingPanel extends Box
{
	public ThreatRatingPanel(ThreatMatrixView viewToUse)
	{
		super(BoxLayout.X_AXIS);
		view = viewToUse;
		framework = view.getThreatRatingFramework();
		
		dropdowns = new HashMap();
		
		ratingSummaryLabel = new PanelTitleLabel();
		ratingSummaryLabel.setVerticalAlignment(JLabel.CENTER);
		ratingSummaryLabel.setHorizontalAlignment(JLabel.CENTER);
		ratingSummaryLabel.setPreferredSize(new Dimension(50,100));
		
		createSummaryPanel();

		Box criteria = createCriteriaSection();
		
		add(criteria);
		add(ratingSummaryPanel);
	}
	
	public Dimension getMaximumSize()
	{
		return super.getPreferredSize();
	}

	public void setBundle(ThreatRatingBundle bundleToUse)
	{
		bundle = bundleToUse;
		updateDropdownsFromBundle();
		updateSummary();
	}

	private void createSummaryPanel()
	{
		int lineWidth = 1;
		ratingSummaryPanel = new JPanel();
		ratingSummaryPanel.setLayout(new BorderLayout());
		ratingSummaryPanel.add(ratingSummaryLabel, BorderLayout.CENTER);
		ratingSummaryPanel.setBorder(new LineBorder(Color.BLACK, lineWidth));
	}

	private Box createCriteriaSection()
	{
		int lineWidth = 1;
		Box criteria = Box.createVerticalBox();

		RatingCriterion[] criterionItems = framework.getCriteria();
		for(int i = 0; i < criterionItems.length; ++i)
		{
			RatingCriterion criterion = criterionItems[i];

			UiLabel criterionLabel = new PanelTitleLabel(criterion.getLabel());
			criterionLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			criterionLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
			criterionLabel.setFont(criterionLabel.getFont().deriveFont(Font.BOLD));

			UiComboBox dropdown = createRatingDropdown(framework.getValueOptions());
			dropdown.addActionListener(new ValueListener(this, criterion));
			dropdowns.put(criterion, dropdown);

			JPanel panel = new JPanel(new BorderLayout());
			panel.setBorder(new LineBorder(Color.BLACK, lineWidth));
			panel.add(criterionLabel, BorderLayout.BEFORE_FIRST_LINE);
			panel.add(dropdown, BorderLayout.CENTER);

			criteria.add(panel);
		}
		return criteria;
	}
	
	
	private void updateDropdownsFromBundle()
	{
		BaseId defaultValueId = framework.getDefaultValueId();
		Iterator iter = dropdowns.keySet().iterator();
		while(iter.hasNext())
		{
			RatingCriterion criterion = (RatingCriterion)iter.next();
			UiComboBoxWithSaneActionFiring dropdown = (UiComboBoxWithSaneActionFiring)dropdowns.get(criterion);

			BaseId valueId = defaultValueId;
			
			if(bundle != null)
			{
				dropdown.setEnabled(true);
				valueId = bundle.getValueId(criterion.getId());
			} 
			else
				dropdown.setEnabled(false);

			if(!valueId.isInvalid())
			{
				ValueOption option = framework.getValueOption(valueId);
				dropdown.setSelectedItemWithoutFiring(option);
			}

		}
	}

	private void updateSummary()
	{
		ValueOption value;
		
		if(bundle == null)
			value = framework.findValueOptionByNumericValue(0);
		else
			value = framework.getBundleValue(bundle);
		
		if(value == null)
		{
			EAM.logError("ThreatRatingPanel.updateSummary: value was null!");
			return;
		}
		
		ratingSummaryLabel.setText(value.getLabel());
		ratingSummaryLabel.setBackground(value.getColor());
		ratingSummaryPanel.setBackground(value.getColor());
	}

	private UiComboBox createRatingDropdown(ValueOption[] options)
	{
		UiComboBox dropDown = ThreatRatingPanel.createThreatDropDown(options);
		return dropDown;
	}
	
	public static UiComboBox createThreatDropDown(ValueOption[] options)
	{
		UiComboBox dropDown = new UiComboBoxWithSaneActionFiring();
		dropDown.setRenderer(new ThreatRenderer());
		
		for(int i = 0; i < options.length; ++i)
		{
			dropDown.addItem(options[i]);
		}
		return dropDown;
	}
	
	public void valueWasChanged(RatingCriterion criterion, ValueOption value)
	{
		if(bundle == null)
			return;
		bundle.setValueId(criterion.getId(), value.getId());
		updateSummary();
		try
		{
			view.setBundleValue(criterion, value);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error setting value"));
		}
	}
	
	static class ValueListener implements ActionListener
	{
		public ValueListener(ThreatRatingPanel panelToUse, RatingCriterion criterionToUse)
		{
			panel = panelToUse;
			criterion = criterionToUse;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			EAM.logWarning("ThreatRatingPanel.ValueListener.actionPerformed: " + event.getActionCommand());
			UiComboBox source = (UiComboBox)event.getSource();
			ValueOption selected = (ValueOption)source.getSelectedItem();
			panel.valueWasChanged(criterion, selected);
		}
		
		ThreatRatingPanel panel;
		RatingCriterion criterion;
	}
	
	ThreatMatrixView view;
	SimpleThreatRatingFramework framework;
	ThreatRatingBundle bundle;
	UiLabel ratingSummaryLabel;
	JPanel ratingSummaryPanel;
	Map dropdowns;
}