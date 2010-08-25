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
package org.miradi.main.menu;

import org.miradi.actions.Actions;
import org.miradi.main.EAM;

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
