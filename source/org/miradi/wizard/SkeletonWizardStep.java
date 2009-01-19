/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.wizard;

import java.awt.BorderLayout;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.miradi.actions.EAMAction;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.ResourcesHandler;
import org.miradi.project.Project;
import org.miradi.utils.HtmlFormEventHandler;
import org.miradi.utils.Translation;
import org.miradi.views.umbrella.UmbrellaView;

public abstract class SkeletonWizardStep extends JPanel implements HtmlFormEventHandler
{
	public SkeletonWizardStep(WizardPanel wizardToUse, String viewNameToUse, String tabIdentifierToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;
		viewName = viewNameToUse;
		tabIdentifier = tabIdentifierToUse;
		
		controls = new Hashtable();
	}
	
	public SkeletonWizardStep(WizardPanel wizardToUse, String viewNameToUse)
	{
		this(wizardToUse, viewNameToUse, null);
	}
	
	abstract public void refresh() throws Exception;

	public Class getAssociatedActionClass()
	{
		return null;
	}
	
	public String getProcessStepTitle()
	{
		return EAM.text("Step (not available)");
	}

	public String getWizardScreenTitle()
	{
		Class associatedActionClass = getAssociatedActionClass();
		if(associatedActionClass != null)
		{
			EAMAction action = getMainWindow().getActions().get(associatedActionClass);
			if(action != null)
				return (String)action.getValue(Action.NAME);
		}
		return getViewName();
	}

	public WizardPanel getWizard()
	{
		return wizard;
	}

	public String getViewName()
	{
		return viewName;
	}
	
	public String getTabIdentifier()
	{
		return tabIdentifier;
	}
	
	public String getSubHeading()
	{
		return null;
	}
	
	public String getTextLeft() throws Exception
	{
		if (!doesExist(LEFT))
			return getText(NEITHER);
		return getText(LEFT);
	}

	public String getTextRight() throws Exception
	{
		if (!doesExist(RIGHT))
			return "";
		return getText(RIGHT);
	}
	
	private String getText(String sideLabel) throws Exception
	{
		String resourceFileName = getResourceFileName(sideLabel);
		if(resourceFileName == null)
			return "Missing text";
		return Translation.getHtmlContent(resourceFileName);
	}
	
	private boolean doesExist(String ext) throws Exception
	{
		URL url = ResourcesHandler.getResourceURL(getResourceFileName(ext));
		return (url!=null);
	}
	
	private String getResourceFileName(String sideLabel)
	{
		return "wizard/" + getViewClassSimpleName() + "/" + getHtmlBaseName() + sideLabel + ".html";
	}

	private String getViewClassSimpleName()
	{
		UmbrellaView view = getMainWindow().getView(getViewName());
		return view.getClass().getSimpleName();
	}
	
	public String getHtmlBaseName()
	{
		return getClass().getSimpleName();
	}

	public void buttonPressed(String buttonName)
	{
		control(buttonName);
	}

	public void linkClicked(String linkDescription)
	{
		if (getMainWindow().mainLinkFunction(linkDescription))
			return;
			
		if(linkDescription.startsWith("View:"))
		{
			control(linkDescription);
		}
	}
	
	public MainWindow getMainWindow()
	{
		return getWizard().getMainWindow();
	}

	private void control(String controlName)
	{
		try
		{
			WizardManager wizardManager = getMainWindow().getWizardManager();
			SkeletonWizardStep step = wizardManager.getCurrentStep();
			Class destinationStepClass = wizardManager.findControlTargetStep(controlName, step);
			if (destinationStepClass==null)
			{
				String errorText = "Control ("+ controlName +") not found for step: " + wizardManager.getStepName(step);
				reportError(EAM.text(errorText));
			}
			
			navigateToStep(destinationStepClass);
			getMainWindow().updateActionsAndStatusBar();
		}
		catch (Exception e)
		{
			String body = EAM.text("Wizard load of control failed:") + controlName;
			EAM.errorDialog(body);
			EAM.logException(e);
		}
	}

	private void navigateToStep(Class destinationStepClass) throws Exception
	{
		Project project = getMainWindow().getProject();
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			WizardManager wizardManager = getMainWindow().getWizardManager();
			wizardManager.setStep(destinationStepClass);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	private void reportError(String msg)
	{
		EAM.logError(msg);
		EAM.errorDialog(msg);
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

	private static final String RIGHT = "Right";
	private static final String LEFT = "Left";
	private static final String NEITHER = "";
	
	private Hashtable controls;
	private WizardPanel wizard;
	private String viewName;
	private String tabIdentifier;
}
