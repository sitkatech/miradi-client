/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogfields;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import org.miradi.actions.ActionRedo;
import org.miradi.actions.ActionUndo;
import org.miradi.actions.Actions;
import org.miradi.actions.MainWindowAction;
import org.miradi.main.MainWindow;

public class UndoRedoKeyHandler
{
	public static void enableUndoAndRedo(MainWindow mainWindow, JTextComponent field)
	{
		Actions actions = mainWindow.getActions();
		
		MainWindowAction undoAction = actions.getMainWindowAction(ActionUndo.class);
		KeyStroke controlZ = KeyStroke.getKeyStroke("ctrl Z");
		Action wrappedUndoAction = new WrapperToWorkAroundSwingStrangenessAction(undoAction);
		field.getInputMap(field.WHEN_FOCUSED).put(controlZ, wrappedUndoAction);
		
		MainWindowAction redoAction = actions.getMainWindowAction(ActionRedo.class);
		KeyStroke controlY = KeyStroke.getKeyStroke("ctrl Y");
		Action wrappedRedoAction = new WrapperToWorkAroundSwingStrangenessAction(redoAction);
		field.getInputMap().put(controlY, wrappedRedoAction);
	}
	
	/*
	 * NOTE: I'm not sure why this is required, but as of 2012-06-26 it is.
	 * If we bind Ctrl-Z directly to the ActionUndo, it won't get executed
	 * when pressed in a text box in a dialog (like Factor Properties).
	 * My theory is that the menu is somehow blocking it.
	 */
	private static class WrapperToWorkAroundSwingStrangenessAction extends AbstractAction
	{
		public WrapperToWorkAroundSwingStrangenessAction(MainWindowAction actionToWrap)
		{
			wrappedAction = actionToWrap;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			wrappedAction.actionPerformed(e);
			wrappedAction.getMainWindow().updateActionsAndStatusBar();
		}
		
		private MainWindowAction wrappedAction;
	}
}

