/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.ViewDoer;
import org.conservationmeasures.eam.wizard.SkeletonWizardStep;
import org.conservationmeasures.eam.wizard.WizardManager;
import org.conservationmeasures.eam.wizard.WizardPanel;

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
