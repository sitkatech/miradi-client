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
package org.miradi.dialogs.diagram;

import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.miradi.actions.ActionShowFullModelMode;
import org.miradi.actions.Actions;
import org.miradi.actions.EAMAction;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.EAM;

public class BrainstormModePanel extends OneRowPanel
{
	public BrainstormModePanel(Actions actions)
	{
		add(new UiLabel(EAM.text("Currently in brainstorm mode, so some factors are hidden. " +
				"To return to normal mode, press this button: ")));
		fullModelAction = actions.get(ActionShowFullModelMode.class);
		add(new UiButton(fullModelAction));
		updateVisibility();
	}
	
	public void updateVisibility()
	{
		setVisible(fullModelAction.getDoer().isAvailable());
	}
	
	EAMAction fullModelAction;
}
