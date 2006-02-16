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

import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.Utilities;

public class RatingSummaryButtonHandler implements ActionListener
{
	public RatingSummaryButtonHandler(ThreatMatrixCellPanel ownerToUse, ThreatRatingFramework frameworkToUse)
	{
		owner = ownerToUse;
		framework = frameworkToUse;
	}

	public void actionPerformed(ActionEvent e)
	{
		JDialog threatRatingDialog = new JDialog();
		threatRatingDialog.setModal(true);
		Container contentPane = threatRatingDialog.getContentPane();
		contentPane.add(new ThreatRatingPanel(framework));
		threatRatingDialog.pack();
		
		threatRatingDialog.setLocationRelativeTo(owner);
		Utilities.fitInScreen(threatRatingDialog);
		threatRatingDialog.show();
	}
	
	ThreatMatrixCellPanel owner;
	ThreatRatingFramework framework;
}
