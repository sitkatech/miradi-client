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

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.views.ViewDoer;
import org.miradi.wizard.SkeletonWizardStep;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;

abstract public class WizardNavigationDoer extends ViewDoer
{
	abstract String getControlName();
	
	public boolean isAvailable()
	{
		if(getWizardPanel() == null)
			return false;
		
		Class destination = getPotentialDestination();
		return (destination != null);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			SkeletonWizardStep currentStep = getWizardManager().getCurrentStep();
			currentStep.buttonPressed(getControlName());
			getMainWindow().updateActionsAndStatusBar();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Error going to wizard step");
		}
	}

	private Class getPotentialDestination()
	{
		SkeletonWizardStep currentStep = getWizardManager().getCurrentStep();
		Class destination = getWizardManager().findControlTargetStep(getControlName(), currentStep);
		return destination;
	}

	private WizardPanel getWizardPanel()
	{
		return getMainWindow().getWizard();
	}

	private WizardManager getWizardManager()
	{
		return getMainWindow().getWizardManager();
	}

}
