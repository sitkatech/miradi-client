/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiButton;

public class ThreatRatingBundleDialog extends JDialog implements ActionListener
{
	public ThreatRatingBundleDialog(ThreatRatingFramework frameworkToUse, ThreatRatingBundle bundleToUse)
	{
		framework = frameworkToUse;
		bundle = bundleToUse;
		
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new ThreatRatingPanel(frameworkToUse, bundleToUse), BorderLayout.CENTER);
		UiButton okButton = new UiButton("OK");
		okButton.addActionListener(this);
		contentPane.add(okButton, BorderLayout.AFTER_LAST_LINE);
		pack();
		
		
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			framework.saveBundle(bundle);
			dispose();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
	}
	
	ThreatRatingFramework framework;
	ThreatRatingBundle bundle;
}
