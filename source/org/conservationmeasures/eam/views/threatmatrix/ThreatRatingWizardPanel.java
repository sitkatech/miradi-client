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
		view = viewToUse;
		
		bundleChooser = new ThreatRatingWizardChooseBundle(this);
		
		setStepChooseBundle();
	}

	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		return bundleChooser.getSelectedBundle();
	}
	
	public ThreatMatrixView getView()
	{
		return view;
	}
	
	public void next()
	{
		
	}
	
	public void setStepChooseBundle()
	{
		setContents(bundleChooser);
		
	}
	
	public void setContents(JPanel contents)
	{
		removeAll();
		add(contents);
	}
	
	ThreatMatrixView view;
	ThreatRatingWizardChooseBundle bundleChooser;
}