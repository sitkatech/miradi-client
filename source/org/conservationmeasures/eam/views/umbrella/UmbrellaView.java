/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.NullDoer;

abstract public class UmbrellaView extends JPanel
{
	public UmbrellaView(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		nullDoer = new NullDoer();
	}
	
	abstract public String cardName();
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public BaseProject getProject()
	{
		return getMainWindow().getProject();
	}
	
	public Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	public JToolBar getToolBar()
	{
		return toolBar;
	}
	
	protected void setToolBar(JToolBar newToolBar)
	{
		toolBar = newToolBar;
	}
	
	////////////////////////////////////////////////////////////
	// these doers are available in this class
	
	public Doer getAboutDoer()
	{
		return new About();
	}
	
	public Doer getNewProjectDoer()
	{
		return new NewProject(getMainWindow());
	}
	
	public Doer getOpenProjectDoer()
	{
		return new OpenProject(getMainWindow());
	}
	
	public Doer getCloseDoer()
	{
		return new Close(getMainWindow());
	}
	
	public Doer getExitDoer()
	{
		return new Exit(getMainWindow());
	}
	
	public Doer getUndoDoer()
	{
		return new Undo(getProject());
	}
	
	public Doer getRedoDoer()
	{
		return new Redo(getProject());
	}
	
	public Doer getViewDiagram()
	{
		return new ViewDiagram(getProject());
	}
	
	public Doer getViewInterview()
	{
		return new ViewInterview(getProject());
	}
	
	////////////////////////////////////////////////////////////
	// these doers are not available in this class
	
	public Doer getInsertGoalDoer(Point invocationPoint)
	{
		return nullDoer;
	}
	
	public Doer getInsertThreatDoer(Point invocationPoint)
	{
		return nullDoer;
	}
	
	public Doer getInsertInterventionDoer(Point invocationPoint)
	{
		return nullDoer;
	}
	
	public Doer getSaveJPEGImageDoer()
	{
		return nullDoer;
	}
	
	public Doer getInsertConnectionDoer()
	{
		return nullDoer;
	}
	
	public Doer getCopyDoer()
	{
		return nullDoer;
	}
	
	public Doer getCutDoer()
	{
		return nullDoer;
	}
	
	public Doer getDeleteDoer()
	{
		return nullDoer;
	}
	
	public Doer getPasteDoer(Point invocationPoint)
	{
		return nullDoer;
	}
	
	public Doer getNodePropertiesDoer()
	{
		return nullDoer;
	}
	
	private MainWindow mainWindow;
	private NullDoer nullDoer;
	private JToolBar toolBar;
}
