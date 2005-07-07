/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionContextualHelp;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MouseContextMenuAdapter;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;
import org.martus.swing.UiMenu;
import org.martus.swing.UiPopupMenu;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu
{
	public DiagramComponent(MainWindow mainWindowToUse)
	{
		super(new DefaultGraphModel());
		mainWindow = mainWindowToUse;
		installKeyBindings();
		addMouseListener(new MouseContextMenuAdapter(this));
	}
	
	void installKeyBindings()
	{
		Action helpAction = new ActionContextualHelp(mainWindow);
		bindKey(KeyEvent.VK_F1, KEY_MODIFIER_NONE, helpAction);
	}
	

	private void bindKey(int key, int keyModifier, Action contextMenuAction)
	{
		String thisName = (String)contextMenuAction.getValue(Action.NAME);
		getActionMap().put(thisName, contextMenuAction);
		getInputMap().put(KeyStroke.getKeyStroke(key, keyModifier), thisName);
	}
	
	public UiPopupMenu getPopupMenu()
	{
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
		insertMenu.add(EAM.text("Action|Insert|Target"));
		insertMenu.add(EAM.text("Action|Insert|Threat"));
		insertMenu.add(EAM.text("Action|Insert|Action"));
		return insertMenu;
	}

	public void showContextMenu(MouseEvent e)
	{
		JPopupMenu menu = getPopupMenu();
		menu.show(this, e.getX(), e.getY());
	}

	
	
	static final int KEY_MODIFIER_NONE = 0;

	public MainWindow mainWindow;
}

