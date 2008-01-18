/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;


public class ProcessMenu4c extends MiradiMenu
{
	public ProcessMenu4c(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_4C, actions);

		addDisabledMenuItem(EAM.text("Revise project plan: strategic, monitoring, operational, and work plans"));
	}
}
