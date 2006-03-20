/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;

public abstract class ThreatRatingWizardStep extends JPanel
{
	public ThreatRatingWizardStep(ThreatRatingWizardPanel wizardToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;
		

	}
	
	abstract void refresh() throws Exception;
	abstract boolean save() throws Exception;
	
	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.indexOf("Next") >= 0)
			{
				if(!save())
					return;
				
				wizard.next();
			}
			
			if(buttonName.indexOf("Back") >= 0)
			{
				wizard.previous();
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	ThreatRatingWizardPanel wizard;
}
