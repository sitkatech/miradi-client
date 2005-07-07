/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;
import org.martus.swing.UiMenu;
import org.martus.swing.UiPopupMenu;

public class DiagramComponent extends JGraph
{
	public DiagramComponent(MainWindow mainWindowToUse)
	{
		super(new DefaultGraphModel());
		mainWindow = mainWindowToUse;
		installKeyBindings();
		addMouseListener(new MouseHandler(this));
	}
	
	void installKeyBindings()
	{
		Action helpAction = new ActionHelp();
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
	
	
	class ActionHelp extends AbstractAction
	{
		public ActionHelp()
		{
			super("Help");
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			EAM.logWarning("Help action invoked!");
		}
	}

	static final int KEY_MODIFIER_NONE = 0;

	public MainWindow mainWindow;
}

class MouseHandler extends MouseAdapter
{
	MouseHandler(DiagramComponent owner)
	{
		diagramComponent = owner;
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			handleRightClick(e);
	}

// The following two methods are present in Martus, 
// but I can't see why they would be needed. kbs.
//	public void mouseReleased(MouseEvent e)
//	{
//		if(e.isPopupTrigger())
//			handleRightClick(e);
//	}
//
//	public void mouseClicked(MouseEvent e)
//	{
//		if(e.isPopupTrigger())
//			handleRightClick(e);
//	}

	private void handleRightClick(MouseEvent e)
	{
		if(!e.isPopupTrigger())
			return;
		JPopupMenu menu = diagramComponent.getPopupMenu();
		menu.show(diagramComponent, e.getX(), e.getY());
	}

	DiagramComponent diagramComponent;

}