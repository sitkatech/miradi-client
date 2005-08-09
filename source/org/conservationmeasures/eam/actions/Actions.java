/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.util.Vector;

import org.conservationmeasures.eam.main.MainWindow;

public class Actions
{
	public Actions(MainWindow mainWindow)
	{
		about = new ActionAbout(mainWindow);
		contextualHelp = new ActionContextualHelp(mainWindow);
		copy = new ActionCopy(mainWindow);
		cut = new ActionCut(mainWindow);
		delete = new ActionDelete(mainWindow);
		exit = new ActionExit(mainWindow);
		insertConnection = new ActionInsertConnection(mainWindow);
		insertGoal = new ActionInsertGoal(mainWindow);
		insertIntervention = new ActionInsertIntervention(mainWindow);
		insertThreat = new ActionInsertThreat(mainWindow);
		newProject = new ActionNewProject(mainWindow);
		nodeProperties = new ActionNodeProperties(mainWindow);
		openProject = new ActionOpenProject(mainWindow);
		paste = new ActionPaste(mainWindow);
		redo = new ActionRedo(mainWindow);
		selectAll = new ActionSelectAll(mainWindow);
		undo = new ActionUndo(mainWindow);
		
		updateActionStates();
	}

	public void updateActionStates()
	{
		updateActionState(about);
		updateActionState(contextualHelp);
		updateActionState(copy);
		updateActionState(cut);
		updateActionState(delete);
		updateActionState(exit);
		updateActionState(insertConnection);
		updateActionState(insertGoal);
		updateActionState(insertIntervention);
		updateActionState(insertThreat);
		updateActionState(newProject);
		updateActionState(nodeProperties);
		updateActionState(openProject);
		updateActionState(paste);
		updateActionState(redo);
		updateActionState(selectAll);
		updateActionState(undo);
	}
	
	public void updateActionState(MainWindowAction action)
	{
		action.setEnabled(action.shouldBeEnabled());
	}

	Vector buttons;
	public MainWindowAction about;
	public MainWindowAction contextualHelp;
	public MainWindowAction copy;
	public MainWindowAction cut;
	public MainWindowAction delete;
	public MainWindowAction exit;
	public MainWindowAction insertConnection;
	public InsertNodeAction insertGoal;
	public InsertNodeAction insertIntervention;
	public InsertNodeAction insertThreat;
	public MainWindowAction newProject;
	public MainWindowAction nodeProperties;
	public MainWindowAction openProject;
	public MainWindowAction paste;
	public MainWindowAction redo;
	public MainWindowAction selectAll;
	public MainWindowAction undo;
}
