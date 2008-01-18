/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu5b extends MiradiMenu
{
	public ProcessMenu5b(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_5B, actions);
		
		addDisabledMenuItem(EAM.text("Identify key audiences"));
		addDisabledMenuItem(EAM.text("Develop communications strategy"));
		addDisabledMenuItem(EAM.text("Report to project team and stakeholders"));
		addDisabledMenuItem(EAM.text("Develop and share communication products"));
		addDisabledMenuItem(EAM.text("Use other’s communication products"));

	}
}
