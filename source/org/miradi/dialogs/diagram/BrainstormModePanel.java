/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
