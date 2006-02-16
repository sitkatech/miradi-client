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
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValue;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;

public class ThreatRatingPanel extends JPanel
{
	public ThreatRatingPanel()
	{
		ThreatRatingCriterion criterion1 = new ThreatRatingCriterion(0, "Scope");
		ThreatRatingCriterion criterion2 = new ThreatRatingCriterion(1, "Severity");
		ThreatRatingCriterion criterion3 = new ThreatRatingCriterion(2, "Urgency");
		ThreatRatingCriterion criterion4 = new ThreatRatingCriterion(3, "Custom");
		
		int lineWidth = 1;
		
		Box scopeCell = Box.createVerticalBox();
		scopeCell.add(new UiLabel(criterion1.getLabel()));
		scopeCell.add(createRatingDropdown());
		scopeCell.setBorder(new LineBorder(Color.BLACK, lineWidth));
		
		Box severityCell = Box.createVerticalBox();
		severityCell.add(new UiLabel(criterion2.getLabel()));
		severityCell.add(createRatingDropdown());
		severityCell.setBorder(new LineBorder(Color.BLACK, lineWidth));
		
		Box urgencyCell = Box.createVerticalBox();
		urgencyCell.add(new UiLabel(criterion3.getLabel()));
		urgencyCell.add(createRatingDropdown());
		urgencyCell.setBorder(new LineBorder(Color.BLACK, lineWidth));
		
		Box extraCell = Box.createVerticalBox();
		extraCell.add(new UiLabel(criterion4.getLabel()));
		extraCell.add(createRatingDropdown());
		extraCell.setBorder(new LineBorder(Color.BLACK, lineWidth));

		
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

		Box criteria = Box.createVerticalBox();
		criteria.add(scopeCell);
		criteria.add(severityCell);
		criteria.add(urgencyCell);
		criteria.add(extraCell);

		Box main = Box.createHorizontalBox();
		main.add(criteria);
		main.add(ratingSummaryPanel);
		
		add(main);
	}

	private UiComboBox createRatingDropdown()
	{
		UiComboBox dropDown = NodePropertiesDialog.createThreatDropDown();
		int choice = new Random().nextInt(dropDown.getItemCount());
		dropDown.setSelectedIndex(choice);
		return dropDown;
	}
	
	private ThreatRatingValue getRandomPriority()
	{
		return ThreatRatingValue.createFromInt(new Random().nextInt(4));
	}
}
