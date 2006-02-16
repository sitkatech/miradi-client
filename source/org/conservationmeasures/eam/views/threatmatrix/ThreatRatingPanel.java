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

import org.conservationmeasures.eam.main.NodePropertiesDialog;
import org.conservationmeasures.eam.objects.RatingValueOption;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValue;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;

public class ThreatRatingPanel extends JPanel
{
	public ThreatRatingPanel(RatingValueOption[] options)
	{
		ThreatRatingCriterion[] criterionItems = new ThreatRatingCriterion[] {
			new ThreatRatingCriterion(0, "Scope"), 
			new ThreatRatingCriterion(1, "Severity"),
			new ThreatRatingCriterion(2, "Urgency"),
			new ThreatRatingCriterion(3, "Custom"),
		};
		
		int lineWidth = 1;
		
		Box criteria = Box.createVerticalBox();

		for(int i = 0; i < criterionItems.length; ++i)
		{
			Box scopeCell = Box.createVerticalBox();
			scopeCell.add(new UiLabel(criterionItems[i].getLabel()));
			scopeCell.add(createRatingDropdown(options));
			scopeCell.setBorder(new LineBorder(Color.BLACK, lineWidth));
			
			criteria.add(scopeCell);
		}
		

		
		ThreatRatingValue priority = getRandomPriority();
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

	private UiComboBox createRatingDropdown(RatingValueOption[] options)
	{
		UiComboBox dropDown = NodePropertiesDialog.createThreatDropDown(options);
		int choice = new Random().nextInt(dropDown.getItemCount());
		dropDown.setSelectedIndex(choice);
		return dropDown;
	}
	
	private ThreatRatingValue getRandomPriority()
	{
		return ThreatRatingValue.createFromInt(new Random().nextInt(4));
	}
}
