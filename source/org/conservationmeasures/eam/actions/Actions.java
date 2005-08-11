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

import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.MainWindow;

public class Actions
{
	public Actions(MainWindow mainWindow)
	{
		actions = new HashMap();
		
		BaseProject project = mainWindow.getProject();
		registerAction(new ActionAbout(project));
		registerAction(new ActionContextualHelp(project));
		registerAction(new ActionCopy(project));
		registerAction(new ActionCut(project));
		registerAction(new ActionDelete(project));
		registerAction(new ActionExit(mainWindow));
		registerAction(new ActionInsertConnection(project));
		registerAction(new ActionInsertGoal(project));
		registerAction(new ActionInsertIntervention(project));
		registerAction(new ActionInsertThreat(project));
		registerAction(new ActionNewProject(mainWindow));
		registerAction(new ActionNodeProperties(project));
		registerAction(new ActionOpenProject(mainWindow));
		registerAction(new ActionPaste(project));
		registerAction(new ActionRedo(project));
		registerAction(new ActionSelectAll(project));
		registerAction(new ActionUndo(project));
		
		updateActionStates();
	}
	
	public ProjectAction get(Class c)
	{
		return (ProjectAction)actions.get(c);
	}

	public void updateActionStates()
	{
		Collection actualActions = actions.values();
		Iterator iter = actualActions.iterator();
		while(iter.hasNext())
		{
			ProjectAction action = (ProjectAction)iter.next();
			action.setEnabled(action.shouldBeEnabled());
		}
	}
	
	void registerAction(ProjectAction action)
	{
		actions.put(action.getClass(), action);
	}

	Map actions;
}
