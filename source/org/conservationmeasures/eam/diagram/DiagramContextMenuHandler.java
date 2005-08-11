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
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertGoal;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertThreat;
import org.conservationmeasures.eam.actions.ActionNodeProperties;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.InsertNodeAction;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiMenu;
import org.martus.swing.UiPopupMenu;

public class DiagramContextMenuHandler
{
	public DiagramContextMenuHandler(DiagramView diagramComponentToUse)
	{
		diagramComponent = diagramComponentToUse;
	}
	
	public UiPopupMenu getPopupMenu(Point menuInvokedAt)
	{
		MainWindow mainWindow = getMainWindow();
		BaseProject project = mainWindow.getProject();
		
		UiPopupMenu menu = new UiPopupMenu();
		menu.add(getInsertMenu(menuInvokedAt));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionUndo(project)));
		menu.add(new JMenuItem(new ActionRedo(project)));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionCut(project)));
		menu.add(new JMenuItem(new ActionCopy(project)));
		menu.add(new JMenuItem(new ActionPaste(project)));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionDelete(project)));
		menu.add(new JMenuItem(new ActionSelectAll(project)));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionNodeProperties(project)));
		return menu;
	}

	public UiMenu getInsertMenu(Point menuInvokedAt)
	{
		UiMenu insertMenu = new UiMenu(EAM.text("Menu|Insert"));

		insertMenu.add(getConfiguredAction(ActionInsertGoal.class, menuInvokedAt));
		insertMenu.add(getConfiguredAction(ActionInsertThreat.class, menuInvokedAt));
		insertMenu.add(getConfiguredAction(ActionInsertIntervention.class, menuInvokedAt));
		insertMenu.addSeparator();
		insertMenu.add(new ActionInsertConnection(getMainWindow().getProject()));

		return insertMenu;
	}

	private InsertNodeAction getConfiguredAction(Class c, Point menuInvokedAt)
	{
		Actions actions = getMainWindow().getActions();
		InsertNodeAction insertGoal = (InsertNodeAction)actions.get(c);
		insertGoal.setInvocationPoint(menuInvokedAt);
		return insertGoal;
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
	
	DiagramView diagramComponent;
}