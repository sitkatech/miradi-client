/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.actions.EAMAction;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.views.MainWindowDoer;
import org.miradi.wizard.WizardManager;

public class JumpDoer extends MainWindowDoer
{
	public JumpDoer(Class actionClassToUse)
	{
		actionClass = actionClassToUse;
	}
	
	public boolean isAvailable()
	{
		WizardManager wizardManager = getWizardManager();
		if(!wizardManager.isValidStep(actionClass))
			return false;
		
		if(getProject().isOpen())
			return true;
		
		EAMAction action = getMainWindow().getActions().get(actionClass);
		return action.isAvailableWithoutProject();
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			getWizardManager().setStep(actionClass);
			getMainWindow().forceViewSplitterToMiddle();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private WizardManager getWizardManager()
	{
		return getMainWindow().getWizardManager();
	}

	Class actionClass;
}
