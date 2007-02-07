/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import java.awt.BorderLayout;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlFormEventHandler;

public abstract class SkeletonWizardStep extends JPanel implements HtmlFormEventHandler
{
	public SkeletonWizardStep(WizardPanel wizardToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;
		controls = new Hashtable();

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
	
	public void buttonPressed(String buttonName)
	{
		control(buttonName);
	}

	public void linkClicked(String linkDescription)
	{
		if (getWizard().getMainWindow().mainLinkFunction(linkDescription))
			return;
			
		if(linkDescription.startsWith("View:"))
		{
			control(linkDescription);
		}
	}
	

	private void control(String controlName)
	{
		try
		{
			getWizard().control(controlName);
		}
		catch (Exception e)
		{
			String body = EAM.text("Wizard load of control failed:") + controlName;
			EAM.errorDialog(body);
			EAM.logException(e);
		}
	}
	
	SkeletonWizardStep createControl(String controlName , Class controlStep)
	{
		addControl(controlName, controlStep);
		return this;
	}
	
	SkeletonWizardStep createNextControl(Class controlStep)
	{
		return createControl("Next", controlStep);
	}
	
	SkeletonWizardStep createBackControl(Class controlStep)
	{
		return createControl("Back", controlStep);
	}
	
	public JPopupMenu getRightClickMenu(String url)
	{
		return null;
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void setComponent(String name, JComponent component)
	{
	}
	
	public Class getControl(String controlName)
	{
		return (Class) controls.get(controlName);
	}

	public void addControl(String controlName, Class targetStepp)
	{
		controls.put(controlName, targetStepp);
	}

	private Hashtable controls;
	private WizardPanel wizard;
}
