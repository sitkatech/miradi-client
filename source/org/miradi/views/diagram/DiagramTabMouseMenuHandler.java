/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.views.diagram;

import javax.swing.JPopupMenu;

import org.martus.swing.UiPopupMenu;
import org.miradi.actions.ActionDeleteResultsChain;
import org.miradi.actions.ActionRenameResultsChain;
import org.miradi.actions.Actions;

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
