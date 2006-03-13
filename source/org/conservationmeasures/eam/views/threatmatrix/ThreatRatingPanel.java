/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;

public class ThreatRatingPanel extends JPanel implements ItemListener
{
	public ThreatRatingPanel(ThreatRatingFramework frameworkToUse, ThreatRatingBundle bundleToUse)
	{
		framework = frameworkToUse;
		bundle = bundleToUse;
		
		ratingSummaryLabel = new UiLabel();
		ratingSummaryLabel.setVerticalAlignment(JLabel.CENTER);
		ratingSummaryLabel.setHorizontalAlignment(JLabel.CENTER);
		
		createSummaryPanel();

		Box criteria = createCriteriaSection();
		

		Box main = Box.createHorizontalBox();
		main.add(criteria);
		main.add(ratingSummaryPanel);
				
		updateBundleValueComponent(getBundleValue());
		
		add(main);
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

		ThreatRatingCriterion[] criterionItems = framework.getCriteria();
		for(int i = 0; i < criterionItems.length; ++i)
		{
			ThreatRatingCriterion criterion = criterionItems[i];

			UiLabel criterionLabel = new UiLabel(criterion.getLabel());
			criterionLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			criterionLabel.setBorder(new EmptyBorder(2, 2, 2, 2));

			UiComboBox dropdown = createRatingDropdown(framework.getValueOptions());
			dropdown.addItemListener(new ValueListener(bundle, criterion));
			dropdown.addItemListener(this);
			
			int valueId = bundle.getValueId(criterion.getId());
			if(valueId != IdAssigner.INVALID_ID)
			{
				ThreatRatingValueOption option = framework.getValueOption(valueId);
				dropdown.setSelectedItem(option);
			}

			JPanel panel = new JPanel(new BorderLayout());
			panel.setBorder(new LineBorder(Color.BLACK, lineWidth));
			panel.add(criterionLabel, BorderLayout.BEFORE_FIRST_LINE);
			panel.add(dropdown, BorderLayout.CENTER);

			criteria.add(panel);
		}
		return criteria;
	}

	private ThreatRatingValueOption getBundleValue()
	{
		ThreatRatingValueOption value = framework.getBundleValue(bundle);
		return value;
	}

	private void updateBundleValueComponent(ThreatRatingValueOption value)
	{
		if(value == null)
			System.out.println(value);
		ratingSummaryLabel.setText(value.getLabel());
		ratingSummaryLabel.setBackground(value.getColor());
		ratingSummaryPanel.setBackground(value.getColor());
	}

	private UiComboBox createRatingDropdown(ThreatRatingValueOption[] options)
	{
		UiComboBox dropDown = ThreatRatingPanel.createThreatDropDown(options);
		return dropDown;
	}
	
	public static UiComboBox createThreatDropDown(ThreatRatingValueOption[] options)
	{
		UiComboBox dropDown = new UiComboBox();
		dropDown.setRenderer(new ThreatRenderer());
		
		for(int i = 0; i < options.length; ++i)
		{
			dropDown.addItem(options[i]);
		}
		return dropDown;
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		updateBundleValueComponent(getBundleValue());
	}
	
	static class ValueListener implements ItemListener
	{
		public ValueListener(ThreatRatingBundle bundleToUse, ThreatRatingCriterion criterionToUse)
		{
			bundle = bundleToUse;
			criterion = criterionToUse;
		}
		
		public void itemStateChanged(ItemEvent e)
		{
			UiComboBox source = (UiComboBox)e.getSource();
			ThreatRatingValueOption selected = (ThreatRatingValueOption)source.getSelectedItem();
			bundle.setValueId(criterion.getId(), selected.getId());
		}
		
		ThreatRatingBundle bundle;
		ThreatRatingCriterion criterion;
	}
	
	ThreatRatingFramework framework;
	ThreatRatingBundle bundle;
	UiLabel ratingSummaryLabel;
	JPanel ratingSummaryPanel;
}