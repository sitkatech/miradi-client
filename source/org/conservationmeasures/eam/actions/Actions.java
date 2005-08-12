/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.conservationmeasures.eam.main.MainWindow;

public class Actions
{
	public Actions(MainWindow mainWindow)
	{
		actions = new HashMap();
		
		registerAction(new ActionAbout(mainWindow));
		registerAction(new ActionContextualHelp(mainWindow));
		registerAction(new ActionCopy(mainWindow));
		registerAction(new ActionCut(mainWindow));
		registerAction(new ActionDelete(mainWindow));
		registerAction(new ActionExit(mainWindow));
		registerAction(new ActionInsertConnection(mainWindow));
		registerAction(new ActionInsertGoal(mainWindow));
		registerAction(new ActionInsertIntervention(mainWindow));
		registerAction(new ActionInsertThreat(mainWindow));
		registerAction(new ActionNewProject(mainWindow));
		registerAction(new ActionNodeProperties(mainWindow));
		registerAction(new ActionOpenProject(mainWindow));
		registerAction(new ActionPaste(mainWindow));
		registerAction(new ActionRedo(mainWindow));
		registerAction(new ActionSelectAll(mainWindow));
		registerAction(new ActionUndo(mainWindow));
	}
	
	public EAMAction get(Class c)
	{
		return (EAMAction)actions.get(c);
	}

	public void updateActionStates()
	{
		Collection actualActions = actions.values();
		Iterator iter = actualActions.iterator();
		while(iter.hasNext())
		{
			EAMAction action = (EAMAction)iter.next();
			action.setEnabled(action.shouldBeEnabled());
		}
	}
	
	void registerAction(EAMAction action)
	{
		actions.put(action.getClass(), action);
	}

	Map actions;
}
