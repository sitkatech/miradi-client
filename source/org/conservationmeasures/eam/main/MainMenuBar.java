/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.HeadlessException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.ActionClose;
import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertIndirectFactor;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertStress;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionOpenProject;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;

public class MainMenuBar extends JMenuBar
{
	public MainMenuBar(Actions actions) throws HeadlessException
	{
		add(createFileMenu(actions));
		add(createEditMenu(actions));
		add(createInsertMenu(actions));
		add(createViewMenu(actions));
		add(createHelpMenu(actions));
	}

	private JMenu createFileMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|File"));
		menu.add(new JMenuItem(actions.get(ActionNewProject.class)));
		menu.add(new JMenuItem(actions.get(ActionOpenProject.class)));
		menu.add(new JMenuItem(actions.get(ActionSaveImage.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionClose.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionPrint.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionExit.class)));
		return menu;
	}

	private JMenu createEditMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Edit"));
		menu.add(new JMenuItem(actions.get(ActionUndo.class)));
		menu.add(new JMenuItem(actions.get(ActionRedo.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionCut.class)));
		menu.add(new JMenuItem(actions.get(ActionCopy.class)));
		menu.add(new JMenuItem(actions.get(ActionPaste.class)));
		menu.add(new JMenuItem(actions.get(ActionPasteWithoutLinks.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionDelete.class)));
		menu.add(new JMenuItem(actions.get(ActionSelectAll.class)));
		return menu;
	}
	
	private JMenu createInsertMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Insert"));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertIntervention.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertIndirectFactor.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDirectThreat.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertStress.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTarget.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionInsertConnection.class)));
		return menu;
	}
	
	private JMenuItem createJMenuItemCenterLocation(EAMAction action)
	{
		JMenuItem centeredLocationAction = new JMenuItem(action);
		return centeredLocationAction;
	}
	
	private JMenu createViewMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|View"));
		menu.add(new JMenuItem(actions.get(ActionZoomIn.class)));
		menu.add(new JMenuItem(actions.get(ActionZoomOut.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionConfigureLayers.class)));
		return menu;
	}

	private JMenu createHelpMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.add(new JMenuItem(actions.get(ActionAbout.class)));
		return menu;
	}
}
