/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiMenu;
import org.martus.swing.UiPopupMenu;

public class DiagramContextMenuHandler
{
	public DiagramContextMenuHandler(DiagramComponent diagramComponentToUse)
	{
		diagramComponent = diagramComponentToUse;
	}
	
	public UiPopupMenu getPopupMenu()
	{
		MainWindow mainWindow = diagramComponent.getMainWindow();
		
		UiPopupMenu menu = new UiPopupMenu();
		menu.add(getInsertMenu());
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionCut(mainWindow)));
		menu.add(new JMenuItem(new ActionCopy(mainWindow)));
		menu.add(new JMenuItem(new ActionPaste(mainWindow)));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionDelete(mainWindow)));
		menu.add(new JMenuItem(new ActionSelectAll(mainWindow)));
		return menu;
	}
	
	public UiMenu getInsertMenu()
	{
		UiMenu insertMenu = new UiMenu(EAM.text("Menu|Insert"));
		insertMenu.add(new ActionInsertTarget(diagramComponent));
		insertMenu.add(EAM.text("Action|Insert|Threat"));
		insertMenu.add(EAM.text("Action|Insert|Action"));
		return insertMenu;
	}

	public void showContextMenu(MouseEvent e)
	{
		JPopupMenu menu = getPopupMenu();
		menu.show(diagramComponent, e.getX(), e.getY());
	}

	DiagramComponent diagramComponent;
}