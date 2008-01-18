/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.views.diagram;

import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.actions.ActionDeleteResultsChain;
import org.conservationmeasures.eam.actions.ActionRenameResultsChain;
import org.conservationmeasures.eam.actions.Actions;
import org.martus.swing.UiPopupMenu;

public class DiagramTabMouseMenuHandler
{
	public DiagramTabMouseMenuHandler(DiagramView viewToUse)
	{
		view = viewToUse;
	}

	public JPopupMenu getPopupMenu()
	{
		UiPopupMenu menu = new UiPopupMenu();
		Actions actions = view.getMainWindow().getActions();
		menu.add(actions.getMainWindowAction(ActionRenameResultsChain.class));
		menu.add(actions.get(ActionDeleteResultsChain.class));
		return menu;
	}

	DiagramView view;
}
