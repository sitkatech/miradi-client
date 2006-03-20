/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public abstract class ThreatRatingWizardStep extends JPanel
{
	public ThreatRatingWizardStep()
	{
		super(new BorderLayout());

	}
	
	abstract void refresh() throws Exception;
}
