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
	public ThreatRatingWizardPanel(ThreatMatrixView viewToUse) throws Exception
	{
		super(new BorderLayout());
		view = viewToUse;
		
		bundleChooser = new ThreatRatingWizardChooseBundle(this);
		valueSetter = new ThreatRatingWizardSetValue(this);
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
		setStepScope();
	}
	
	public void setStepChooseBundle()
	{
		setContents(bundleChooser);
	}
	
	public void setStepScope()
	{
		setContents(valueSetter);
	}
	
	public void setContents(JPanel contents)
	{
		removeAll();
		add(contents);
		validate();
	}
	
	ThreatMatrixView view;
	ThreatRatingWizardChooseBundle bundleChooser;
	ThreatRatingWizardSetValue valueSetter;
}