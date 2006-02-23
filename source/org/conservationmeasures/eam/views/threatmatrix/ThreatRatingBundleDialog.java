/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Container;

import javax.swing.JDialog;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatRatingBundleDialog extends JDialog
{
	public ThreatRatingBundleDialog(ThreatRatingFramework frameworkToUse, ThreatRatingBundle bundleToUse)
	{
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.add(new ThreatRatingPanel(frameworkToUse, bundleToUse));
		pack();		
	}
}
