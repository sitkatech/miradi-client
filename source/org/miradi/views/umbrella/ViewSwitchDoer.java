/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.views.umbrella;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.MainWindowDoer;
import org.miradi.wizard.WizardManager;

abstract public class ViewSwitchDoer extends MainWindowDoer
{
	abstract protected String getViewName();

	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		String viewName = getViewName();
		WizardManager wizardManager = getMainWindow().getWizardManager();
		String destinationStepName = wizardManager.getOverviewStepName(viewName);
		
		String currentStepName = wizardManager.getCurrentStepName();
		if(destinationStepName.equals(currentStepName))
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			wizardManager.setStep(destinationStepName);
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
}
