/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValue;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;

public class ThreatRatingPanel extends JPanel
{
	public ThreatRatingPanel(ThreatRatingFramework framework)
	{
		int lineWidth = 1;
		
		Box criteria = Box.createVerticalBox();

		ThreatRatingCriterion[] criterionItems = framework.getCriteria();
		for(int i = 0; i < criterionItems.length; ++i)
		{
			Box scopeCell = Box.createVerticalBox();
			scopeCell.add(new UiLabel(criterionItems[i].getLabel()));
			scopeCell.add(createRatingDropdown(framework.getValueOptions()));
			scopeCell.setBorder(new LineBorder(Color.BLACK, lineWidth));
			
			criteria.add(scopeCell);
		}
		

		
		ThreatRatingValue priority = getRandomPriority(framework);
		UiLabel ratingSummaryLabel = new UiLabel();
		ratingSummaryLabel.setText(priority.toString());
		ratingSummaryLabel.setBackground(priority.getColor());
		ratingSummaryLabel.setVerticalAlignment(JLabel.CENTER);

		JPanel ratingSummaryPanel = new JPanel();
		ratingSummaryPanel.setLayout(new BorderLayout());
		ratingSummaryPanel.add(ratingSummaryLabel, BorderLayout.CENTER);
		ratingSummaryPanel.setBorder(new LineBorder(Color.BLACK, lineWidth));
		ratingSummaryPanel.setBackground(priority.getColor());


		Box main = Box.createHorizontalBox();
		main.add(criteria);
		main.add(ratingSummaryPanel);
		
		add(main);
	}

	private UiComboBox createRatingDropdown(ThreatRatingValueOption[] options)
	{
		UiComboBox dropDown = ThreatRatingPanel.createThreatDropDown(options);
		int choice = new Random().nextInt(dropDown.getItemCount());
		dropDown.setSelectedIndex(choice);
		return dropDown;
	}
	
	public ThreatRatingValue getRandomPriority(ThreatRatingFramework framework)
	{
		ThreatRatingValueOption[] options = framework.getValueOptions();
		int index = Math.abs(new Random().nextInt()) % options.length;
		return new ThreatRatingValue(options[index]);
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
}
