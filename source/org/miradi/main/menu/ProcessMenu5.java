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

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.main.EAM;

public class ProcessMenu5 extends MiradiMenu
{
	public ProcessMenu5(Actions actions)
	{
		super(EAM.text("Menu|5. Capture and Share Learning"), actions);
		setMnemonic(KeyEvent.VK_C);
		
		add(new ProcessMenu5a(actions));
		add(new ProcessMenu5b(actions));
		add(new ProcessMenu5c(actions));
//		addMenuItem(ActionJumpAnalyzeData.class, KeyEvent.VK_A);
//		addMenuItem(ActionJumpAnalyzeStrategies.class, KeyEvent.VK_S);
//		addMenuItem(ActionJumpCommunicateResults.class, KeyEvent.VK_C);
	}
}
