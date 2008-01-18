/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu5a extends MiradiMenu
{
	public ProcessMenu5a(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_5A, actions);
		
		addDisabledMenuItem(EAM.text("Document key results and lessons"));
	}
}
