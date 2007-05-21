/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import java.awt.BorderLayout;
import java.net.URL;
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

	public String getTextLeft() throws Exception
	{
		if (!doesExisits("Left"))
			return getText("");
		return getText("Left");
	}

	public String getTextRight() throws Exception
	{
		if (!doesExisits("Right"))
			return "";
		return getText("Right");
	}
	
	private String getText(String ext) throws Exception
	{
		String resourceFileName = getResourceFileName(ext);
		if(resourceFileName == null)
			return "Missing text";
		return EAM.loadResourceFile(getClass(), resourceFileName);
	}
	
	//NOTE: Temp code to allow check in during wizard conversion
	private boolean doesExisits(String ext) throws Exception
	{
		URL url = EAM.getResourceURL(getClass(), getResourceFileName(ext));
		return (url!=null);
	}
	
	private String getResourceFileName(String ext)
	{
		return getClass().getSimpleName() + ext + ".html";
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
