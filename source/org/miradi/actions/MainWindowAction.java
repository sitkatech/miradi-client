/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.Icon;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.views.Doer;
import org.miradi.views.umbrella.UmbrellaView;

public abstract class MainWindowAction extends EAMAction
{
	public MainWindowAction(MainWindow mainWindowToUse, String label)
	{
		super(label, "icons/blankicon.png");
		mainWindow = mainWindowToUse;
	}
	
	public MainWindowAction(MainWindow mainWindowToUse, String label, String icon)
	{
		super(label, icon);
		mainWindow = mainWindowToUse;
	}
	
	public MainWindowAction(MainWindow mainWindowToUse, String label, Icon icon)
	{
		super(label, icon);
		mainWindow = mainWindowToUse;
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Project getProject()
	{
		return mainWindow.getProject();
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			doAction(event);
		}
		catch (CommandFailedException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An internal error prevented this operation"));
		}
		
	}

	public void doAction() throws CommandFailedException
	{
		doAction(null);
	}

	public void doAction(EventObject event) throws CommandFailedException
	{
		Cursor prevCursor = EAM.getMainWindow().getCursor();
		EAM.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		try
		{
			ObjectDataInputField.saveFocusedFieldPendingEdits();
			
			getDoer().doIt(event);
			getMainWindow().updateActionStates();
		}
		finally
		{
			EAM.getMainWindow().setCursor(prevCursor);
		}
	}
	
	public boolean shouldBeEnabled()
	{
		Doer doer = getDoer();
		if(doer == null)
			return false;
		return doer.isAvailable();
	}
	
	public Doer getDoer()
	{
		UmbrellaView currentView = mainWindow.getCurrentView();
		if(currentView == null)
			return null;
		
		Doer doer = currentView.getDoer(getClass());
		doer.setMainWindow(mainWindow);
		doer.setProject(mainWindow.getProject());
		return doer;
	}
	
	MainWindow mainWindow;
}
