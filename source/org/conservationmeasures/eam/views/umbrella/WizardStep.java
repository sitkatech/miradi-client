/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;

public abstract class WizardStep extends JPanel
{
	public WizardStep(WizardPanel wizardToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;
	}
	
	public WizardPanel getWizard()
	{
		return wizard;
	}
	
	abstract public boolean save() throws Exception;
	
	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.indexOf("Next") >= 0)
			{
				if(!save())
					return;
				
				getWizard().next();
			}
			
			if(buttonName.indexOf("Back") >= 0)
			{
				getWizard().previous();
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	

	
	private WizardPanel wizard;

}
