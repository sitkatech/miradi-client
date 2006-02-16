/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.conservationmeasures.eam.objects.RatingValueOption;
import org.conservationmeasures.eam.objects.ThreatRatingValue;

public class ThreatSummaryPanel extends JPanel
{
	public ThreatSummaryPanel(RatingValueOption[] options)
	{
		ThreatRatingValue priority = getPriority();
		JButton highButton = new JButton(priority.toString());
		Color priorityColor = priority.getColor();
		highButton.setBackground(priorityColor);
		highButton.addActionListener(new RatingSummaryButtonHandler(this, options));
		add(highButton);
		setBackground(priorityColor);
	}
	
	public ThreatRatingValue getPriority()
	{
		int value = Math.abs(new Random().nextInt()) % 4;
		return ThreatRatingValue.createFromInt(value);
	}
}
