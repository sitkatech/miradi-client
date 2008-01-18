/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu5c extends MiradiMenu
{
	public ProcessMenu5c(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_5C, actions);
		
		addDisabledMenuItem(EAM.text("Share feedback formally or informally"));
		addDisabledMenuItem(EAM.text("Conduct evaluations and/or audits at appropriate times during the project cycle"));
		addDisabledMenuItem(EAM.text("Demonstrate commitment from leaders to learning and innovation"));
		addDisabledMenuItem(EAM.text("Provide a safe environment for encouraging experimentation"));
		addDisabledMenuItem(EAM.text("Share success & failures with practitioners around the world"));

	}
}
