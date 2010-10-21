/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.Icon;

import org.miradi.commands.Command;
import org.miradi.dialogfields.FieldSaver;
import org.miradi.exceptions.UnexpectedNonSideEffectException;
import org.miradi.exceptions.UnexpectedSideEffectException;
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
		catch (UnexpectedNonSideEffectException e)
		{
			EAM.logException(e);
			displayUndoRedoErrorMessage();
		}
		catch (UnexpectedSideEffectException e)
		{
			EAM.logException(e);
			
			Command lastCommand = getProject().getLastExecutedCommand();
			if(lastCommand == null)
				EAM.friendlyInternalError(EAM.text("Attempted to execute command as side effect before any command had been executed "));

			EAM.friendlyInternalError(
					EAM.text("Attempt to execute command while in side effect mode:\n" +
							" tried  " + e.getCommand().toString() + "\n" +
							"  within " + lastCommand.toString())
			);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An internal error prevented this operation"));
		}
	}

	private void displayUndoRedoErrorMessage()
	{
		EAM.displayHtmlWarningDialog("<html>" + 
			  	EAM.text("An unexpected error has occurred.") + 
			  	EAM.text("<br> The operation completed successfully, but Undo/Redo may not work correctly.") +
			  	EAM.text("<br> Please report this problem to the Miradi support team ") +
			  	"(<a href=\"mailto:support@miradi.org\">support@miradi.org<a>)" + 
			  "</html>");
	}

	@Override
	public void doAction() throws Exception
	{
		doAction(null);
	}

	public void doAction(EventObject event) throws Exception
	{
		Cursor prevCursor = getMainWindow().getCursor();
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		try
		{
			FieldSaver.savePendingEdits();
			
			getDoer().doIt(event);
			getMainWindow().updateActionStates();
		}
		catch (OutOfMemoryError e)
		{
			EAM.logException(new RuntimeException(e));
			EAM.errorDialog(getOutOfMemoryErrorMessageText());
		}
		finally
		{
			getMainWindow().setCursor(prevCursor);
		}
	}

	private String getOutOfMemoryErrorMessageText()
	{
		return EAM.text("Miradi could not complete that operation \n" +
					"because it ran out of memory.\n" +
					"Please exit Miradi and try again. \n" +
					"If that does not work, contact Miradi support.");
	}
	
	@Override
	public boolean shouldBeEnabled()
	{
		Doer doer = getDoer();
		if(doer == null)
			return false;
		return doer.isAvailable();
	}
	
	@Override
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
