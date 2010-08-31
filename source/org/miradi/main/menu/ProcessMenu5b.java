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

public class ProcessMenu5b extends MiradiMenu
{
	public ProcessMenu5b(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_5B, actions);
		
		addDisabledMenuItem(EAM.text("Identify key audiences"));
		addDisabledMenuItem(EAM.text("Develop communications strategy"));
		addDisabledMenuItem(EAM.text("Report to project team and stakeholders"));
		addDisabledMenuItem(EAM.text("Develop and share communication products"));
		addDisabledMenuItem(EAM.text("Use other's communication products"));

	}
}
