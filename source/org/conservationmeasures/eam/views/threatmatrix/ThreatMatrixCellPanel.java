/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.Utilities;

public class ThreatMatrixCellPanel extends JPanel implements ActionListener
{
	public ThreatMatrixCellPanel(ThreatMatrixView viewToUse, ThreatRatingFramework frameworkToUse, ThreatRatingBundle bundleToUse)
	{
		view = viewToUse;
		framework = frameworkToUse;
		bundle = bundleToUse;
		
		highButton = new JButton();
		add(highButton);
		refreshCell();

		highButton.addActionListener(this);
	}

	private void refreshCell()
	{
		ThreatRatingValueOption value = framework.getBundleValue(bundle);
		highButton.setText(value.getLabel());
		Color color = value.getColor();
		highButton.setBackground(color);
		setBackground(color);
		view.cellHasChanged();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JDialog threatRatingDialog = new ThreatRatingBundleDialog(framework, bundle);
		threatRatingDialog.setLocationRelativeTo(this);
		Utilities.fitInScreen(threatRatingDialog);
		threatRatingDialog.show();
		refreshCell();
	}
	
	ThreatMatrixView view;
	ThreatRatingFramework framework;
	ThreatRatingBundle bundle;
	JButton highButton;
}
