/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.umbrella;

import org.miradi.actions.EAMAction;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
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
		
		beginTransactionIfProjectOpen();
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
			endTransactionIfProjectOpen();
		}
	}

	private void beginTransactionIfProjectOpen() throws CommandFailedException
	{
		Project project = getProject();
		if(!project.isOpen())
			return;
		
		project.executeCommand(new CommandBeginTransaction());
	}

	private void endTransactionIfProjectOpen() throws CommandFailedException
	{
		Project project = getProject();
		if(!project.isOpen())
			return;
		
		project.executeCommand(new CommandEndTransaction());
	}

	private WizardManager getWizardManager()
	{
		return getMainWindow().getWizardManager();
	}

	Class actionClass;
}
