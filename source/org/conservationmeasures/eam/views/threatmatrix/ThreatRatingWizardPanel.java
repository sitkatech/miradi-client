/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;

public class ThreatRatingWizardPanel extends JPanel
{
	public ThreatRatingWizardPanel(ThreatMatrixView viewToUse)
	{
		super(new BorderLayout());
		bundleChooser = new ThreatRatingWizardChooseBundle(viewToUse);
		add(bundleChooser);
	}

	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		return bundleChooser.getSelectedBundle();
	}
	
	ThreatRatingWizardChooseBundle bundleChooser;
}