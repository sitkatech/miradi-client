/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.wizard.WizardManager;

abstract public class ViewSwitchDoer extends MainWindowDoer
{
	abstract String getViewName();

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
