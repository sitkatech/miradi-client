/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
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
	
	public UiPopupMenu getPopupMenu(Point menuInvokedAt)
	{
		MainWindow mainWindow = getMainWindow();
		
		UiPopupMenu menu = new UiPopupMenu();
		menu.add(getInsertMenu(menuInvokedAt));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionCut(mainWindow)));
		menu.add(new JMenuItem(new ActionCopy(mainWindow)));
		menu.add(new JMenuItem(new ActionPaste(mainWindow)));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionDelete(mainWindow)));
		menu.add(new JMenuItem(new ActionSelectAll(mainWindow)));
		return menu;
	}

	public UiMenu getInsertMenu(Point menuInvokedAt)
	{
		UiMenu insertMenu = new UiMenu(EAM.text("Menu|Insert"));
		insertMenu.add(new ActionInsertThreat(getMainWindow(), menuInvokedAt));
		insertMenu.add(EAM.text("Action|Insert|Activity"));
		return insertMenu;
	}

	public void showContextMenu(MouseEvent e)
	{
		JPopupMenu menu = getPopupMenu(e.getPoint());
		menu.show(diagramComponent, e.getX(), e.getY());
	}

	private MainWindow getMainWindow()
	{
		return diagramComponent.getMainWindow();
	}
	
	DiagramComponent diagramComponent;
}