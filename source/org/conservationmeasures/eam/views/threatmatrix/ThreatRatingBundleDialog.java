/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiScrollPane;

public class ThreatRatingBundleDialog extends JDialog
{
	public ThreatRatingBundleDialog(JFrame parent, Project projectToUse, ThreatRatingBundle bundleToUse) throws Exception
	{
		super(parent);
		project = projectToUse;
		originalBundle = bundleToUse;
		workingBundle = new ThreatRatingBundle(originalBundle);
		
		setModal(true);
		ThreatRatingBundlePanel bundlePanel = new ThreatRatingBundlePanel(project, new OkButtonListener(), new CancelButtonListener());
		bundlePanel.setBundle(workingBundle);
		getContentPane().add(new UiScrollPane(bundlePanel));
		pack();
	}

	public class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			dispose();
		}
	}
	
	public class CancelButtonListener extends ButtonListener
	{
		
	}

	public class OkButtonListener extends ButtonListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				originalBundle.pullDataFrom(workingBundle);
				project.getThreatRatingFramework().saveBundle(originalBundle);
				super.actionPerformed(event);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
			
		}
	}
	
	Project project;
	ThreatRatingBundle originalBundle;
	ThreatRatingBundle workingBundle;
}
