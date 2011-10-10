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

package org.miradi.views.diagram;

import javax.swing.JPopupMenu;

import org.martus.swing.UiPopupMenu;
import org.miradi.actions.ActionCreateConceptualModel;
import org.miradi.actions.ActionCreateResultsChain;
import org.miradi.actions.ActionDeleteConceptualModel;
import org.miradi.actions.ActionDeleteResultsChain;
import org.miradi.actions.ActionDiagramProperties;
import org.miradi.actions.ActionRenameConceptualModel;
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
		menu.add(actions.get(ActionDiagramProperties.class));
		menu.addSeparator();
		menu.add(actions.get(ActionCreateConceptualModel.class));
		menu.add(actions.getMainWindowAction(ActionRenameConceptualModel.class));
		menu.add(actions.get(ActionDeleteConceptualModel.class));
		menu.addSeparator();
		menu.add(actions.get(ActionCreateResultsChain.class));
		menu.add(actions.getMainWindowAction(ActionRenameResultsChain.class));
		menu.add(actions.get(ActionDeleteResultsChain.class));
		return menu;
	}

	DiagramView view;
}
