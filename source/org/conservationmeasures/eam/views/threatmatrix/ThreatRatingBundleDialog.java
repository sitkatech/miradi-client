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

import javax.swing.Box;
import javax.swing.JDialog;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiButton;

public class ThreatRatingBundleDialog extends JDialog
{
	public ThreatRatingBundleDialog(ThreatRatingFramework frameworkToUse, ThreatRatingBundle bundleToUse) throws Exception
	{
		framework = frameworkToUse;
		originalBundle = bundleToUse;
		workingBundle = new ThreatRatingBundle(originalBundle);
		
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new ThreatRatingPanel(frameworkToUse, workingBundle), BorderLayout.CENTER);
		
		Box buttonBox = Box.createHorizontalBox();
		UiButton okButton = new UiButton("OK");
		okButton.addActionListener(new OkButtonListener());
		UiButton cancelButton = new UiButton("Cancel");
		cancelButton.addActionListener(new ButtonListener());
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(okButton);
		buttonBox.add(cancelButton);
		buttonBox.add(Box.createHorizontalGlue());
		contentPane.add(buttonBox, BorderLayout.AFTER_LAST_LINE);
		pack();
		
		
	}
	
	public class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			dispose();
		}
	}

	public class OkButtonListener extends ButtonListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				originalBundle.pullDataFrom(workingBundle);
				framework.saveBundle(originalBundle);
				super.actionPerformed(event);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
			
		}
	}
	
	ThreatRatingFramework framework;
	ThreatRatingBundle originalBundle;
	ThreatRatingBundle workingBundle;
}
