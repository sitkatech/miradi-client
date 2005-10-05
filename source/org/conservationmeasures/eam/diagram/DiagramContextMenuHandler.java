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
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertFactor;
import org.conservationmeasures.eam.actions.ActionNodeProperties;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.LocationAction;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiMenu;
import org.martus.swing.UiPopupMenu;

public class DiagramContextMenuHandler
{
	public DiagramContextMenuHandler(DiagramComponent diagramComponentToUse, Actions actionsToUse)
	{
		diagramComponent = diagramComponentToUse;
		actions = actionsToUse;
	}
	
	public UiPopupMenu getPopupMenu(Point menuInvokedAt)
	{
		UiPopupMenu menu = new UiPopupMenu();
		menu.add(getInsertMenu(menuInvokedAt));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionUndo.class)));
		menu.add(new JMenuItem(actions.get(ActionRedo.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionCut.class)));
		menu.add(new JMenuItem(actions.get(ActionCopy.class)));
		menu.add(new JMenuItem(getConfiguredAction(ActionPaste.class, menuInvokedAt)));
		menu.add(new JMenuItem(getConfiguredAction(ActionPasteWithoutLinks.class, menuInvokedAt)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionDelete.class)));
		menu.add(new JMenuItem(actions.get(ActionSelectAll.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionNodeProperties.class)));
		return menu;
	}

	public UiMenu getInsertMenu(Point menuInvokedAt)
	{
		UiMenu insertMenu = new UiMenu(EAM.text("Menu|Insert"));

		insertMenu.add(getConfiguredAction(ActionInsertTarget.class, menuInvokedAt));
		insertMenu.add(getConfiguredAction(ActionInsertFactor.class, menuInvokedAt));
		insertMenu.add(getConfiguredAction(ActionInsertIntervention.class, menuInvokedAt));
		insertMenu.addSeparator();
		insertMenu.add(actions.get(ActionInsertConnection.class));

		return insertMenu;
	}

	private LocationAction getConfiguredAction(Class c, Point menuInvokedAt)
	{
		LocationAction action = (LocationAction)actions.get(c);
		action.setInvocationPoint(menuInvokedAt);
		return action;
	}

	public void showContextMenu(MouseEvent e)
	{
		JPopupMenu menu = getPopupMenu(e.getPoint());
		menu.show(diagramComponent, e.getX(), e.getY());
	}

	DiagramComponent diagramComponent;
	Actions actions;
}