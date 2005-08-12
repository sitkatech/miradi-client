/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Point;

import javax.swing.JPanel;

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
	
	public About getAboutDoer()
	{
		return new About();
	}
	
	public NewProject getNewProjectDoer()
	{
		return new NewProject(getMainWindow());
	}
	
	public OpenProject getOpenProjectDoer()
	{
		return new OpenProject(getMainWindow());
	}
	
	public Exit getExitDoer()
	{
		return new Exit(getMainWindow());
	}
	
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
	
	private MainWindow mainWindow;
	private NullDoer nullDoer;
}
