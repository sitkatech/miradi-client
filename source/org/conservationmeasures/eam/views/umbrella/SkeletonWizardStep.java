/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HyperlinkHandler;

public abstract class SkeletonWizardStep extends JPanel implements HyperlinkHandler
{
	public SkeletonWizardStep(WizardPanel wizardToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;

	}
	
	abstract public void refresh() throws Exception;

	public WizardPanel getWizard()
	{
		return wizard;
	}

	public String getText() throws Exception
	{
		String resourceFileName = getResourceFileName();
		if(resourceFileName == null)
			return "Missing text";
		
		return EAM.loadResourceFile(getClass(), resourceFileName);
	}

	public String getResourceFileName()
	{
		return null;
	}
	
	public boolean save() throws Exception
	{
		return true;
	}
	
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
	
	public void linkClicked(String linkDescription)
	{
	}

	public JPopupMenu getRightClickMenu(String url)
	{
		return null;
	}

	public void valueChanged(String widget, String newValue)
	{
	}




	private WizardPanel wizard;
}
