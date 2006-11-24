/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftStrategy;
import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionProperties;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.LocationAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.LocationHolder;
import org.conservationmeasures.eam.utils.MenuItemWithoutLocation;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.martus.swing.UiMenu;
import org.martus.swing.UiPopupMenu;
import org.martus.swing.Utilities;

public class DiagramContextMenuHandler
{
	public DiagramContextMenuHandler(MainWindow  mainWindowToUse, DiagramComponent diagramComponentToUse, Actions actionsToUse)
	{
		mainWindow = mainWindowToUse;
		diagramComponent = diagramComponentToUse;
		actions = actionsToUse;
	}
	
	public UiPopupMenu getPopupMenu(Point menuInvokedAt)
	{
		UiPopupMenu menu = new UiPopupMenu();
		menu.add(getInsertMenu(menuInvokedAt));
		menu.addSeparator();
		menu.add(new MenuItemWithoutLocation(actions.get(ActionUndo.class)));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionRedo.class)));
		menu.addSeparator();
		menu.add(new MenuItemWithoutLocation(actions.get(ActionCut.class)));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionCopy.class)));
		menu.add(createMenuItem(ActionPaste.class, menuInvokedAt));
		menu.add(createMenuItem(ActionPasteWithoutLinks.class, menuInvokedAt));
		menu.addSeparator();
		menu.add(new MenuItemWithoutLocation(actions.get(ActionDelete.class)));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionSelectAll.class)));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionSelectChain.class)));
		menu.add(getModeSwitchMenuItem());
		menu.addSeparator();
		menu.add(createMenuItem(ActionProperties.class, menuInvokedAt));
		return menu;
	}

	public UiMenu getInsertMenu(Point menuInvokedAt)
	{
		UiMenu insertMenu = new UiMenu(EAM.text("Menu|Insert"));

		insertMenu.add(createMenuItem(ActionInsertDraftStrategy.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertStrategy.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertContributingFactor.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertDirectThreat.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertTarget.class, menuInvokedAt));
		insertMenu.addSeparator();
		insertMenu.add(actions.get(ActionInsertFactorLink.class));

		return insertMenu;
	}

	private JMenuItem createMenuItem(Class c, Point menuInvokedAt)
	{
		LocationAction action = (LocationAction)actions.get(c);
		action.setInvocationPoint(menuInvokedAt);
		return new ContextMenuItemAtLocation(action);
	}

	public void showContextMenu(MouseEvent e)
	{
		Point screenPoint = e.getPoint();
		Point2D scaledPoint2D = diagramComponent.fromScreen(screenPoint);
		
		Point scaledPoint = Utilities.createPointFromPoint2D(scaledPoint2D);
		
		JPopupMenu menu = getPopupMenu(scaledPoint);
		menu.show(diagramComponent, e.getX(), e.getY());
	}
	
	private MenuItemWithoutLocation getModeSwitchMenuItem()
	{
		String mode = ((DiagramView)mainWindow.getCurrentView()).getCurrentMode();
		if(mode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
			return new MenuItemWithoutLocation(actions.get(ActionShowFullModelMode.class));
		
		return new MenuItemWithoutLocation(actions.get(ActionShowSelectedChainMode.class));
	}
	
	static class ContextMenuItemAtLocation extends JMenuItem implements LocationHolder
	{
		public ContextMenuItemAtLocation(Action action)
		{
			super(action);
		}
		
		public boolean hasLocation()
		{
			return true;
		}
	}

	MainWindow mainWindow;
	DiagramComponent diagramComponent;
	Actions actions;
}
