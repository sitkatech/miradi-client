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
