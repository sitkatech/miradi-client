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
package org.miradi.dialogfields;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.miradi.actions.ActionRedo;
import org.miradi.actions.ActionUndo;
import org.miradi.actions.Actions;
import org.miradi.actions.EAMAction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;

public class KeyHandler extends KeyAdapter
{
	public KeyHandler(Actions actionsToUse)
	{
		actions = actionsToUse;
	}
	
	public void keyTyped(KeyEvent event)
	{
		try
		{
			char keyChar = event.getKeyChar();
			if(keyChar == ctrl('Z'))
			{
				ObjectDataInputField.saveFocusedFieldPendingEdits();
				getUndoAction().doAction();
			}
			if(keyChar == ctrl('Y'))
			{
				ObjectDataInputField.saveFocusedFieldPendingEdits();
				getRedoAction().doAction();
			}
		}
		catch(CommandFailedException e)
		{
			EAM.errorDialog(EAM.text("An unexpected error prevented that operation"));
		}
	}

	private int ctrl(char letter)
	{
		return Character.toUpperCase(letter) - '@';
	}
	
	private EAMAction getUndoAction()
	{
		return getActions().get(ActionUndo.class);
	}
	
	private EAMAction getRedoAction()
	{
		return getActions().get(ActionRedo.class);
	}
	
	private Actions getActions()
	{
		return actions;
	}
	
	private Actions actions;
}

