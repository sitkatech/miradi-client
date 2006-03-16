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
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.Utilities;

public class ThreatMatrixCellPanel extends JPanel implements ActionListener
{
	public ThreatMatrixCellPanel(ThreatGridPanel parentToUse, ThreatRatingBundle bundleToUse) throws Exception
	{
		parent = parentToUse;
		bundle = bundleToUse;

		mainWindow = parent.getMainWindow();
		project = parent.getProject();
		
		highButton = new JButton();
		add(highButton);
		refreshCell();

		highButton.addActionListener(this);
	}

	private void refreshCell() throws Exception
	{
		ThreatRatingValueOption value = project.getThreatRatingFramework().getBundleValue(bundle);
		highButton.setText(value.getLabel());
		Color color = value.getColor();
		highButton.setBackground(color);
		setBackground(color);
		parent.cellHasChanged();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			JDialog threatRatingDialog = new ThreatRatingBundleDialog(mainWindow, project, bundle);
			threatRatingDialog.setLocationRelativeTo(this);
			Utilities.fitInScreen(threatRatingDialog);
			threatRatingDialog.show();
			refreshCell();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	JFrame mainWindow;
	ThreatGridPanel parent;
	Project project;
	ThreatRatingBundle bundle;
	JButton highButton;
}
