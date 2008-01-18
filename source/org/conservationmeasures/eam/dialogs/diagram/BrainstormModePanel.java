/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.layout.OneRowPanel;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

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
