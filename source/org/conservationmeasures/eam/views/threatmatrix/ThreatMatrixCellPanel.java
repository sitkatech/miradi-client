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
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatMatrixCellPanel extends JPanel
{
	public ThreatMatrixCellPanel(ThreatRatingFramework framework)
	{
		ThreatRatingValue priority = getRandomPriority(framework);
		JButton highButton = new JButton(priority.toString());
		Color priorityColor = priority.getColor();
		highButton.setBackground(priorityColor);
		highButton.addActionListener(new RatingSummaryButtonHandler(this, framework));
		add(highButton);
		setBackground(priorityColor);
	}
	
	public ThreatRatingValue getRandomPriority(ThreatRatingFramework framework)
	{
		RatingValueOption[] options = framework.getRatingValueOptions();
		int index = Math.abs(new Random().nextInt()) % options.length;
		return new ThreatRatingValue(options[index]);
	}
}
