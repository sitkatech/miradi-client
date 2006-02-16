/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import org.conservationmeasures.eam.objects.RatingValueOption;
import org.martus.swing.Utilities;

public class RatingSummaryButtonHandler implements ActionListener
{
	public RatingSummaryButtonHandler(ThreatSummaryPanel ownerToUse, RatingValueOption[] optionsToUse)
	{
		owner = ownerToUse;
		options = optionsToUse;
	}

	public void actionPerformed(ActionEvent e)
	{
		JDialog threatRatingDialog = new JDialog();
		threatRatingDialog.setModal(true);
		Container contentPane = threatRatingDialog.getContentPane();
		contentPane.add(new ThreatRatingPanel(options));
		threatRatingDialog.pack();
		
		threatRatingDialog.setLocationRelativeTo(owner);
		Utilities.fitInScreen(threatRatingDialog);
		threatRatingDialog.show();
	}
	
	ThreatSummaryPanel owner;
	RatingValueOption[] options;
}
